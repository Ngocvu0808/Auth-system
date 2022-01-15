package com.ttt.mar.leads.service.impl;

import com.ttt.mar.leads.config.Constants;
import com.ttt.mar.leads.config.ServiceMessageCode;
import com.ttt.mar.leads.dto.ProjectUserRequestDto;
import com.ttt.mar.leads.dto.ProjectUserResponseDto;
import com.ttt.mar.leads.dto.UserResponseDto;
import com.ttt.mar.leads.entities.Project;
import com.ttt.mar.leads.entities.ProjectUser;
import com.ttt.mar.leads.entities.ProjectUserPermission;
import com.ttt.mar.leads.filter.ProjectUserFilter;
import com.ttt.mar.leads.filter.UserExtendsFilter;
import com.ttt.mar.leads.mapper.UserMapper;
import com.ttt.mar.leads.repositories.ProjectUserPermissionRepository;
import com.ttt.mar.leads.repositories.ProjectUserRepository;
import com.ttt.mar.leads.repositories.UserRepositories;
import com.ttt.mar.leads.service.iface.ProjectService;
import com.ttt.mar.leads.service.iface.ProjectUserService;
import com.ttt.mar.leads.service.iface.UserService;
import com.ttt.rnd.common.config.ServiceInfo;
import com.ttt.rnd.common.dto.DataPagingResponse;
import com.ttt.rnd.common.exception.IdentifyBlankException;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import com.ttt.rnd.lib.entities.Role;
import com.ttt.rnd.lib.entities.RoleType;
import com.ttt.rnd.lib.entities.User;
import com.ttt.rnd.lib.entities.UserStatus;
import com.ttt.rnd.lib.repositories.RoleRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * @author kietdt
 * @created_date 20/04/2021
 */
@Service
public class ProjectUserServiceImpl implements ProjectUserService {

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
      .getLogger(ProjectUserServiceImpl.class);

  private final UserService userService;
  private final ProjectService projectService;
  private final ProjectUserRepository projectUserRepository;
  private final RoleRepository roleRepository;
  private final ProjectUserPermissionRepository projectUserPermissionRepository;
  private final UserRepositories userRepositories;
  private final UserExtendsFilter userExtendsFilter;
  private final ProjectUserFilter projectUserFilter;
  private final UserMapper userMapper;
  private final ProjectUserPermissionRepository projectUserPermisionRepository;

  public ProjectUserServiceImpl(UserService userService, ProjectService projectService,
      ProjectUserRepository projectUserRepository, RoleRepository roleRepository,
      ProjectUserPermissionRepository projectUserPermissionRepository,
      UserRepositories userRepositories, UserExtendsFilter userExtendsFilter,
      ProjectUserFilter projectUserFilter, UserMapper userMapper,
      ProjectUserPermissionRepository projectUserPermisionRepository) {
    this.userService = userService;
    this.projectService = projectService;
    this.projectUserRepository = projectUserRepository;
    this.roleRepository = roleRepository;
    this.projectUserPermissionRepository = projectUserPermissionRepository;
    this.userRepositories = userRepositories;
    this.userExtendsFilter = userExtendsFilter;
    this.projectUserFilter = projectUserFilter;
    this.userMapper = userMapper;
    this.projectUserPermisionRepository = projectUserPermisionRepository;
  }

  /**
   * @param userId    ID of User
   * @param projectId ID of Project
   * @param dto       RequestBody thêm mới nhân sự tham gia
   * @throws OperationNotImplementException
   * @throws ResourceNotFoundException
   * @apiNote Api thêm mới nhân sự tham gia theo dự án
   */
  @Override
  public void createProjectUsers(Integer userId, Integer projectId, ProjectUserRequestDto dto)
      throws OperationNotImplementException, ResourceNotFoundException {
    userService.checkValidUser(userId);
    Project project = projectService.getProjectById(projectId);
    getRoleForProjectUser(dto.getRoleId());

    List<ProjectUser> projectUsers = validUserAndSetProjectUser(userId, project, dto.getUserIds());
    saveProjectUsers(userId, projectUsers, dto.getRoleId());
  }

  private void saveProjectUsers(Integer userId, List<ProjectUser> projectUsers, Integer roleId) {
    if (projectUsers == null || projectUsers.isEmpty()) {
      return;
    }
    projectUserRepository.saveAll(projectUsers);

    List<ProjectUserPermission> projectUserPermissions = new ArrayList<>();
    for (ProjectUser projectUser : projectUsers) {
      ProjectUserPermission projectUserPermission = new ProjectUserPermission();
      projectUserPermission.setRoleId(roleId);
      projectUserPermission.setProjectUser(projectUser);
      projectUserPermission.setCreatorUserId(userId);
      projectUserPermissions.add(projectUserPermission);
    }
    projectUserPermissionRepository.saveAll(projectUserPermissions);
  }

