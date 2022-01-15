package com.ttt.mar.auth.mapper;

import com.ttt.mar.auth.dto.auth.CustomGroupDto;
import com.ttt.rnd.lib.entities.Group;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class CustomGroupDtoMapper {

  public abstract CustomGroupDto toDto(Group group);
}
