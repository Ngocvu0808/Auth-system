package com.ttt.mar.leads.service.iface;

import com.ttt.mar.leads.dto.DistributeByProjectResponse;
import com.ttt.mar.leads.dto.ProjectDistributeRequestDto;
import com.ttt.mar.leads.dto.ProjectDistributeResponseDto;
import com.ttt.mar.leads.entities.DistributeApiStatus;
import com.ttt.rnd.common.dto.DataPagingResponse;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import java.util.List;

/**
 * @author bontk
 * @created_date 19/04/2021
 */
public interface ProjectDistributeService {

  void createProjectDistributes(Integer userId, Integer projectId, ProjectDistributeRequestDto dto)
      throws OperationNotImplementException, ResourceNotFoundException;

  List<DistributeByProjectResponse> findDistributeNotExistProject(Integer projectId,
      DistributeApiStatus status) throws ResourceNotFoundException;

  DataPagingResponse<ProjectDistributeResponseDto> getProjectDistributeFilter(Integer projectId,
      Integer page,
      Integer limit, String sort) throws ResourceNotFoundException;

  Boolean deleteProjectDistribute(Integer userId, Integer projectId, Integer projectDistributeId)
      throws OperationNotImplementException, ResourceNotFoundException;

}
