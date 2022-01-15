package com.ttt.mar.leads.entities;

/**
 * @author nguyen
 * @create_date 08/09/2021
 */
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
