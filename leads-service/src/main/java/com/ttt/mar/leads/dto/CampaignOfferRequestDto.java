package com.ttt.mar.leads.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.validation.constraints.NotNull;

/**
 * @author KietDt
 * @create_date 00/08/2021
 */
public class CampaignOfferRequestDto {

  @JsonIgnore
  private Integer campaignId;

  @NotNull(message = "offerId not null.")
  private Integer offerId;

  public Integer getCampaignId() {
    return campaignId;
  }

  public void setCampaignId(Integer campaignId) {
    this.campaignId = campaignId;
  }

  public Integer getOfferId() {
    return offerId;
  }

  public void setOfferId(Integer offerId) {
    this.offerId = offerId;
  }
}
