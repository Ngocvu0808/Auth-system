package com.ttt.mar.email.dto.mail;

import java.util.List;

public class EmailRequestDto {

  private Integer requestId;
  private String cc;
  private String bcc;
  private String content;
  private Integer idEmailConfig;
  private String receiver;

  public Integer getRequestId() {
    return requestId;
  }

  public void setRequestId(Integer requestId) {
    this.requestId = requestId;
  }

  public String getCc() {
    return cc;
  }

  public void setCc(String cc) {
    this.cc = cc;
  }

  public String getBcc() {
    return bcc;
  }

  public void setBcc(String bcc) {
    this.bcc = bcc;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public Integer getIdEmailConfig() {
    return idEmailConfig;
  }

  public void setIdEmailConfig(Integer idEmailConfig) {
    this.idEmailConfig = idEmailConfig;
  }

  public String getReceiver() {
    return receiver;
  }

  public void setReceiver(String receiver) {
    this.receiver = receiver;
  }

  public String toString(List<String> listFileName) {
    return "{" +
        "requestId='" + requestId + '\'' +
        ", cc='" + cc + '\'' +
        ", bcc='" + bcc + '\'' +
        ", content=" + content +
        ", idEmailConfig=" + idEmailConfig +
        ", receiver=" + receiver +
        '}';
  }
}
