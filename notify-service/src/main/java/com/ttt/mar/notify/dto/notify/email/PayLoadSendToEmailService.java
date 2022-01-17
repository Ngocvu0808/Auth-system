package com.ttt.mar.notify.dto.notify.email;

import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class PayLoadSendToEmailService {

  @NotNull(message = "accountCode not null")
  @Pattern(regexp = "^[A-Za-z0-9_.+-@%]+$", message = "code has spec character")
  private String accountCode;

  private String bcc;

  private String cc;

  private Long requestId;

  private String receiver;

  private String brandName;

  private ContentEmail content;

  private String type;

  private List<EmailAttachment> emailAttachmentList;

  public String getAccountCode() {
    return accountCode;
  }

  public void setAccountCode(String accountCode) {
    this.accountCode = accountCode;
  }

  public String getBcc() {
    return bcc;
  }

  public void setBcc(String bcc) {
    this.bcc = bcc;
  }

  public String getCc() {
    return cc;
  }

  public void setCc(String cc) {
    this.cc = cc;
  }

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

  public String getBrandName() {
    return brandName;
  }

  public void setBrandName(String brandName) {
    this.brandName = brandName;
  }

  public ContentEmail getContent() {
    return content;
  }

  public void setContent(ContentEmail content) {
    this.content = content;
  }

  public List<EmailAttachment> getEmailAttachmentList() {
    return emailAttachmentList;
  }

  public void setEmailAttachmentList(
      List<EmailAttachment> emailAttachmentList) {
    this.emailAttachmentList = emailAttachmentList;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}
