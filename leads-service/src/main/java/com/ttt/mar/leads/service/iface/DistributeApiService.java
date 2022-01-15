package com.ttt.mar.leads.service.iface;

import com.ttt.mar.leads.dto.DistributeApiDetailResponse;
import com.ttt.mar.leads.dto.DistributeApiRequestDto;
import com.ttt.mar.leads.dto.DistributeApiResponseDto;
import com.ttt.mar.leads.dto.DistributeApiUpdateStatusDto;
import com.ttt.mar.leads.dto.DistributeMethodPOSTResponseDto;
import com.ttt.mar.leads.dto.DistributeResponseDto;
import com.ttt.mar.leads.entities.ApiMethod;
import com.ttt.mar.leads.entities.DistributeApiStatus;
import com.ttt.rnd.common.dto.DataPagingResponse;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author kietdt
 * @created_date 20/04/2021
 */
public interface DistributeApiService {

  @Transactional
  Integer createDistributeApi(Integer userId, DistributeApiRequestDto dto)
      throws ResourceNotFoundException, OperationNotImplementException;

  DataPagingResponse<DistributeApiResponseDto> getDistributeFilter(DistributeApiStatus status,
      String search,
      String sort, Integer page, Integer limit);

  DistributeApiDetailResponse getById(Integer id) throws ResourceNotFoundException;

  @Transactional
  Integer updateStatusDistributeApi(Integer userId, DistributeApiUpdateStatusDto dto)
      throws ResourceNotFoundException, OperationNotImplementException;

  @Transactional
  Boolean deleteDistribute(Integer userId, Integer id)
      throws ResourceNotFoundException, OperationNotImplementException;

  @Transactional
  Integer updateDistributeApi(Integer userId, DistributeApiRequestDto dto)
      throws ResourceNotFoundException, OperationNotImplementException;

  List<DistributeApiResponseDto> getListDistributeApiActiveAndNotExist(Integer projectId)
      throws OperationNotImplementException, ResourceNotFoundException;

  List<DistributeResponseDto> getDistributeToCampaignManage(Integer projectId,
      DistributeApiStatus status);

  List<DistributeMethodPOSTResponseDto> getDistributeForLead(ApiMethod method);
}
