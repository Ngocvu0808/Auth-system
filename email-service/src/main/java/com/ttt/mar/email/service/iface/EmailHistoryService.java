package com.ttt.mar.email.service.iface;

import com.ttt.mar.email.dto.emailhistory.DataPagingEmailHistoryResponse;
import com.ttt.mar.email.dto.emailhistory.EmailHistoryDtoResponse;
import com.ttt.mar.email.dto.mail.EmailRequestDto;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import java.io.IOException;
import java.text.ParseException;

public interface EmailHistoryService {

  DataPagingEmailHistoryResponse<EmailHistoryDtoResponse> getEmailHistories(String search,
      String status, String idHistoryConfigs, Long fromDate, Long toDate, String sort, Integer page,
      Integer limit) throws ParseException, OperationNotImplementException;

  String exportEmailHistory(String search, String status, String idHistoryConfigs, Long fromDate,
      Long toDate, String sort) throws ParseException, OperationNotImplementException, IOException;

  Boolean addEmailHistory(EmailRequestDto dto) throws ResourceNotFoundException;
}
