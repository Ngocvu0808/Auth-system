package com.ttt.mar.email.service.iface;

import com.ttt.mar.email.dto.emailpublisher.EmailPublisherCreateRequestDto;
import com.ttt.mar.email.dto.emailpublisher.EmailPublisherPublicResponseDto;
import com.ttt.mar.email.dto.emailpublisher.EmailPublisherResponseDto;
import com.ttt.mar.email.dto.emailpublisher.EmailPublisherUpdateRequestDto;
import com.ttt.rnd.common.dto.DataPagingResponse;
import com.ttt.rnd.common.exception.IdentifyBlankException;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import java.util.List;
import javax.transaction.Transactional;

public interface EmailPublisherService {

  @Transactional
  Integer createEmailPublisher(EmailPublisherCreateRequestDto request, Integer userId)
      throws IdentifyBlankException, ResourceNotFoundException, OperationNotImplementException;

  @Transactional
  Integer updateEmailPublisher(EmailPublisherUpdateRequestDto request, Integer userId)
      throws IdentifyBlankException, ResourceNotFoundException, OperationNotImplementException;

  DataPagingResponse<EmailPublisherResponseDto> getEmailPublisherPaging(String search, String sort,
      Integer page, Integer limit);

  @Transactional
  Boolean deleteEmailPublisher(Integer id, Integer userId)
      throws IdentifyBlankException, ResourceNotFoundException, OperationNotImplementException;

  List<EmailPublisherPublicResponseDto> getListPublishers();
}
