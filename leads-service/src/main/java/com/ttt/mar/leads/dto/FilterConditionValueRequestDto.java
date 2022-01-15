package com.ttt.mar.leads.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FilterConditionValueRequestDto {

  private Integer filterConditionId;

  private String value;
}
