package com.ttt.mar.leads.mapper;

import com.ttt.mar.leads.dto.DistributeFieldMappingRequestDto;
import com.ttt.mar.leads.dto.DistributeFieldMappingResponseDto;
import com.ttt.mar.leads.entities.DistributeFieldMapping;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class DistributeFieldMappingMapper {

  public abstract DistributeFieldMapping fromDto(DistributeFieldMappingRequestDto dto);

  public abstract DistributeFieldMappingResponseDto toDto(DistributeFieldMapping entity);
}
