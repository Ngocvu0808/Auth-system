package com.ttt.mar.email.dto.mail;

import java.util.List;

public class EmailRequestSendDto {

  private String accountCode;
  private String brandName;
  private List<EmailRecipient> recipients;
  private String type;
  private String subject;
  private String body;
  private List<EmailAttachment> emailAttachments;
  private Integer requestId;
  private String cc;
  private String bcc;
  private String content;
  private String receiver;

  public String getAccountCode() {
    return accountCode;
  }

  public void setAccountCode(String accountCode) {
    this.accountCode = accountCode;
  }

  public String getBrandName() {
    return brandName;
  }

  public void setBrandName(String brandName) {
    this.brandName = brandName;
  }

  public List<EmailRecipient> getRecipients() {
    return recipients;
  }

  public void setRecipients(List<EmailRecipient> recipients) {
    this.recipients = recipients;
  }

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

  public List<EmailAttachment> getEmailAttachments() {
    return emailAttachments;
  }

  public void setEmailAttachments(
      List<EmailAttachment> emailAttachments) {
    this.emailAttachments = emailAttachments;
  }

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

  public String getReceiver() {
    return receiver;
  }

  public void setReceiver(String receiver) {
    this.receiver = receiver;
  }
}
