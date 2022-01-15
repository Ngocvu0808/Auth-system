package com.ttt.mar.leads.dto;

public class FilterResponseUpdateDto {

  private String name;
  private String code;
  private String description;
  private FilterConditionRequestDto conditions;

  public FilterResponseUpdateDto(String name, String code, String description,
      FilterConditionRequestDto conditions) {
    this.name = name;
    this.code = code;
    this.description = description;
    this.conditions = conditions;
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

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public FilterConditionRequestDto getConditions() {
    return conditions;
  }

  public void setConditions(FilterConditionRequestDto conditions) {
    this.conditions = conditions;
  }
}
