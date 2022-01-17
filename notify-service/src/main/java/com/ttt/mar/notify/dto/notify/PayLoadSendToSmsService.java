package com.ttt.mar.notify.dto.notify;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class PayLoadSendToSmsService {

  @NotNull(message = "requestId not null")
  private Long requestId;

  @NotNull(message = "receiver not null")
  @Size(max = 11, message = "phoneNumber longer than 11 characters")
  @Pattern(regexp = "^[0-9,]+$", message = "receiver in valid")
  private String receiver;

  @NotNull(message = "accountCode not null")
  @Pattern(regexp = "^[A-Za-z0-9_.+-@%]+$", message = "code has spec character")
  private String accountCode;

  @NotNull(message = "content not null")
  private String content;

  public Long getRequestId() {
    return requestId;
  }

  public void setRequestId(Long requestId) {
    this.requestId = requestId;
  }

  public String getReceiver() {
    return receiver;
  }

  public void setReceiver(String receiver) {
    this.receiver = receiver;
  }

  public String getAccountCode() {
    return accountCode;
  }

  public void setAccountCode(String accountCode) {
    this.accountCode = accountCode;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }
}
