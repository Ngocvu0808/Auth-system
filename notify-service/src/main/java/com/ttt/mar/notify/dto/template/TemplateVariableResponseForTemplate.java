package com.ttt.mar.notify.dto.template;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author NamBN
 * @created_date 11/05/2021
 */
@ApiModel(description = "Thong tin bien cho template")
public class TemplateVariableResponseForTemplate {

  @ApiModelProperty(notes = "Ma ten bien", example = "1")
  private Integer id;

  @ApiModelProperty(notes = "Ten bien", example = "name")
  private String name;

  @ApiModelProperty(notes = "Ma bien", example = "code")
  private String code;

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

}
