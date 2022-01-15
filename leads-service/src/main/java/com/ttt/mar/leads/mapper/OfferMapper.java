package com.ttt.mar.leads.mapper;

import com.ttt.mar.leads.dto.OfferRequestDto;
import com.ttt.mar.leads.dto.OfferResponseListDto;
import com.ttt.mar.leads.entities.Offer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class OfferMapper {

  public abstract Offer fromDto(OfferRequestDto dto);

  public abstract OfferResponseListDto toDto(Offer offer);
}