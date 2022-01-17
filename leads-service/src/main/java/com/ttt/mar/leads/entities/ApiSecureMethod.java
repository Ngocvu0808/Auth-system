package com.ttt.mar.leads.entities;

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
