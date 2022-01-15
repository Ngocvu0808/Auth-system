package com.ttt.mar.leads.dto;

/**
 * @author kietdt
 * @created_date 20/04/2021
 */
public class DistributeFieldMappingResponseDto {

  private Integer id;
  private String fieldSource;
  private String fieldTarget;
  private String validationCode;
  private Boolean require;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

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
