package com.ttt.mar.leads.service.impl;

import com.ttt.mar.leads.config.ServiceMessageCode;
import com.ttt.mar.leads.dto.DistributeByProjectResponse;
import com.ttt.mar.leads.dto.ProjectDistributeRequestDto;
import com.ttt.mar.leads.dto.ProjectDistributeResponseDto;
import com.ttt.mar.leads.entities.DistributeApi;
import com.ttt.mar.leads.entities.DistributeApiStatus;
import com.ttt.mar.leads.entities.Project;
import com.ttt.mar.leads.entities.ProjectDistribute;
import com.ttt.mar.leads.filter.DistributeApiFilter;
import com.ttt.mar.leads.filter.ProjectDistributeFilter;
import com.ttt.mar.leads.mapper.DistributeApiMapper;
import com.ttt.mar.leads.mapper.ProjectDistributeMapper;
import com.ttt.mar.leads.repositories.DistributeApiRepository;
import com.ttt.mar.leads.repositories.ProjectDistributeRepository;
import com.ttt.mar.leads.service.iface.ProjectDistributeService;
import com.ttt.mar.leads.service.iface.ProjectService;
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
public class ProjectDistributeServiceImpl implements ProjectDistributeService {

  private static final Logger logger = LoggerFactory.getLogger(ProjectDistributeService.class);

  private final DistributeApiRepository distributeApiRepository;
  private final ProjectDistributeRepository projectDistributeRepository;
  private final UserService userService;
  private final ProjectService projectService;
  private final DistributeApiMapper distributeApiMapper;
  private final ProjectDistributeMapper projectDistributeMapper;
  private final ProjectDistributeFilter projectDistributeFilter;
  private final DistributeApiFilter distributeApiFilter;

  public ProjectDistributeServiceImpl(DistributeApiRepository distributeApiRepository,
      ProjectDistributeRepository projectDistributeRepository,
      UserService userService, ProjectService projectService,
      DistributeApiMapper distributeApiMapper, ProjectDistributeMapper projectDistributeMapper,
      ProjectDistributeFilter projectDistributeFilter, DistributeApiFilter distributeApiFilter) {
    this.distributeApiRepository = distributeApiRepository;
    this.projectDistributeRepository = projectDistributeRepository;
    this.userService = userService;
    this.projectService = projectService;
    this.distributeApiMapper = distributeApiMapper;
    this.projectDistributeMapper = projectDistributeMapper;
    this.projectDistributeFilter = projectDistributeFilter;
    this.distributeApiFilter = distributeApiFilter;
  }

  /**
   * @param userId    ID of User
   * @param projectId Id of Project
   * @param dto       RequestBody of createProjectDistributes
   * @throws OperationNotImplementException
   * @throws ResourceNotFoundException
   * @apiNote : Tạo kênh phân phối theo dự án
   */
  public void createProjectDistributes(Integer userId, Integer projectId,
      ProjectDistributeRequestDto dto)
      throws OperationNotImplementException, ResourceNotFoundException {
    logger.info("--- createProjectDistributes() ---");
    userService.checkValidUser(userId);
    Project project = projectService.getProjectById(projectId);
    Set<Integer> distributeIds = dto.getDistributeIds();
    if (distributeIds == null || distributeIds.isEmpty()) {
      return;
    }
    validExistProjectDistribute(projectId, distributeIds);

    saveProjectSources(userId, project, distributeIds);
  }

  /**
   * @param userId        ID of USER
   * @param project       DTO of Project
   * @param distributeIds ID of Distribute
   * @throws ResourceNotFoundException
   * @apiNote : Lưu vào DB.
   */
  public void saveProjectSources(Integer userId, Project project, Set<Integer> distributeIds)
      throws ResourceNotFoundException {
    if (distributeIds == null || distributeIds.isEmpty()) {
      return;
    }

    List<ProjectDistribute> projectDistributes = new ArrayList<>();
    Map<Integer, DistributeApi> distributeApiMap = mapDistribute(distributeIds);
    for (Integer distributeId : distributeIds) {
      ProjectDistribute projectDistribute = new ProjectDistribute();
      if (!distributeApiMap.containsKey(distributeId)
          || distributeApiMap.get(distributeId) == null) {
        throw new ResourceNotFoundException(
            String.format("DistributeApi: %s not found", distributeId),
            ServiceInfo.getId() + ServiceMessageCode.DISTRIBUTE_NOT_FOUND);
      }
      projectDistribute.setDistributeApi(distributeApiMap.get(distributeId));
      projectDistribute.setProject(project);
      projectDistribute.setCreatorUserId(userId);
      projectDistributes.add(projectDistribute);
    }

    projectDistributeRepository.saveAll(projectDistributes);
  }

  /**
   * @param projectId     ID of Project
   * @param distributeIds ID of Distribute
   * @throws OperationNotImplementException
   * @apiNote : Kiểm tra xem đã có ProjectDistribute tồn tại theo project và danh sách IdSource
   */
  public void validExistProjectDistribute(Integer projectId, Set<Integer> distributeIds)
      throws OperationNotImplementException {
    logger.info("--- validExistProjectDistribute(), projectId: {} ---", projectId);
    List<ProjectDistribute> projectDistributes = projectDistributeRepository
        .findByProjectIdAndDistributeApiIdInAndIsDeletedFalse(projectId, distributeIds);
    if (projectDistributes != null && !projectDistributes.isEmpty()) {
      throw new OperationNotImplementException(
          "Project Distribute is exist for projectId: " + projectId,
          ServiceInfo.getId() + ServiceMessageCode.PROJECT_DISTRIBUTE_EXIST_FOR_PROJECT);
    }
  }

