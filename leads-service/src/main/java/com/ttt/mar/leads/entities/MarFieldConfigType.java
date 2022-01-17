package com.ttt.mar.leads.entities;

public enum MarFieldConfigType {
  SYS(0), DEFAULT(1), PARAM(2);
  private Integer value;

  MarFieldConfigType(Integer value) {
    this.value = value;
  }
}
