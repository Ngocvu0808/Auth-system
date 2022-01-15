package com.ttt.mar.leads.dto.distributeLead;

import com.ttt.mar.leads.entities.ApiMethod;
import javax.persistence.OneToOne;

/**
 * @author nguyen
 * @create_date 13/10/2021
 */
public class ApiEndpoint {

  private String url;

  private Integer serviceReceiveId;

  private ApiMethod apiMethod;

  private ApiAuthDto apiAuth;

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public Integer getServiceReceiveId() {
    return serviceReceiveId;
  }

  public void setServiceReceiveId(Integer serviceReceiveId) {
    this.serviceReceiveId = serviceReceiveId;
  }

  public ApiMethod getApiMethod() {
    return apiMethod;
  }

  public void setApiMethod(ApiMethod apiMethod) {
    this.apiMethod = apiMethod;
  }

  public ApiAuthDto getApiAuth() {
    return apiAuth;
  }

  public void setApiAuth(ApiAuthDto apiAuth) {
    this.apiAuth = apiAuth;
  }
}
