package com.ttt.mar.leads.entities;

public enum ScheduleStatus {
  ACTIVE(0), DEACTIVE(1);
  private final Integer value;

  ScheduleStatus(Integer value) {
    this.value = value;
  }

  public Integer getValue() {
    return value;
  }
}
