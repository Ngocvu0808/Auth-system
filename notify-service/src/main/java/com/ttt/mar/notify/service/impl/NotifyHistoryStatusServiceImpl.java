package com.ttt.mar.notify.service.impl;

import com.ttt.mar.notify.config.NotifyMessageCode;
import com.ttt.mar.notify.dto.notify.PayLoadStatusHistoryUpdateDto;
import com.ttt.mar.notify.entities.NotifyHistory;
import com.ttt.mar.notify.entities.NotifyHistoryStatus;
import com.ttt.mar.notify.entities.StatusNotification;
import com.ttt.mar.notify.repositories.NotifyHistoryRepository;
import com.ttt.mar.notify.repositories.NotifyHistoryStatusRepository;
import com.ttt.mar.notify.repositories.StatusNotifiCationRepository;
import com.ttt.mar.notify.service.iface.NotifyHistoryStatusService;
import com.ttt.rnd.common.config.ServiceInfo;
import com.ttt.rnd.common.exception.IdentifyBlankException;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotifyHistoryStatusServiceImpl implements NotifyHistoryStatusService {

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
      .getLogger(NotifyHistoryStatusServiceImpl.class);

  @Autowired
  private NotifyHistoryStatusRepository notifyHistoryStatusRepository;

  @Autowired
  private StatusNotifiCationRepository statusNotifyCationRepository;

  @Autowired
  private NotifyHistoryRepository notifyHistoryRepository;


  private String OTHER_STATUS_HISTORY_NOTIFICATION = "OTHER";

  @Override
  public Boolean updateStatusHistory(PayLoadStatusHistoryUpdateDto payLoadStatusHistoryUpdateDto)
      throws ResourceNotFoundException, IdentifyBlankException, OperationNotImplementException {

    checkValidPayLoadStatusHistoryUpdate(payLoadStatusHistoryUpdateDto);

    NotifyHistory notifyHistory = notifyHistoryRepository
        .findByIdAndIsDeletedFalse(payLoadStatusHistoryUpdateDto.getRequestId());

    if (notifyHistory == null) {
      throw new ResourceNotFoundException("notifyHistory not found",
          ServiceInfo.getId() + NotifyMessageCode.RESOURCE_NOT_FOUND);
    }

    StatusNotification statusNotification = statusNotifyCationRepository
        .findByCode(payLoadStatusHistoryUpdateDto.getStatus());
    logger.info("statusNotification: {}", payLoadStatusHistoryUpdateDto.getStatus());


    logger.info("payLoadStatusHistoryUpdateDto: {}", payLoadStatusHistoryUpdateDto);

    if (statusNotification != null) {
      logger.info("payLoadStatusHistoryUpdateDto: {}", payLoadStatusHistoryUpdateDto.getStatus());
      NotifyHistoryStatus notifyHistoryStatus = new NotifyHistoryStatus();
      notifyHistoryStatus.setNotifyHistory(notifyHistory);
      notifyHistoryStatus.setStatusNotification(statusNotification);
      notifyHistoryStatus.setStatus(statusNotification.getName());
      notifyHistoryStatus.setCreatedTime(new Date(System.currentTimeMillis()));
      notifyHistoryStatus.setMessage(payLoadStatusHistoryUpdateDto.getMessage());
      notifyHistoryStatusRepository.save(notifyHistoryStatus);
    }
//    else {
//      StatusNotification otherStatus = statusNotifyCationRepository
//          .findByName(OTHER_STATUS_HISTORY_NOTIFICATION);
//      NotifyHistoryStatus notifyHistoryStatus = new NotifyHistoryStatus();
//      notifyHistoryStatus.setNotifyHistory(notifyHistory);
//      notifyHistoryStatus.setStatusNotification(otherStatus);
//      notifyHistoryStatus.setStatus(payLoadStatusHistoryUpdateDto.getStatus());
//      notifyHistoryStatus.setCreatedTime(new Date(System.currentTimeMillis()));
//      notifyHistoryStatus.setMessage(payLoadStatusHistoryUpdateDto.getMessage());
//      notifyHistoryStatusRepository.save(notifyHistoryStatus);
//    }

    return true;
  }

  public void checkValidPayLoadStatusHistoryUpdate(
      PayLoadStatusHistoryUpdateDto payLoadStatusHistoryUpdateDto)
      throws OperationNotImplementException {
    if (payLoadStatusHistoryUpdateDto == null) {
      throw new OperationNotImplementException("body null",
          ServiceInfo.getId() + NotifyMessageCode.OPERATION_NOT_IMPLEMENT);
    }

    if (payLoadStatusHistoryUpdateDto.getStatus() == null || payLoadStatusHistoryUpdateDto
        .getStatus().isEmpty()) {
      throw new OperationNotImplementException("status null",
          ServiceInfo.getId() + NotifyMessageCode.OPERATION_NOT_IMPLEMENT);
    }

  }
}
