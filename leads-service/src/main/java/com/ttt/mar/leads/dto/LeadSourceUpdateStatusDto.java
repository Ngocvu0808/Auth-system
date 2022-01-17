package com.ttt.mar.leads.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ttt.mar.leads.entities.LeadSourceStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;

@ApiModel(description = "THONG TIN CAP NHAT TRANG THAI NGUON CAP LEADS")
public class LeadSourceUpdateStatusDto {

  @JsonIgnore
  private Integer id;
  @NotNull(message = "Status not null")
  @ApiModelProperty(value = "Thong tin cap nhat trang thai", required = true, example = "ACTIVE / DEACTIVE")
  private LeadSourceStatus status;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public LeadSourceStatus getStatus() {
    return status;
  }

  public void setStatus(LeadSourceStatus status) {
    this.status = status;
  }
}
