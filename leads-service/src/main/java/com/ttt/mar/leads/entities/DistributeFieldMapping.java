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
@Table(name = "lead_distribute_field_mapping_api")
public class DistributeFieldMapping implements Serializable {

  private static final long serialVersionUID = -1658986924923578417L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "field_source")
  private String fieldSource;

  @Column(name = "field_target")
  private String fieldTarget;

  @Column(name = "validation_code")
  private String validationCode;

  @Column(name = "\"require\"")
  private Boolean require;

  @Column(name = "distribute_id", insertable = false, updatable = false)
  private Integer distributeId;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "distribute_id", referencedColumnName = "id")
  private DistributeApi distribute;

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

  public String getFieldSource() {
    return fieldSource;
  }

  public void setFieldSource(String fieldSource) {
    this.fieldSource = fieldSource;
  }

  public String getFieldTarget() {
    return fieldTarget;
  }

  public void setFieldTarget(String fieldTarget) {
    this.fieldTarget = fieldTarget;
  }

  public String getValidationCode() {
    return validationCode;
  }

  public void setValidationCode(String validationCode) {
    this.validationCode = validationCode;
  }

  public Boolean getRequire() {
    return require;
  }

  public void setRequire(Boolean require) {
    this.require = require;
  }

  public Integer getDistributeId() {
    return distributeId;
  }

  public void setDistributeId(Integer distributeId) {
    this.distributeId = distributeId;
  }

  public DistributeApi getDistribute() {
    return distribute;
  }

  public void setDistribute(DistributeApi distribute) {
    this.distribute = distribute;
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
