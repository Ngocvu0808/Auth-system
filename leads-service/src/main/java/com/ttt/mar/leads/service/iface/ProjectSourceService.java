package com.ttt.mar.leads.service.iface;

import com.ttt.mar.leads.dto.LeadSourceByProjectResponse;
import com.ttt.mar.leads.dto.ProjectSourceRequestDto;
import com.ttt.mar.leads.dto.ProjectSourceResponse;
import com.ttt.mar.leads.entities.LeadSourceStatus;
import com.ttt.rnd.common.dto.DataPagingResponse;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import java.util.List;

/**
 * @author bontk
 * @created_date 19/04/2021
 */
public interface ProjectSourceService {

  List<LeadSourceByProjectResponse> findLeadSourceNotExistProject(Integer projectId,
      LeadSourceStatus status) throws ResourceNotFoundException;

  DataPagingResponse<ProjectSourceResponse> getProjectSourceFilter(Integer projectId, Integer page,
      Integer limit, String sort) throws ResourceNotFoundException;

  void createProjectSources(Integer userId, Integer projectId, ProjectSourceRequestDto dto)
      throws OperationNotImplementException, ResourceNotFoundException;

  Boolean deleteProjectSource(Integer userId, Integer projectId, Integer projectSourceId)
      throws OperationNotImplementException, ResourceNotFoundException;
}
