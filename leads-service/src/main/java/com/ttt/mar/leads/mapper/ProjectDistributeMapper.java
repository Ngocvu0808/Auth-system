package com.ttt.mar.leads.mapper;

import com.ttt.mar.leads.dto.ProjectDistributeResponseDto;
import com.ttt.mar.leads.entities.ProjectDistribute;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author kietdt
 * @created_date 20/04/2021
 */
@Mapper(componentModel = "spring")
public abstract class ProjectDistributeMapper {

  @Mapping(target = "name", source = "distributeApi.name")
  @Mapping(target = "url", source = "distributeApi.url")
  @Mapping(target = "method", source = "distributeApi.method")
  @Mapping(target = "status", source = "distributeApi.status")
  public abstract ProjectDistributeResponseDto toDto(ProjectDistribute entity);
}
