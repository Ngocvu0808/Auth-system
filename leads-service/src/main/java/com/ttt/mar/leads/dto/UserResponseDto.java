package com.ttt.mar.leads.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author kietdt
 * @created_date 20/04/2021
 */
@ApiModel(description = "Info User")
public class UserResponseDto {

  @ApiModelProperty(value = "ID user", example = "1")
  private Integer id;
  @ApiModelProperty(value = "username of User", example = "Admin")
  private String username;
  @ApiModelProperty(value = "Name of User", example = "Dinh tien Kiet")
  private String name;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
