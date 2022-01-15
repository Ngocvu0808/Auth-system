package com.ttt.mar.leads.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ttt.mar.leads.entities.ApiMethod;
import com.ttt.mar.leads.entities.ApiSecureMethod;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author kietdt
 * @created_date 20/04/2021
 */
@ApiModel(description = "Thong tin khoi tao/ cap nhat kenh phan phoi")
public class DistributeApiRequestDto {

  @ApiModelProperty(value = "Ma kenh phan phoi", example = "1")
  @JsonIgnore
  private Integer id;
  @ApiModelProperty(value = "Ten kenh phan phoi", required = true, example = "VP Bank_1")
  @NotBlank(message = "Name not Blank")
  @Size(max = 255, message = "Name longer than 255 characters")
  private String name;
  @ApiModelProperty(value = "Duong dan kenh phan phoi", required = true, example = "/lead/{id}")
  @NotBlank(message = "url not Blank")
  @Size(max = 500, message = "url longer than 255 characters")
  private String url;
  @ApiModelProperty(value = "Method Api", required = true, example = "GET/POST/PUT/DELETE")
  @NotNull(message = "method not Blank")
  private ApiMethod method;
  @ApiModelProperty(value = "Phuong thuc bao mat", required = true, example = "NONE/BASIC")
  @NotNull(message = "secureMethod not Blank")
  private ApiSecureMethod secureMethod;
  @ApiModelProperty(value = "Ten dang nhap phuong thuc bao mat", example = "abc")
  @Size(max = 255, message = "username longer than 255 characters")
  private String username;
  @ApiModelProperty(value = "responseId from response data", example = "data.id")
  private String responseId;
  @ApiModelProperty(value = "Mat khau dang nhap cua phuong thuc bao mat", example = "abc")
  @Size(max = 255, message = "password longer than 255 characters")
  private String password;
  private List<DistributeApiDataRequestDto> apiDataRequestDtos;
  private List<DistributeApiHeaderRequestDto> apiHeaderRequestDtos;
  private List<DistributeFieldMappingRequestDto> fieldMappingRequestDtos;

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

  public List<DistributeApiDataRequestDto> getApiDataRequestDtos() {
    return apiDataRequestDtos;
  }

  public void setApiDataRequestDtos(
      List<DistributeApiDataRequestDto> apiDataRequestDtos) {
    this.apiDataRequestDtos = apiDataRequestDtos;
  }

  public String getResponseId() {
    return responseId;
  }

  public void setResponseId(String responseId) {
    this.responseId = responseId;
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
}
