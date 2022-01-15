package com.ttt.mar.leads.mapper;

import com.ttt.mar.leads.dto.DistributeApiDataRequestDto;
import com.ttt.mar.leads.dto.DistributeApiDataResponseDto;
import com.ttt.mar.leads.entities.DistributeApiData;
import org.mapstruct.Mapper;

/**
 * @author kietdt
 * @created_date 20/04/2021
 */
@Mapper(componentModel = "spring")
public abstract class DistributeApiDataMapper {

  public abstract DistributeApiData fromDto(DistributeApiDataRequestDto dto);

  public abstract DistributeApiDataResponseDto toDto(DistributeApiData entity);
}
