package com.ttt.mar.auth.dto.app;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

/**
 * @author bontk
 * @created_date 01/08/2020
 */
@Data
public class ClientUserAddRequestDto {

  @JsonProperty("user_id")
  private Integer userId;
  @JsonProperty("role_ids")
  private List<Integer> roleIds;

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public List<Integer> getRoleIds() {
    return roleIds;
  }

  public void setRoleIds(List<Integer> roleIds) {
    this.roleIds = roleIds;
  }
}
