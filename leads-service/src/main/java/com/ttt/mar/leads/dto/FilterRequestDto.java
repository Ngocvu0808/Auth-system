package com.ttt.mar.leads.dto;

import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class FilterRequestDto {

  @NotBlank(message = "name not blank.")
  @Size(max = 255, message = "fieldCode longer than 255 characters")
  private String name;

  @NotBlank(message = "code not blank.")
  @Size(max = 100, message = "code longer than 100 characters")
  private String code;

  @Size(max = 500, message = "description longer than 500 characters")
  private String description;

  private List<LeadConditionRequestDto> conditions;

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

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<LeadConditionRequestDto> getConditions() {
    return conditions;
  }

  public void setConditions(List<LeadConditionRequestDto> conditions) {
    this.conditions = conditions;
  }
}
