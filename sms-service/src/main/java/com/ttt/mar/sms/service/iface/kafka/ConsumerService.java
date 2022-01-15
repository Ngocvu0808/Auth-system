package com.ttt.mar.sms.service.iface.kafka;

import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;

public interface ConsumerService<T> {

  void consume(T t, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic);

}
