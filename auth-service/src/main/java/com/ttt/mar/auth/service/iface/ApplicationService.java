package com.ttt.mar.auth.service.iface;

import com.ttt.mar.auth.dto.api.DataRequestLogResponse;
import com.ttt.mar.auth.dto.app.ClientApiKeyResponseDto;
import com.ttt.mar.auth.dto.app.ClientUserAddRequestDto;
import com.ttt.mar.auth.dto.app.LogRequestResponseDto;
import com.ttt.mar.auth.dto.app.UserAppPermission;
import com.ttt.mar.auth.dto.app.UserClientCustomDto;
import com.ttt.mar.auth.dto.appservice.ApiClientServiceDto;
import com.ttt.mar.auth.dto.appservice.ClientApiAddDto;
import com.ttt.mar.auth.dto.appservice.ClientServiceAddDto;
import com.ttt.mar.auth.dto.filter.ClientAuthTypeDto;
import com.ttt.mar.auth.dto.filter.LogRequestStatusDto;
import com.ttt.mar.auth.dto.refreshtoken.AccessTokenResponseDto;
import com.ttt.mar.auth.dto.refreshtoken.AccessTokenStatusRequestDto;
import com.ttt.mar.auth.dto.refreshtoken.RefreshTokenResponseDto;
import com.ttt.mar.auth.dto.service.CustomServiceDto;
import com.ttt.mar.auth.dto.service.ServiceResponseDto;
import com.ttt.mar.auth.entities.enums.RefreshTokenStatus;
import com.ttt.mar.auth.entities.enums.ServiceStatus;
import com.ttt.mar.auth.entities.enums.TokenStatus;
import com.ttt.rnd.common.dto.DataPagingResponse;
import com.ttt.rnd.common.exception.IdentifyBlankException;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import com.ttt.rnd.lib.dto.RoleCustomDto;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface ApplicationService {

  List<RefreshTokenResponseDto> getRefreshTokensByClientId(RefreshTokenStatus status, Integer id)
      throws IdentifyBlankException, ResourceNotFoundException, OperationNotImplementException;

  void addUser(Integer clientId, Integer creatorUserId, ClientUserAddRequestDto requestDto)
      throws ResourceNotFoundException, OperationNotImplementException, IdentifyBlankException;

  List<AccessTokenResponseDto> getAccessTokenByClientId(TokenStatus status, Integer id)
      throws IdentifyBlankException, ResourceNotFoundException, OperationNotImplementException;

  List<UserClientCustomDto> getUsers(Integer clientId, String roles, String search, String sort)
      throws ResourceNotFoundException;

  void deleteUser(Integer clientId, Integer userId, Integer deleterUserId)
      throws ResourceNotFoundException;

  void updateRoleUser(Integer clientId, Integer updaterUserId, ClientUserAddRequestDto dto)
      throws ResourceNotFoundException, OperationNotImplementException;

  List<RoleCustomDto> getListRoleAssigned(Integer clientId) throws ResourceNotFoundException;

  void updateStatusOfAccessToken(AccessTokenStatusRequestDto dto, Integer userId)
      throws ResourceNotFoundException, IdentifyBlankException, OperationNotImplementException;

  List<LogRequestStatusDto> getListStatusLogRequest();

  DataRequestLogResponse<?> getLogRequestByAppId(Integer appId, String search, Long fromDate,
      Long toDate, String status, String sort, Integer page, Integer limit)
      throws ResourceNotFoundException, IdentifyBlankException, ParseException, OperationNotImplementException;

  String exportLog(Integer appId, Long fromDate, Long toDate, String status, String search)
      throws IdentifyBlankException, ResourceNotFoundException, OperationNotImplementException, ParseException, IOException;

  void addService(Integer clientId, ClientServiceAddDto dto, Integer creatorId)
      throws ResourceNotFoundException, OperationNotImplementException;

  void pendingService(Integer clientId, Integer serviceId, Integer updaterId)
      throws ResourceNotFoundException, OperationNotImplementException;

  void deleteService(Integer clientId, Integer serviceId, Integer deleterId)
      throws ResourceNotFoundException, OperationNotImplementException;

  List<CustomServiceDto> getServices(Integer clientId) throws ResourceNotFoundException;

  CustomServiceDto getService(Integer clientId, Integer serviceId) throws ResourceNotFoundException;

  LogRequestResponseDto getLogRequestById(Long id)
      throws ResourceNotFoundException, IdentifyBlankException;

  ServiceStatus getStatusOfService(Integer clientId, Integer serviceId)
      throws ResourceNotFoundException, OperationNotImplementException;

  void addApi(Integer clientId, ClientApiAddDto dto, Integer creatorId)
      throws ResourceNotFoundException, OperationNotImplementException, IdentifyBlankException;

  void deleteApi(Integer clientId, Long api, Integer deleterId)
      throws ResourceNotFoundException, OperationNotImplementException;

  List<ApiClientServiceDto> getApis(Integer clientId, Integer serviceId)
      throws ResourceNotFoundException;

  DataPagingResponse<ServiceResponseDto> getListServiceNotSettingOnApp(
      Integer appId, String search, String systems, String sort, Integer page, Integer limit)
      throws ResourceNotFoundException, IdentifyBlankException;

  List<UserAppPermission> getAllUserAppPermission(Integer userId)
      throws ResourceNotFoundException, OperationNotImplementException;

  String createApiKey(Integer clientId, Integer userId)
      throws ResourceNotFoundException, OperationNotImplementException;

  List<ClientAuthTypeDto> getListClientAuthType();

  DataPagingResponse<ClientApiKeyResponseDto> getListClientApiKeyForApplication(
      Integer appId, String sort, Integer page, Integer limit)
      throws IdentifyBlankException, ResourceNotFoundException, OperationNotImplementException;

  void deleteApiKey(Integer id, Integer userId)
      throws ResourceNotFoundException, OperationNotImplementException;

  void cancelApiKey(String id, Integer userId)
      throws ResourceNotFoundException, OperationNotImplementException;

}
