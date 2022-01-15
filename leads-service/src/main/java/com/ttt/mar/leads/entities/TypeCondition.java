package com.ttt.mar.leads.entities;

public enum TypeCondition {
  TEXT(0), TAG(1), NONE(3);
  private final Integer type;

  TypeCondition(Integer type) {
    this.type = type;
  }

  public Integer getTypeCondition() {
    return type;
  }
}
