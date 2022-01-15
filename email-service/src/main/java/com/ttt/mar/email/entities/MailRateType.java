package com.ttt.mar.email.entities;

/**
 * @author bontk
 * @created_date 17/07/2020
 */
public enum MailRateType {
  HOUR(0), DAY(1), MONTH(2),YEAR(3);

  private final Integer value;

  MailRateType(Integer value) {
    this.value = value;
  }

  public Integer getValue() {
    return value;
  }
}
