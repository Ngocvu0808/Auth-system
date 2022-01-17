package com.ttt.mar.auth.dto.app;

import com.ttt.mar.auth.entities.enums.ClientStatus;
import lombok.Data;

@Data
public class ChangeClientStatusRequestDto {

  private ClientStatus status;

  public ClientStatus getStatus() {
    return status;
  }

  public void setStatus(ClientStatus status) {
    this.status = status;
  }
}
