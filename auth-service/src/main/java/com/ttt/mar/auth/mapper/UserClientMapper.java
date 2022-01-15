package com.ttt.mar.auth.mapper;

import com.ttt.mar.auth.dto.app.UserClientCustomDto;
import com.ttt.mar.auth.entities.application.ClientUser;
import com.ttt.rnd.lib.entities.User;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.springframework.stereotype.Component;

/**
 * @author bontk
 * @date 26/02/2020
 */

@Mapper(componentModel = "spring", uses = {ClientUserPermissionMapper.class},
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
@Component
public abstract class UserClientMapper {

  @Mapping(target = "id",source = "user.id")
  @Mapping(target = "username",source = "user.username")
  @Mapping(target = "name",source = "user.name")
  @Mapping(target = "email",source = "user.email")
  @Mapping(target = "status",source = "user.status")
  public abstract UserClientCustomDto toDto(ClientUser clientUser);

  public abstract User fromDto(UserClientCustomDto dto);

  @BeforeMapping
  public void validSourceDto(UserClientCustomDto dto) {
    dto.setName(dto.getName().trim());
    dto.setUsername(dto.getUsername().trim());
  }


  public abstract void updateModel(@MappingTarget User user, UserClientCustomDto dto);

}
