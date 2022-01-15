package com.ttt.mar.auth.mapper;

import com.ttt.rnd.lib.dto.GroupUserCustomDto;
import com.ttt.rnd.lib.entities.GroupUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", uses = {GroupMapper.class},
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class GroupUserMapper {

  @Mapping(target = "id", source = "group.id")
  @Mapping(target = "name", source = "group.name")
  @Mapping(target = "code", source = "group.code")
  public abstract GroupUserCustomDto toDto(GroupUser groupUser);

}
