package com.ttt.mar.leads.dto;

/**
 * @author kietdt
 * @created_date 20/04/2021
 */
public class DistributeApiHeaderResponseDto {

  private Integer id;
  private String key;
  private String value;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

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
