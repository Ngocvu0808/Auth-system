package com.ttt.mar.leads.dto;

/**
 * @author nguyen
 * @create_date 05/10/2021
 */
public class ValidateLeadDto {

  private boolean result;
  private String reason;

  public boolean isResult() {
    return result;
  }

  public void setResult(boolean result) {
    this.result = result;
  }

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }
}
