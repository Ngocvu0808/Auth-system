package com.ttt.mar.leads.entities;

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
import org.hibernate.annotations.CreationTimestamp;

/**
 * @author kietdt
 * @created_date 22/04/2021
 */
@Entity
@Table(name = "lead_project_user_permission")
public class ProjectUserPermission implements Serializable {

  private static final long serialVersionUID = -8243776226308783820L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "project_user_id", insertable = false, updatable = false)
  private Integer projectUserId;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "project_user_id", referencedColumnName = "id")
  private ProjectUser projectUser;

  @Column(name = "role_id")
  private Integer roleId;

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

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getProjectUserId() {
    return projectUserId;
  }

  public void setProjectUserId(Integer projectUserId) {
    this.projectUserId = projectUserId;
  }

  public ProjectUser getProjectUser() {
    return projectUser;
  }

  public void setProjectUser(ProjectUser projectUser) {
    this.projectUser = projectUser;
  }

  public Integer getRoleId() {
    return roleId;
  }

  public void setRoleId(Integer roleId) {
    this.roleId = roleId;
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

  public void setModifiedTime(Date modifiedTime) {
    this.modifiedTime = modifiedTime;
  }

  public Boolean getDeleted() {
    return isDeleted;
  }

  public void setDeleted(Boolean deleted) {
    isDeleted = deleted;
  }
}
