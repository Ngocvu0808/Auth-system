package com.ttt.mar.leads.entities;

/**
 * @author kietdt
 * @created_date 20/04/2021
 */
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
