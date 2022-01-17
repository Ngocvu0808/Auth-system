package com.ttt.mar.config.mapper;


import com.ttt.mar.config.dto.ConfigCustomDto;
import com.ttt.mar.config.dto.ConfigDto;
import com.ttt.mar.config.entities.MarConfig;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
@Component
public abstract class ConfigMapper {

  @Mapping(target = "creatorName", source = "creatorUser.username")
  public abstract ConfigDto toDto(MarConfig config);

  public abstract ConfigCustomDto toCustomDto(MarConfig config);

  public abstract MarConfig fromDto(ConfigDto dto);

  @BeforeMapping
  public void validSourceDto(ConfigDto dto) {
    dto.setKey(dto.getKey().trim());
    dto.setValue(dto.getValue().trim());
    dto.setNote(dto.getNote().trim());
  }

  @Mapping(target = "createdTime", source = "createdTime", ignore = true)
  public abstract void updateModel(@MappingTarget MarConfig config, ConfigDto dto);


}
