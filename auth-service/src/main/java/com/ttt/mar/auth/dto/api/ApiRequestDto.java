package com.ttt.mar.auth.dto.api;

import com.ttt.mar.auth.dto.app.ClientCustomDto;
import com.ttt.mar.auth.dto.appservice.SystemCustomDto;
import com.ttt.mar.auth.dto.service.CustomServiceDto;
import com.ttt.mar.auth.entities.enums.ApiRequestStatus;
import com.ttt.mar.auth.entities.enums.ApiType;
import com.ttt.mar.auth.entities.enums.HttpMethod;
import java.util.Date;

/**
 * @author bontk
 * @created_date 05/08/2020
 */
public class ApiRequestDto {

  private Long id;
  private String name;
  private String code;
  private Long apiId;
  private String api;
  private HttpMethod method;
  private SystemCustomDto system;
  private CustomServiceDto service;
  private ClientCustomDto client;
  private ApiType type;
  private ApiRequestStatus status;
  private String purpose;
  private String username;
  private Date createdTime;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
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

  public Long getApiId() {
    return apiId;
  }

  public void setApiId(Long apiId) {
    this.apiId = apiId;
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

  public SystemCustomDto getSystem() {
    return system;
  }

  public void setSystem(SystemCustomDto system) {
    this.system = system;
  }

  public CustomServiceDto getService() {
    return service;
  }

  public void setService(CustomServiceDto service) {
    this.service = service;
  }

  public ClientCustomDto getClient() {
    return client;
  }

  public void setClient(ClientCustomDto client) {
    this.client = client;
  }

  public ApiType getType() {
    return type;
  }

  public void setType(ApiType type) {
    this.type = type;
  }

  public ApiRequestStatus getStatus() {
    return status;
  }

  public void setStatus(ApiRequestStatus status) {
    this.status = status;
  }

  public String getPurpose() {
    return purpose;
  }

  public void setPurpose(String purpose) {
    this.purpose = purpose;
  }

  public Date getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(Date createdTime) {
    this.createdTime = createdTime;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }
}
