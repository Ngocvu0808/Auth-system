package com.ttt.mar.leads.mapper;

import com.ttt.mar.leads.dto.CampaignFilterInCampaignResponseDto;
import com.ttt.mar.leads.dto.CampaignFilterRequestDto;
import com.ttt.mar.leads.dto.CampaignFilterResponseDto;
import com.ttt.mar.leads.dto.FilterOfCampaignResponseDto;
import com.ttt.mar.leads.entities.CampaignFilter;
import com.ttt.mar.leads.entities.Filter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author nguyen
 * @create_date 09/08/2021
 */

@Mapper(componentModel = "spring")
public abstract class CampaignFilterMapper {

  @Mapping(target = "code", source = "filter.code")
  @Mapping(target = "name", source = "filter.name")
  public abstract CampaignFilterInCampaignResponseDto toFilterCampaignDto(CampaignFilter e);

  public abstract CampaignFilter fromDto(CampaignFilterRequestDto dto);

}
