package com.ttt.mar.email.mapper;

import com.ttt.mar.email.dto.emailpublisher.EmailPublisherCreateRequestDto;
import com.ttt.mar.email.dto.emailpublisher.EmailPublisherPublicResponseDto;
import com.ttt.mar.email.dto.emailpublisher.EmailPublisherResponseDto;
import com.ttt.mar.email.dto.emailpublisher.EmailPublisherUpdateRequestDto;
import com.ttt.mar.email.entities.EmailProtocol;
import com.ttt.mar.email.entities.EmailPublisher;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public abstract class EmailPublisherMapper {

  @Mapping(target = "endPoint", source = "endPoint", ignore = true)
  @Mapping(target = "port", source = "port", ignore = true)
  @Mapping(target = "host", source = "host", ignore = true)
  public abstract EmailPublisher fromDto(EmailPublisherCreateRequestDto dto);

  @Mapping(target = "endPoint", source = "endPoint", ignore = true)
  @Mapping(target = "port", source = "port", ignore = true)
  @Mapping(target = "host", source = "host", ignore = true)
  @Mapping(target = "id", source = "id", ignore = true)
  @Mapping(target = "code", source = "code", ignore = true)
  public abstract void updateFromDto(EmailPublisherUpdateRequestDto dto,
      @MappingTarget EmailPublisher entity);

  @Mapping(target = "creatorName", source = "creatorUser.username")
  public abstract EmailPublisherResponseDto toDto(EmailPublisher entity);

  public abstract EmailPublisherPublicResponseDto toDtoPublic(EmailPublisher entity);

  @AfterMapping
  public <T extends EmailPublisherCreateRequestDto> void afterMapping(
      @MappingTarget EmailPublisher entity, T dto) {
    if (EmailProtocol.API.equals(dto.getProtocol())) {
      entity.setEndPoint(dto.getEndPoint());
      entity.setHost(null);
      entity.setPort(null);
    } else {
      entity.setEndPoint(null);
      entity.setHost(dto.getHost());
      entity.setPort(dto.getPort());
    }
  }
}
