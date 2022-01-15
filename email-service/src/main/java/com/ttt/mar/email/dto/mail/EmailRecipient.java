package com.ttt.mar.email.dto.mail;

/**
 * @author bontk
 * @created_date 16/07/2020
 */
public class EmailRecipient {

  private String email;
  private EmailRecipientType emailRecipientType;

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public EmailRecipientType getEmailRecipientType() {
    return emailRecipientType;
  }

  public void setEmailRecipientType(EmailRecipientType emailRecipientType) {
    this.emailRecipientType = emailRecipientType;
  }
}
