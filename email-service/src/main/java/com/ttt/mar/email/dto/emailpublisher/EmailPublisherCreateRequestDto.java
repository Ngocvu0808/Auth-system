package com.ttt.mar.email.dto.emailpublisher;

import com.ttt.mar.email.entities.EmailProtocol;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class EmailPublisherCreateRequestDto {

  @NotNull(message = "Name not null")
  @Size(max = 255, message = "Name longer than 255 characters")
  private String name;

  @Size(max = 150, message = "Name longer than 255 characters")
  private String code;

  private String endPoint;

  private String host;

  private Integer port;

  private EmailProtocol protocol;

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

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public Integer getPort() {
    return port;
  }

  public void setPort(Integer port) {
    this.port = port;
  }

  public EmailProtocol getProtocol() {
    return protocol;
  }

  public void setProtocol(EmailProtocol protocol) {
    this.protocol = protocol;
  }
}
