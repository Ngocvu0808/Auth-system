package com.ttt.mar.auth.service.iface;


import com.ttt.mar.auth.dto.api.ApiAddDto;
import com.ttt.mar.auth.dto.api.ApiDto;
import com.ttt.mar.auth.dto.api.ApiRequestDto;
import com.ttt.mar.auth.dto.api.ApiUpdateDto;
import com.ttt.mar.auth.dto.filter.ApiRequestStatusDto;
import com.ttt.mar.auth.dto.filter.ApiStatusDto;
import com.ttt.mar.auth.dto.filter.ApiTypeDto;
import com.ttt.mar.auth.dto.filter.HttpMethodDto;
import com.ttt.mar.auth.dto.service.ServiceRequestDto;
import com.ttt.mar.auth.dto.service.ServiceRequestUpdateStatusDto;
import com.ttt.mar.auth.dto.service.ServiceResponseDto;
import com.ttt.mar.auth.entities.enums.ApiRequestStatus;
import com.ttt.mar.auth.entities.enums.ApiStatus;
import com.ttt.mar.auth.entities.service.ExternalApi;
import com.ttt.rnd.common.dto.DataPagingResponse;
import com.ttt.rnd.common.exception.DuplicateEntityException;
import com.ttt.rnd.common.exception.IdentifyBlankException;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import java.util.List;

public interface ServiceService {

  Integer createService(ServiceRequestDto serviceRequestDto, Integer userId)
      throws ResourceNotFoundException, IdentifyBlankException, OperationNotImplementException, DuplicateEntityException;

  List<ApiTypeDto> getListApiType();

  List<HttpMethodDto> getListMethod();

  List<ApiStatusDto> getListApiStatus();

  DataPagingResponse<ServiceResponseDto> getServices(String systems, String status, String sort,
      String search, Integer page, Integer limit);

  List<ApiRequestStatusDto> getListApiRequestStatus();

  ExternalApi addApi(ApiAddDto dto, Integer creatorId)
      throws ResourceNotFoundException, OperationNotImplementException, DuplicateEntityException;

  Boolean updateStatusService(ServiceRequestUpdateStatusDto serviceRequestUpdateStatusDto,
      Integer updateUser)
      throws OperationNotImplementException, IdentifyBlankException, ResourceNotFoundException;

  Boolean deleteServiceById(Integer id, Integer userId)
      throws ResourceNotFoundException, IdentifyBlankException, OperationNotImplementException;

  List<ServiceResponseDto> getAll();

  Boolean updateService(ServiceRequestDto serviceRequestDto, Integer userId)
      throws ResourceNotFoundException, IdentifyBlankException, OperationNotImplementException;

  void changeApiStatus(Long apiId, ApiStatus status, Integer updaterId)
      throws ResourceNotFoundException, OperationNotImplementException;

  ApiDto getApiById(Long apiId) throws OperationNotImplementException;

  void deleteApi(Long apiId, Integer deleterId)
      throws OperationNotImplementException, ResourceNotFoundException;

  DataPagingResponse<ApiDto> getApis(String systems, String services, String types, String status,
      String sort, String search, Integer page, Integer limit);

  DataPagingResponse<ApiRequestDto> getApiRequests(String systems, String services, String clients,
      String types, String status, String sort, String search, Integer page, Integer limit);

  ApiRequestDto getApiRequestById(Long apiId) throws ResourceNotFoundException;

  void changeStatusApiRequest(Long apiId, ApiRequestStatus status, Integer updaterId)
      throws ResourceNotFoundException;

  ApiDto updateApi(Long apiId, ApiUpdateDto dto, Integer updaterId)
      throws ResourceNotFoundException, OperationNotImplementException;

  ServiceResponseDto getServiceById(Integer id)
      throws IdentifyBlankException, ResourceNotFoundException;
}
