package com.ttt.mar.sms.service.iface;

import com.ttt.mar.sms.entities.SmsConfig;

public interface SmsHistoryService {

  Boolean addHistorySentSms(SmsConfig smsConfig, String content, String receiver);
}
