package com.ttt.mar.leads.entities;

/**
 * @author kietdt
 * @created_date 20/04/2021
 */
public enum CampaignStatus {

  ACTIVE(0), DEACTIVE(1), PENDING(2);
  private final Integer value;

  CampaignStatus(Integer value) {
    this.value = value;
  }

  public Integer getValue() {
    return value;
  }
}
