package com.ttt.mar.email.dto.emailhistory;

import com.ttt.mar.email.entities.StatusEmail;
import java.util.Date;

public class EmailHistoryDtoResponse {

  private Long id;

  private String receiver;

  private String title;

  private String content;

  private Date creationTime;

  private Long creationTimeStamp;

  private Date lastModified;

  private String attachFileUrl;

  private StatusEmail status;

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

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
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

  public Long getCreationTimeStamp() {
    return creationTimeStamp;
  }

  public void setCreationTimeStamp(Long creationTimeStamp) {
    this.creationTimeStamp = creationTimeStamp;
  }

  public String getAttachFileUrl() {
    return attachFileUrl;
  }

  public void setAttachFileUrl(String attachFileUrl) {
    this.attachFileUrl = attachFileUrl;
  }


  public StatusEmail getStatus() {
    return status;
  }

  public void setStatus(StatusEmail status) {
    this.status = status;
  }

  public Date getLastModified() {
    return lastModified;
  }

  public void setLastModified(Date lastModified) {
    this.lastModified = lastModified;
  }
}
