package com.ttt.mar.sms.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "sms_history")
public class SmsHistory implements Serializable {

  private static final long serialVersionUID = 4835933664381644422L;

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "receiver")
  private String receiver;

  @Column(name = "config_sms_id", updatable = false, insertable = false)
  private Integer configSmsId;

  @Column(name = "content", columnDefinition = "TEXT")
  private String content;

  @ManyToOne
  @JoinColumn(name = "config_sms_id")
  private SmsConfig smsConfig;

  @Column(name = "created_time")
  private Date createdTime;

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

  public Integer getConfigSmsId() {
    return configSmsId;
  }

  public void setConfigSmsId(Integer configSmsId) {
    this.configSmsId = configSmsId;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public SmsConfig getSmsConfig() {
    return smsConfig;
  }

  public void setSmsConfig(SmsConfig smsConfig) {
    this.smsConfig = smsConfig;
  }

  public Date getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(Date createdTime) {
    this.createdTime = createdTime;
  }


}
