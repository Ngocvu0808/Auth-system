package com.ttt.mar.sms.service.impl;

import com.ttt.mar.sms.dto.inforsms.PayLoadSendToAdapter;
import com.ttt.mar.sms.exception.AdapterException;
import com.ttt.mar.sms.service.iface.SmsSendInforToAdapterService;
import com.ttt.rnd.common.utils.JsonUtils;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsSendInforToAdapterServiceImpl implements SmsSendInforToAdapterService {

  @Value("${adapter-service.authorization-key:}")
  private String authorizationKey;

  @Value("${adapter-service.authorization-value:}")
  private String authorizationValue;

  @Value("${adapter-service.url-sms}")
  private String URL_SMS_ADAPTER_SERVICE;

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
      .getLogger(SmsSendInforToAdapterServiceImpl.class);

  @Override
  public void sendInforSmsToAdapterService(PayLoadSendToAdapter payLoadSenToAdapter)
      throws Exception {

    logger.info("start send infor to adapter: {} ", payLoadSenToAdapter);

    HttpResponse<JsonNode> response = Unirest.post(URL_SMS_ADAPTER_SERVICE)
        .header("Content-Type", "application/json")
        .header(authorizationKey, authorizationValue)
        .body(JsonUtils.toJson(payLoadSenToAdapter))
        .asJson();
    if (response != null) {
      JsonNode jsonNode = response.getBody();
      if (jsonNode != null) {
        logger.info("response from adapter-service: {} {}", response.getStatus(),
            jsonNode.toString());
      }
      if (response.getStatus() != 200) {
        throw new AdapterException("Adapter-service unreceived sms");
      }
      Boolean status = (Boolean) response.getBody().getObject().get("status");
      if (status == null || status.equals(Boolean.FALSE)) {
        throw new AdapterException("Adapter-service unreceived sms");
      }

    }
  }
}
