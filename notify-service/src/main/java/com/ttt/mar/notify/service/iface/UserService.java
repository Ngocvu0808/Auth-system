package com.ttt.mar.notify.service.iface;

import com.ttt.mar.notify.dto.template.UserResponseDto;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import com.ttt.rnd.lib.entities.User;
import java.util.Map;
import java.util.Set;

public interface UserService {

  Map<Integer, UserResponseDto> mapByListId(Set<Integer> ids);

  User checkValidUser(Integer userId)
      throws ResourceNotFoundException, OperationNotImplementException;
}
