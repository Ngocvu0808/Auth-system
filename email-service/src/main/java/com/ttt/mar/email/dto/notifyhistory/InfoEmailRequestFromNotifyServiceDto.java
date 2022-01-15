package com.ttt.mar.email.dto.notifyhistory;

import com.ttt.mar.email.dto.mail.EmailAttachment;
import com.ttt.mar.email.dto.mail.EmailContentRequestDto;
import java.util.List;

public class InfoEmailRequestFromNotifyServiceDto {

  private Integer requestId;

  private String accountCode;

  private String receiver;

  private String type;

  private String cc;

  private String bcc;

  private String brandName;

  private EmailContentRequestDto content;

  private List<EmailAttachment> emailAttachmentList;

  public Integer getRequestId() {
    return requestId;
  }

  public void setRequestId(Integer requestId) {
    this.requestId = requestId;
  }

  public String getAccountCode() {
    return accountCode;
  }

  public void setAccountCode(String accountCode) {
    this.accountCode = accountCode;
  }

  public String getReceiver() {
    return receiver;
  }

  public void setReceiver(String receiver) {
    this.receiver = receiver;
  }

  public String getCc() {
    return cc;
  }

  public void setCc(String cc) {
    this.cc = cc;
  }

  public String getBcc() {
    return bcc;
  }

  public void setBcc(String bcc) {
    this.bcc = bcc;
  }

  public String getBrandName() {
    return brandName;
  }

  public void setBrandName(String brandName) {
    this.brandName = brandName;
  }

  public EmailContentRequestDto getContent() {
    return content;
  }

  public void setContent(EmailContentRequestDto content) {
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
