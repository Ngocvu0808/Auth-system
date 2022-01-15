package com.ttt.mar.leads.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.CreationTimestamp;

/**
 * @author bontk
 * @created_date 22/03/2021
 */

@Entity
@Table(name = "lead_distribute_api")
public class DistributeApi implements Serializable {

  private static final long serialVersionUID = 7858481825046248473L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String name;

  private String description;

  @Enumerated(EnumType.STRING)
  private ApiMethod method;

  @Column(name = "secure_method")
  @Enumerated(EnumType.STRING)
  private ApiSecureMethod secureMethod;

  private String username;

  private String password;

  @Column(length = 500)
  private String url;

  @Enumerated(EnumType.STRING)
  private DistributeApiStatus status = DistributeApiStatus.DEACTIVE;

  @Column(name = "response_id")
  private String responseId;

  @Column(name = "creator_id")
  private Integer creatorUserId;

  @Column(name = "updater_id")
  private Integer updaterUserId;

  @Column(name = "deleter_id")
  private Integer deleterUserId;

  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "created_time", updatable = false)
  private Date createdTime;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "modified_time")
  private Date modifiedTime;

  @Column(name = "is_deleted", nullable = false, columnDefinition = "boolean not null default false")
  private Boolean isDeleted = false;

  public String getResponseId() {
    return responseId;
  }

  public void setResponseId(String responseId) {
    this.responseId = responseId;
  }

  public Integer getId() {
    return id;
  }

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

  public ApiMethod getMethod() {
    return method;
  }

  public void setMethod(ApiMethod method) {
    this.method = method;
  }

  public ApiSecureMethod getSecureMethod() {
    return secureMethod;
  }

  public void setSecureMethod(ApiSecureMethod secureMethod) {
    this.secureMethod = secureMethod;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public DistributeApiStatus getStatus() {
    return status;
  }

  public void setStatus(DistributeApiStatus status) {
    this.status = status;
  }

  public Integer getCreatorUserId() {
    return creatorUserId;
  }

  public void setCreatorUserId(Integer creatorUserId) {
    this.creatorUserId = creatorUserId;
  }

  public Integer getUpdaterUserId() {
    return updaterUserId;
  }

  public void setUpdaterUserId(Integer updaterUserId) {
    this.updaterUserId = updaterUserId;
  }

  public Integer getDeleterUserId() {
    return deleterUserId;
  }

  public void setDeleterUserId(Integer deleterUserId) {
    this.deleterUserId = deleterUserId;
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

  @PreUpdate
  public void setModifiedTime() {
    this.modifiedTime = new Date();
  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(Boolean deleted) {
    isDeleted = deleted;
  }
}
