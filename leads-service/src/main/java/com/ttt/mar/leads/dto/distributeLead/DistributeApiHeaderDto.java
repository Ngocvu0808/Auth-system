package com.ttt.mar.leads.dto.distributeLead;

import javax.persistence.Column;

/**
 * @author nguyen
 * @create_date 13/10/2021
 */
public class DistributeApiHeaderDto {

  private String key;

  private String value;

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}
