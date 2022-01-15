package com.ttt.mar.leads.entities;

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

/**
 * @author nguyen
 * @create_date 07/09/2021
 */
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "lead_campaign_schedule")
public class Schedule {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "campaign_id", insertable = false, updatable = false)
  private Integer campaignId;

  @ManyToOne(cascade = CascadeType.MERGE)
  @OnDelete(action = OnDeleteAction.NO_ACTION)
  @JoinColumn(name = "campaign_id", referencedColumnName = "id")
  private Campaign campaign;

  @Column(name = "type", updatable = false)
  private String type;

  @Column(name = "name")
  private String name;

  @Column(name = "is_limit")
  private boolean isLimit;

  @Column(name = "days_limit")
  private int dayLimit;

  @Column(name = "start_time")
  private Date startTime;

  @Enumerated(EnumType.STRING)
  private ScheduleStatus status = ScheduleStatus.DEACTIVE;

  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "create_time", updatable = false)
  private Date createdTime;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "modified_time")
  private Date modifiedTime;

  @Column(name = "creator_id")
  private Integer creatorId;

  @Column(name = "updater_id")
  private Integer updaterId;

  @Column(name = "is_deleted", nullable = false, columnDefinition = "boolean not null default false")
  private Boolean isDeleted = false;

  @Column(name = "deleter_id")
  private Integer deleterUserId;

  @PreUpdate
  public void setModifiedTime() {
    this.modifiedTime = new Date();
  }

}
