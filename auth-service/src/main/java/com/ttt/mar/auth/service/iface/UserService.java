package com.ttt.mar.auth.service.iface;

import com.ttt.mar.auth.dto.auth.ChangePassRequestDto;
import com.ttt.mar.auth.dto.auth.DeleteUserListDto;
import com.ttt.mar.auth.dto.auth.RegisterRequestDto;
import com.ttt.mar.auth.dto.auth.UpdateStatusUserListDto;
import com.ttt.mar.auth.dto.filter.UserStatusDto;
import com.ttt.rnd.common.dto.DataPagingResponse;
import com.ttt.rnd.common.exception.CryptoException;
import com.ttt.rnd.common.exception.DuplicateEntityException;
import com.ttt.rnd.common.exception.IdentifyBlankException;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import com.ttt.rnd.lib.entities.UserStatus;
import com.ttt.rnd.lib.dto.GroupAndUserDto;
import com.ttt.rnd.lib.dto.GroupUserCustomResponseDto;
import com.ttt.rnd.lib.dto.RoleCustomDto;
import com.ttt.rnd.lib.dto.RoleDto;
import com.ttt.rnd.lib.dto.UserDto;
import com.ttt.rnd.lib.dto.auth.Permission;
import java.util.List;

public interface UserService {

  UserDto createUser(RegisterRequestDto request, Integer userId)
      throws DuplicateEntityException, IdentifyBlankException;

  void deleteUser(Integer userId, Integer deleterId) throws ResourceNotFoundException;

  UserDto updateStatusUser(Integer userId, Integer updaterId, UserStatus status)
      throws ResourceNotFoundException;

  List<RoleDto> getRolesUser(Integer userId);

  void addRoleUser(Integer userId, Integer creatorId, List<Integer> roleIds)
      throws ResourceNotFoundException, DuplicateEntityException;

  void updateRoleUser(Integer userId, Integer updaterId, List<Integer> roleIds)
      throws ResourceNotFoundException, DuplicateEntityException;

  void deleteRoleUser(Integer userId, Integer deleterId, List<Integer> roleIds)
      throws ResourceNotFoundException, DuplicateEntityException;

  UserDto getUserById(String token, Integer userId)
      throws ResourceNotFoundException, DuplicateEntityException, IdentifyBlankException;

  UserDto updateUser(UserDto userDto, Integer updaterUserId)
      throws ResourceNotFoundException, IdentifyBlankException, DuplicateEntityException;

  List<GroupAndUserDto> getAllGroupAndUser(String filter);

  List<RoleCustomDto> getAllRoleAssignedUser();

  List<GroupUserCustomResponseDto> getAllGroupAssignedUser();

  DataPagingResponse<UserDto> getAll(Integer page, Integer limit, String status,
      String roles, String groups, String search, String sort);

  void updateStatusListUser(UpdateStatusUserListDto dto, Integer updaterId);

  void deleteUserList(DeleteUserListDto dto, Integer deleterId) throws ResourceNotFoundException;

  List<UserStatusDto> getListStatusUser();

  void deleteAll(Integer deleterId) throws ResourceNotFoundException;

  void updateStatusAll(Integer updaterId, UserStatus status)
      throws ResourceNotFoundException;

  Permission getPermissionsOfUser(Integer userId) throws IdentifyBlankException;

  UserDto resetPassword(Integer userId, Integer updaterId) throws ResourceNotFoundException;

  UserDto changePass(Integer userId, ChangePassRequestDto dto)
      throws ResourceNotFoundException, CryptoException, OperationNotImplementException;
}
