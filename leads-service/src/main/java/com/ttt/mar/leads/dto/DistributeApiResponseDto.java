package com.ttt.mar.leads.dto;

import com.ttt.mar.leads.entities.ApiMethod;
import com.ttt.mar.leads.entities.DistributeApiStatus;
import java.util.Date;

public class DistributeApiResponseDto {

  private Integer id;
  private String name;
  private ApiMethod method;
  private String url;
  private DistributeApiStatus status;
  private String responseId;
  private Date createdTime;
  private UserResponseDto userResponseDto;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getResponseId() {
    return responseId;
  }

  public void setResponseId(String responseId) {
    this.responseId = responseId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ApiMethod getMethod() {
    return method;
  }

  public void setMethod(ApiMethod method) {
    this.method = method;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public DistributeApiStatus getStatus() {
    return status;
  }

  public void setStatus(DistributeApiStatus status) {
    this.status = status;
  }

  public Date getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(Date createdTime) {
    this.createdTime = createdTime;
  }

  public UserResponseDto getUserResponseDto() {
    return userResponseDto;
  }

  public void setUserResponseDto(UserResponseDto userResponseDto) {
    this.userResponseDto = userResponseDto;
  }
}
