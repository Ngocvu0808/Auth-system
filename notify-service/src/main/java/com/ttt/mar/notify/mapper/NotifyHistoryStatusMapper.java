package com.ttt.mar.notify.mapper;

import com.google.gson.Gson;
import com.ttt.mar.notify.dto.notify.Message;
import com.ttt.mar.notify.dto.notify.NotifyHistoryStatusResponse;
import com.ttt.mar.notify.entities.NotifyHistoryStatus;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public abstract class NotifyHistoryStatusMapper {

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
      .getLogger(NotifyHistoryStatusMapper.class);

  public static final String MASSAGE_KEY = "invalidEmails";

  @Mapping(target = "message", source = "message", ignore = true)
  public abstract NotifyHistoryStatusResponse toDto(NotifyHistoryStatus entity);

  @AfterMapping
  public void afterMapping(@MappingTarget NotifyHistoryStatusResponse dto,
      NotifyHistoryStatus entity) {
    String messageValue = entity.getMessage();
    System.out.println(messageValue);
    if (StringUtils.isBlank(messageValue)) {
      return;
    }
    logger.info("messageValue: {}", messageValue);
    Message message = new Message();
    if (messageValue.contains(MASSAGE_KEY)) {
      logger.info("messageValue contains {}", MASSAGE_KEY);
      try {
        Map<String, List<String>> map = new Gson().fromJson(messageValue,Map.class);
        if (map.containsKey(MASSAGE_KEY)) {
          List<String> contentMassageList = map.get(MASSAGE_KEY);
          message.setInvalidEmails(contentMassageList);
        }
      } catch (Exception e) {
        logger.error(e.getMessage());
        message.setMessage(messageValue);
      }
    } else {
      logger.info("messageValue not contains {}", MASSAGE_KEY);
      message.setMessage(messageValue);
    }
    dto.setMessage(message);
  }
}
