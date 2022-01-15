package com.ttt.mar.sms.dto.inforsms;

public class PayLoadHistoryStatusSms {

  private String status;

  private String message;

  private Long requestId;

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Long getRequestId() {
    return requestId;
  }

  public void setRequestId(Long requestId) {
    this.requestId = requestId;
  }
}
