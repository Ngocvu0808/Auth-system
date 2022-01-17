package com.ttt.mar.notify.service.impl;


import com.ttt.mar.notify.config.ServiceMessageCode;
import com.ttt.mar.notify.dto.template.UserResponseDto;
import com.ttt.mar.notify.mapper.template.UserMapper;
import com.ttt.mar.notify.repositories.template.UserRepositories;
import com.ttt.mar.notify.service.iface.UserService;
import com.ttt.rnd.common.config.ServiceInfo;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import com.ttt.rnd.lib.entities.User;
import com.ttt.rnd.lib.entities.UserStatus;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * @author kietdt
 * @created_date 20/04/2021
 */
@Service
public class UserServiceImpl implements UserService {

  private final UserRepositories userRepositories;
  private final UserMapper userMapper;

  public UserServiceImpl(UserRepositories userRepositories, UserMapper userMapper) {
    this.userRepositories = userRepositories;
    this.userMapper = userMapper;
  }

  @Override
  public Map<Integer, UserResponseDto> mapByListId(Set<Integer> ids) {
    Map<Integer, UserResponseDto> responseMap = new HashMap<>();
    if (ids == null || ids.isEmpty()) {
      return responseMap;
    }
    List<User> users = userRepositories
        .findByIdInAndIsDeletedFalseAndStatus(ids, UserStatus.ACTIVE);
    if (users.isEmpty()) {
      return responseMap;
    }

    List<UserResponseDto> userResponseDtos = users.stream().map(userMapper::toDto)
        .collect(Collectors.toList());

    responseMap.putAll(userResponseDtos.stream()
        .collect(Collectors.toMap(UserResponseDto::getId, entity -> entity)));

    return responseMap;
  }

  @Override
  public User checkValidUser(Integer userId)
      throws ResourceNotFoundException, OperationNotImplementException {
    Optional<User> userOptionalById = userRepositories.findById(userId);
    if (userOptionalById.isEmpty() || userOptionalById.get().getIsDeleted()) {
      throw new ResourceNotFoundException("Resource not found" + userId,
          ServiceInfo.getId() + ServiceMessageCode.USER_NOT_FOUND);
    }
    if (userOptionalById.get().getStatus().equals(UserStatus.DEACTIVE)) {
      throw new OperationNotImplementException("User deactive",
          ServiceInfo.getId() + ServiceMessageCode.USER_DEACTIVE);
    }

    return userOptionalById.get();
  }
}
