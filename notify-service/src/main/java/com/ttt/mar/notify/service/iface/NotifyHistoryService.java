package com.ttt.mar.notify.service.iface;

import com.ttt.mar.notify.dto.notify.DataRequestNotifyResponse;
import com.ttt.mar.notify.dto.notify.NotifyHistoryDetailResponseDto;
import com.ttt.mar.notify.dto.notify.email.EmailAttachment;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Map;
import org.springframework.transaction.annotation.Transactional;

public interface NotifyHistoryService {

  DataRequestNotifyResponse<?> getNotifyHistoryPaging(Long fromDate, Long toDate, Integer statusId,
      Integer typeSendId, String search, String sort, Integer page, Integer limit)
      throws ParseException, OperationNotImplementException;

  String exportNotifyHistory(Long fromDate, Long toDate, Integer statusId,
      Integer typeSendId, String search, String sort)
      throws ParseException, OperationNotImplementException, IOException;

  NotifyHistoryDetailResponseDto getNotifyHistoryById(Long id) throws ResourceNotFoundException;

  @Transactional
  Long createNotification(Map<String, Object> notify, Integer userId) throws Exception;

  EmailAttachment getEmailAttachment(String url);
}
