package com.ttt.mar.auth.entities.service;

import com.ttt.mar.auth.entities.enums.ApiStatus;
import com.ttt.mar.auth.entities.enums.ApiType;
import com.ttt.mar.auth.entities.enums.HttpMethod;
import com.ttt.rnd.lib.entities.BaseEntity;
import java.io.Serializable;
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
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "auth_external_api")
@Data
public class ExternalApi extends BaseEntity<Integer> implements Serializable {

  private static final long serialVersionUID = -5381497739820721634L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String api;

  @Enumerated(EnumType.STRING)
  private HttpMethod method;

  private String name;

  private String code;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Column(name = "service_id", insertable = false, updatable = false)
  private Integer serviceId;

  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "service_id", referencedColumnName = "id")
  private Service service;

  @Enumerated(EnumType.STRING)
  private ApiType type;

  @Enumerated(EnumType.STRING)
  private ApiStatus status;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getApi() {
    return api;
  }

  public void setApi(String api) {
    this.api = api;
  }

  public HttpMethod getMethod() {
    return method;
  }

  public void setMethod(HttpMethod method) {
    this.method = method;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Integer getServiceId() {
    return serviceId;
  }

  public void setServiceId(Integer serviceId) {
    this.serviceId = serviceId;
  }

  public Service getService() {
    return service;
  }

  public void setService(Service service) {
    this.service = service;
  }

  public ApiType getType() {
    return type;
  }

  public void setType(ApiType type) {
    this.type = type;
  }

  public ApiStatus getStatus() {
    return status;
  }

  public void setStatus(ApiStatus status) {
    this.status = status;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }
}
