package com.ttt.mar.leads.entities;

public enum FieldType {
  TEXT(0), NUMBER(1), PHONE(2), EMAIL(3), DATE(4);
  private final Integer value;

  FieldType(Integer value) {
    this.value = value;
  }

  public Integer getValue() {
    return value;
  }
}
