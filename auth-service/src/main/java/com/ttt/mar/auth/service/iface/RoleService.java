package com.ttt.mar.auth.service.iface;

import com.ttt.mar.auth.dto.auth.RoleDtoExtended;
import com.ttt.mar.auth.dto.auth.RoleDtoRequest;
import com.ttt.mar.auth.dto.auth.RolePermissionDto;
import com.ttt.mar.auth.dto.auth.RoleResponseDto;
import com.ttt.mar.auth.dto.auth.RoleTypeDto;
import com.ttt.rnd.common.dto.DataPagingResponse;
import com.ttt.rnd.common.exception.DuplicateEntityException;
import com.ttt.rnd.common.exception.IdentifyBlankException;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import com.ttt.rnd.lib.dto.RoleCustomDto;
import com.ttt.rnd.lib.dto.RoleDto;
import java.util.List;

public interface RoleService {

  List<String> findAllCodePermission(List<String> roles);

  RoleResponseDto addRole(Integer creatorId, RoleDtoRequest dto)
      throws ResourceNotFoundException, DuplicateEntityException, IdentifyBlankException;

  RoleResponseDto getRole(Integer roleId) throws ResourceNotFoundException;

  RoleResponseDto updateRole(Integer updaterId, Integer roleId, RoleDtoRequest dto)
      throws ResourceNotFoundException, OperationNotImplementException, IdentifyBlankException;

  void deleteRole(Integer deleterId, Integer roleId)
      throws ResourceNotFoundException, OperationNotImplementException;

  List<RolePermissionDto> findAllSysPermission();

  List<RoleDto> findAllRole();

  DataPagingResponse<RoleDtoExtended> getAllRole(Integer page, Integer limit, String search, String sort,Boolean isSystemRole);

  List<RoleCustomDto> getRoleByType(String type);

  /**
   * Lấy ra danh sách permission theo danh sách vai trò và object code
   *
   * @param roleIds
   * @param objectCodes
   * @return
   */
  List<RolePermissionDto> findListPermission(List<Integer> roleIds, List<String> objectCodes);

  List<RoleDto> findListRoleByListObjectCode(String objectCodeList);

  List<RoleCustomDto> findListRoleByTypes(String types);

  List<RoleTypeDto> getAllRoleType();
}
