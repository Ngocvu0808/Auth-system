package com.ttt.mar.config.mapper;

import com.ttt.mar.config.dto.MarFieldConfigObjectDto;
import com.ttt.mar.config.entities.MarFieldConfigObject;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class MarFieldConfigObjectMapper {

  public abstract MarFieldConfigObjectDto toDto(MarFieldConfigObject entity);
}
