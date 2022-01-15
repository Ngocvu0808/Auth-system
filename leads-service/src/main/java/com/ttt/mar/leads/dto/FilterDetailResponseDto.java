package com.ttt.mar.leads.dto;

public class FilterDetailResponseDto {

  private String name;
  private String createDate;
  private String modifiedDate;
  private String description;
  private FilterConditionRequestDto conditions;

  public FilterDetailResponseDto(String name, String createDate, String modifiedDate,
      String description, FilterConditionRequestDto conditions) {
    this.name = name;
    this.createDate = createDate;
    this.modifiedDate = modifiedDate;
    this.description = description;
    this.conditions = conditions;
  }

  public FilterDetailResponseDto() {

  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCreateDate() {
    return createDate;
  }

  public void setCreateDate(String createDate) {
    this.createDate = createDate;
  }

  public String getModifiedDate() {
    return modifiedDate;
  }

  public void setModifiedDate(String modifiedDate) {
    this.modifiedDate = modifiedDate;
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
