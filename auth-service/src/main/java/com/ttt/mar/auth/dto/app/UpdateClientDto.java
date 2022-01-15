package com.ttt.mar.auth.dto.app;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ttt.mar.auth.entities.enums.ClientAuthType;
import lombok.Data;

/**
 * @author bontk
 * @created_date 11/07/2020
 */
@Data
public class UpdateClientDto {

  private String name;
  private String description;
  @JsonProperty("approve_require")
  private Boolean approveRequire;
  @JsonProperty("share_token")
  private Boolean shareToken;
  @JsonProperty("auth_type")
  private ClientAuthType authType;

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
}
