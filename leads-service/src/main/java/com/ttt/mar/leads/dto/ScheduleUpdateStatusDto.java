package com.ttt.mar.leads.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ttt.mar.leads.entities.ScheduleStatus;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;

/**
 * @author Chien Chill
 * @create_date 07/09/2021
 */
public class ScheduleUpdateStatusDto {

  @JsonIgnore
  private Integer id;
  @NotNull(message = "Status not null")
  @ApiModelProperty(value = "Schedule Status", required = true, example = "ACTIVE/DEACTIVE")
  private ScheduleStatus status;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public ScheduleStatus getStatus() {
    return status;
  }

  public void setStatus(ScheduleStatus status) {
    this.status = status;
  }
}
