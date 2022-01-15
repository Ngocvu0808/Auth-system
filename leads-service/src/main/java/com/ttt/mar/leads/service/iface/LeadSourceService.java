package com.ttt.mar.leads.service.iface;

import com.ttt.mar.leads.dto.LeadSourceDetailResponseDto;
import com.ttt.mar.leads.dto.LeadSourceRequestDto;
import com.ttt.mar.leads.dto.LeadSourceResponse;
import com.ttt.mar.leads.dto.LeadSourceResponseDto;
import com.ttt.mar.leads.dto.LeadSourceUpdateStatusDto;
import com.ttt.mar.leads.dto.ValidationDto;
import com.ttt.mar.leads.entities.LeadSourceStatus;
import com.ttt.rnd.common.dto.DataPagingResponse;
import com.ttt.rnd.common.exception.IdentifyBlankException;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author bontk
 * @created_date 22/03/2021
 */
public interface LeadSourceService {

  List<ValidationDto> findAllValidation();

  @Transactional
  Integer createLeadSource(Integer userId, LeadSourceRequestDto dto)
      throws OperationNotImplementException, ResourceNotFoundException;

  LeadSourceDetailResponseDto getById(Integer id)
      throws IdentifyBlankException, ResourceNotFoundException, OperationNotImplementException;

  @Transactional
  Integer updateLeadSource(Integer userId, LeadSourceRequestDto dto)
      throws ResourceNotFoundException, OperationNotImplementException, IdentifyBlankException;

  DataPagingResponse<LeadSourceResponse> getLeadSourceFilter(LeadSourceStatus status, String search,
      String sort, Integer page, Integer limit);

  @Transactional
  Integer updateStatusLeadSource(Integer userId, LeadSourceUpdateStatusDto dto)
      throws ResourceNotFoundException, OperationNotImplementException;

  @Transactional
  Boolean deleteLeadSource(Integer userId, Integer id)
      throws ResourceNotFoundException, OperationNotImplementException;

  List<LeadSourceResponseDto> leadSourceToCampaignManage(LeadSourceStatus status,
      Integer projectId);
}
