package com.ttt.mar.email.entities;

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
