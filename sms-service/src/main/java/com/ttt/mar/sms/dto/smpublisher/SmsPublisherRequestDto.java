package com.ttt.mar.sms.dto.smpublisher;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class SmsPublisherRequestDto {

  private Integer id;

  @NotNull(message = "name not null")
  @Size(max = 255, message = "Name longer than 255 characters")
  private String name;

  @NotNull(message = "code not null")
  @Pattern(regexp = "^[A-Za-z0-9_.+-@%]+$", message = "code has spec character")
  @Size(max = 150, message = "code longer than 150 characters")
  private String code;

  @NotNull(message = "endPoint not null")
  @Size(max = 500, message = "endPoint longer than 500 characters")
  private String endPoint;


  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getEndPoint() {
    return endPoint;
  }

  public void setEndPoint(String endPoint) {
    this.endPoint = endPoint;
  }
}
