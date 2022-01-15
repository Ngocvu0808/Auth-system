package com.ttt.mar.leads.dto;

import java.util.Set;
import javax.validation.constraints.NotEmpty;

/**
 * @author kietdt
 * @created_date 20/04/2021
 */
public class ProjectSourceRequestDto {

  @NotEmpty(message = "sourceIds not empty")
  private Set<Integer> sourceIds;

  public Set<Integer> getSourceIds() {
    return sourceIds;
  }

  public void setSourceIds(Set<Integer> sourceIds) {
    this.sourceIds = sourceIds;
  }
}
