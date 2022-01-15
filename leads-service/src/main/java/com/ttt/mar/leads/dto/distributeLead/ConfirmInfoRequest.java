package com.ttt.mar.leads.dto.distributeLead;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * @author nguyen
 * @create_date 11/10/2021
 */
public class ConfirmInfoRequest {

  @SerializedName("account_code")
  private String accountCode;
  @SerializedName("bcc")
  private String bcc;
  @SerializedName("brand_name")
  private String brandName;
  @SerializedName("cc")
  private String cc;
  @SerializedName("content")
  private EmailContentRequestDto content;
  @SerializedName("request_id")
  private String requestId;
  @SerializedName("receiver")
  private String receiver;
  @SerializedName("email_attachment_list")
  List<EmailAttachment> emailAttachmentList;

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

  public String getBrandName() {
    return brandName;
  }

  public void setBrandName(String brandName) {
    this.brandName = brandName;
  }

  public String getCc() {
    return cc;
  }

  public void setCc(String cc) {
    this.cc = cc;
  }

  public EmailContentRequestDto getContent() {
    return content;
  }

  public void setContent(EmailContentRequestDto content) {
    this.content = content;
  }

  public String getRequestId() {
    return requestId;
  }

  public void setRequestId(String requestId) {
    this.requestId = requestId;
  }

  public String getReceiver() {
    return receiver;
  }

  public void setReceiver(String receiver) {
    this.receiver = receiver;
  }

  public List<EmailAttachment> getEmailAttachmentList() {
    return emailAttachmentList;
  }

  public void setEmailAttachmentList(
      List<EmailAttachment> emailAttachmentList) {
    this.emailAttachmentList = emailAttachmentList;
  }
}
