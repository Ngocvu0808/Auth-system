package com.ttt.mar.leads.mapper;

import com.ttt.mar.leads.dto.ProjectSourceResponse;
import com.ttt.mar.leads.entities.ProjectSource;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author kietdt
 * @created_date 20/04/2021
 */
@Mapper(componentModel = "spring")
public abstract class ProjectSourceMapper {

  @Mapping(target = "nameSource", source = "leadSource.name")
  @Mapping(target = "utmSource", source = "leadSource.utmSource")
  @Mapping(target = "source", source = "leadSource.source")
  @Mapping(target = "status", source = "leadSource.status")
  public abstract ProjectSourceResponse toDto(ProjectSource entity);
}
