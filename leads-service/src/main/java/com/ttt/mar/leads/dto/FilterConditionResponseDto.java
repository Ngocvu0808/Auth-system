package com.ttt.mar.leads.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilterConditionResponseDto {

  private Integer id;

  private Integer fieldId;

  private Integer conditionId;

  List<String> value;
}
