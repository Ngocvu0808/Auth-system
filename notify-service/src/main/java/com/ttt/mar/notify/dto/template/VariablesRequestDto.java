package com.ttt.mar.notify.dto.template;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author KietDT
 * @created_date 05/05/2021
 */
@ApiModel(description = "Thong tin khoi tao bien")
public class VariablesRequestDto {

  @ApiModelProperty(notes = "Ten bien", required = true, example = "name")
  @NotBlank(message = "Name not null.")
  @Size(message = "Name longer than 255 characters ")
  private String name;
  @ApiModelProperty(notes = "Ma bien", required = true, example = "$name$")
  @NotBlank(message = "Code not null.")
  @Size(message = "Code longer than 255 characters ")
  private String code;

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
