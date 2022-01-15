package com.ttt.mar.auth.dto.filter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author bontk
 * @created_date 04/08/2020
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiTypeDto {

  private String name;
  private Integer value;
  private String description;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getValue() {
    return value;
  }

  public void setValue(Integer value) {
    this.value = value;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
