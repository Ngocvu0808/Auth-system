package com.ttt.mar.leads.mapper;

import com.ttt.mar.leads.dto.CampaignOfferInCampaignResponseDto;
import com.ttt.mar.leads.entities.CampaignOffer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author nguyen
 * @create_date 13/08/2021
 */

@Mapper(componentModel = "spring")
public abstract class CampaignOfferMapper {

  @Mapping(target = "code", source = "offer.code")
  @Mapping(target = "name", source = "offer.name")
  public abstract CampaignOfferInCampaignResponseDto toOfferCampaignDto(CampaignOffer e);

}
