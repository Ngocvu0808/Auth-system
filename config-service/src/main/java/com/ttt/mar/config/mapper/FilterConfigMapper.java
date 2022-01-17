package com.ttt.mar.config.mapper;


import com.ttt.mar.config.dto.FilterConfigDto;
import com.ttt.mar.config.entities.MarFilterConfig;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
@Component
public abstract class FilterConfigMapper {

  public abstract FilterConfigDto toDto(MarFilterConfig config);

  public abstract MarFilterConfig fromDto(FilterConfigDto dto);
}
