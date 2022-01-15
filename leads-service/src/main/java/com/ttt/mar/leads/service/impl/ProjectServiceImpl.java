package com.ttt.mar.leads.service.impl;

import com.ttt.mar.leads.config.ServiceMessageCode;
import com.ttt.mar.leads.dto.ProjectDetailResponse;
import com.ttt.mar.leads.dto.ProjectDtoToCampaignManage;
import com.ttt.mar.leads.dto.ProjectRequestDto;
import com.ttt.mar.leads.dto.ProjectResponseDto;
import com.ttt.mar.leads.dto.ProjectUpdateRequest;
import com.ttt.mar.leads.dto.ProjectUpdateStatusDto;
import com.ttt.mar.leads.dto.UserResponseDto;
import com.ttt.mar.leads.entities.Project;
import com.ttt.mar.leads.entities.ProjectStatus;
import com.ttt.mar.leads.filter.ProjectFilter;
import com.ttt.mar.leads.mapper.ProjectMapper;
import com.ttt.mar.leads.repositories.ProjectRepository;
import com.ttt.mar.leads.repositories.ProjectUserPermissionRepository;
import com.ttt.mar.leads.repositories.ProjectUserRepository;
import com.ttt.mar.leads.service.iface.ProjectService;
import com.ttt.mar.leads.service.iface.UserService;
import com.ttt.mar.leads.utils.Constants;
import com.ttt.mar.leads.utils.DateUtil;
import com.ttt.rnd.common.config.ServiceInfo;
import com.ttt.rnd.common.dto.DataPagingResponse;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import com.ttt.rnd.common.utils.SortingUtils;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * @author kietdt
 * @created_date 20/04/2021
 */
@Service
public class ProjectServiceImpl implements ProjectService {

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
      .getLogger(ProjectServiceImpl.class);

  private final UserService userService;
  private final ProjectRepository projectRepository;
  private final ProjectMapper projectMapper;


  public ProjectServiceImpl(UserService userService, ProjectRepository projectRepository,
      ProjectUserRepository projectUserRepository, ProjectMapper projectMapper,
      ProjectUserPermissionRepository projectUserPermisionRepository) {
    this.userService = userService;
    this.projectRepository = projectRepository;
    this.projectMapper = projectMapper;
  }

  public Integer createProject(Integer userId, ProjectRequestDto dto)
      throws OperationNotImplementException, ResourceNotFoundException {
    logger.info("--- createProject() ---");
    userService.checkValidUser(userId);
    validateExistByCode(dto.getCode());
    validateStartAndEndDate(dto.getStartDate(), dto.getEndDate());
    Project project = projectMapper.fromDto(dto);
    project.setCreatorUserId(userId);
    projectRepository.save(project);

    return project.getId();
  }

  public Integer updateProjectStatus(Integer userId, ProjectUpdateStatusDto dto)
      throws OperationNotImplementException, ResourceNotFoundException, ParseException {
    userService.checkValidUser(userId);
    Project project = getProjectById(dto.getId());
    Long timeNow = DateUtil.getOnlyDateFromTimeStamp(System.currentTimeMillis());
    if (timeNow > project.getEndDate().getTime()) {
      throw new OperationNotImplementException("End date of project is over",
          ServiceInfo.getId() + ServiceMessageCode.PROJECT_TIME_OVER);
    }
    project.setStatus(dto.getStatus());
    project.setUpdaterUserId(userId);
    projectRepository.save(project);

    return project.getId();
  }

  public ProjectDetailResponse getById(Integer id) throws ResourceNotFoundException {
    logger.info("--- getProjectById(), ID: {}", id);
    Project project = getProjectById(id);
    return mappingProject(project);
  }

  public ProjectDetailResponse mappingProject(Project project) {
    logger.info("--- mappingProject() ---");
    ProjectDetailResponse response = projectMapper.toDetailDto(project);
    Integer creatorId = project.getCreatorUserId();
    if (creatorId != null) {
      Map<Integer, UserResponseDto> userResponseDtoMap = userService
          .mapByListId(Collections.singleton(creatorId));
      if (!userResponseDtoMap.isEmpty() && userResponseDtoMap.containsKey(creatorId)) {
        UserResponseDto userResponseDto = userResponseDtoMap.get(creatorId);
        if (userResponseDto != null) {
          response.setCreatorName(userResponseDto.getUsername());
        }
      }
    }

    return response;
  }

