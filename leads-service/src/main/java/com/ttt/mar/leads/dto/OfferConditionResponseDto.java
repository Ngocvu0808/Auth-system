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
public class OfferConditionResponseDto {

  private Integer id;

  private Integer fieldId;

  private Integer conditionId;

  private List<String> values;
}
