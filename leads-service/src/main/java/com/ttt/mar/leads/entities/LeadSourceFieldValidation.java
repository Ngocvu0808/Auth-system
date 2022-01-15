package com.ttt.mar.leads.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
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
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * @author bontk
 * @created_date 22/03/2021
 */
@Table(name = "lead_source_field_validation")
@Entity
@EntityListeners({AuditingEntityListener.class})
public class LeadSourceFieldValidation implements Serializable {

  private static final long serialVersionUID = -7447297991096586683L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "field_code")
  private String fieldCode;

  @Column(name = "\"require\"")
  private Boolean require;

  @JoinColumn(name = "validation_code")
  private String validationCode;

  @Column(name = "lead_source_id", insertable = false, updatable = false)
  private Integer leadSourceId;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "lead_source_id", referencedColumnName = "id")
  private LeadSource leadSource;

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

  public String getFieldCode() {
    return fieldCode;
  }

  public void setFieldCode(String fieldCode) {
    this.fieldCode = fieldCode;
  }

  public Boolean getRequire() {
    return require;
  }

  public void setRequire(Boolean require) {
    this.require = require;
  }

  public String getValidationCode() {
    return validationCode;
  }

  public void setValidationCode(String validationCode) {
    this.validationCode = validationCode;
  }

  public Integer getLeadSourceId() {
    return leadSourceId;
  }

  public void setLeadSourceId(Integer leadSourceId) {
    this.leadSourceId = leadSourceId;
  }

  public LeadSource getLeadSource() {
    return leadSource;
  }

  public void setLeadSource(LeadSource leadSource) {
    this.leadSource = leadSource;
  }

  public static long getSerialVersionUID() {
    return serialVersionUID;
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
