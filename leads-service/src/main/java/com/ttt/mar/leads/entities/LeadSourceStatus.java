package com.ttt.mar.leads.entities;

public enum LeadSourceStatus {
  ACTIVE(0), DEACTIVE(1);
  private final Integer value;

  LeadSourceStatus(Integer value) {
    this.value = value;
  }

  public Integer getValue() {
    return value;
  }
}
