package com.ttt.mar.auth.mapper;

import com.ttt.mar.auth.dto.app.ClientApiKeyResponseDto;
import com.ttt.mar.auth.entities.application.ClientApiKey;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class ClientApiKeyMapper {

  @Mapping(target = "creatorName", source = "creatorUser.username")
  public abstract ClientApiKeyResponseDto toDto(ClientApiKey entity);
}
