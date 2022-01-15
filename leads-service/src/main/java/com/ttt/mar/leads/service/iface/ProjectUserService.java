package com.ttt.mar.leads.service.iface;

import com.ttt.mar.leads.dto.ProjectUserRequestDto;
import com.ttt.mar.leads.dto.ProjectUserResponseDto;
import com.ttt.mar.leads.dto.UserResponseDto;
import com.ttt.rnd.common.dto.DataPagingResponse;
import com.ttt.rnd.common.exception.IdentifyBlankException;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import com.ttt.rnd.lib.entities.UserStatus;
import java.util.List;

/**
 * @author kietdt
 * @created_date 22/04/2021
 */
public interface ProjectUserService {

  void createProjectUsers(Integer userId, Integer projectId, ProjectUserRequestDto dto)
      throws OperationNotImplementException, ResourceNotFoundException;

  List<UserResponseDto> findProjectUserNotExistProject(Integer projectId,
      UserStatus status) throws ResourceNotFoundException;

  DataPagingResponse<ProjectUserResponseDto> getProjectUserFilter(Integer projectId, Integer page,
      Integer limit, String sort) throws ResourceNotFoundException, OperationNotImplementException;

  boolean deleteUserOfProject(Integer deletedUserId, Integer projectId, Integer id)
      throws ResourceNotFoundException, OperationNotImplementException, IdentifyBlankException;
}
