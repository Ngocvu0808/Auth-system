package com.ttt.mar.leads.dto;

import com.ttt.mar.leads.entities.TypeCondition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LeadConditionResponseDto {

  private Integer id;

  private String title;

  private String code;

  private TypeCondition inputType;

}
