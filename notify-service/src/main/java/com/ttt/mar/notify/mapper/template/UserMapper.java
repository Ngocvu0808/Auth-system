package com.ttt.mar.notify.mapper.template;


import com.ttt.mar.notify.dto.template.UserResponseDto;
import com.ttt.rnd.lib.entities.User;
import org.mapstruct.Mapper;

/**
 * @author KietDT
 * @created_date 05/05/2021
 */
@Mapper(componentModel = "spring")
public abstract class UserMapper {

  public abstract UserResponseDto toDto(User entity);
}
