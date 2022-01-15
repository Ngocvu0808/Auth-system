package com.ttt.mar.leads.dto;

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
public class CampaignOfferInCampaignResponseDto {

  private Integer id;

  private Integer campaignId;

  private Integer offerId;

  private String name;

  private String code;
}