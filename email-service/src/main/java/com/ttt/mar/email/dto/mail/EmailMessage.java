package com.ttt.mar.email.dto.mail;

import com.ttt.mar.email.entities.EmailProtocol;
import java.util.List;

public class EmailMessage {

  private String userName;
  private String password;
  private String host;
  private String port;
  private String token;
  private String fromEmail;
  private String brandName;
  private List<EmailRecipient> recipients;
  private EmailProtocol emailProtocol;
  private String type;
  private String subject;
  private String body;
  private List<EmailAttachment> emailAttachments;

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public String getPort() {
    return port;
  }

  public void setPort(String port) {
    this.port = port;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getFromEmail() {
    return fromEmail;
  }

  public void setFromEmail(String fromEmail) {
    this.fromEmail = fromEmail;
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

  public EmailProtocol getEmailProtocol() {
    return emailProtocol;
  }

  public void setEmailProtocol(EmailProtocol emailProtocol) {
    this.emailProtocol = emailProtocol;
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

  public void setEmailAttachments(List<EmailAttachment> emailAttachments) {
    this.emailAttachments = emailAttachments;
  }
}
