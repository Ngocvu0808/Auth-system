package com.ttt.mar.leads.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.CreationTimestamp;

/**
 * @author bontk
 * @created_date 22/03/2021
 */
@Table(name = "lead_source")
@Entity
public class LeadSource implements Serializable {

  private static final long serialVersionUID = -5453594700758859160L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String source;

  private String name;

  private String utmSource;

  @OneToMany(mappedBy = "leadSource")
  private List<LeadSourceFieldValidation> leadSourceFieldValidations;

  @Enumerated(EnumType.STRING)
  private LeadSourceStatus status = LeadSourceStatus.DEACTIVE;

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

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUtmSource() {
    return utmSource;
  }

  public void setUtmSource(String utmSource) {
    this.utmSource = utmSource;
  }

  public List<LeadSourceFieldValidation> getLeadSourceFieldValidations() {
    return leadSourceFieldValidations;
  }

  public void setLeadSourceFieldValidations(
      List<LeadSourceFieldValidation> leadSourceFieldValidations) {
    this.leadSourceFieldValidations = leadSourceFieldValidations;
  }

  public LeadSourceStatus getStatus() {
    return status;
  }

  public void setStatus(LeadSourceStatus status) {
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
    return this.isDeleted;
  }

  public void setIsDeleted(final Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }
}
