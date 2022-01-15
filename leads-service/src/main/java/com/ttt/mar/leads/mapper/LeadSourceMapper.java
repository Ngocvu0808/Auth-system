package com.ttt.mar.leads.mapper;

import com.ttt.mar.leads.dto.LeadSourceByProjectResponse;
import com.ttt.mar.leads.dto.LeadSourceDetailResponseDto;
import com.ttt.mar.leads.dto.LeadSourceRequestDto;
import com.ttt.mar.leads.dto.LeadSourceResponse;
import com.ttt.mar.leads.dto.LeadSourceResponseDto;
import com.ttt.mar.leads.entities.LeadSource;
import com.ttt.mar.leads.entities.LeadSourceFieldValidation;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * @author kietdt
 * @created_date 20/04/2021
 */
@Mapper(componentModel = "spring", uses = {LeadSourceFieldValidationMapper.class})
public abstract class LeadSourceMapper {

  @Mapping(target = "leadSourceFieldValidations", source = "leadSourceFieldValidationRequestDtos")
  public abstract LeadSource fromDto(LeadSourceRequestDto dto);

  public abstract LeadSourceDetailResponseDto toDetailDto(LeadSource entity);

  @BeforeMapping
  public void beforeMapping(@MappingTarget LeadSourceDetailResponseDto dto, LeadSource entity) {
    List<LeadSourceFieldValidation> leadSourceFieldValidations = entity
        .getLeadSourceFieldValidations();
    if (!leadSourceFieldValidations.isEmpty()) {
      leadSourceFieldValidations = leadSourceFieldValidations.stream()
          .filter(data -> !data.getIsDeleted()).collect(Collectors.toList());
    }
    entity.setLeadSourceFieldValidations(leadSourceFieldValidations);
  }

  public abstract LeadSourceResponse toDto(LeadSource entity);

  public abstract LeadSourceByProjectResponse toDtoByProject(LeadSource entity);

  public abstract LeadSourceResponseDto toDtoToCampaignManage(LeadSource entity);
}
