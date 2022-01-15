package com.ttt.mar.sms.dto.smshistory;

import java.util.Date;

public class SmsHistoryDtoResponse {

  private Long id;
  private String receiver;
  private String content;
  private Date creationTime;
  private Long creationTimeStamp;
  private Date lastModified;
  private String sender;
//  private SmsStatus status;


  public Long getCreationTimeStamp() {
    return creationTimeStamp;
  }

  public void setCreationTimeStamp(Long creationTimeStamp) {
    this.creationTimeStamp = creationTimeStamp;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getReceiver() {
    return receiver;
  }

  public void setReceiver(String receiver) {
    this.receiver = receiver;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public Date getCreationTime() {
    return creationTime;
  }

  public void setCreationTime(Date creationTime) {
    this.creationTime = creationTime;
  }

  public Date getLastModified() {
    return lastModified;
  }

  public void setLastModified(Date lastModified) {
    this.lastModified = lastModified;
  }

  public String getSender() {
    return sender;
  }

  public void setSender(String sender) {
    this.sender = sender;
  }

//  public SmsStatus getStatus() {
//    return status;
//  }
//
//  public void setStatus(SmsStatus status) {
//    this.status = status;
//  }
}
