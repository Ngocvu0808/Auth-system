package com.ttt.mar.leads.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class FilterUpdateRequestDto {

  @JsonIgnore
  private Integer id;

  @NotBlank(message = "name not blank.")
  @Size(max = 255, message = "fieldCode longer than 255 characters")
  private String name;

  @Size(max = 500, message = "description longer than 500 characters")
  private String description;

  private List<LeadConditionRequestDto> conditions;

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
