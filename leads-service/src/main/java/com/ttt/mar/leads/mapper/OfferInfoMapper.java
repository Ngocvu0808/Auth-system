package com.ttt.mar.leads.mapper;

import com.ttt.mar.leads.dto.OfferInfoResponseDto;
import com.ttt.mar.leads.dto.OfferInfoRequestDto;
import com.ttt.mar.leads.entities.OfferInfo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class OfferInfoMapper {

  public abstract OfferInfoResponseDto toDto(OfferInfo offerInfo);

  public abstract OfferInfo fromDto(OfferInfoRequestDto dto);
}