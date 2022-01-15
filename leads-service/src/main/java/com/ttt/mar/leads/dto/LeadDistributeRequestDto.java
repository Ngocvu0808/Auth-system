package com.ttt.mar.leads.dto;

import com.ttt.mar.leads.entities.ApiMethod;
import com.ttt.mar.leads.entities.ApiSecureMethod;
import java.util.List;

/**
 * @author nguyen
 * @create_date 07/10/2021
 */
public class LeadDistributeRequestDto {

  private int id;
  private String url;
  private ApiMethod method;
  private List<DistributeApiDataRequestDto> apiDataRequestDtos;
  private List<DistributeApiHeaderRequestDto> apiHeaderRequestDtos;
  private List<DistributeFieldMappingRequestDto> fieldMappingRequestDtos;
  private String name;
  private String password;
  private String userName;
  private ApiSecureMethod secureMethod;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
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

  public List<DistributeApiDataRequestDto> getApiDataRequestDtos() {
    return apiDataRequestDtos;
  }

  public void setApiDataRequestDtos(
      List<DistributeApiDataRequestDto> apiDataRequestDtos) {
    this.apiDataRequestDtos = apiDataRequestDtos;
  }

  public List<DistributeApiHeaderRequestDto> getApiHeaderRequestDtos() {
    return apiHeaderRequestDtos;
  }

  public void setApiHeaderRequestDtos(
      List<DistributeApiHeaderRequestDto> apiHeaderRequestDtos) {
    this.apiHeaderRequestDtos = apiHeaderRequestDtos;
  }

  public List<DistributeFieldMappingRequestDto> getFieldMappingRequestDtos() {
    return fieldMappingRequestDtos;
  }

  public void setFieldMappingRequestDtos(
      List<DistributeFieldMappingRequestDto> fieldMappingRequestDtos) {
    this.fieldMappingRequestDtos = fieldMappingRequestDtos;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public ApiSecureMethod getSecureMethod() {
    return secureMethod;
  }

  public void setSecureMethod(ApiSecureMethod secureMethod) {
    this.secureMethod = secureMethod;
  }
}
