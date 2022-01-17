package com.ttt.mar.notify.entities.template;

/**
 * @author nambn
 * @created_date 5/5/2021
 */

public enum TemplateSemanticType {

  HEADER(0), FOOTER(1);
  private final Integer value;

  TemplateSemanticType(Integer value) {
    this.value = value;
  }

  public Integer getValue() {
    return value;
  }
}
