package com.ttt.mar.auth.entities.application;

import com.ttt.rnd.lib.entities.User;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * @author bontk
 * @created_date 03/06/2020
 */

@Data
@Entity
@Table(name = "auth_api_key")
public class ApiKey implements Serializable {

  private static final long serialVersionUID = -8448334656386508649L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "api_key")
  private String apiKey;

  @Column(name = "user_id", insertable = false, updatable = false)
  private Integer userId;

  @ManyToOne(cascade = CascadeType.MERGE)
  @OnDelete(action = OnDeleteAction.NO_ACTION)
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private User user;

  @Column(name = "created_time")
  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  private Date createdTime;

  @Column(name = "is_deleted")
  private Boolean isDeleted;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getApiKey() {
    return apiKey;
  }

  public void setApiKey(String apiKey) {
    this.apiKey = apiKey;
  }

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public Date getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(Date creationTime) {
    this.createdTime = creationTime;
  }

  public Boolean getDeleted() {
    return isDeleted;
  }

  public void setDeleted(Boolean deleted) {
    isDeleted = deleted;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
