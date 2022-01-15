package com.ttt.mar.auth.entities.enums;

public enum AccessTokenStatusFilter {
  ACTIVE(0), PENDING(1), EXPIRED(2), REJECTED(3);
  private final Integer value;

  AccessTokenStatusFilter(Integer value) {
    this.value = value;
  }

  public Integer getValue() {
    return value;
  }
}
