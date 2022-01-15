package com.ttt.mar.email.service.iface;

import com.ttt.mar.email.dto.emailconfig.EmailConfigCreateRequestDto;
import com.ttt.mar.email.dto.emailconfig.EmailConfigDetailResponseDto;
import com.ttt.mar.email.dto.emailconfig.EmailConfigResponseDto;
import com.ttt.mar.email.dto.emailconfig.EmailConfigUpdateRequestDto;
import com.ttt.rnd.common.dto.DataPagingResponse;
import com.ttt.rnd.common.exception.IdentifyBlankException;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import javax.transaction.Transactional;

public interface EmailConfigService {

  @Transactional
  Integer createEmailConfigService(EmailConfigCreateRequestDto request, Integer userId)
      throws IdentifyBlankException, ResourceNotFoundException, OperationNotImplementException;

  DataPagingResponse<EmailConfigResponseDto> getEmailConfigs(Integer publisherId, String search,
      String sort, Integer page, Integer limit);
  @Transactional
  Integer updateEmailConfig(EmailConfigUpdateRequestDto request, Integer userId)
      throws ResourceNotFoundException, OperationNotImplementException;

  @Transactional
  Boolean deleteEmailConfigById(Integer id, Integer userId)
      throws ResourceNotFoundException, IdentifyBlankException, OperationNotImplementException;

  EmailConfigDetailResponseDto getEmailConfigById(Integer id) throws ResourceNotFoundException;
}
