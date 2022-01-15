package com.ttt.mar.auth.dto.api;

import com.ttt.mar.auth.entities.enums.ApiRequestStatus;
import javax.validation.constraints.NotNull;

/**
 * @author bontk
 * @created_date 05/08/2020
 */
public class ApiRequestStatusUpdateDto {

  @NotNull(message = "status not null")
  private ApiRequestStatus status;

  public ApiRequestStatus getStatus() {
    return status;
  }

  public void setStatus(ApiRequestStatus status) {
    this.status = status;
  }
}
