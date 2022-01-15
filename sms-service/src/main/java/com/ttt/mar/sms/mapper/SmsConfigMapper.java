package com.ttt.mar.sms.mapper;

import com.ttt.mar.sms.dto.smsconfig.SmsConfigDtoRequest;
import com.ttt.mar.sms.dto.smsconfig.SmsConfigDtoResponse;
import com.ttt.mar.sms.dto.smsconfig.SmsConfigResponseDetailDto;
import com.ttt.mar.sms.entities.SmsConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public abstract class SmsConfigMapper {

  public abstract SmsConfigDtoRequest toDto(SmsConfig smsConfig);

  public abstract SmsConfig fromDto(SmsConfigDtoRequest smsConfigDtoRequest);

  @Mapping(source = "smsPublisher.name", target = "publisherName")
  @Mapping(source = "creatorUser.username", target = "creatorName")
  public abstract SmsConfigDtoResponse toSmsConfigResponse(SmsConfig smsConfig);

  @Mapping(target = "accountCode", source = "accountCode", ignore = true)
  public abstract void updateModel(@MappingTarget SmsConfig smsConfig,
      SmsConfigDtoRequest smsConfigDtoRequest);

  @Mapping(source = "smsPublisher", target = "publisher")
  public abstract SmsConfigResponseDetailDto toSmsConfigResponseDetailDto(SmsConfig smsConfig);
}
