package com.ttt.mar.sms.service.impl.kafka;

import com.ttt.mar.sms.dto.inforsms.PayloadRequestFromNotifyServiceDto;
import com.ttt.mar.sms.service.iface.kafka.ProducerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProducerServiceImpl implements ProducerService<PayloadRequestFromNotifyServiceDto> {

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
      .getLogger(ProducerServiceImpl.class);

  private final KafkaTemplate<String, Object> kafkaTemplate;

  public ProducerServiceImpl(KafkaTemplate<String, Object> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  @Value("${sms-service.topics.default-topic: sms-infors}")
  private String topicReceiveSms;

  @Override
  public Boolean sendData(PayloadRequestFromNotifyServiceDto payloadRequestFromNotifyServiceDto) {
    try {
      kafkaTemplate.send(topicReceiveSms, payloadRequestFromNotifyServiceDto);
      return true;
    } catch (Exception ex) {
      logger.error("error detail: {}", ex.getMessage());
      return false;
    }
  }

  @Override
  public Boolean sendDataError(
      PayloadRequestFromNotifyServiceDto payloadRequestFromNotifyServiceDto, String topic) {
    try {
      kafkaTemplate.send(topic, payloadRequestFromNotifyServiceDto);
      return true;
    } catch (Exception ex) {
      logger.error("error detail: {}", ex.getMessage());
      return false;
    }
  }
}
