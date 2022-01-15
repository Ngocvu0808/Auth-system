package com.ttt.mar.leads.entities;

/**
 * @author bontk
 * @created_date 22/03/2021
 */
public enum DataType {
  INT(0), FLOAT(1), DOUBLE(2), STRING(3);
  private final Integer value;

  DataType(Integer value) {
    this.value = value;
  }

  public Integer getValue() {
    return value;
  }
}
