package com.ttt.mar.leads.entities;

/**
 * @author kietdt
 * @created_date 20/04/2021
 */
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
