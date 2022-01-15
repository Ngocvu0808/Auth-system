package com.ttt.mar.auth.mapper;

import com.ttt.mar.auth.dto.auth.RoleDtoExtended;
import com.ttt.rnd.lib.dto.RoleCustomDto;
import com.ttt.rnd.lib.dto.RoleDto;
import com.ttt.rnd.lib.entities.Role;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;

/**
 * @author bontk
 * @date 05/03/2020
 */

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class RoleMapper {

  @Mapping(target = "creatorName", source = "creatorUser.username")
  public abstract RoleDto toDto(Role entity);

  @Mapping(target = "creatorName", source = "creatorUser.username")
  public abstract RoleDtoExtended toExtendedDto(Role entity);

  public abstract Role fromDto(RoleDto dto);

  public abstract RoleCustomDto toRoleCustomerDto(Role role);

  @BeforeMapping
  public void validSourceDto(RoleDto dto) {
    dto.setCode(dto.getCode().trim());
    dto.setNote(dto.getNote().trim());
  }

  public abstract void updateModel(@MappingTarget Role entity, RoleDto dto);


}
