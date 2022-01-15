package com.ttt.mar.leads.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author nguyen
 * @create_date 09/08/2021
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CampaignFilterRequestDto {

  @JsonIgnore
  private Integer campaignId;

  @NotNull(message = "filterId not null.")
  private Integer filterId;
}
