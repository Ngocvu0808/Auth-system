package com.ttt.mar.leads.service.impl;

import com.ttt.mar.leads.config.ServiceMessageCode;
import com.ttt.mar.leads.dto.LeadSourceByProjectResponse;
import com.ttt.mar.leads.dto.ProjectSourceRequestDto;
import com.ttt.mar.leads.dto.ProjectSourceResponse;
import com.ttt.mar.leads.entities.LeadSource;
import com.ttt.mar.leads.entities.LeadSourceStatus;
import com.ttt.mar.leads.entities.Project;
import com.ttt.mar.leads.entities.ProjectSource;
import com.ttt.mar.leads.filter.LeadSourceFilter;
import com.ttt.mar.leads.filter.ProjectSourceFilter;
import com.ttt.mar.leads.mapper.LeadSourceMapper;
import com.ttt.mar.leads.mapper.ProjectSourceMapper;
import com.ttt.mar.leads.repositories.LeadSourceRepository;
import com.ttt.mar.leads.repositories.ProjectSourceRepository;
import com.ttt.mar.leads.service.iface.ProjectService;
import com.ttt.mar.leads.service.iface.ProjectSourceService;
import com.ttt.mar.leads.service.iface.UserService;
import com.ttt.rnd.common.config.ServiceInfo;
import com.ttt.rnd.common.dto.DataPagingResponse;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import com.ttt.rnd.common.utils.SortingUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * @author bontk
 * @created_date 19/04/2021
 */
@Service
public class ProjectSourceServiceImpl implements ProjectSourceService {

  private static final Logger logger = LoggerFactory.getLogger(ProjectSourceService.class);

  private final LeadSourceRepository leadSourceRepository;
  private final ProjectSourceRepository projectSourceRepository;
  private final UserService userService;
  private final ProjectService projectService;
  private final LeadSourceMapper leadSourceMapper;
  private final ProjectSourceMapper projectSourceMapper;
  private final LeadSourceFilter leadSourceFilter;
  private final ProjectSourceFilter projectSourceFilter;

  ProjectSourceServiceImpl(LeadSourceRepository leadSourceRepository,
      ProjectSourceRepository projectSourceRepository, ProjectService projectService,
      UserService userService,
      LeadSourceMapper leadSourceMapper, ProjectSourceMapper projectSourceMapper,
      LeadSourceFilter leadSourceFilter, ProjectSourceFilter projectSourceFilter) {
    this.leadSourceRepository = leadSourceRepository;
    this.projectSourceRepository = projectSourceRepository;
    this.userService = userService;
    this.projectService = projectService;
    this.leadSourceMapper = leadSourceMapper;
    this.projectSourceMapper = projectSourceMapper;
    this.leadSourceFilter = leadSourceFilter;
    this.projectSourceFilter = projectSourceFilter;
  }

  /**
   * @param projectId: ID of Project
   * @param status:    Trạng thái nguồn cấp Lead
   * @return List<LeadSourceByProjectResponse>
   * @throws ResourceNotFoundException
   * @apiNote: Lấy danh sách nguồn cấp lead không tồn tại trong dự án
   */
  public List<LeadSourceByProjectResponse> findLeadSourceNotExistProject(Integer projectId,
      LeadSourceStatus status) throws ResourceNotFoundException {
    logger.info("--- findLeadSourceNotByProject() ---");
    projectService.getProjectById(projectId);
    Set<Integer> projectIds = projectSourceRepository.getSourceIdByProjectId(projectId);
    List<LeadSource> leadSources = leadSourceRepository
        .findAll(leadSourceFilter.filterNotExistProject(projectIds, status));

    return leadSources.stream().map(leadSourceMapper::toDtoByProject).collect(
        Collectors.toList());
  }

  /**
   * @param projectId ID of Project
   * @param page      Page Number
   * @param limit     Limit data
   * @param sort
   * @return DataPagingResponse<ProjectSourceResponse>
   * @throws ResourceNotFoundException
   * @apiNote: Method tìm kiếm danh sách nguồn cấp Lead.
   */
  public DataPagingResponse<ProjectSourceResponse> getProjectSourceFilter(Integer projectId,
      Integer page, Integer limit, String sort) throws ResourceNotFoundException {
    logger.info("--- getProjectSourceFilter() ---");
    projectService.getProjectById(projectId);

    Map<String, String> map = SortingUtils.detectSortType(sort);
    if (map.isEmpty()) {
      map.put("createdTime", "desc");
    }

    Page<ProjectSource> projectSourcePage = projectSourceRepository
        .findAll(projectSourceFilter.filter(projectId, map), PageRequest.of(page - 1, limit));
    List<ProjectSourceResponse> projectSourceResponses = projectSourcePage.getContent().stream()
        .map(projectSourceMapper::toDto).collect(Collectors.toList());

    DataPagingResponse<ProjectSourceResponse> dataPagingResponses = new DataPagingResponse<>();
    dataPagingResponses.setList(projectSourceResponses);
    dataPagingResponses.setTotalPage(projectSourcePage.getTotalPages());
    dataPagingResponses.setNum(projectSourcePage.getTotalElements());
    dataPagingResponses.setCurrentPage(page);

    return dataPagingResponses;
  }

