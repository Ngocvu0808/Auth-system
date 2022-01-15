package com.ttt.mar.auth.mapper;

import com.ttt.mar.auth.dto.app.UserActivityRequestDto;
import com.ttt.mar.auth.entities.UserActivity;
import org.mapstruct.Mapper;

/**
 * @author nguyen
 * @create_date 09/11/2021
 */

@Mapper(componentModel = "spring")
public abstract class UserActivityMapper {

  public abstract UserActivity fromDto(UserActivityRequestDto dto);
}
