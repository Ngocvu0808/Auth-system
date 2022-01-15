package com.ttt.mar.sms.service.iface;

import com.ttt.mar.sms.dto.inforsms.PayloadRequestFromNotifyServiceDto;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import java.util.Map;

public interface SmsEnQueueService {

  Map<String, Object> receiveInforSmsFromNotifyService(
      PayloadRequestFromNotifyServiceDto payloadRequestFromNotifyServiceDto)
      throws ResourceNotFoundException, OperationNotImplementException;
}
