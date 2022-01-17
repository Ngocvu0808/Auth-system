package com.ttt.mar.config.mapper;


import com.ttt.mar.config.dto.FieldConfigDto;
import com.ttt.mar.config.entities.MarFieldConfig;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
@Component
public abstract class FieldConfigMapper {

  @Mapping(target = "creatorName", source = "creatorUser.username")
  public abstract FieldConfigDto toDto(MarFieldConfig config);

  public abstract MarFieldConfig fromDto(FieldConfigDto dto);

  @BeforeMapping
  public void validSourceDto(FieldConfigDto dto) {
    dto.setKey(dto.getKey().trim());
    dto.setName(dto.getName().trim());
    dto.setNote(dto.getNote().trim());
    dto.setFormatValue(dto.getFormatValue().trim());
  }

  @Mapping(target = "createdTime", source = "createdTime", ignore = true)
  public abstract void updateModel(@MappingTarget MarFieldConfig config, FieldConfigDto dto);


}
