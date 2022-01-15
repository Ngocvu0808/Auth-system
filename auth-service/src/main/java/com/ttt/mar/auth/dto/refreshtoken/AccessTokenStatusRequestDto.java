package com.ttt.mar.auth.dto.refreshtoken;

import com.ttt.mar.auth.entities.enums.TokenStatus;

public class AccessTokenStatusRequestDto {

  private Long id;

  private TokenStatus status;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public TokenStatus getStatus() {
    return status;
  }

  public void setStatus(TokenStatus status) {
    this.status = status;
  }
}
