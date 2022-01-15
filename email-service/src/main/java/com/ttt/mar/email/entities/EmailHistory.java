package com.ttt.mar.email.entities;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Table(name = "email_history")
@Entity
public class EmailHistory {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "receiver")
  private String receiver;

  @Column(name = "content",columnDefinition = "TEXT")
  private String content;

  @Column(name = "cc",columnDefinition = "TEXT")
  private String cc;

  @Column(name = "bcc",columnDefinition = "TEXT")
  private String bcc;

  @Column(name = "createdTime")
  private Date createdTime;

  @Column(name = "email_config_id", updatable = false, insertable = false)
  private Integer emailConfigId;

  @ManyToOne
  @JoinColumn(name = "email_config_id")
  private EmailConfig emailConfig;

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

  public Date getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(Date createdTime) {
    this.createdTime = createdTime;
  }

  public Integer getEmailConfigId() {
    return emailConfigId;
  }

  public void setEmailConfigId(Integer emailConfigId) {
    this.emailConfigId = emailConfigId;
  }

  public EmailConfig getEmailConfig() {
    return emailConfig;
  }

  public void setEmailConfig(EmailConfig emailConfig) {
    this.emailConfig = emailConfig;
  }
}
