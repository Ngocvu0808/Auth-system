package com.ttt.mar.auth.mapper;

import com.ttt.mar.auth.dto.api.ApiRequestDto;
import com.ttt.mar.auth.entities.application.ApiRequest;
import java.util.Date;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", uses = {ServiceMapper.class, SystemMapper.class,
    ClientMapper.class, ExternalApiMapper.class},
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class ApiRequestMapper {

  @Mapping(target = "apiId", source = "api.id")
  @Mapping(target = "name", source = "api.name")
  @Mapping(target = "code", source = "api.code")
  @Mapping(target = "api", source = "api.api")
  @Mapping(target = "method", source = "api.method")
  @Mapping(target = "type", source = "api.type")
  @Mapping(target = "system", source = "api.service.system")
  @Mapping(target = "service", source = "api.service")
  @Mapping(target = "username", source = "creatorUser.username")
  @Mapping(source = "createdTime", target = "createdTime", resultType = Long.class)
  public abstract ApiRequestDto toDto(ApiRequest entity);

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
