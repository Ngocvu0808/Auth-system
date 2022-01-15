package com.ttt.mar.leads.entities;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "lead_filter")
public class Filter {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(length = 50)
  private String code;

  @Column(name = "name")
  private String name;

  @Column(name = "creator_id")
  private Integer creatorUserId;

  @Column(name = "description", length = 1000)
  private String description;

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

  @PreUpdate
  public void setModifiedTime() {
    this.modifiedTime = new Date();
  }
}
