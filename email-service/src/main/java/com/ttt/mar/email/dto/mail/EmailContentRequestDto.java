package com.ttt.mar.email.dto.mail;

public class EmailContentRequestDto {

  private String type;
  private String subject;
  private String body;
  private String attachments;

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public String getAttachments() {
    return attachments;
  }

  public void setAttachments(String attachments) {
    this.attachments = attachments;
  }
}
