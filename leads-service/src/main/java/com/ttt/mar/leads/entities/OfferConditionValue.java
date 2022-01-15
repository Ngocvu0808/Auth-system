package com.ttt.mar.leads.entities;

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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lead_offer_condition_value")
public class OfferConditionValue {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "offer_condition_id", insertable = false, updatable = false)
  private Integer offerConditionId;

  @ManyToOne(cascade = CascadeType.ALL)
  @OnDelete(action = OnDeleteAction.NO_ACTION)
  @JoinColumn(name = "offer_condition_id", referencedColumnName = "id")
  private OfferCondition offerCondition;

  @Column(name = "value", columnDefinition = "TEXT")
  private String value;

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

  @Column(name = "is_deleted", nullable = false, columnDefinition = "boolean not null default false")
  private Boolean isDeleted = false;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "modified_time")
  private Date modifiedTime;

  public OfferConditionValue(OfferCondition offerCondition, String value,
      Integer creatorUserId) {
    this.offerCondition = offerCondition;
    this.value = value;
    this.creatorUserId = creatorUserId;
  }

  @PreUpdate
  public void setModifiedTime() {
    this.modifiedTime = new Date();
  }
}
