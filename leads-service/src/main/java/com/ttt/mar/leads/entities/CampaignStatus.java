package com.ttt.mar.leads.entities;

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
