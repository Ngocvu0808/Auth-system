package com.ttt.mar.leads.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FilterConditionRequestDto {

  private Integer filterId;

  private Integer conditionId;

  private String fieldCode;

  private List<FilterConditionValueRequestDto> value;

}
