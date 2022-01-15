package com.ttt.mar.auth.mapper;

import com.ttt.mar.auth.dto.app.RefreshTokenDto;
import com.ttt.mar.auth.dto.refreshtoken.RefreshTokenResponseDto;
import com.ttt.mar.auth.entities.application.RefreshToken;
import com.ttt.mar.auth.entities.enums.RefreshTokenStatus;
import java.util.Date;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class RefreshTokenMapper {

  @Mapping(target = "createdTime", source = "createdTime", resultType = Long.class)
  @Mapping(target = "clientName", source = "client.name")
  @Mapping(target = "clientId", source = "client.clientId")
  @Mapping(target = "clientSecret", source = "client.clientSecret")
  public abstract RefreshTokenDto toDto(RefreshToken entity);

  @AfterMapping
  public void afterMapping(@MappingTarget RefreshTokenResponseDto refreshTokenResponseDto,
      RefreshToken refreshToken) {
    refreshTokenResponseDto.setToken(refreshToken.getToken().substring(0, 15) + "...");
    if (refreshToken.getExpireTime() <= System.currentTimeMillis()) {
      refreshTokenResponseDto.setStatus(RefreshTokenStatus.EXPIRED);
    }
  }

  @Mapping(target = "developer", source = "apiKey.user.username")
  public abstract RefreshTokenResponseDto toRefreshTokenResponseDto(RefreshToken refreshToken);

  public abstract RefreshToken fromDto(RefreshTokenDto dto);

  Long map(Date value) {
    return value.getTime();
  }

  Date map(Long value) {
    return new Date(value);
  }
}
