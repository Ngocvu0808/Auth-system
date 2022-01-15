package com.ttt.mar.email.entities;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Table(name = "status_notification")
@Entity
public class StatusNotification {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @Column(name = "name", length = 100)
  private String name;

  private String description;

  public Integer getId() {
    return id;
  }

  @LazyCollection(LazyCollectionOption.TRUE)
  @OneToMany(mappedBy = "statusNotification")
  private List<NotifyHistoryStatus> notifyHistoryStatuses;

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<NotifyHistoryStatus> getNotifyHistoryStatuses() {
    return notifyHistoryStatuses;
  }

  public void setNotifyHistoryStatuses(
      List<NotifyHistoryStatus> notifyHistoryStatuses) {
    this.notifyHistoryStatuses = notifyHistoryStatuses;
  }
}
