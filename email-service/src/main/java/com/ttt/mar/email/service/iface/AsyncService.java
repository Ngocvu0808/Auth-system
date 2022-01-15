package com.ttt.mar.email.service.iface;

import org.springframework.scheduling.annotation.Async;

public interface AsyncService {

  @Async
  void updateStatusNotifyHistoryToNotifyService(Integer idRequest, String status,
      String message) throws Exception;
}
