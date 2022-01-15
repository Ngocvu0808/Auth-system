package com.ttt.mar.email.dto.mail;

/**
 * @author bontk
 * @created_date 16/07/2020
 */
public enum EmailRecipientType {
  UNKNOWN_EMAIL_RECIPIENT_TYPE(0), TO(1), CC(1), BCC(2);

  EmailRecipientType(Integer value) {
    this.value = value;
  }

  private Integer value;

  public Integer getValue() {
    return value;
  }
}
