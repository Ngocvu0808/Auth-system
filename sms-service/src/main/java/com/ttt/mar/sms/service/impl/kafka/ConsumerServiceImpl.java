package com.ttt.mar.sms.service.impl.kafka;

import com.ttt.mar.sms.config.ServiceMessageCode;
import com.ttt.mar.sms.dto.inforsms.MessageOfStatus;
import com.ttt.mar.sms.dto.inforsms.PayLoadHistoryStatusSms;
import com.ttt.mar.sms.dto.inforsms.PayLoadSendToAdapter;
import com.ttt.mar.sms.dto.inforsms.PayloadRequestFromNotifyServiceDto;
import com.ttt.mar.sms.entities.SmsConfig;
import com.ttt.mar.sms.entities.SmsPublisher;
import com.ttt.mar.sms.exception.AdapterException;
import com.ttt.mar.sms.repositories.SmsConfigRepository;
import com.ttt.mar.sms.service.iface.SmsHistoryService;
import com.ttt.mar.sms.service.iface.SmsSendInforToAdapterService;
import com.ttt.mar.sms.service.iface.kafka.ConsumerService;
import com.ttt.mar.sms.service.iface.kafka.ProducerService;
import com.ttt.rnd.common.config.ServiceInfo;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import com.ttt.rnd.common.utils.JsonUtils;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
public class ConsumerServiceImpl implements ConsumerService<PayloadRequestFromNotifyServiceDto> {

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
      .getLogger(ConsumerServiceImpl.class);

  @Autowired
  private SmsSendInforToAdapterService smsSendInforToAdapterService;

  @Autowired
  private SmsHistoryService smsHistoryService;

  @Autowired
  private SmsConfigRepository smsConfigRepository;

  @Autowired
  private ProducerService<PayloadRequestFromNotifyServiceDto> producerService;

  @Value("${adapter-service.authorization-key:}")
  private String authorizationKey;

  @Value("${adapter-service.authorization-value:}")
  private String authorizationValue;

  @Value(("${notify-service.url-update-status}"))
  private String NOTIFY_SERVICE_URL_UPDATE_STATUS_SEND_SMS_HISTORY;

  private static final String STATUS_ERROR_CODE = "ERROR";

