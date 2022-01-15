package com.ttt.mar.email.service.impl.kafka;

import com.ttt.mar.email.dto.mail.EmailRequestSendDto;
import com.ttt.mar.email.service.iface.kafka.producer.ProducerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProducerServiceImpl implements ProducerService<EmailRequestSendDto> {

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
      .getLogger(ProducerServiceImpl.class);

  private final KafkaTemplate<String, Object> kafkaTemplate;

  public ProducerServiceImpl(KafkaTemplate<String, Object> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  @Value("${email-service.topics.default-topic: email-info}")
  private String topicReceiveEmail;

  @Override
  public Boolean sendMessage(
      EmailRequestSendDto data) {
    try {
      kafkaTemplate.send(topicReceiveEmail, data);
      return true;
    } catch (Exception e) {
      logger.error("error detail: {}", e.getMessage());
      return false;
    }
  }

  @Override
  public Boolean sendDataError(EmailRequestSendDto data, String topic) {
    try {
      kafkaTemplate.send(topic, data);
      return true;
    } catch (Exception ex) {
      logger.error("error detail: {}", ex.getMessage());
      return false;
    }
  }
}
