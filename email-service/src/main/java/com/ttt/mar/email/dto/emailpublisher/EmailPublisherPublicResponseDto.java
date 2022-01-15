package com.ttt.mar.email.dto.emailpublisher;

import com.ttt.mar.email.entities.EmailProtocol;

public class EmailPublisherPublicResponseDto {

  private Integer id;

  private String name;

  private String code;

  private EmailProtocol protocol;

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

  public EmailProtocol getProtocol() {
    return protocol;
  }

  public void setProtocol(EmailProtocol protocol) {
    this.protocol = protocol;
  }
}
