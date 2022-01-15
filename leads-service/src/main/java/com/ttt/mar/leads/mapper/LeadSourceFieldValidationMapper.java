package com.ttt.mar.leads.mapper;

import com.ttt.mar.leads.dto.LeadSourceFieldValidationRequestDto;
import com.ttt.mar.leads.entities.LeadSourceFieldValidation;
import org.mapstruct.Mapper;

/**
 * @author kietdt
 * @created_date 20/04/2021
 */
@Mapper(componentModel = "spring")
public abstract class LeadSourceFieldValidationMapper {

  public abstract LeadSourceFieldValidation fromDto(LeadSourceFieldValidationRequestDto dto);
}
