package com.ttt.mar.leads.mapper;

import com.ttt.mar.leads.dto.FilterOfCampaignResponseDto;
import com.ttt.mar.leads.dto.FilterRequestDto;
import com.ttt.mar.leads.dto.FilterResponse;
import com.ttt.mar.leads.dto.FilterResponseListDto;
import com.ttt.mar.leads.entities.Filter;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class FilterMapper {


  public abstract Filter fromDto(FilterRequestDto dto);

  public abstract FilterResponse toDto(Filter entity);

  public abstract FilterResponseListDto toDtoList(Filter filter);

  public abstract FilterOfCampaignResponseDto toFilterCampaignDto(Filter e);
}
