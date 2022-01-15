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
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "lead_offer")
public class Offer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String name;

  @Column(length = 50)
  @NotNull
  private String code;

  @Column(name = "csid")
  private Integer csId;

  @Column(name = "creator_id")
  private Integer creatorUserId;

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

  @PreUpdate
  public void setModifiedTime() {
    this.modifiedTime = new Date();
  }
}
