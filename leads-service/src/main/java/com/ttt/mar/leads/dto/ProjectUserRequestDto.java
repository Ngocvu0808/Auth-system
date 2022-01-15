package com.ttt.mar.leads.dto;

import java.util.Set;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author kietdt
 * @created_date 22/04/2021
 */
public class ProjectUserRequestDto {

  @NotEmpty(message = "userIds not empty.")
  private Set<Integer> userIds;
  @NotNull(message = "roleId not null.")
  private Integer roleId;

  public Set<Integer> getUserIds() {
    return userIds;
  }

  public void setUserIds(Set<Integer> userIds) {
    this.userIds = userIds;
  }

  public Integer getRoleId() {
    return roleId;
  }

  public void setRoleId(Integer roleId) {
    this.roleId = roleId;
  }
}
