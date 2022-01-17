package com.ttt.mar.notify.dto.template;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class TemplateUpdateRequestDto extends TemplateRequestDto {

  @JsonIgnore
  private String type;
  @JsonIgnore
  private String code;
}
