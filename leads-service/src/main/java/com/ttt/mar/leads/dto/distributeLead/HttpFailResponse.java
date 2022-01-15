package com.ttt.mar.leads.dto.distributeLead;

/**
 * @author nguyen
 * @create_date 28/10/2021
 */
public class HttpFailResponse {

  private boolean status;
  private String message;
  private int httpCode;

  public boolean isStatus() {
    return status;
  }

  public void setStatus(boolean status) {
    this.status = status;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public int getHttpCode() {
    return httpCode;
  }

  public void setHttpCode(int httpCode) {
    this.httpCode = httpCode;
  }
}
