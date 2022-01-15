package com.ttt.mar.leads.entities;

/**
 * @author nguyen
 * @create_date 07/09/2021
 */
public enum ScheduleType {
  ALWAYS(0), CALENDER(1);

  private final Integer value;

  ScheduleType(Integer value) {
    this.value = value;
  }

  public Integer getValue() {
    return value;
  }
}
