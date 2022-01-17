package com.ttt.mar.config.entities;

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
