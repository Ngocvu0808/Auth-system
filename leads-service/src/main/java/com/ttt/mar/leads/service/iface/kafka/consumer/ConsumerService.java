package com.ttt.mar.leads.service.iface.kafka.consumer;


import com.ttt.rnd.common.exception.OperationNotImplementException;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;

public interface ConsumerService<T> {

  HttpResponse<JsonNode> consume(T t, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) throws OperationNotImplementException;

}
