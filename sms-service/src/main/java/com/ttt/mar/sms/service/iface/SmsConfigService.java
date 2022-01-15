package com.ttt.mar.sms.service.iface;

import com.ttt.mar.sms.dto.smsconfig.SmsConfigDtoRequest;
import com.ttt.mar.sms.dto.smsconfig.SmsConfigDtoResponse;
import com.ttt.mar.sms.dto.smsconfig.SmsConfigResponseDetailDto;
import com.ttt.rnd.common.dto.DataPagingResponse;
import com.ttt.rnd.common.exception.DuplicateEntityException;
import com.ttt.rnd.common.exception.IdentifyBlankException;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import org.springframework.transaction.annotation.Transactional;

public interface SmsConfigService {

  @Transactional
  Integer createSmsConfig(SmsConfigDtoRequest smsConfigDtoRequest, Integer userId)
      throws OperationNotImplementException, ResourceNotFoundException, IdentifyBlankException, DuplicateEntityException;

  DataPagingResponse<SmsConfigDtoResponse> getSmsConfigs(String search, Integer publisherId,
      String sort,
      Integer page, Integer limit);

  @Transactional
  Boolean deleteSmsConfigById(Integer id, Integer userId)
      throws ResourceNotFoundException, IdentifyBlankException, OperationNotImplementException;


  @Transactional
  Boolean updateSmsConfigById(SmsConfigDtoRequest smsConfigDtoRequest, Integer userId)
      throws OperationNotImplementException, ResourceNotFoundException, IdentifyBlankException, DuplicateEntityException;


  SmsConfigResponseDetailDto getSmsConfigById(Integer smsConfigId)
      throws ResourceNotFoundException, IdentifyBlankException;



}
