package com.ttt.mar.notify.dto.template;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * @author KietDT
 * @created_date 05/05/2021
 */
@ApiModel(description = "Thong tin bien")
public class VariableResponse {

  @ApiModelProperty(notes = "Ma ten bien", example = "1")
  private Integer id;

  @ApiModelProperty(notes = "Ten bien", example = "name")
  private String name;

  @ApiModelProperty(notes = "Ma bien", example = "code")
  private String code;

  @ApiModelProperty(value = "userName of User", example = "Admin")
  private String userName;

  private Date createdTime;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public Date getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(Date createdTime) {
    this.createdTime = createdTime;
  }
}
