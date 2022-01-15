package com.ttt.mar.auth.dto.app;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ttt.mar.auth.entities.enums.ClientAuthType;
import com.ttt.mar.auth.entities.enums.ClientStatus;

/**
 * @author bontk
 * @created_date 09/06/2020
 */
public class ClientResponseDto {

  private Integer id;

  private String name;

  @JsonProperty("client_id")
  private String clientId;

  @JsonProperty("client_secret")
  private String clientSecret;

  private ClientStatus status;

  @JsonProperty("created_time")
  private Long createdTime;

  @JsonProperty("approve_require")
  private Boolean approveRequire;

  @JsonProperty("share_token")
  private Boolean shareToken;

  @JsonProperty("auth_type")
  private ClientAuthType authType;

  private String creatorName;

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

  public Long getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(Long createdTime) {
    this.createdTime = createdTime;
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public String getClientSecret() {
    return clientSecret;
  }

  public void setClientSecret(String clientSecret) {
    this.clientSecret = clientSecret;
  }

  public ClientStatus getStatus() {
    return status;
  }

  public void setStatus(ClientStatus status) {
    this.status = status;
  }

  public Boolean getApproveRequire() {
    return approveRequire;
  }

  public void setApproveRequire(Boolean approveRequire) {
    this.approveRequire = approveRequire;
  }

  public Boolean getShareToken() {
    return shareToken;
  }

  public void setShareToken(Boolean shareToken) {
    this.shareToken = shareToken;
  }

  public ClientAuthType getAuthType() {
    return authType;
  }

  public void setAuthType(ClientAuthType authType) {
    this.authType = authType;
  }

  public String getCreatorName() {
    return creatorName;
  }

  public void setCreatorName(String creatorName) {
    this.creatorName = creatorName;
  }
}
