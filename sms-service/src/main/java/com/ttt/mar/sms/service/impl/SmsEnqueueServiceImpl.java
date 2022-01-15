package com.ttt.mar.sms.service.impl;

import com.ttt.mar.sms.config.ServiceMessageCode;
import com.ttt.mar.sms.dto.inforsms.PayloadRequestFromNotifyServiceDto;
import com.ttt.mar.sms.entities.SmsConfig;
import com.ttt.mar.sms.entities.SmsPublisher;
import com.ttt.mar.sms.repositories.SmsConfigRepository;
import com.ttt.mar.sms.service.iface.SmsEnQueueService;
import com.ttt.mar.sms.service.iface.kafka.ProducerService;
import com.ttt.rnd.common.config.ServiceInfo;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SmsEnqueueServiceImpl implements SmsEnQueueService {

  @Autowired
  private SmsConfigRepository smsConfigRepository;

  @Autowired
  private ProducerService<PayloadRequestFromNotifyServiceDto> producerService;

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
      .getLogger(SmsEnqueueServiceImpl.class);


  @Override
  public Map<String, Object> receiveInforSmsFromNotifyService(
      PayloadRequestFromNotifyServiceDto payloadRequestFromNotifyServiceDto)
      throws ResourceNotFoundException, OperationNotImplementException {
    Map<String, Object> response = checkValidInforSmsFromNotifyService(
        payloadRequestFromNotifyServiceDto);

    String receivers = payloadRequestFromNotifyServiceDto.getReceiver();
    String[] receiverSplits = receivers.split(",");
    logger.info("receiverSplits: {}", receiverSplits);

    List<String> listReceiverValid = new ArrayList<>();
    for (String receiver : receiverSplits) {
      if (!receiver.isBlank()) {
        listReceiverValid.add(receiver);
      }
    }

    if (listReceiverValid.isEmpty()) {
      throw new OperationNotImplementException("receiver invalid",
          ServiceInfo.getId() + ServiceMessageCode.OPERATION_NOT_IMPLEMENT);
    }

    if (listReceiverValid.size() == 1) {
      payloadRequestFromNotifyServiceDto.setReceiver(listReceiverValid.get(0));
    }
    if (listReceiverValid.size() > 1) {
      String receiverValid = "";
      for (int index = 0; index < listReceiverValid.size(); index++) {
        if (index == listReceiverValid.size() - 1) {
          receiverValid += listReceiverValid.get(index);
        } else {
          receiverValid += listReceiverValid.get(index) + ",";

        }
      }
      logger.info("listReceiverValid: {}", listReceiverValid);
      if (!receiverValid.isEmpty()) {
        payloadRequestFromNotifyServiceDto.setReceiver(receiverValid);
      }

    }

    Boolean resultSend = producerService.sendData(payloadRequestFromNotifyServiceDto);

    if (resultSend) {
      return response;
    }
    return null;
  }

  public Map<String, Object> checkValidInforSmsFromNotifyService(
      PayloadRequestFromNotifyServiceDto payloadRequestFromNotifyServiceDto)
      throws OperationNotImplementException, ResourceNotFoundException {

    // check valid body request
    if (payloadRequestFromNotifyServiceDto == null) {
      throw new OperationNotImplementException("Body request null",
          ServiceInfo.getId() + ServiceMessageCode.OPERATION_NOT_IMPLEMENT);
    }
    if (payloadRequestFromNotifyServiceDto.getAccountCode() == null) {
      throw new OperationNotImplementException("accountCode null",
          ServiceInfo.getId() + ServiceMessageCode.OPERATION_NOT_IMPLEMENT);
    }
    if (payloadRequestFromNotifyServiceDto.getContent() == null
        || payloadRequestFromNotifyServiceDto.getContent().isEmpty()) {
      throw new OperationNotImplementException("content null",
          ServiceInfo.getId() + ServiceMessageCode.OPERATION_NOT_IMPLEMENT);
    }
    if (payloadRequestFromNotifyServiceDto.getReceiver() == null
        || payloadRequestFromNotifyServiceDto.getReceiver().isEmpty()) {
      throw new OperationNotImplementException("receiver null",
          ServiceInfo.getId() + ServiceMessageCode.OPERATION_NOT_IMPLEMENT);
    }
    if (payloadRequestFromNotifyServiceDto.getReceiver().length() < 10
        || payloadRequestFromNotifyServiceDto.getReceiver().length() > 11) {
      throw new OperationNotImplementException("receiver invalid");
    }
    char firstCharacterNumber = payloadRequestFromNotifyServiceDto.getReceiver().charAt(0);
    if (firstCharacterNumber != '0') {
      throw new OperationNotImplementException("receiver invalid");

    }
    SmsConfig smsConfigByAccountCode = smsConfigRepository
        .findByAccountCodeAndIsDeletedFalse(payloadRequestFromNotifyServiceDto.getAccountCode());

    if (smsConfigByAccountCode == null) {
      throw new ResourceNotFoundException("accountCode not found",
          ServiceInfo.getId() + ServiceMessageCode.RESOURCE_NOT_FOUND);
    }

    SmsPublisher smsPublisher = smsConfigByAccountCode.getSmsPublisher();
    if (smsPublisher == null || smsPublisher.getIsDeleted()) {
      throw new ResourceNotFoundException("publisher not found",
          ServiceInfo.getId() + ServiceMessageCode.RESOURCE_NOT_FOUND);
    }

    Map<String, Object> response = new HashMap<>();
    response.put("brandName", smsConfigByAccountCode.getBrandName());
    response.put("publisher", smsConfigByAccountCode.getSmsPublisher().getName());
    response.put("phoneNumber", smsConfigByAccountCode.getPhoneNumber());
    logger.info("response: {}", response);

    return response;
  }
}
