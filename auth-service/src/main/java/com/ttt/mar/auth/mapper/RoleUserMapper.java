package com.ttt.mar.auth.mapper;

import com.ttt.rnd.lib.dto.RoleCustomDto;
import com.ttt.rnd.lib.entities.RoleUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", uses = {RoleMapper.class},
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class RoleUserMapper {

  @Mapping(target = "name", source = "role.name")
  @Mapping(target = "id", source = "role.id")
  public abstract RoleCustomDto toDto(RoleUser roleUser);

}
