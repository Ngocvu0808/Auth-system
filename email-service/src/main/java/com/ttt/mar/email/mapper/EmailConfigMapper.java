package com.ttt.mar.email.mapper;

import com.ttt.mar.email.dto.emailconfig.EmailConfigCreateRequestDto;
import com.ttt.mar.email.dto.emailconfig.EmailConfigDetailResponseDto;
import com.ttt.mar.email.dto.emailconfig.EmailConfigResponseDto;
import com.ttt.mar.email.dto.emailconfig.EmailConfigUpdateRequestDto;
import com.ttt.mar.email.entities.EmailConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {EmailPublisherMapper.class})
public abstract class EmailConfigMapper {

  @Mapping(target = "emailPublisherPublicResponseDto", source = "emailPublisher")
  public abstract EmailConfigDetailResponseDto toDto(EmailConfig emailConfig);

  @Mapping(target = "token", source = "token", ignore = true)
  public abstract EmailConfig fromDto(EmailConfigCreateRequestDto dto);

  @Mapping(target = "creatorName", source = "creatorUser.username")
  @Mapping(target = "publisherName", source = "emailPublisher.name")
  public abstract EmailConfigResponseDto toEmailConfigResponse(EmailConfig emailConfig);

  @Mapping(target = "accountCode", source = "accountCode", ignore = true)
  public abstract void updateModel(@MappingTarget EmailConfig entity,
      EmailConfigUpdateRequestDto dto);


}
