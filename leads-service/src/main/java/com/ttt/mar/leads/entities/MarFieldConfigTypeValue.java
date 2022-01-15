package com.ttt.mar.leads.entities;

/**
 * @author kietdt
 * @created_date 20/04/2021
 */
public enum MarFieldConfigTypeValue {
  TEXT(0), NUMBER(1), PHONE(2), EMAIL(3), DATE(4);
  private final Integer value;

  MarFieldConfigTypeValue(Integer value) {
    this.value = value;
  }

  public Integer getValue() {
    return value;
  }
}
