package com.ttt.mar.leads.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ttt.mar.leads.entities.DistributeApiStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;

/**
 * @author kietdt
 * @created_date 20/04/2021
 */
@ApiModel(description = "Thong tin cap nhat trang thai kenh phan phoi lead")
public class DistributeApiUpdateStatusDto {

  @JsonIgnore
  private Integer id;
  @NotNull(message = "Status not null")
  @ApiModelProperty(value = "Thong tin cap nhat trang thai", required = true, example = "ACTIVE / DEACTIVE]")
  private DistributeApiStatus status;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public DistributeApiStatus getStatus() {
    return status;
  }

  public void setStatus(DistributeApiStatus status) {
    this.status = status;
  }
}
