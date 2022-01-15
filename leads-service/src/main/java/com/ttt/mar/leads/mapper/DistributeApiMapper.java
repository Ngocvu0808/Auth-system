package com.ttt.mar.leads.mapper;

import com.ttt.mar.leads.dto.DistributeApiDetailResponse;
import com.ttt.mar.leads.dto.DistributeApiRequestDto;
import com.ttt.mar.leads.dto.DistributeApiResponseDto;
import com.ttt.mar.leads.dto.DistributeByProjectResponse;
import com.ttt.mar.leads.dto.DistributeResponseDto;
import com.ttt.mar.leads.entities.ApiSecureMethod;
import com.ttt.mar.leads.entities.DistributeApi;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * @author kietdt
 * @created_date 20/04/2021
 */
@Mapper(componentModel = "spring")
public abstract class DistributeApiMapper {

  @Mapping(target = "username", source = "username", ignore = true)
  @Mapping(target = "password", source = "password", ignore = true)
  public abstract DistributeApi fromDto(DistributeApiRequestDto dto);

  public abstract DistributeApiResponseDto toDto(DistributeApi entity);

  public abstract DistributeApiDetailResponse toDetailDto(DistributeApi entity);

  public abstract DistributeByProjectResponse toDtoByProject(DistributeApi entity);

  public abstract DistributeResponseDto toDtoResponseToCampaignManage(DistributeApi entity);

  @BeforeMapping
  public void beforeMapping(@MappingTarget DistributeApi entity, DistributeApiRequestDto dto) {
    if (ApiSecureMethod.BASIC.equals(dto.getSecureMethod())) {
      entity.setUsername(dto.getUsername());
      entity.setPassword(dto.getPassword());
    }
  }

  @BeforeMapping
  public void beforeMapping(@MappingTarget DistributeApiDetailResponse dto, DistributeApi entity) {

  }


}
