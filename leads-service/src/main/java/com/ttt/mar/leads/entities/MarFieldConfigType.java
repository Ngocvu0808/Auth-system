package com.ttt.mar.leads.entities;

/**
 * @author kietdt
 * @created_date 20/04/2021
 */
public enum MarFieldConfigType {
  SYS(0), DEFAULT(1), PARAM(2);
  private Integer value;

  MarFieldConfigType(Integer value) {
    this.value = value;
  }
}
