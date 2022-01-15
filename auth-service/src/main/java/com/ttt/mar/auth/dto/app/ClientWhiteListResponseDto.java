package com.ttt.mar.auth.dto.app;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

/**
 * @author bontk
 * @created_date 06/07/2020
 */
@Data
public class ClientWhiteListResponseDto {

  @JsonProperty("client_id")
  private Integer clientId;

  @JsonProperty("client_name")
  private String clientName;

  @JsonProperty("ip_list")
  private List<String> ipList;

  public Integer getClientId() {
    return clientId;
  }

  public void setClientId(Integer clientId) {
    this.clientId = clientId;
  }

  public String getClientName() {
    return clientName;
  }

  public void setClientName(String clientName) {
    this.clientName = clientName;
  }

  public List<String> getIpList() {
    return ipList;
  }

  public void setIpList(List<String> ipList) {
    this.ipList = ipList;
  }
}
