package com.ttt.mar.auth.mapper;


import com.ttt.rnd.lib.dto.SysPermissionDto;
import com.ttt.rnd.lib.entities.SysPermission;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.springframework.stereotype.Component;

/**
 * @author bontk
 * @date 06/03/2020
 */

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
@Component
public abstract class SysPermissionMapper {

  public abstract SysPermissionDto toDto(SysPermission entity);

  public abstract SysPermission fromDto(SysPermissionDto dto);

  @BeforeMapping
  public void validSourceDto(SysPermissionDto dto) {
    dto.setName(dto.getName().trim());
    dto.setCode(dto.getCode().trim());
  }


  public abstract void updateModel(@MappingTarget SysPermission entity, SysPermissionDto dto);


}
