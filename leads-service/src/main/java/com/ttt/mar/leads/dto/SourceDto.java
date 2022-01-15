package com.ttt.mar.leads.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author kietdt
 * @created_date 20/04/2021
 */
@ApiModel(description = "Model thong tin nguon")
public class SourceDto {

  @ApiModelProperty(notes = "Ma nguon", example = "1")
  private Integer id;
  @ApiModelProperty(notes = "Ma code cua nguon", example = "VPB")
  private String code;
  @ApiModelProperty(notes = "Ten nguon", example = "Vpbank")
  private String name;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
