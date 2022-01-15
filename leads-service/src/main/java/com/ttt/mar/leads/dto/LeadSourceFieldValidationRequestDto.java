package com.ttt.mar.leads.dto;

import javax.validation.constraints.NotBlank;

/**
 * @author kietdt
 * @created_date 20/04/2021
 */
public class LeadSourceFieldValidationRequestDto {

  @NotBlank(message = "fieldCode not Blank")
  private String fieldCode;
  private String validationCode;
  private Boolean require = false;

  public String getFieldCode() {
    return fieldCode;
  }

  public void setFieldCode(String fieldCode) {
    this.fieldCode = fieldCode;
  }

  public String getValidationCode() {
    return validationCode;
  }

  public void setValidationCode(String validationCode) {
    this.validationCode = validationCode;
  }

  public Boolean getRequire() {
    return require;
  }

  public void setRequire(Boolean require) {
    this.require = require;
  }
}
