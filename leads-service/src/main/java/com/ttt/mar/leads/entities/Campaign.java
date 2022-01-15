package com.ttt.mar.leads.entities;

import com.ttt.rnd.lib.entities.GroupUser;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 * @author kietdt
 * @created_date 20/04/2021
 */
@Entity
@Table(name = "lead_campaign")
public class Campaign implements Serializable {

  private static final long serialVersionUID = 2397652506933228399L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String name;

  private String code;

  @Column(name = "start_date")
  @Temporal(TemporalType.DATE)
  private Date startDate;

  @Column(name = "end_date")
  @Temporal(TemporalType.DATE)
  private Date endDate;

  @Column(name = "project_id", insertable = false, updatable = false)
  private Integer projectId;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "project_id", referencedColumnName = "id")
  private Project project;

  @Enumerated(EnumType.STRING)
  private CampaignStatus status = CampaignStatus.DEACTIVE;

  @Column(length = 1000)
  private String description;

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

  @LazyCollection(LazyCollectionOption.TRUE)
  @OneToMany(mappedBy = "campaign", cascade = {CascadeType.ALL})
  private List<CampaignDistribute> campaignDistributes;

  @LazyCollection(LazyCollectionOption.TRUE)
  @OneToMany(mappedBy = "campaign", cascade = {CascadeType.ALL})
  private List<CampaignSource> campaignSources;

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

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public Integer getProjectId() {
    return projectId;
  }

  public void setProjectId(Integer projectId) {
    this.projectId = projectId;
  }

  public Project getProject() {
    return project;
  }

  public void setProject(Project project) {
    this.project = project;
  }

  public CampaignStatus getStatus() {
    return status;
  }

  public void setStatus(CampaignStatus status) {
    this.status = status;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
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

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setDeleted(Boolean deleted) {
    isDeleted = deleted;
  }

  public List<CampaignDistribute> getCampaignDistributes() {
    return campaignDistributes;
  }

  public void setCampaignDistributes(
      List<CampaignDistribute> campaignDistributes) {
    this.campaignDistributes = campaignDistributes;
  }

  public List<CampaignSource> getCampaignSources() {
    return campaignSources;
  }

  public void setCampaignSources(List<CampaignSource> campaignSources) {
    this.campaignSources = campaignSources;
  }
}
