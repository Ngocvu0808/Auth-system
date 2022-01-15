package com.ttt.mar.email.dto.mail;

/**
 * @author bontk
 * @created_date 16/07/2020
 */
public enum EmailMimeType {
  UNKNOWN_EMAIL_MIME_TYPE(0), TEXT(1), HTML(2);

  EmailMimeType(Integer value) {
    this.value = value;
  }

  private Integer value;

  public Integer getValue() {
    return value;
  }
}
