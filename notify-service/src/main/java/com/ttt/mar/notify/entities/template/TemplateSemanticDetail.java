package com.ttt.mar.notify.entities.template;

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

@Entity
@Table(name = "template_semantic_detail")
public class TemplateSemanticDetail implements Serializable {

  private static final long serialVersionUID = 2746992921155240209L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "template_id", insertable = false, updatable = false)
  private Integer templateId;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "template_id", referencedColumnName = "id")
  private Template template;

  @Column(name = "template_semantic_id", insertable = false, updatable = false)
  private Integer templateSemanticId;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "template_semantic_id", referencedColumnName = "id")
  private TemplateSemantic templateSemantic;

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

  public Integer getTemplateId() {
    return templateId;
  }

  public void setTemplateId(Integer templateId) {
    this.templateId = templateId;
  }

  public Template getTemplate() {
    return template;
  }

  public void setTemplate(Template template) {
    this.template = template;
  }

  public Integer getTemplateSemanticId() {
    return templateSemanticId;
  }

  public void setTemplateSemanticId(Integer templateSemanticId) {
    this.templateSemanticId = templateSemanticId;
  }

  public TemplateSemantic getTemplateSemantic() {
    return templateSemantic;
  }

  public void setTemplateSemantic(TemplateSemantic templateSemantic) {
    this.templateSemantic = templateSemantic;
  }

  public TemplateSemantic getTemplateSematic() {
    return templateSemantic;
  }

  public void setTemplateSematic(TemplateSemantic templateSemantic) {
    this.templateSemantic = templateSemantic;
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
