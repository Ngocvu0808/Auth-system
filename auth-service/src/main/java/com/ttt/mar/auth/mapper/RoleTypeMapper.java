package com.ttt.mar.auth.mapper;

import com.ttt.mar.auth.dto.auth.RoleTypeDto;
import com.ttt.rnd.lib.entities.RoleType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class RoleTypeMapper {

  @Mapping(source = "isDefault", target = "isDefault")
  public abstract RoleTypeDto toDto(RoleType model);

}