  public Integer updateProject(Integer userId, ProjectUpdateRequest dto)
      throws ResourceNotFoundException, OperationNotImplementException {
    logger.info("--- updateProject() ---");
    userService.checkValidUser(userId);
    validateStartAndEndDate(dto.getStartDate(), dto.getEndDate());
    Project project = getProjectById(dto.getId());
    BeanUtils.copyProperties(dto, project);
    project.setUpdaterUserId(userId);
    projectRepository.save(project);

    return project.getId();
  }

  public Project getProjectById(Integer id) throws ResourceNotFoundException {
    Project project = projectRepository.findByIdAndIsDeletedFalse(id);
    if (project == null) {
      throw new ResourceNotFoundException("Project Not Found",
          ServiceInfo.getId() + ServiceMessageCode.PROJECT_NOT_FOUND);
    }
    return project;
  }

  public void validateStartAndEndDate(Date startDate, Date endDate)
      throws OperationNotImplementException {
    long currentTime = System.currentTimeMillis();
    long startTime = startDate.getTime();
    long endTime = endDate.getTime();
    if (startTime > endTime || endTime < currentTime) {
      throw new OperationNotImplementException("Start Date, End Date Invalid",
          ServiceInfo.getId() + ServiceMessageCode.START_END_DATE_INVALID);
    }
  }

  public void validateExistByCode(String code) throws OperationNotImplementException {
    logger.info("--- validateExistByCode(), Code {} ---", code);
    if (StringUtils.isBlank(code)) {
      return;
    }
    List<Project> projects = projectRepository.findByCodeAndIsDeletedFalse(code);
    if (projects != null && !projects.isEmpty()) {
      throw new OperationNotImplementException("Code of Project is exist",
          ServiceInfo.getId() + ServiceMessageCode.CODE_PROJECT_EXIST);
    }
  }

  @Override
  public DataPagingResponse<ProjectResponseDto> getAll(Integer page, Integer limit, Long startDate,
      Long endDate, ProjectStatus status, String searchValue, String sort) {
    Map<String, String> map = SortingUtils.detectSortType(sort);
    if (map.isEmpty()) {
      map.put("createdTime", Constants.SORT_DESC);
    }

    Page<Project> pageData = projectRepository
        .findAll(new ProjectFilter().filter(startDate, endDate, status, searchValue, map),
            PageRequest.of(page - 1, limit));
    List<ProjectResponseDto> data = pageData.getContent().stream().map(projectMapper::toDto)
        .collect(Collectors.toList());

    DataPagingResponse<ProjectResponseDto> response = new DataPagingResponse<>();
    response.setCurrentPage(page);
    response.setList(data);
    response.setTotalPage(pageData.getTotalPages());
    response.setNum(pageData.getTotalElements());

    return response;
  }

  @Override
  public boolean deleteProject(Integer deleterUserId, Integer id)
      throws ResourceNotFoundException, OperationNotImplementException {
    userService.checkValidUser(deleterUserId);

    Project project = this.getProjectById(id);
    if (ProjectStatus.ACTIVE.equals(project.getStatus())) {
      throw new OperationNotImplementException("Invalid Status Project ",
          ServiceInfo.getId() + ServiceMessageCode.INVALID_STATUS_PROJECT);
    }
    project.setDeleterUserId(deleterUserId);
    project.setIsDeleted(true);
    projectRepository.save(project);
    return true;
  }

  @Override
  public List<ProjectDtoToCampaignManage> getProjectToCampaignManage(ProjectStatus status) {
    List<ProjectDtoToCampaignManage> listProjectDtoCampaignManageData = new ArrayList<>();
    List<Project> listProject = new ArrayList<>();
    listProject = projectRepository.findAllByIsDeletedFalse();
    if (status != null) {
      listProject = projectRepository.findByStatusAndIsDeletedFalse(status);
    }
    listProjectDtoCampaignManageData = listProject.stream()
        .map(projectMapper::toDtoToCampaignManage)
        .collect(Collectors.toList());
    return listProjectDtoCampaignManageData;
  }

}
