package com.ttt.mar.leads.mapper;

import com.ttt.mar.leads.dto.LeadConditionRequestDto;
import com.ttt.mar.leads.dto.OfferConditionResponseDto;
import com.ttt.mar.leads.entities.OfferCondition;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class OfferConditionMapper {

  public abstract OfferConditionResponseDto toDto(OfferCondition offerCondition);

  @Mapping(target = "conditionId", source = "conditionId", ignore = true)
  public abstract OfferCondition fromDto(LeadConditionRequestDto dto);
}