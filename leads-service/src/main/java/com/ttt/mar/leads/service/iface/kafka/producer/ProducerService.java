package com.ttt.mar.leads.service.iface.kafka.producer;

public interface ProducerService<T> {

  Boolean sendMessage(T t, String topic);

  Boolean sendDataError(T t, String topic);
}
