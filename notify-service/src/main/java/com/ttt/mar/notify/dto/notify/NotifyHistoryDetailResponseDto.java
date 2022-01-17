package com.ttt.mar.notify.dto.notify;

import java.util.Date;
import java.util.List;

public class NotifyHistoryDetailResponseDto {

  private Integer id;
  private String accountCode;
  private String typeSendName;
  private String sender;
  private String receiver;
  private String publisher;
  private ContentNotifyResponse content;
  private String brandName;
  private String status;
  private Date createdTime;
  private Date modifiedTime;
  private String creatorName;
  private String typeSendCode;
  private List<NotifyHistoryStatusResponse> notifyHistoryStatuses;
  private String cc;
  private String bcc;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getAccountCode() {
    return accountCode;
  }

  public void setAccountCode(String accountCode) {
    this.accountCode = accountCode;
  }

  public String getTypeSendName() {
    return typeSendName;
  }

  public void setTypeSendName(String typeSendName) {
    this.typeSendName = typeSendName;
  }

  public String getSender() {
    return sender;
  }

  public void setSender(String sender) {
    this.sender = sender;
  }

  public String getReceiver() {
    return receiver;
  }

  public void setReceiver(String receiver) {
    this.receiver = receiver;
  }

  public String getPublisher() {
    return publisher;
  }

  public void setPublisher(String publisher) {
    this.publisher = publisher;
  }

  public ContentNotifyResponse getContent() {
    return content;
  }

  public void setContent(ContentNotifyResponse content) {
    this.content = content;
  }

  public String getBrandName() {
    return brandName;
  }

  public void setBrandName(String brandName) {
    this.brandName = brandName;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Date getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(Date createdTime) {
    this.createdTime = createdTime;
  }

  public Date getModifiedTime() {
    return modifiedTime;
  }

  public void setModifiedTime(Date modifiedTime) {
    this.modifiedTime = modifiedTime;
  }

  public String getCreatorName() {
    return creatorName;
  }

  public void setCreatorName(String creatorName) {
    this.creatorName = creatorName;
  }

  public String getTypeSendCode() {
    return typeSendCode;
  }

  public void setTypeSendCode(String typeSendCode) {
    this.typeSendCode = typeSendCode;
  }

  public List<NotifyHistoryStatusResponse> getNotifyHistoryStatuses() {
    return notifyHistoryStatuses;
  }

  public void setNotifyHistoryStatuses(
      List<NotifyHistoryStatusResponse> notifyHistoryStatuses) {
    this.notifyHistoryStatuses = notifyHistoryStatuses;
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
}
