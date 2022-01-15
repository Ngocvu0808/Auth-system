package com.ttt.mar.email.service.impl;

import com.ttt.mar.email.controller.EmailController;
import com.ttt.mar.email.dto.mail.PayLoadHistoryStatusEmail;
import com.ttt.mar.email.service.iface.AsyncService;
import com.ttt.rnd.common.utils.JsonUtils;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AsyncServiceImpl implements AsyncService {

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
      .getLogger(EmailController.class);

  @Value("${notify-service.url-update-status}")
  private String NOTIFY_SERVICE_URL_UPDATE_STATUS_SEND_SMS_HISTORY;

  @Value("${notify-service.authorization-key:}")
  private String authorizationKey;

  @Value("${notify-service.authorization-value:}")
  private String authorizationValue;

  public void updateStatusNotifyHistoryToNotifyService(Integer idRequest, String status,
      String message) throws Exception {
    PayLoadHistoryStatusEmail payLoadHistoryStatusEmail = new PayLoadHistoryStatusEmail();
    payLoadHistoryStatusEmail.setRequestId(idRequest);
    payLoadHistoryStatusEmail.setStatus(status);
    payLoadHistoryStatusEmail.setMessage(message);

    HttpResponse<JsonNode> response = Unirest
        .post(NOTIFY_SERVICE_URL_UPDATE_STATUS_SEND_SMS_HISTORY)
        .header("Content-Type", "application/json")
        .header(authorizationKey, authorizationValue)
        .body(JsonUtils.toJson(payLoadHistoryStatusEmail))
        .asJson();
    if (response != null) {
      JsonNode jsonNode = response.getBody();
      if (jsonNode != null) {
        logger.info("response from api updateHistoryStatusSendEmail to notify service: {} {}",
            response.getStatus(),
            jsonNode.toString());
      }
      if (response.getStatus() != 200) {
        throw new Exception("Send updateHistoryStatusSendEmail fail");
      }
    }
  }
}
