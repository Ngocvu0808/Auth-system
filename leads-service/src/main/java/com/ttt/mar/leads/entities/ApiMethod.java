package com.ttt.mar.leads.entities;

/**
 * @author kietdt
 * @created_date 20/04/2021
 */
public enum ApiMethod {
  GET(0), POST(1), PUT(2), DELETE(3), PATCH(4);
  private final Integer value;

  ApiMethod(Integer value) {
    this.value = value;
  }

  public Integer getValue() {
    return value;
  }
}
