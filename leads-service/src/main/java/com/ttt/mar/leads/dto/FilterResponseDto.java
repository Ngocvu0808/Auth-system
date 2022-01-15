package com.ttt.mar.leads.dto;

import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FilterResponseDto {

  private Integer id;

  private String code;

  private String name;

  private Date createTime;

  private Date modifiedTime;

  private String creatorName;

  private String updaterName;

  private String description;

  private String modifier;

  private List<FilterConditionResponseDto> listFilterCondition;

}
