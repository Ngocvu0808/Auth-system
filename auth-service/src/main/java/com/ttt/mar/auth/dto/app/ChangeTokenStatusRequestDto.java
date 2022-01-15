package com.ttt.mar.auth.dto.app;

import com.ttt.mar.auth.entities.enums.RefreshTokenStatus;
import lombok.Data;

/**
 * @author bontk
 * @created_date 08/07/2020
 */
@Data
public class ChangeTokenStatusRequestDto {

  private RefreshTokenStatus status;

  public RefreshTokenStatus getStatus() {
    return status;
  }

  public void setStatus(RefreshTokenStatus status) {
    this.status = status;
  }
}
