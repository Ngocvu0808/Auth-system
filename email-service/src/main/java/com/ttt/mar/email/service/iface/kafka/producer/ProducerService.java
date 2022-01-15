package com.ttt.mar.email.service.iface.kafka.producer;

public interface ProducerService<T> {

  Boolean sendMessage(T t);

  Boolean sendDataError(T t, String topic);
}
