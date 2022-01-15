package com.ttt.mar.leads.dto;

import com.ttt.mar.leads.entities.ApiMethod;
import com.ttt.mar.leads.entities.DistributeApiStatus;

/**
 * @author kietdt
 * @created_date 20/04/2021
 */
public class ProjectDistributeResponseDto {

  private Integer id;

  private String name;

  private String url;

  private ApiMethod method;

  private DistributeApiStatus status;

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

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public ApiMethod getMethod() {
    return method;
  }

  public void setMethod(ApiMethod method) {
    this.method = method;
  }

  public DistributeApiStatus getStatus() {
    return status;
  }

  public void setStatus(DistributeApiStatus status) {
    this.status = status;
  }
}
