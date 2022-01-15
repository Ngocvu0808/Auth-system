package com.ttt.mar.sms.service.iface.kafka;

public interface ProducerService<T> {

  Boolean sendData(T t);

  Boolean sendDataError(T t, String topic);
}
