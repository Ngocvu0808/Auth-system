package com.ttt.mar.leads.service.iface;

import com.ttt.mar.leads.dto.UserResponseDto;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import com.ttt.rnd.lib.entities.User;
import java.util.Map;
import java.util.Set;

/**
 * @author KietDT
 * @created_date 05/04/2021
 */
public interface UserService {

  Map<Integer, UserResponseDto> mapByListId(Set<Integer> ids);

  User checkValidUser(Integer userId)
      throws ResourceNotFoundException, OperationNotImplementException;

  User getUserById(Integer id) throws ResourceNotFoundException;
}
