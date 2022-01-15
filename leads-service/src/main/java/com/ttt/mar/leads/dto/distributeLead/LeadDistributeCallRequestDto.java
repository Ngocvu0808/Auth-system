package com.ttt.mar.leads.dto.distributeLead;

import com.ttt.mar.leads.entities.ApiMethod;
import com.ttt.mar.leads.entities.ApiSecureMethod;
import com.ttt.mar.leads.entities.DistributeApiStatus;
import java.util.List;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * @author nguyen
 * @create_date 13/10/2021
 */
public class LeadDistributeCallRequestDto {

  private Integer id;
  private String name;

  private String description;

  @Enumerated(EnumType.STRING)
  private ApiMethod method;

  @Enumerated(EnumType.STRING)
  private ApiSecureMethod secureMethod;

  private String username;

  private String password;

  private String url;

  private ApiAuthDto apiAuth;

  public ApiAuthDto getApiAuth() {
    return apiAuth;
  }

  public void setApiAuth(ApiAuthDto apiAuth) {
    this.apiAuth = apiAuth;
  }

  @Enumerated(EnumType.STRING)
  private DistributeApiStatus status = DistributeApiStatus.DEACTIVE;

  List<DistributeApiDataDto> dataDtos;
  List<DistributeApiHeaderDto> headerDtos;
  List<DistributeFieldMappingDto> fieldMappingDtos;

  private String responseId;

  public String getResponseId() {
    return responseId;
  }

  public void setResponseId(String responseId) {
    this.responseId = responseId;
  }

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

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
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

  public List<DistributeApiDataDto> getDataDtos() {
    return dataDtos;
  }

  public void setDataDtos(
      List<DistributeApiDataDto> dataDtos) {
    this.dataDtos = dataDtos;
  }

  public List<DistributeApiHeaderDto> getHeaderDtos() {
    return headerDtos;
  }

  public void setHeaderDtos(
      List<DistributeApiHeaderDto> headerDtos) {
    this.headerDtos = headerDtos;
  }

  public List<DistributeFieldMappingDto> getFieldMappingDtos() {
    return fieldMappingDtos;
  }

  public void setFieldMappingDtos(
      List<DistributeFieldMappingDto> fieldMappingDtos) {
    this.fieldMappingDtos = fieldMappingDtos;
  }
}
