package com.ttt.mar.leads.dto.distributeLead;

/**
 * @author nguyen
 * @create_date 20/10/2021
 */
public class KeyMapResponseDto {

  private String key;
  private String value;

  public String getKey() {
    return key;
  }

  public KeyMapResponseDto(String key, String value) {
    this.key = key;
    this.value = value;
  }

  public KeyMapResponseDto() {
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
