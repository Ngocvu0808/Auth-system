package com.ttt.mar.leads.mapper;

import com.ttt.mar.leads.dto.CampaignRequestDto;
import com.ttt.mar.leads.dto.CampaignResponse;
import com.ttt.mar.leads.dto.CampaignResponseDto;
import com.ttt.mar.leads.entities.Campaign;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class CampaignMapper {

  public abstract Campaign fromDto(CampaignRequestDto dto);

  public abstract CampaignResponse toDto(Campaign entity);

  public abstract CampaignResponseDto toDtoDetail(Campaign entity);
}
