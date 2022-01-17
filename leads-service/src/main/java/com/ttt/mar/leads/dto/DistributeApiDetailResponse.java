package com.ttt.mar.leads.dto;

import com.ttt.mar.leads.entities.ApiMethod;
import com.ttt.mar.leads.entities.ApiSecureMethod;
import com.ttt.mar.leads.entities.LeadSourceStatus;
import java.util.Date;
import java.util.List;

public class DistributeApiDetailResponse {

  private Integer id;
  private String name;
  private String url;
  private ApiMethod method;
  private ApiSecureMethod secureMethod;
  private String username;
  private String password;
  private Date createdTime;
  private String creatorName;
  private String responseId;
  private Date modifiedTime;
  private String updaterName;
  private LeadSourceStatus status;
  private List<DistributeApiDataResponseDto> distributeApiDataResponseDtos;
  private List<DistributeApiHeaderResponseDto> distributeApiHeaderResponseDtos;
  private List<DistributeFieldMappingResponseDto> distributeFieldMappingResponseDtos;

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

  public String getResponseId() {
    return responseId;
  }

  public void setResponseId(String responseId) {
    this.responseId = responseId;
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

  public ApiSecureMethod getSecureMethod() {
    return secureMethod;
  }

  public void setSecureMethod(ApiSecureMethod secureMethod) {
    this.secureMethod = secureMethod;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Date getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(Date createdTime) {
    this.createdTime = createdTime;
  }

  public String getCreatorName() {
    return creatorName;
  }

  public void setCreatorName(String creatorName) {
    this.creatorName = creatorName;
  }

  public Date getModifiedTime() {
    return modifiedTime;
  }

  public void setModifiedTime(Date modifiedTime) {
    this.modifiedTime = modifiedTime;
  }

  public String getUpdaterName() {
    return updaterName;
  }

  public void setUpdaterName(String updaterName) {
    this.updaterName = updaterName;
  }

  public LeadSourceStatus getStatus() {
    return status;
  }

  public void setStatus(LeadSourceStatus status) {
    this.status = status;
  }

  public List<DistributeApiDataResponseDto> getDistributeApiDataResponseDtos() {
    return distributeApiDataResponseDtos;
  }

  public void setDistributeApiDataResponseDtos(
      List<DistributeApiDataResponseDto> distributeApiDataResponseDtos) {
    this.distributeApiDataResponseDtos = distributeApiDataResponseDtos;
  }

  public List<DistributeApiHeaderResponseDto> getDistributeApiHeaderResponseDtos() {
    return distributeApiHeaderResponseDtos;
  }

  public void setDistributeApiHeaderResponseDtos(
      List<DistributeApiHeaderResponseDto> distributeApiHeaderResponseDtos) {
    this.distributeApiHeaderResponseDtos = distributeApiHeaderResponseDtos;
  }

  public List<DistributeFieldMappingResponseDto> getDistributeFieldMappingResponseDtos() {
    return distributeFieldMappingResponseDtos;
  }

  public void setDistributeFieldMappingResponseDtos(
      List<DistributeFieldMappingResponseDto> distributeFieldMappingResponseDtos) {
    this.distributeFieldMappingResponseDtos = distributeFieldMappingResponseDtos;
  }
}
