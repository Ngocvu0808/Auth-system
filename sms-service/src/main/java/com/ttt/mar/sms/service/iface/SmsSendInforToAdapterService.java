package com.ttt.mar.sms.service.iface;

import com.ttt.mar.sms.dto.inforsms.PayLoadSendToAdapter;

public interface SmsSendInforToAdapterService {

  void sendInforSmsToAdapterService(
      PayLoadSendToAdapter payloadRequestFromNotifyServiceDto) throws Exception;


}
