package com.ttt.mar.leads.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FilterResponse {

  private Integer id;

  private String name;

  private String code;

  private Date createdTime;

  private UserResponseDto userResponseDto;


}
