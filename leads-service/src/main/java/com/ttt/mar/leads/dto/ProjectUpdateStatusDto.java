package com.ttt.mar.leads.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ttt.mar.leads.entities.ProjectStatus;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;

/**
 * @author kietdt
 * @created_date 20/04/2021
 */
public class ProjectUpdateStatusDto {

  @JsonIgnore
  private Integer id;
  @NotNull(message = "Status not null")
  @ApiModelProperty(value = "Project Status", required = true, example = "ACTIVE/DEACTIVE")
  private ProjectStatus status;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public ProjectStatus getStatus() {
    return status;
  }

  public void setStatus(ProjectStatus status) {
    this.status = status;
  }
}
