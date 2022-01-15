package com.ttt.mar.sms.mapper;

import com.ttt.mar.sms.dto.smpublisher.SmsPublisherCustomResponseDto;
import com.ttt.mar.sms.dto.smpublisher.SmsPublisherRequestDto;
import com.ttt.mar.sms.dto.smpublisher.SmsPublisherResponseDto;
import com.ttt.mar.sms.entities.SmsPublisher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public abstract class SmsPublisherMapper {

  @Mapping(target = "creatorName", source = "creatorUser.username")
  public abstract SmsPublisherResponseDto toDto(SmsPublisher smsPublisher);

  public abstract SmsPublisher fromDto(SmsPublisherRequestDto publisherRequestDto);

  @Mapping(target = "code", source = "code", ignore = true)
  public abstract void updateModel(@MappingTarget SmsPublisher smsPublisher,
      SmsPublisherRequestDto smsPublisherRequestDto);


  public abstract SmsPublisherCustomResponseDto toSmsPublisherCustomResponseDto(
      SmsPublisher smsPublisher);
}
