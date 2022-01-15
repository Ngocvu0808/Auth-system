package com.ttt.mar.leads.mapper;

import com.ttt.mar.leads.dto.ProjectDetailResponse;
import com.ttt.mar.leads.dto.ProjectDtoToCampaignManage;
import com.ttt.mar.leads.dto.ProjectRequestDto;
import com.ttt.mar.leads.dto.ProjectResponseDto;
import com.ttt.mar.leads.entities.Project;
import org.mapstruct.Mapper;

/**
 * @author kietdt
 * @created_date 20/04/2021
 */
@Mapper(componentModel = "spring")
public abstract class ProjectMapper {

  public abstract Project fromDto(ProjectRequestDto dto);

  public abstract ProjectResponseDto toDto(Project project);

  public abstract ProjectDetailResponse toDetailDto(Project entity);

  public abstract ProjectDtoToCampaignManage toDtoToCampaignManage(Project project);
}
