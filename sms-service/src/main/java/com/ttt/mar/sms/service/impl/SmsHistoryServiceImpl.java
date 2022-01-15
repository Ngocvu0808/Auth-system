package com.ttt.mar.sms.service.impl;

import com.ttt.mar.sms.entities.SmsConfig;
import com.ttt.mar.sms.entities.SmsHistory;
import com.ttt.mar.sms.repositories.SmsHistoryRepository;
import com.ttt.mar.sms.service.iface.SmsHistoryService;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SmsHistoryServiceImpl implements SmsHistoryService {

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
      .getLogger(SmsHistoryServiceImpl.class);

  @Autowired
  private SmsHistoryRepository smsHistoryRepository;

  @Override
  public Boolean addHistorySentSms(SmsConfig smsConfig, String content, String receiver) {
    SmsHistory smsHistory = new SmsHistory();
    smsHistory.setSmsConfig(smsConfig);
    smsHistory.setContent(content);
    smsHistory.setReceiver(receiver);
    smsHistory.setCreatedTime(new Date(System.currentTimeMillis()));
    smsHistoryRepository.save(smsHistory);
    return true;
  }
}
