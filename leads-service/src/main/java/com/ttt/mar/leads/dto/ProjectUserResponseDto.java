package com.ttt.mar.leads.dto;

import com.ttt.rnd.lib.entities.UserStatus;

public class ProjectUserResponseDto extends UserResponseDto {

  private UserStatus status;
  private String roleName;

  public UserStatus getStatus() {
    return status;
  }

  public void setStatus(UserStatus status) {
    this.status = status;
  }

  public String getRoleName() {
    return roleName;
  }

  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }
}
