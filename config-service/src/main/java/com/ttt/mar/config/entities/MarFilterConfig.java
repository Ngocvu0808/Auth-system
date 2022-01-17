package com.ttt.mar.config.entities;

import com.ttt.rnd.lib.entities.BaseEntity;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "mar_filter_config")
public class MarFilterConfig extends BaseEntity<Integer> implements Serializable {

  private static final long serialVersionUID = -1882559597006433313L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @ManyToOne()
  @JoinColumn(name = "serviceid", referencedColumnName = "id")
  private MarServiceConfig service;
  @Column(name = "serviceid", insertable = false, updatable = false)
  private Integer serviceId;
  @Column(name = "filter_type")
  private String filterType;
  private String name;
  private String value;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public MarServiceConfig getService() {
    return service;
  }

  public void setService(MarServiceConfig service) {
    this.service = service;
  }

  public Integer getServiceId() {
    return serviceId;
  }

  public void setServiceId(Integer serviceId) {
    this.serviceId = serviceId;
  }

  public String getFilterType() {
    return filterType;
  }

  public void setFilterType(String filterType) {
    this.filterType = filterType;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }


}