  private List<ProjectUser> validUserAndSetProjectUser(
      Integer creatorId, Project project, Set<Integer> userIds)
      throws OperationNotImplementException, ResourceNotFoundException {
    List<ProjectUser> projectUserExists = projectUserRepository
        .findByProjectIdAndUserIdInAndIsDeletedFalse(project.getId(), userIds);
    if (projectUserExists != null && !projectUserExists.isEmpty()) {
      throw new OperationNotImplementException("Project User is exist",
          ServiceInfo.getId() + ServiceMessageCode.PROJECT_USER_EXIST);
    }
    Map<Integer, UserResponseDto> userResponseDtoMap = userService.mapByListId(userIds);
    List<ProjectUser> projectUsers = new ArrayList<>();
    for (Integer userId : userIds) {
      if (!userResponseDtoMap.containsKey(userId) || userResponseDtoMap.get(userId) == null) {
        throw new ResourceNotFoundException("User not found",
            ServiceInfo.getId() + ServiceMessageCode.USER_NOT_FOUND);
      }
      ProjectUser projectUser = new ProjectUser();
      projectUser.setProject(project);
      projectUser.setCreatorUserId(creatorId);
      projectUser.setUserId(userId);

      projectUsers.add(projectUser);
    }

    return projectUsers;
  }

  private Role getRoleForProjectUser(Integer roleId)
      throws ResourceNotFoundException, OperationNotImplementException {
    Optional<Role> optionalRole = roleRepository.findById(roleId);
    if (optionalRole.isEmpty() || optionalRole.get().getIsDeleted()) {
      throw new ResourceNotFoundException("Role not found",
          ServiceInfo.getId() + ServiceMessageCode.ROLE_NOT_FOUND);
    }
    Role role = optionalRole.get();
    RoleType roleType = role.getType();
    if (roleType == null || !Constants.LEADS_CODE.equals(roleType.getCode())) {
      throw new OperationNotImplementException("Invalid roleType for Role",
          ServiceInfo.getId() + ServiceMessageCode.INVALID_ROLE_TYPE);
    }

    return role;
  }

  /**
   * @param projectId ID of Project
   * @param status    Status of User
   * @return List<UserResponseDto>
   * @throws ResourceNotFoundException
   * @apiNote Api lấy danh sách User không tồn tại trong dự án
   */
  @Override
  public List<UserResponseDto> findProjectUserNotExistProject(Integer projectId,
      UserStatus status) throws ResourceNotFoundException {
    logger.info("--- findProjectUserNotExistProject() ---");
    projectService.getProjectById(projectId);
    Set<Integer> userIdExists = projectUserRepository.getUserIdByProjectId(projectId);
    List<User> users = userRepositories
        .findAll(userExtendsFilter.filterUserNotExistProject(userIdExists, status));
    return users.stream().map(userMapper::toDto).collect(Collectors.toList());
  }

  /**
   * @param projectId ID of Project
   * @param page      Page Number
   * @param limit     Limit Data
   * @param sort      Format Sort
   * @return DataPagingResponse<ProjectUserResponseDto>
   * @throws ResourceNotFoundException
   * @throws OperationNotImplementException
   * @apiNote Api lấy danh sách nhân sự tham gia theo dự án
   */
  @Override
  public DataPagingResponse<ProjectUserResponseDto> getProjectUserFilter(Integer projectId,
      Integer page, Integer limit, String sort)
      throws ResourceNotFoundException, OperationNotImplementException {
    logger.info("--- getProjectUserFilter() ---");
    projectService.getProjectById(projectId);
    Page<ProjectUser> projectUserPage = projectUserRepository
        .findAll(projectUserFilter.filter(projectId), PageRequest.of(page - 1, limit));

    List<ProjectUserResponseDto> projectUserResponseDtos = mappingProjectUserResponse(
        projectUserPage.getContent());

    DataPagingResponse<ProjectUserResponseDto> dataPagingResponses = new DataPagingResponse<>();
    dataPagingResponses.setList(projectUserResponseDtos);
    dataPagingResponses.setTotalPage(projectUserPage.getTotalPages());
    dataPagingResponses.setNum(projectUserResponseDtos.size());
    dataPagingResponses.setCurrentPage(page);

    return dataPagingResponses;
  }

  private List<ProjectUserResponseDto> mappingProjectUserResponse(List<ProjectUser> projectUsers) {
    logger.info("--- mingProjectUserResponse() ---");
    if (projectUsers == null || projectUsers.isEmpty()) {
      return Collections.emptyList();
    }
    Set<Integer> userIds = projectUsers.stream().map(ProjectUser::getUserId).filter(
        Objects::nonNull).collect(Collectors.toSet());
    Map<Integer, User> userMap = new HashMap<>();
    if (!userIds.isEmpty()) {
      List<User> users = userRepositories.findByIdInAndIsDeletedFalse(userIds);
      if (users != null && !users.isEmpty()) {
        userMap.putAll(users.stream()
            .collect(Collectors.toMap(User::getId, entity -> entity)));
      }
    }

    Set<Integer> projectUserIds = projectUsers.stream().map(ProjectUser::getId).filter(
        Objects::nonNull).collect(Collectors.toSet());
    Map<Integer, String> projectUserIdByRoleMap = mapRoleNameByProjectUser(projectUserIds);

    return setProjectUserResponse(projectUsers, userMap, projectUserIdByRoleMap);
  }

