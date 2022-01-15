package com.ttt.mar.leads.service.iface;

import com.ttt.mar.leads.dto.*;
import com.ttt.mar.leads.entities.Project;
import com.ttt.mar.leads.entities.ProjectStatus;
import com.ttt.rnd.common.dto.DataPagingResponse;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import java.text.ParseException;
import java.util.List;

/**
 * @author kietdt
 * @created_date 20/04/2021
 */
public interface ProjectService {

  Integer createProject(Integer userId, ProjectRequestDto dto)
      throws OperationNotImplementException, ResourceNotFoundException;

  Project getProjectById(Integer id) throws ResourceNotFoundException;

  DataPagingResponse<ProjectResponseDto> getAll(Integer page, Integer limit, Long startDate,
      Long endDate,
      ProjectStatus status, String searchValue, String sort);

  Integer updateProjectStatus(Integer userId, ProjectUpdateStatusDto dto)
      throws OperationNotImplementException, ResourceNotFoundException, ParseException;

  ProjectDetailResponse getById(Integer id) throws ResourceNotFoundException;

  Integer updateProject(Integer userId, ProjectUpdateRequest dto)
      throws ResourceNotFoundException, OperationNotImplementException;

  boolean deleteProject(Integer deleterUserId, Integer id)
      throws ResourceNotFoundException, OperationNotImplementException;

  List<ProjectDtoToCampaignManage> getProjectToCampaignManage(ProjectStatus status);

}
