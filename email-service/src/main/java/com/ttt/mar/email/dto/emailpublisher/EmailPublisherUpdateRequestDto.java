package com.ttt.mar.email.dto.emailpublisher;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class EmailPublisherUpdateRequestDto extends EmailPublisherCreateRequestDto {

  private Integer id;

  @JsonIgnore
  private String code;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }
}
