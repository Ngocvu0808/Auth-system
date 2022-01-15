package com.ttt.mar.leads.mapper;

import com.ttt.mar.leads.dto.UserResponseDto;
import com.ttt.rnd.lib.entities.User;
import org.mapstruct.Mapper;

/**
 * @author kietdt
 * @created_date 20/04/2021
 */
@Mapper(componentModel = "spring")
public abstract class UserMapper {

  public abstract UserResponseDto toDto(User entity);

}
