package com.ttt.mar.leads.dto.distributeLead;

import javax.persistence.Column;

/**
 * @author nguyen
 * @create_date 13/10/2021
 */
public class DistributeFieldMappingDto {

  private String fieldSource;

  private String fieldTarget;

  private String validationCode;

  private Boolean require;

  public String getFieldSource() {
    return fieldSource;
  }

  public void setFieldSource(String fieldSource) {
    this.fieldSource = fieldSource;
  }

  public String getFieldTarget() {
    return fieldTarget;
  }

  public void setFieldTarget(String fieldTarget) {
    this.fieldTarget = fieldTarget;
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