  @KafkaListener(topics = "#{'${sms-service.topics.sms-infor}'.split(',')}")
  @Override
  public void consume(PayloadRequestFromNotifyServiceDto data,
      @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {

    try {

      SmsConfig smsConfig = checkAndGetSmsConfigValidByAccountCode(data.getAccountCode());
      PayLoadSendToAdapter payLoadSenToAdapter = convertPayLoadFromNotifyServiceToPayLoadSendAdapterService(
          smsConfig, data);
      smsSendInforToAdapterService.sendInforSmsToAdapterService(payLoadSenToAdapter);
      smsHistoryService.addHistorySentSms(smsConfig, payLoadSenToAdapter.getContent(),
          payLoadSenToAdapter.getReceiver());
    } catch (OperationNotImplementException ex) {
      try {
        MessageOfStatus messageOfStatus = new MessageOfStatus();
        messageOfStatus.setMessage(ex.getMessage());
        updateStatusHistoryToNotifyService(data, STATUS_ERROR_CODE, messageOfStatus);
      } catch (Exception e) {
        logger.warn("error detail: {}", ex.getMessage());
      }
      logger.warn("error detail: {}", ex.getMessage());
    } catch (ResourceNotFoundException ex) {
      try {
        MessageOfStatus messageOfStatus = new MessageOfStatus();
        messageOfStatus.setMessage(ex.getMessage());
        updateStatusHistoryToNotifyService(data, STATUS_ERROR_CODE, messageOfStatus);
      } catch (Exception e) {
        logger.warn("error detail: {}", ex.getMessage());
      }
    } catch (AdapterException ex) {
      producerService.sendDataError(data, "err-" + topic);
    } catch (Exception ex) {
      try {
        MessageOfStatus messageOfStatus = new MessageOfStatus();
        messageOfStatus.setMessage(ex.getMessage());
        updateStatusHistoryToNotifyService(data, STATUS_ERROR_CODE, messageOfStatus);
      } catch (Exception e) {
        logger.warn("error detail: {}", ex.getMessage());
      }
      producerService.sendDataError(data, "err-" + topic);
    }

  }

  public void updateStatusHistoryToNotifyService(PayloadRequestFromNotifyServiceDto smsRequest,
      String status,
      MessageOfStatus message)
      throws Exception {
    PayLoadHistoryStatusSms payLoadHistoryStatusSms = new PayLoadHistoryStatusSms();
    payLoadHistoryStatusSms.setRequestId(smsRequest.getIdRequest());
    payLoadHistoryStatusSms.setStatus(status);
    payLoadHistoryStatusSms.setMessage(message.getMessage());
    HttpResponse<JsonNode> response = Unirest
        .post(NOTIFY_SERVICE_URL_UPDATE_STATUS_SEND_SMS_HISTORY)
        .header("Content-Type", "application/json")
        .header(authorizationKey, authorizationValue)
        .body(JsonUtils.toJson(payLoadHistoryStatusSms))
        .asJson();
    if (response != null) {
      JsonNode jsonNode = response.getBody();
      if (jsonNode != null) {
        logger.info("response from api updateHistoryStatusSendSms to notify service: {} {}",
            response.getStatus(),
            jsonNode.toString());
      }
      if (response.getStatus() != 200) {
        throw new Exception("Send updateHistoryStatusSendSms fail");
      }
    }
  }

  public PayLoadSendToAdapter convertPayLoadFromNotifyServiceToPayLoadSendAdapterService(
      SmsConfig smsConfig,
      PayloadRequestFromNotifyServiceDto payloadRequestFromNotifyServiceDto) {

    if (payloadRequestFromNotifyServiceDto == null || smsConfig == null) {
      return null;
    }
    PayLoadSendToAdapter payLoadSenToAdapter = new PayLoadSendToAdapter();
    payLoadSenToAdapter.setContent(payloadRequestFromNotifyServiceDto.getContent());
    payLoadSenToAdapter.setReceiver(payloadRequestFromNotifyServiceDto.getReceiver());
    payLoadSenToAdapter.setRequestId(payloadRequestFromNotifyServiceDto.getIdRequest());
    payLoadSenToAdapter.setUsername(smsConfig.getUsername());
    payLoadSenToAdapter.setUsername(smsConfig.getPassword());
    payLoadSenToAdapter.setEndPoint(smsConfig.getSmsPublisher().getEndPoint());
    payLoadSenToAdapter.setToken(smsConfig.getToken());
    payLoadSenToAdapter.setPhoneNumber(smsConfig.getPhoneNumber());
    payLoadSenToAdapter.setBrandName(smsConfig.getBrandName());
    payLoadSenToAdapter.setPublisherCode(smsConfig.getSmsPublisher().getCode());
    return payLoadSenToAdapter;
  }

  public SmsConfig checkAndGetSmsConfigValidByAccountCode(String accountCode)
      throws ResourceNotFoundException, OperationNotImplementException {

    logger.info("accountCode: {}", accountCode);
    if (accountCode == null) {
      throw new OperationNotImplementException("accountCode null",
          ServiceInfo.getId() + ServiceMessageCode.OPERATION_NOT_IMPLEMENT);
    }
    SmsConfig smsConfig = smsConfigRepository
        .findByAccountCodeAndIsDeletedFalse(accountCode);
    if (smsConfig == null) {
      throw new ResourceNotFoundException("smsConfig not found",
          ServiceInfo.getId() + ServiceMessageCode.RESOURCE_NOT_FOUND);
    }
    SmsPublisher smsPublisher = smsConfig.getSmsPublisher();
    if (smsPublisher == null || smsPublisher.getIsDeleted()) {
      throw new ResourceNotFoundException("Publisher not found",
          ServiceInfo.getId() + ServiceMessageCode.RESOURCE_NOT_FOUND);
    }
    return smsConfig;
  }

}
