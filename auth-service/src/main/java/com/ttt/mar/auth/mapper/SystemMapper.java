package com.ttt.mar.auth.mapper;

import com.ttt.mar.auth.dto.appservice.SystemCustomDto;
import com.ttt.mar.auth.entities.service.System;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class SystemMapper {

  public abstract SystemCustomDto toDto(System api);

  public abstract System fromDto(SystemCustomDto apiDto);
}
