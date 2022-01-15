package com.ttt.mar.leads.mapper;

import com.ttt.mar.leads.dto.ValidationDto;
import com.ttt.mar.leads.entities.Validation;
import org.mapstruct.Mapper;

/**
 * @author kietdt
 * @created_date 20/04/2021
 */
@Mapper(componentModel = "spring")
public abstract class ValidationMapper {

  public abstract ValidationDto toDto(Validation entity);
}
