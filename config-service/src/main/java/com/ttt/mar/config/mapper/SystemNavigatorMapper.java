package com.ttt.mar.config.mapper;

import com.ttt.mar.config.dto.SystemNavigatorResponse;
import com.ttt.mar.config.entities.SystemNavigator;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public abstract class SystemNavigatorMapper {

  public abstract SystemNavigatorResponse toDto(SystemNavigator entity);
}
