package com.ttt.mar.sms.service.iface;

import com.ttt.mar.sms.dto.smpublisher.SmsPublisherCustomResponseDto;
import com.ttt.mar.sms.dto.smpublisher.SmsPublisherRequestDto;
import com.ttt.mar.sms.dto.smpublisher.SmsPublisherResponseDto;
import com.ttt.rnd.common.dto.DataPagingResponse;
import com.ttt.rnd.common.exception.DuplicateEntityException;
import com.ttt.rnd.common.exception.IdentifyBlankException;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public interface SmsPublisherService {

  @Transactional
  Integer createSmsPublisher(SmsPublisherRequestDto smsPublisherRequestDto, Integer userId)
      throws OperationNotImplementException, IdentifyBlankException, DuplicateEntityException, ResourceNotFoundException;

  @Transactional
  Boolean updateSmsPublisher(SmsPublisherRequestDto smsPublisherRequestDto, Integer userId)
      throws OperationNotImplementException, IdentifyBlankException, ResourceNotFoundException;

  @Transactional
  Boolean deleteSmsPublisher(Integer idPublisher, Integer userId)
      throws OperationNotImplementException, IdentifyBlankException, ResourceNotFoundException;

  DataPagingResponse<SmsPublisherResponseDto> getPublisherPaging(String search, Integer page,
      String sort, Integer limit);

  List<SmsPublisherCustomResponseDto> getAllPublisher();
}
