package com.ttt.mar.auth.service.impl;

import com.ttt.mar.auth.dto.auth.RoleDtoExtended;
import com.ttt.mar.auth.dto.auth.RoleDtoRequest;
import com.ttt.mar.auth.dto.auth.RolePermissionDto;
import com.ttt.mar.auth.dto.auth.RoleResponseDto;
import com.ttt.mar.auth.dto.auth.RoleTypeDto;
import com.ttt.mar.auth.exception.AuthServiceMessageCode;
import com.ttt.mar.auth.filter.RoleFilter;
import com.ttt.mar.auth.mapper.RoleMapper;
import com.ttt.mar.auth.mapper.RoleTypeMapper;
import com.ttt.mar.auth.mapper.SysPermissionMapper;
import com.ttt.mar.auth.repositories.RoleDetailRepositoryExtended;
import com.ttt.mar.auth.repositories.RoleExtendedRepository;
import com.ttt.mar.auth.repositories.RoleTypeRepository;
import com.ttt.mar.auth.service.iface.RoleService;
import com.ttt.rnd.common.config.ServiceInfo;
import com.ttt.rnd.common.dto.DataPagingResponse;
import com.ttt.rnd.common.exception.DuplicateEntityException;
import com.ttt.rnd.common.exception.IdentifyBlankException;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import com.ttt.rnd.common.utils.SortingUtils;
import com.ttt.rnd.lib.dto.RoleCustomDto;
import com.ttt.rnd.lib.dto.RoleDto;
import com.ttt.rnd.lib.dto.SysPermissionDto;
import com.ttt.rnd.lib.entities.Role;
import com.ttt.rnd.lib.entities.RoleDetail;
import com.ttt.rnd.lib.entities.RoleType;
import com.ttt.rnd.lib.entities.SysPermission;
import com.ttt.rnd.lib.entities.User;
import com.ttt.rnd.lib.repositories.SysPermissionRepository;
import com.ttt.rnd.lib.repositories.UserRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

  private final RoleExtendedRepository roleRepository;
  private final SysPermissionRepository sysPermissionRepository;
  private final RoleDetailRepositoryExtended roleDetailRepository;
  private final UserRepository userRepository;
  private final RoleTypeRepository roleTypeRepository;

  private final SysPermissionMapper sysPermissionMapper;
  private final RoleMapper roleMapper;
  private final RoleTypeMapper roleTypeMapper;


  public RoleServiceImpl(RoleExtendedRepository roleRepository,
      SysPermissionRepository sysPermissionRepository,
      RoleDetailRepositoryExtended roleDetailRepository,
      UserRepository userRepository,
      RoleTypeRepository roleTypeRepository,
      SysPermissionMapper sysPermissionMapper,
      RoleMapper roleMapper, RoleTypeMapper roleTypeMapper) {
    this.roleRepository = roleRepository;
    this.sysPermissionRepository = sysPermissionRepository;
    this.roleDetailRepository = roleDetailRepository;
    this.userRepository = userRepository;
    this.roleTypeRepository = roleTypeRepository;
    this.sysPermissionMapper = sysPermissionMapper;
    this.roleMapper = roleMapper;
    this.roleTypeMapper = roleTypeMapper;
  }

  @Override
  public List<String> findAllCodePermission(List<String> roles) {
    List<Role> roleList = roleRepository.findAllByCodeIn(roles);
    List<Integer> ids = roleList.stream().map(Role::getId).collect(Collectors.toList());
    List<SysPermission> permissionList = findAllSysPermissionByRoleIds(ids);
    return permissionList.stream().map(SysPermission::getCode).collect(Collectors.toList());
  }

  @Override
  public RoleResponseDto addRole(Integer creatorId, RoleDtoRequest dto)
      throws ResourceNotFoundException, DuplicateEntityException, IdentifyBlankException {
    Optional<User> user = userRepository.findById(creatorId);
    if (user.isEmpty()) {
      throw new ResourceNotFoundException("User doesn't exist",
          ServiceInfo.getId() + AuthServiceMessageCode.USER_NOT_EXIST);
    }

    if (dto.getRoleType() == null) {
      throw new ResourceNotFoundException("Role type not null",
          ServiceInfo.getId() + AuthServiceMessageCode.ROLE_TYPE_NOT_NULL);
    }

    Optional<RoleType> roleType = roleTypeRepository.findById(dto.getRoleType());
    if (roleType.isEmpty()) {
      throw new ResourceNotFoundException("Role type not found",
          ServiceInfo.getId() + AuthServiceMessageCode.ROLE_TYPE_NOT_FOUND);
    }

    RoleDto roleDto = new RoleDto();
    roleDto.setCode(dto.getCode());
    Optional<Role> roleOptional = roleRepository.findByCodeAndIsDeletedFalse(dto.getCode());
    if (roleOptional.isPresent()) {
      throw new DuplicateEntityException("Role already exist",
          ServiceInfo.getId() + AuthServiceMessageCode.ROLE_ALREADY_EXIST);
    }
    Role r = new Role();
    r.setCode(dto.getCode());
    r.setName(dto.getName());
    r.setNote(dto.getNote());
    r.setDefaultRole(dto.getDefaultRole());
    r.setCreatorUser(user.get());
    r.setType(roleType.get());

    Role role = roleRepository.save(r);
    List<Integer> sysPermissionIds = dto.getPermissions();
    if (sysPermissionIds == null || sysPermissionIds.isEmpty()) {
      throw new IdentifyBlankException("Permissions not null",
          ServiceInfo.getId() + AuthServiceMessageCode.PERMISSION_NOT_NULL);
    }
    addPermissionRole(creatorId, role, sysPermissionIds);
    return getRoleResponseDto(role);
  }

  public RoleResponseDto getRoleResponseDto(Role role) {
    RoleResponseDto res = new RoleResponseDto();
    res.setId(role.getId());
    res.setCode(role.getCode());
    res.setName(role.getName());
    res.setDefaultRole(role.getDefaultRole());
    res.setPermissions(findAllPermissionOfRoleList(Collections.singletonList(role)));
    res.setRoleType(role.getType().getId());
    return res;
  }

  private void addPermissionRole(Integer creatorId, Role role, List<Integer> ids)
      throws ResourceNotFoundException {
    List<RoleDetail> roleDetailList = new ArrayList<>();
    for (Integer i : ids) {
      Optional<SysPermission> sysPermissionOptional = sysPermissionRepository.findById(i);
      if (sysPermissionOptional.isEmpty()) {
        throw new ResourceNotFoundException("Permission doesn't exist",
            ServiceInfo.getId() + AuthServiceMessageCode.PERMISSION_NOT_EXIST);
      }
      SysPermission permission = sysPermissionOptional.get();
      RoleDetail roleDetail = new RoleDetail();
      roleDetail.setCreatorUserId(creatorId);
      roleDetail.setPermission(permission);
      roleDetail.setRole(role);
      roleDetailList.add(roleDetail);
    }
    roleDetailRepository.saveAll(roleDetailList);
  }

  @Override
  public RoleResponseDto getRole(Integer roleId) throws ResourceNotFoundException {
    Optional<Role> optional = roleRepository.findByIdAndIsDeletedFalse(roleId);
    if (optional.isEmpty() || optional.get().getIsDeleted().equals(Boolean.TRUE)) {
      throw new ResourceNotFoundException("Role doesn't exist",
          ServiceInfo.getId() + AuthServiceMessageCode.ROLE_NOT_EXIST);
    }
    Role role = optional.get();
    return getRoleResponseDto(role);
  }

  @Override
  public RoleResponseDto updateRole(Integer updaterId, Integer roleId, RoleDtoRequest dto)
      throws ResourceNotFoundException, OperationNotImplementException, IdentifyBlankException {
    Optional<Role> optional = roleRepository.findByIdAndIsDeletedFalse(roleId);
    if (optional.isEmpty() || optional.get().getIsDeleted().equals(Boolean.TRUE)) {
      throw new ResourceNotFoundException("Role doesn't exist",
          ServiceInfo.getId() + AuthServiceMessageCode.ROLE_NOT_EXIST);
    }
    Role role = optional.get();
    if (role.getIsSystemRole().equals(Boolean.TRUE) ||
        (dto.getCode() != null && !dto.getCode().equals(role.getCode()))) {
      throw new OperationNotImplementException("Can't edit code role",
          ServiceInfo.getId() + AuthServiceMessageCode.ROLE_CODE_CANNOT_MODIFY);
    }

    List<Integer> permissionIds = dto.getPermissions();
    if (permissionIds == null || permissionIds.isEmpty()) {
      throw new IdentifyBlankException("Permissions not null",
          ServiceInfo.getId() + AuthServiceMessageCode.PERMISSION_NOT_NULL);
    }
    for (Integer i : permissionIds) {
      Optional<SysPermission> sysPermissionOptional = sysPermissionRepository.findById(i);
      if (sysPermissionOptional.isEmpty()) {
        throw new ResourceNotFoundException("Permission doesn't exist",
            ServiceInfo.getId() + AuthServiceMessageCode.PERMISSION_NOT_EXIST);
      }
    }
    if (dto.getName() != null && !dto.getName().isBlank()) {
      role.setName(dto.getName());
    }
    if (dto.getDefaultRole() != null) {
      role.setDefaultRole(dto.getDefaultRole());
    }
    if (dto.getRoleType() != null) {
      Optional<RoleType> roleType = roleTypeRepository.findById(dto.getRoleType());
      if (roleType.isEmpty()) {
        throw new ResourceNotFoundException("Role type not found",
            ServiceInfo.getId() + AuthServiceMessageCode.ROLE_TYPE_NOT_FOUND);
      }
      role.setType(roleType.get());
    }

    Role marRole = roleRepository.save(role);

    // update permission
    List<RoleDetail> roleDetailList = roleDetailRepository.findAllByRoleId(roleId);
    List<Integer> idListActive = roleDetailList.stream()
        .filter(r -> r.getIsDeleted().equals(Boolean.FALSE))
        .map(RoleDetail::getPermissionId).collect(Collectors.toList());
    List<Integer> listIdAdd = permissionIds.stream().distinct()
        .filter(id -> !idListActive.contains(id))
        .collect(Collectors.toList());
    List<Integer> listIdRemove = idListActive.stream().distinct()
        .filter(id -> !permissionIds.contains(id))
        .collect(Collectors.toList());
    for (Integer id : listIdRemove) {
      Optional<RoleDetail> roleDetailOptional = roleDetailRepository
          .findByRoleIdAndPermissionIdAndIsDeletedFalse(roleId, id);
      if (roleDetailOptional.isPresent()) {
        RoleDetail r = roleDetailOptional.get();
        r.setIsDeleted(Boolean.TRUE);
        r.setDeleterUserId(updaterId);
        roleDetailRepository.save(r);
      }
    }
    addPermissionRole(updaterId, role, listIdAdd);

    return getRoleResponseDto(marRole);
  }

  @Override
  public void deleteRole(Integer deleterId, Integer roleId)
      throws ResourceNotFoundException, OperationNotImplementException {
    Optional<Role> optional = roleRepository.findByIdAndIsDeletedFalse(roleId);
    if (optional.isEmpty()) {
      throw new ResourceNotFoundException("Role doesn't exist",
          ServiceInfo.getId() + AuthServiceMessageCode.ROLE_NOT_EXIST);
    }
    Role role = optional.get();
    if (role.getIsSystemRole().equals(Boolean.TRUE)) {
      throw new OperationNotImplementException("can't delete role",
          ServiceInfo.getId() + AuthServiceMessageCode.ROLE_INVALID);
    }

    role.setIsDeleted(Boolean.TRUE);
    role.setUpdaterUserId(deleterId);

    List<RoleDetail> roleDetailList = roleDetailRepository.findAllByRoleId(roleId).stream()
        .filter(m -> m.getIsDeleted().equals(Boolean.FALSE))
        .collect(Collectors.toList());
    roleDetailList.forEach(r -> {
      r.setIsDeleted(Boolean.TRUE);
      r.setDeleterUserId(deleterId);
    });
    roleDetailRepository.saveAll(roleDetailList);
  }

  @Override
  public List<RolePermissionDto> findAllSysPermission() {
    List<SysPermission> permissions = sysPermissionRepository.findAll();
    return findAllPermission(permissions);
  }

  @Override
  public List<RoleDto> findAllRole() {
    List<Role> roleList = roleRepository.findAllByIsDeletedFalse();
    return roleList.stream()
        .filter(it -> it.getIsSystemRole().equals(Boolean.FALSE))
        .map(roleMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  public DataPagingResponse<RoleDtoExtended> getAllRole(Integer page, Integer limit, String search,
      String sort, Boolean isSystemRole) {
    Map<String, String> map = SortingUtils.detectSortType(sort);
    Page<Role> marRolePage = roleRepository
        .findAll(new RoleFilter().getByFilter(search, map, false, isSystemRole),
            PageRequest.of(page - 1, limit));
    List<Role> list = marRolePage.getContent();
    List<RoleDtoExtended> data = list.stream()
        .map(roleMapper::toExtendedDto)
        .collect(Collectors.toList());
    DataPagingResponse<RoleDtoExtended> dataPagingResponse = new DataPagingResponse<>();
    dataPagingResponse.setList(data);
    dataPagingResponse.setNum(marRolePage.getTotalElements());
    dataPagingResponse.setTotalPage(marRolePage.getTotalPages());
    dataPagingResponse.setCurrentPage(page);
    return dataPagingResponse;
  }

  @Override
  public List<RoleCustomDto> getRoleByType(String type) {
    Optional<RoleType> roleTypeOptional = roleTypeRepository.findByCode(type);
    if (roleTypeOptional.isEmpty()) {
      return new ArrayList<>();
    }
    RoleType roleType = roleTypeOptional.get();
    List<Role> roles = roleRepository.findAllByIsDeletedFalseAndTypeId(roleType.getId());
    List<RoleCustomDto> listCustomDto = new ArrayList<>();
    roles.forEach(role -> listCustomDto.add(roleMapper.toRoleCustomerDto(role)));
    return listCustomDto;
  }

  @Override
  public List<RolePermissionDto> findListPermission(List<Integer> roleIds,
      List<String> objectCodes) {
    List<SysPermission> permissions = findAllSysPermissionByRoleIds(roleIds);
    List<SysPermission> permissionList = permissions.stream()
        .filter(it -> objectCodes.isEmpty() || objectCodes.contains(it.getObjectCode()))
        .collect(Collectors.toList());
    return findAllPermission(permissionList);
  }


  private List<SysPermission> findAllSysPermissionByRoleIds(List<Integer> ids) {
    List<RoleDetail> roleDetailList = roleDetailRepository.findAllByRoleIdIn(ids);
    return roleDetailList.stream()
        .filter(p -> p.getIsDeleted().equals(Boolean.FALSE))
        .map(RoleDetail::getPermission)
        .filter(p -> p.getIsDeleted() == null || p.getIsDeleted().equals(Boolean.FALSE))
        .collect(Collectors.toList());
  }

  private List<RolePermissionDto> findAllPermissionOfRoleList(List<Role> list) {
    List<Integer> ids = list.stream().map(Role::getId).collect(Collectors.toList());
    List<SysPermission> permissions = findAllSysPermissionByRoleIds(ids);
    return findAllPermission(permissions);
  }

  private List<RolePermissionDto> findAllPermission(List<SysPermission> permissions) {
    List<String> objectNames = permissions.stream()
        .map(SysPermission::getObjectCode)
        .distinct()
        .collect(Collectors.toList());
    List<RolePermissionDto> rolePermissionDtoList = new ArrayList<>();
    for (String name : objectNames) {
      RolePermissionDto rolePermissionDto = new RolePermissionDto();
      List<SysPermissionDto> permissionDtoList = new ArrayList<>();
      rolePermissionDto.setObjectCode(name);
      permissions.forEach(p -> {
        if (p.getObjectCode().equals(name)) {
          rolePermissionDto.setObjectName(p.getObjectName());
          rolePermissionDto.setService(p.getService());
          permissionDtoList.add(sysPermissionMapper.toDto(p));
        }
      });
      rolePermissionDto.setSysPermissions(permissionDtoList);
      rolePermissionDtoList.add(rolePermissionDto);
    }
    return rolePermissionDtoList;
  }

  @Override
  public List<RoleDto> findListRoleByListObjectCode(String objectCodeList) {
    List<String> objectCode = Arrays.stream(objectCodeList.split(","))
        .map(String::toUpperCase).collect(Collectors.toList());
    List<Integer> ids = roleDetailRepository.findRoleOnlyContainsPermissionObject(objectCode);
    List<Role> roleList = roleRepository.findAllByIsDeletedFalseAndIdIn(ids);
    return roleList.stream().map(roleMapper::toDto).collect(Collectors.toList());
  }

  @Override
  public List<RoleCustomDto> findListRoleByTypes(String types) {
    if (types == null || types.isEmpty()) {
      List<Role> roles = roleRepository.findAllByIsDeletedFalse();
      return roles.stream().map(roleMapper::toRoleCustomerDto).collect(Collectors.toList());
    }

    String[] typeArr = types.split(",");
    List<RoleType> roleTypes = roleTypeRepository
        .findAllByCodeInAndIsDeletedFalse(Arrays.asList(typeArr));
    if (roleTypes.isEmpty()) {
      return new ArrayList<>();
    }

    List<Integer> roleTypeIds = roleTypes.stream()
        .map(RoleType::getId).collect(Collectors.toList());
    List<Role> roles = roleRepository.findAllByIsDeletedFalseAndTypeIdIn(roleTypeIds);
    return roles.stream().map(roleMapper::toRoleCustomerDto).collect(Collectors.toList());
  }


  @Override
  public List<RoleTypeDto> getAllRoleType() {
    List<RoleType> data = roleTypeRepository.findAll();

    return data.stream().map(roleTypeMapper::toDto).collect(Collectors.toList());
  }
}
