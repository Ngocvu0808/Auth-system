package com.ttt.mar.notify.entities;

import com.ttt.rnd.lib.entities.BaseEntity;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(name = "notify_history")
public class NotifyHistory extends BaseEntity<Integer> implements Serializable {

  private static final long serialVersionUID = 59128674105867349L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "account_code")
  private String accountCode;

  @Column(columnDefinition = "TEXT")
  private String receiver;

  @Column(columnDefinition = "TEXT")
  private String cc;

  @Column(columnDefinition = "TEXT")
  private String bcc;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "request_time")
  private Date requestTime;

  @Column(columnDefinition = "TEXT")
  private String content;

  private String brandName;

  private String publisher;

  @Column(name = "type_send_id", updatable = false, insertable = false)
  private Integer typeSendId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "type_send_id")
  private TypeSend typeSend;

  @LazyCollection(LazyCollectionOption.TRUE)
  @OneToMany(mappedBy = "notifyHistory")
  private List<NotifyHistoryStatus> notifyHistoryStatuses;

  @Column(name = "sender")
  private String sender;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public Date getRequestTime() {
    return requestTime;
  }

  public void setRequestTime(Date requestTime) {
    this.requestTime = requestTime;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getBrandName() {
    return brandName;
  }

  public void setBrandName(String brandName) {
    this.brandName = brandName;
  }

  public String getPublisher() {
    return publisher;
  }

  public void setPublisher(String publisher) {
    this.publisher = publisher;
  }

  public Integer getTypeSendId() {
    return typeSendId;
  }

  public void setTypeSendId(Integer typeSendId) {
    this.typeSendId = typeSendId;
  }

  public TypeSend getTypeSend() {
    return typeSend;
  }

  public void setTypeSend(TypeSend typeSend) {
    this.typeSend = typeSend;
  }

  public List<NotifyHistoryStatus> getNotifyHistoryStatuses() {
    return notifyHistoryStatuses;
  }

  public void setNotifyHistoryStatuses(
      List<NotifyHistoryStatus> notifyHistoryStatuses) {
    this.notifyHistoryStatuses = notifyHistoryStatuses;
  }

  public String getSender() {
    return sender;
  }

  public void setSender(String sender) {
    this.sender = sender;
  }
}
