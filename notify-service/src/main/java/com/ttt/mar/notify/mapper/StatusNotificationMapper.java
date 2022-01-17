package com.ttt.mar.notify.mapper;

import com.ttt.mar.notify.dto.statusnotify.StatusNotificationResponseDto;
import com.ttt.mar.notify.entities.StatusNotification;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class StatusNotificationMapper {

  public abstract StatusNotificationResponseDto toDto(StatusNotification stauStatusNotification);

}
