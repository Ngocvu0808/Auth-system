package com.ttt.mar.leads.dto;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author nguyen
 * @create_date 13/08/2021
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CampaignFilterInCampaignResponseDto {

  private Integer id;

  private Integer campaignId;

  private Integer filterId;

  private String name;

  private String code;
}
