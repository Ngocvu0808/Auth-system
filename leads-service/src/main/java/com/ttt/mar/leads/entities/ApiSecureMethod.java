package com.ttt.mar.leads.entities;

/**
 * @author kietdt
 * @created_date 20/04/2021
 */
public enum ApiSecureMethod {
  NONE(0), BASIC(1);
  private final Integer method;

  ApiSecureMethod(Integer method) {
    this.method = method;
  }

  public Integer getMethod() {
    return method;
  }
}
