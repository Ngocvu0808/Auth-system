package com.ttt.mar.notify.service.impl;

import com.ttt.mar.notify.dto.statusnotify.StatusNotificationResponseDto;
import com.ttt.mar.notify.entities.StatusNotification;
import com.ttt.mar.notify.mapper.StatusNotificationMapper;
import com.ttt.mar.notify.repositories.StatusNotifiCationRepository;
import com.ttt.mar.notify.service.iface.StatusNotificationService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatusNotificationServiceImpl implements StatusNotificationService {

  @Autowired
  private StatusNotifiCationRepository statusNotifiCationRepository;

  @Autowired
  private StatusNotificationMapper statusNotificationMapper;

  @Override
  public List<StatusNotificationResponseDto> getAll() {

    List<StatusNotificationResponseDto> statusNotificationResponseDtos = new ArrayList<>();

    List<StatusNotification> listStatus = statusNotifiCationRepository.findAll();

    listStatus.forEach(
        statusNotification -> statusNotificationResponseDtos
            .add(statusNotificationMapper.toDto(statusNotification)));

    return statusNotificationResponseDtos;
  }
}
