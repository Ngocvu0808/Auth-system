package com.ttt.mar.auth.mapper;


import com.ttt.mar.auth.dto.app.ClientCustomDto;
import com.ttt.mar.auth.dto.app.ClientDetailDto;
import com.ttt.mar.auth.dto.app.ClientResponseDto;
import com.ttt.mar.auth.entities.application.Client;
import java.util.Date;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class ClientMapper {

  @Mapping(source = "createdTime", target = "createdTime", resultType = Long.class)
  @Mapping(target = "creatorName", source = "creatorUser.username")
  public abstract ClientDetailDto toDto(Client entity);

  public abstract ClientCustomDto toCustomDto(Client entity);

  public abstract Client fromDto(ClientDetailDto dto);

  @Mapping(source = "createdTime", target = "createdTime", resultType = Long.class)
  @Mapping(target = "creatorName", source = "creatorUser.username")
  public abstract ClientResponseDto getClient(Client entity);

  public abstract void updateModel(@MappingTarget Client entity, ClientDetailDto dto);

  Long map(Date value) {
    if (value == null) {
      return null;
    }
    return value.getTime();
  }

  Date map(Long value) {
    if (value == null) {
      return null;
    }
    return new Date(value);
  }

}
