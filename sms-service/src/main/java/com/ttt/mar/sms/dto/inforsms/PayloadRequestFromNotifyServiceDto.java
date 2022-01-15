package com.ttt.mar.sms.dto.inforsms;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class PayloadRequestFromNotifyServiceDto {

  @JsonProperty(value = "requestId")
  @NotNull(message = "requestId not null")
  private Long idRequest;

  @NotNull(message = "receiver not null")
  @Size(max = 11, message = "phoneNumber invalid")
  @Pattern(regexp = "^[0-9,]+$", message = "receiver in valid")
  private String receiver;

  @NotNull(message = "accountCode not null")
  @Pattern(regexp = "^[A-Za-z0-9_.+-@%]+$", message = "code has spec character")
  private String accountCode;

  @NotNull(message = "content not null")
  private String content;

  public Long getIdRequest() {
    return idRequest;
  }

  public void setIdRequest(Long idRequest) {
    this.idRequest = idRequest;
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
