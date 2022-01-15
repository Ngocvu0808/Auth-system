package com.ttt.mar.email.entities;

/**
 * @author bontk
 * @created_date 17/07/2020
 */
public enum MailServerStatus {
  ACTIVE(0), DE_ACTIVE(1), PENDING(2);

  private final Integer value;

  MailServerStatus(Integer value) {
    this.value = value;
  }

  public Integer getValue() {
    return value;
  }
}
