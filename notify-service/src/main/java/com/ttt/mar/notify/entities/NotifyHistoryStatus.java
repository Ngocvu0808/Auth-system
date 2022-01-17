package com.ttt.mar.notify.entities;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "notify_history_status")
public class NotifyHistoryStatus {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @Column(length = 100)
  private String status;

  @Column(columnDefinition = "TEXT")
  private String message;

  @Column(name = "notification_id", insertable = false, updatable = false)
  private Integer notificationId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "notification_id")
  private NotifyHistory notifyHistory;

  @Column(name = "status_notification_id", insertable = false, updatable = false)
  private Integer statusNotificationId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "status_notification_id")
  private StatusNotification statusNotification;

  @Column(name = "created_time")
  @Temporal(TemporalType.TIMESTAMP)
  @CreationTimestamp
  private Date createdTime;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Integer getNotificationId() {
    return notificationId;
  }

  public void setNotificationId(Integer notificationId) {
    this.notificationId = notificationId;
  }

  public NotifyHistory getNotifyHistory() {
    return notifyHistory;
  }

  public void setNotifyHistory(NotifyHistory notifyHistory) {
    this.notifyHistory = notifyHistory;
  }

  public Integer getStatusNotificationId() {
    return statusNotificationId;
  }

  public void setStatusNotificationId(Integer statusNotificationId) {
    this.statusNotificationId = statusNotificationId;
  }

  public StatusNotification getStatusNotification() {
    return statusNotification;
  }

  public void setStatusNotification(StatusNotification statusNotification) {
    this.statusNotification = statusNotification;
  }

  public Date getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(Date createdTime) {
    this.createdTime = createdTime;
  }
}
