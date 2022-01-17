package com.ttt.mar.notify.mapper;

import com.google.gson.Gson;
import com.ttt.mar.notify.dto.notify.ContentNotifyResponse;
import com.ttt.mar.notify.dto.notify.NotifyHistoryDetailResponseDto;
import com.ttt.mar.notify.dto.notify.NotifyHistoryResponseDto;
import com.ttt.mar.notify.dto.notify.NotifyHistoryStatusResponse;
import com.ttt.mar.notify.entities.NotifyHistory;
import com.ttt.mar.notify.entities.NotifyHistoryStatus;
import com.ttt.rnd.lib.entities.User;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {NotifyHistoryStatusMapper.class})
public abstract class NotifyHistoryMapper {
  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
      .getLogger(NotifyHistoryMapper.class);

  @Mapping(target = "typeSendName", source = "typeSend.name")
  @Mapping(target = "creatorName", source = "creatorUser.username")
  @Mapping(target = "content", source = "content", ignore = true)
  public abstract NotifyHistoryResponseDto toDto(NotifyHistory entity);

  @Mapping(target = "typeSendName", source = "typeSend.name")
  @Mapping(target = "creatorName", source = "creatorUser.username")
  @Mapping(target = "typeSendCode", source = "typeSend.code")
  @Mapping(target = "content", source = "content", ignore = true)
  public abstract NotifyHistoryDetailResponseDto toDtoDetail(NotifyHistory entity);

  @AfterMapping
  public void afterMapping(@MappingTarget NotifyHistoryResponseDto dto, NotifyHistory entity) {
    List<NotifyHistoryStatus> notifyHistoryStatuses = entity.getNotifyHistoryStatuses();
    if (!notifyHistoryStatuses.isEmpty()) {
      notifyHistoryStatuses = notifyHistoryStatuses.stream()
          .sorted((o1, o2) -> o2.getId().compareTo(o1.getId()))
          .collect(Collectors.toList());
      NotifyHistoryStatus notifyHistoryStatusLast = notifyHistoryStatuses.get(0);
      if (notifyHistoryStatusLast != null) {
        dto.setStatus(notifyHistoryStatusLast.getStatus());
        dto.setStatusId(notifyHistoryStatusLast.getStatusNotificationId());
      }
    }
    if (StringUtils.isNotBlank(entity.getContent())) {
      logger.info("Content: {}", entity.getContent());
      try {
        ContentNotifyResponse emailContentRequestDto = new Gson()
            .fromJson(entity.getContent(), ContentNotifyResponse.class);
        dto.setContent(emailContentRequestDto.getBody());
      } catch (Exception e) {
        logger.error(e.getMessage());
      }
    }
  }

  @AfterMapping
  public void afterMapping(@MappingTarget NotifyHistoryDetailResponseDto dto, NotifyHistory entity) {
    List<NotifyHistoryStatusResponse> notifyHistoryStatuses = dto.getNotifyHistoryStatuses();
    if (!notifyHistoryStatuses.isEmpty()) {
      notifyHistoryStatuses = notifyHistoryStatuses.stream()
          .sorted((o1, o2) -> o2.getId().compareTo(o1.getId()))
          .collect(Collectors.toList());
      NotifyHistoryStatusResponse notifyHistoryStatusLast = notifyHistoryStatuses.get(0);
      if (notifyHistoryStatusLast != null) {
        dto.setStatus(notifyHistoryStatusLast.getStatus());
      }
      dto.setNotifyHistoryStatuses(notifyHistoryStatuses);
    }
    if (StringUtils.isNotBlank(entity.getContent())) {
      logger.info("Content: {}", entity.getContent());
      try {
        ContentNotifyResponse emailContentRequestDto = new Gson()
            .fromJson(entity.getContent(), ContentNotifyResponse.class);
        dto.setContent(emailContentRequestDto);
      } catch (Exception e) {
        logger.error(e.getMessage());
      }
    }
  }
}
