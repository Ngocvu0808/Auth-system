package com.ttt.mar.leads.service.impl.kafka;

import com.ttt.mar.leads.service.iface.kafka.producer.ProducerService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProducerServiceImpl implements ProducerService<Object> {

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
      .getLogger(ProducerServiceImpl.class);


  private final KafkaTemplate<Object, Object> kafkaTemplate;

  public ProducerServiceImpl(KafkaTemplate<Object, Object> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  public Boolean sendMessage(Object object, String topic) {
    try {
      logger.info("Topic: {}", topic);
      kafkaTemplate.send(topic, object);
      return true;
    } catch (Exception e) {
      logger.error("error detail: {}", e.getMessage());
      return false;
    }
  }


  public Boolean sendDataError(Object object, String topic) {
    try {
      kafkaTemplate.send(topic, object);
      return true;
    } catch (Exception e) {
      logger.error("error detail: {}", e.getMessage());
      return false;
    }
  }
}