  /**
   * @param userId    ID of User
   * @param projectId ID of Project
   * @param dto       RequestBody for createProjectSources
   * @throws OperationNotImplementException
   * @throws ResourceNotFoundException
   * @apiNote: Api thêm mới nguồn cấp lead cho dự án
   */
  public void createProjectSources(Integer userId, Integer projectId, ProjectSourceRequestDto dto)
      throws OperationNotImplementException, ResourceNotFoundException {
    userService.checkValidUser(userId);
    Project project = projectService.getProjectById(projectId);
    Set<Integer> sourceIds = dto.getSourceIds();
    if (sourceIds == null || sourceIds.isEmpty()) {
      return;
    }
    validExistProjectSource(projectId, sourceIds);

    saveProjectSources(userId, project, sourceIds);
  }

  public void saveProjectSources(Integer userId, Project project, Set<Integer> sourceIds)
      throws ResourceNotFoundException {
    if (sourceIds == null || sourceIds.isEmpty()) {
      return;
    }

    List<ProjectSource> projectSources = new ArrayList<>();

    Map<Integer, LeadSource> leadSourceMap = mapLeadSource(sourceIds);
    for (Integer sourceId : sourceIds) {
      ProjectSource projectSource = new ProjectSource();
      if (!leadSourceMap.containsKey(sourceId) || leadSourceMap.get(sourceId) == null) {
        throw new ResourceNotFoundException(String.format("LeadSource: %s not found", sourceId),
            ServiceInfo.getId() + ServiceMessageCode.LEAD_SOURCE_NOT_FOUND);
      }
      projectSource.setLeadSource(leadSourceMap.get(sourceId));
      projectSource.setProject(project);
      projectSource.setCreatorUserId(userId);
      projectSources.add(projectSource);
    }

    projectSourceRepository.saveAll(projectSources);
  }

  public void validExistProjectSource(Integer projectId, Set<Integer> sourceIds)
      throws OperationNotImplementException {
    logger.info("--- validExistProjectSource(), projectId: {} ---", projectId);
    List<ProjectSource> projectSources = projectSourceRepository
        .findByProjectIdAndSourceIdInAndIsDeletedFalse(projectId, sourceIds);
    if (projectSources != null && !projectSources.isEmpty()) {
      throw new OperationNotImplementException(
          "Project Source is exist for projectId: " + projectId,
          ServiceInfo.getId() + ServiceMessageCode.PROJECT_SOURCE_EXIST_FOR_PROJECT);
    }
  }

  public Map<Integer, LeadSource> mapLeadSource(Set<Integer> sourceIds) {
    logger.info("--- mapLeadSource() ---");
    Map<Integer, LeadSource> leadSourceMap = new HashMap<>();
    if (sourceIds == null || sourceIds.isEmpty()) {
      return leadSourceMap;
    }

    List<LeadSource> leadSources = leadSourceRepository
        .findByIdInAndStatusAndIsDeletedFalse(sourceIds, LeadSourceStatus.ACTIVE);
    if (!leadSources.isEmpty()) {
      leadSourceMap.putAll(leadSources.stream()
          .collect(Collectors.toMap(LeadSource::getId, entity -> entity)));
    }

    return leadSourceMap;
  }

  /**
   * @param userId          ID of User
   * @param projectId       ID of Project
   * @param projectSourceId ID of ProjectSource
   * @return Boolean
   * @throws OperationNotImplementException
   * @throws ResourceNotFoundException
   * @apiNote: API xóa nguồn cấp lead theo dự án
   */
  public Boolean deleteProjectSource(Integer userId, Integer projectId, Integer projectSourceId)
      throws OperationNotImplementException, ResourceNotFoundException {
    // Step 1: Kiểm tra thông tin data tồn tại
    userService.checkValidUser(userId);
    projectService.getProjectById(projectId);
    ProjectSource projectSource = projectSourceRepository
        .findByIdAndProjectIdAndIsDeletedFalse(projectSourceId, projectId);
    if (projectSource == null) {
      throw new ResourceNotFoundException("ProjectSource Not Found",
          ServiceInfo.getId() + ServiceMessageCode.PROJECT_SOURCE_NOT_FOUND);
    }
    LeadSource leadSource = projectSource.getLeadSource();
    if (leadSource == null || leadSource.getIsDeleted()) {
      throw new ResourceNotFoundException("leadSource Not Found",
          ServiceInfo.getId() + ServiceMessageCode.LEAD_SOURCE_NOT_FOUND);
    }

    // Step 2: cập nhật thông tin
    projectSource.setDeleterUserId(userId);
    projectSource.setIsDeleted(true);
    projectSourceRepository.save(projectSource);

    return true;
  }
}