  /**
   * @param distributeIds danh sách distributeIds
   * @return Map<Integer, DistributeApi>
   * @apiNote : Map danh sách DistributeApi theo ID.
   */
  public Map<Integer, DistributeApi> mapDistribute(Set<Integer> distributeIds) {
    logger.info("--- mapDistribute() ---");
    Map<Integer, DistributeApi> distributeApiMap = new HashMap<>();
    if (distributeIds == null || distributeIds.isEmpty()) {
      return distributeApiMap;
    }

    List<DistributeApi> distributeApis = distributeApiRepository
        .findByIdInAndStatusAndIsDeletedFalse(distributeIds, DistributeApiStatus.ACTIVE);
    if (!distributeApis.isEmpty()) {
      distributeApiMap.putAll(distributeApis.stream()
          .collect(Collectors.toMap(DistributeApi::getId, entity -> entity)));
    }

    return distributeApiMap;
  }

  /**
   * @param projectId ID of Project
   * @param status    Status of DistributeApi
   * @return List<DistributeByProjectResponse>
   * @throws ResourceNotFoundException
   * @apiNote : Hàm lấy danh sách kênh phân phối không nằm trong dự án
   */
  public List<DistributeByProjectResponse> findDistributeNotExistProject(Integer projectId,
      DistributeApiStatus status) throws ResourceNotFoundException {
    logger.info("--- findDistributeNotExistProject() ---");
    // Step 1: Kiểm tra Project có tồn tại.
    projectService.getProjectById(projectId);
    // step 2: Lấy danh sách ProjectId đã tồn tại theo dự án
    Set<Integer> projectIds = projectDistributeRepository.getDistributeIdByProjectId(projectId);
    // step 3: Filter theo điều kiện tìm kiếm
    List<DistributeApi> distributeApis = distributeApiRepository
        .findAll(distributeApiFilter.filterNotExistProject(projectIds, status));

    return distributeApis.stream().map(distributeApiMapper::toDtoByProject).collect(
        Collectors.toList());
  }

  /**
   * @param projectId ID of Project
   * @param page      Page Number
   * @param limit     Limit Data
   * @param sort      Sort format
   * @return DataPagingResponse<ProjectDistributeResponseDto>
   * @throws ResourceNotFoundException
   * @apiNote : Lấy danh sách kênh phân phối của dự án theo điều kiện tìm kiếm.
   */
  public DataPagingResponse<ProjectDistributeResponseDto> getProjectDistributeFilter(
      Integer projectId, Integer page,
      Integer limit, String sort) throws ResourceNotFoundException {
    logger.info("--- getProjectDistributeFilter() ---");

    projectService.getProjectById(projectId);
    Map<String, String> map = SortingUtils.detectSortType(sort);
    if (map.isEmpty()) {
      map.put("createdTime", "desc");
    }

    Page<ProjectDistribute> projectDistributePage = projectDistributeRepository
        .findAll(projectDistributeFilter.filter(projectId, map), PageRequest.of(page - 1, limit));
    List<ProjectDistributeResponseDto> projectSourceResponses =
        projectDistributePage.getContent().stream()
            .map(projectDistributeMapper::toDto).collect(Collectors.toList());
    DataPagingResponse<ProjectDistributeResponseDto> dataPagingResponses = new DataPagingResponse<>();
    dataPagingResponses.setList(projectSourceResponses);
    dataPagingResponses.setTotalPage(projectDistributePage.getTotalPages());
    dataPagingResponses.setNum(projectDistributePage.getTotalElements());
    dataPagingResponses.setCurrentPage(page);

    return dataPagingResponses;
  }

  /**
   * @param userId              ID of User
   * @param projectId           ID of Project
   * @param projectDistributeId ID of ProjectDistribute
   * @return Boolean
   * @throws OperationNotImplementException
   * @throws ResourceNotFoundException
   * @apiNote: Hàm thực hiện xóa kênh phân phối
   */
  public Boolean deleteProjectDistribute(Integer userId, Integer projectId,
      Integer projectDistributeId)
      throws OperationNotImplementException, ResourceNotFoundException {
    // Step 1: Kiểm tra thông tin data tồn tại
    userService.checkValidUser(userId);
    projectService.getProjectById(projectId);
    ProjectDistribute projectDistribute = projectDistributeRepository
        .findByIdAndProjectIdAndIsDeletedFalse(projectDistributeId, projectId);
    if (projectDistribute == null) {
      throw new ResourceNotFoundException("ProjectDistribute Not Found",
          ServiceInfo.getId() + ServiceMessageCode.PROJECT_DISTRIBUTE_NOT_FOUND);
    }
    DistributeApi distributeApi = projectDistribute.getDistributeApi();
    if (distributeApi == null || distributeApi.getIsDeleted()) {
      throw new ResourceNotFoundException("DistributeApi Not Found",
          ServiceInfo.getId() + ServiceMessageCode.DISTRIBUTE_NOT_FOUND);
    }

    // Step 2: cập nhật thông tin
    projectDistribute.setDeleted(true);
    projectDistribute.setDeleterUserId(userId);
    projectDistributeRepository.save(projectDistribute);

    return true;
  }

}
