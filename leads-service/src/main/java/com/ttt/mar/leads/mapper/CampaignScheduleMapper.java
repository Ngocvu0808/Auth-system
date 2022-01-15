package com.ttt.mar.leads.mapper;

import com.ttt.mar.leads.dto.ScheduleRequestDto;
import com.ttt.mar.leads.dto.ScheduleResponseDto;
import com.ttt.mar.leads.dto.ScheduleResponseListDto;
import com.ttt.mar.leads.entities.Schedule;
import com.ttt.mar.leads.entities.ScheduleValue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author nguyen
 * @create_date 08/09/2021
 */

@Mapper(componentModel = "spring")
public abstract class CampaignScheduleMapper {

  @Mapping(target = "createTime", source = "createdTime")
  @Mapping(target = "status", source = "status")
  public abstract ScheduleResponseListDto toScheduleDto(Schedule e);

  @Mapping(target = "limit", source = "isLimit")
  public abstract Schedule fromDto(ScheduleRequestDto dto);

  @Mapping(target = "id", source = "schedule.id")
  @Mapping(target = "isLimit", source = "schedule.limit")
  public abstract ScheduleResponseDto toDto(Schedule schedule, ScheduleValue scheduleValue);

  @Mapping(target = "id", source = "schedule.id")
  @Mapping(target = "isLimit", source = "schedule.limit")
  public abstract ScheduleResponseDto toScheduleResponseDto(Schedule schedule);


}
