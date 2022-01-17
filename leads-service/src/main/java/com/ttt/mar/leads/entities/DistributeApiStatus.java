package com.ttt.mar.leads.entities;

public enum DistributeApiStatus {
  ACTIVE(0), DEACTIVE(1);
  private final Integer value;

  DistributeApiStatus(Integer value) {
    this.value = value;
  }

  public Integer getValue() {
    return value;
  }
}
