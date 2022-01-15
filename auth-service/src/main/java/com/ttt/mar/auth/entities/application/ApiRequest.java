package com.ttt.mar.auth.entities.application;

import com.ttt.mar.auth.entities.enums.ApiRequestStatus;
import com.ttt.mar.auth.entities.service.ExternalApi;
import com.ttt.rnd.lib.entities.User;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

/**
 * @author bontk
 * @created_date 31/07/2020
 */
@Data
@Entity
@Table(name = "auth_client_api_request")
public class ApiRequest implements Serializable {

  public static final long serialVersionUID = -6295276633351457168L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "client_id", insertable = false, updatable = false)
  private Integer clientId;

  @Column(name = "api_id", insertable = false, updatable = false)
  private Long apiId;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Enumerated(EnumType.STRING)
  private ApiRequestStatus status;

  @Column(name = "created_time")
  @Temporal(TemporalType.TIMESTAMP)
  @CreationTimestamp
  private Date createdTime;

  @Column(name = "is_deleted")
  private Boolean isDeleted = false;

  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "client_id", referencedColumnName = "id")
  private Client client;

  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "api_id", referencedColumnName = "id")
  private ExternalApi api;

  @Column(name = "creator_user_id", insertable = false, updatable = false)
  private Integer creatorUserId;

  @Column(name = "updater_user_id")
  private Integer updaterUserI;

  @Column(name = "deleter_user_id")
  private Integer deleterUserId;

  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "creator_user_id", referencedColumnName = "id")
  private User creatorUser;

  @Column(columnDefinition = "TEXT")
  private String purpose;


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Integer getClientId() {
    return clientId;
  }

  public void setClientId(Integer clientId) {
    this.clientId = clientId;
  }

  public Long getApiId() {
    return apiId;
  }

  public void setApiId(Long apiId) {
    this.apiId = apiId;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public ApiRequestStatus getStatus() {
    return status;
  }

  public void setStatus(ApiRequestStatus status) {
    this.status = status;
  }

  public Date getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(Date createdTime) {
    this.createdTime = createdTime;
  }

  public Boolean getDeleted() {
    return isDeleted;
  }

  public void setDeleted(Boolean deleted) {
    isDeleted = deleted;
  }

  public Client getClient() {
    return client;
  }

  public void setClient(Client client) {
    this.client = client;
  }

  public ExternalApi getApi() {
    return api;
  }

  public void setApi(ExternalApi api) {
    this.api = api;
  }

  public Integer getCreatorUserId() {
    return creatorUserId;
  }

  public void setCreatorUserId(Integer creatorUserId) {
    this.creatorUserId = creatorUserId;
  }

  public User getCreatorUser() {
    return creatorUser;
  }

  public void setCreatorUser(User creatorUser) {
    this.creatorUser = creatorUser;
  }

  public Integer getUpdaterUserI() {
    return updaterUserI;
  }

  public void setUpdaterUserI(Integer updaterUserI) {
    this.updaterUserI = updaterUserI;
  }

  public Integer getDeleterUserId() {
    return deleterUserId;
  }

  public void setDeleterUserId(Integer deleterUserId) {
    this.deleterUserId = deleterUserId;
  }

  public String getPurpose() {
    return purpose;
  }

  public void setPurpose(String purpose) {
    this.purpose = purpose;
  }
}
