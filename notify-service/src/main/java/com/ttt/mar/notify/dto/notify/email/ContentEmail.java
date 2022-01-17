package com.ttt.mar.notify.dto.notify.email;

import java.util.List;

public class ContentEmail {

  private String body;

  private String subject;

  private String type;

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String toString(List<String> listFileName) {
    return "{" +
        "body:" + '\"' + body + '\"' +
        ", subject:" + '\"' + subject + '\"' +
        ", type:" + '\"' + type + '\"' +
        ", files:" + listFileName + '}';
  }
}
