package com.ttt.mar.email.dto.emailconfig;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class EmailConfigUpdateRequestDto extends EmailConfigCreateRequestDto{

  private Integer id;

  @JsonIgnore
  private String accountCode;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }
}
