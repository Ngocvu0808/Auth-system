package com.ttt.mar.leads.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author kietdt
 * @created_date 20/04/2021
 */
@ApiModel(description = "Thong tin validate")
public class ValidationDto {

  @ApiModelProperty(notes = "Ma validate", example = "1")
  private Integer id;
  @ApiModelProperty(notes = "Ma code", example = "EMAIL")
  private String code;
  @ApiModelProperty(notes = "Ten tieu de", example = "Email")
  private String title;
  @ApiModelProperty(notes = "Ten function xu ly", example = "validateEmail()")
  private String functionName;

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

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getFunctionName() {
    return functionName;
  }

  public void setFunctionName(String functionName) {
    this.functionName = functionName;
  }
}
