package com.ttt.mar.leads.dto;

import java.util.Set;
import javax.validation.constraints.NotEmpty;

/**
 * @author kietdt
 * @created_date 20/04/2021
 */
public class ProjectDistributeRequestDto {

  @NotEmpty(message = "distributeIds not empty")
  private Set<Integer> distributeIds;

  public Set<Integer> getDistributeIds() {
    return distributeIds;
  }

  public void setDistributeIds(Set<Integer> distributeIds) {
    this.distributeIds = distributeIds;
  }
}
