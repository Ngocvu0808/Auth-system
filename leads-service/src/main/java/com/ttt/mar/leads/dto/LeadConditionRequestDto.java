package com.ttt.mar.leads.dto;

import java.util.List;
import javax.validation.constraints.NotNull;

public class LeadConditionRequestDto {


  @NotNull(message = "fieldId Not Null.")
  private Integer fieldId;

  @NotNull(message = "conditionId Not Null.")
  private Integer conditionId;

  private List<String> values;


  public Integer getFieldId() {
    return fieldId;
  }

  public void setFieldId(Integer fieldId) {
    this.fieldId = fieldId;
  }

  public Integer getConditionId() {
    return conditionId;
  }

  public void setConditionId(Integer conditionId) {
    this.conditionId = conditionId;
  }

  public List<String> getValues() {
    return values;
  }

  public void setValues(List<String> values) {
    this.values = values;
  }

  public LeadConditionRequestDto(
      @NotNull(message = "fieldId Not Null.") Integer fieldId,
      @NotNull(message = "conditionId Not Null.") Integer conditionId,
      List<String> values) {
    this.fieldId = fieldId;
    this.conditionId = conditionId;
    this.values = values;
  }
}
