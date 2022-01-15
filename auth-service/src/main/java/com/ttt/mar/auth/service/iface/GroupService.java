package com.ttt.mar.auth.service.iface;

import com.ttt.mar.auth.dto.auth.CustomGroupDto;
import com.ttt.rnd.common.dto.DataPagingResponse;
import com.ttt.rnd.common.exception.DuplicateEntityException;
import com.ttt.rnd.common.exception.IdentifyBlankException;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import com.ttt.rnd.lib.dto.GroupCustomDto;
import com.ttt.rnd.lib.dto.GroupDto;
import com.ttt.rnd.lib.dto.RoleCustomDto;
import com.ttt.rnd.lib.dto.RoleDto;
import com.ttt.rnd.lib.dto.UserDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface GroupService {

  GroupDto createGroup(GroupDto groupDto, Integer creatorUserId)
      throws DuplicateEntityException, ResourceNotFoundException;

  GroupDto updateGroupDto(GroupDto groupDto, Integer updaterUserId)
      throws ResourceNotFoundException, IdentifyBlankException, OperationNotImplementException;

  GroupDto findById(Integer groupId)
      throws ResourceNotFoundException, IdentifyBlankException;

  Boolean deleteGroupById(Integer groupId, Integer deleterUserId)
      throws IdentifyBlankException, ResourceNotFoundException, OperationNotImplementException;

  GroupDto addUserToGroup(GroupCustomDto groupCustomDto, Integer updaterUserId)
      throws IdentifyBlankException, ResourceNotFoundException, DuplicateEntityException;

  GroupDto updateUserToGroup(GroupCustomDto groupCustomDto, Integer updateUserId)
      throws IdentifyBlankException, ResourceNotFoundException;

  Boolean deleteUserFromGroup(Integer groupId, Integer userId, Integer deleterUserId)
      throws IdentifyBlankException, ResourceNotFoundException, OperationNotImplementException;

  Boolean deleteRoleFromGroup(Integer groupId, Integer roleId, Integer deleterUserId)
      throws IdentifyBlankException, ResourceNotFoundException, OperationNotImplementException;

  GroupDto updateRoleToGroup(GroupCustomDto groupCustomDto, Integer updaterUserId)
      throws IdentifyBlankException, ResourceNotFoundException;

  List<UserDto> findAllUserFromGroup(Integer groupId)
      throws IdentifyBlankException, ResourceNotFoundException;

  List<RoleDto> findAllRoleFromGroup(Integer groupId)
      throws IdentifyBlankException, ResourceNotFoundException;

  GroupDto addRoleToGroup(GroupCustomDto groupCustomDto, Integer updaterUserId)
      throws IdentifyBlankException, ResourceNotFoundException, DuplicateEntityException;

  List<RoleCustomDto> getAllRoleAssignedInGroup();

  DataPagingResponse<GroupDto> findAll(String search, String role, String sort,
      Boolean isDeleted, Pageable pageable);

  List<CustomGroupDto> getAllGroup();
}
