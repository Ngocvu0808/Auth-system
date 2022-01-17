package com.ttt.mar.leads.entities;

public enum ProjectStatus {
  ACTIVE(0), DEACTIVE(1);
  private final Integer value;

  ProjectStatus(Integer value) {
    this.value = value;
  }

  public Integer getValue() {
    return value;
  }
}