  private List<ProjectUserResponseDto> setProjectUserResponse(List<ProjectUser> projectUsers,
      Map<Integer, User> userMap, Map<Integer, String> projectUserIdByRoleMap) {
    List<ProjectUserResponseDto> projectUserResponseDtos = new ArrayList<>();
    for (ProjectUser projectUser : projectUsers) {
      Integer userId = projectUser.getUserId();
      Integer id = projectUser.getId();
      if (userId != null && userMap.containsKey(userId)
          && id != null && projectUserIdByRoleMap.containsKey(id)) {
        ProjectUserResponseDto projectUserResponseDto = new ProjectUserResponseDto();
        projectUserResponseDto.setId(projectUser.getId());
        User user = userMap.get(userId);
        BeanUtils.copyProperties(user, projectUserResponseDto, "id");
        projectUserResponseDto.setRoleName(projectUserIdByRoleMap.get(id));
        projectUserResponseDtos.add(projectUserResponseDto);
      }
    }

    return projectUserResponseDtos;
  }

  private Map<Integer, String> mapRoleNameByProjectUser(Set<Integer> projectUserIds) {
    logger.info("--- mapRoleNameByProjectUser() ---");
    Map<Integer, String> projectIdByRoleMap = new HashMap<>();
    if (!projectUserIds.isEmpty()) {
      List<ProjectUserPermission> projectUserPermissions = projectUserPermissionRepository
          .findByProjectUserIdInAndIsDeletedFalse(projectUserIds);
      Map<Integer, Integer> projectUserIdMapByRole = new HashMap<>();
      if (!projectUserPermissions.isEmpty()) {
        for (ProjectUserPermission projectUserPermission : projectUserPermissions) {
          Integer projectUserId = projectUserPermission.getProjectUserId();
          if (projectUserIdMapByRole.containsKey(projectUserId)) {
            continue;
          }
          projectUserIdMapByRole.put(projectUserId, projectUserPermission.getRoleId());
        }

        List<Role> roles = roleRepository.findAllByIsDeletedFalseAndIdIn(
            new ArrayList<>(projectUserIdMapByRole.values()));
        Map<Integer, String> roleNameMap = new HashMap<>(roles.stream()
            .collect(Collectors.toMap(Role::getId, Role::getName)));

        for (Integer projectUserId : projectUserIdMapByRole.keySet()) {
          Integer roleId = projectUserIdMapByRole.get(projectUserId);
          if (roleNameMap.containsKey(roleId)) {
            projectIdByRoleMap.put(projectUserId, roleNameMap.get(roleId));
          }
        }
      }
    }

    return projectIdByRoleMap;
  }

  /**
   * @param deletedUserId ID of UserDeleter
   * @param projectId     ID of Project
   * @param id            ID if ProjectUser
   * @return boolean
   * @throws ResourceNotFoundException
   * @throws OperationNotImplementException
   * @throws IdentifyBlankException
   * @apiNote APi xóa người tham gia của dự án
   */
  @Transactional
  @Override
  public boolean deleteUserOfProject(Integer deletedUserId, Integer projectId, Integer id)
      throws ResourceNotFoundException, OperationNotImplementException, IdentifyBlankException {
    userService.checkValidUser(deletedUserId);
    if (projectId == null || id == null) {
      throw new IdentifyBlankException(" Id is null ",
          ServiceInfo.getId() + ServiceMessageCode.ID_NOT_NULL);
    }
    Project project = projectService.getProjectById(projectId);
    if (project == null) {
      throw new ResourceNotFoundException("Project not found ",
          ServiceInfo.getId() + ServiceMessageCode.PROJECT_NOT_FOUND);
    }
    ProjectUser projectUser = projectUserRepository.getProjectUser(id, projectId);
    if (projectUser == null) {
      throw new ResourceNotFoundException("Project user not found ",
          ServiceInfo.getId() + ServiceMessageCode.PROJECT_USER_NOT_FOUND);
    }
    // update table project user
    projectUser.setDeleted(true);
    projectUser.setDeleterUserId(deletedUserId);
    projectUserRepository.save(projectUser);
    // update table project user permission
    List<ProjectUserPermission> listProjectUserPermission = projectUserPermisionRepository
        .getOneProjectUserPermission(projectUser.getId());
    if (listProjectUserPermission.stream().count() == 0) {
      return true;
    } else {
      for (ProjectUserPermission oneProjectUserPermission : listProjectUserPermission) {
        oneProjectUserPermission.setDeleted(true);
        oneProjectUserPermission.setDeleterUserId(deletedUserId);
        projectUserPermisionRepository.save(oneProjectUserPermission);
      }
    }
    return true;
  }
}
