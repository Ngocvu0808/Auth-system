package com.ttt.mar.leads.mapper;

import com.ttt.mar.leads.dto.DistributeApiHeaderRequestDto;
import com.ttt.mar.leads.dto.DistributeApiHeaderResponseDto;
import com.ttt.mar.leads.entities.DistributeApiHeader;
import org.mapstruct.Mapper;

/**
 * @author kietdt
 * @created_date 20/04/2021
 */
@Mapper(componentModel = "spring")
public abstract class DistributeApiHeaderMapper {

  public abstract DistributeApiHeader fromDto(DistributeApiHeaderRequestDto dto);

  public abstract DistributeApiHeaderResponseDto toDto(DistributeApiHeader entity);
}
