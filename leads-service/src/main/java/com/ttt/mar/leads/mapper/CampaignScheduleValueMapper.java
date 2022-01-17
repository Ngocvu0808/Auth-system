package com.ttt.mar.leads.mapper;

import com.ttt.mar.leads.dto.ScheduleRequestDto;
import com.ttt.mar.leads.entities.Schedule;
import com.ttt.mar.leads.entities.ScheduleValue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public abstract class CampaignScheduleValueMapper {

  public abstract ScheduleValue fromDto(ScheduleRequestDto dto);


}
