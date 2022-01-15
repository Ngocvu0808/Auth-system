package com.ttt.mar.leads.service.iface;

import com.ttt.mar.leads.dto.ScheduleRequestDto;
import com.ttt.mar.leads.dto.ScheduleResponseDto;
import com.ttt.mar.leads.dto.ScheduleResponseListDto;
import com.ttt.mar.leads.dto.ScheduleUpdateStatusDto;
import com.ttt.rnd.common.dto.DataPagingResponse;
import com.ttt.rnd.common.exception.DuplicateEntityException;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import com.ttt.rnd.common.exception.ValidationException;
import java.text.ParseException;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Chien Chill
 * @create_date 06/09/2021
 */

public interface ScheduleService {

  @Transactional
  Boolean deleteSchedule(Integer userId, Integer campaignId, Integer scheduleId)
      throws ResourceNotFoundException, OperationNotImplementException;

  ScheduleResponseDto getSchedule(Integer campaignId, Integer scheduleId)
      throws ResourceNotFoundException;

  DataPagingResponse<ScheduleResponseListDto> getListSchedule(Integer campaignId,
      Integer limit, Integer page, String sort)
      throws ResourceNotFoundException;

  Integer createSchedule(Integer userId, Integer campaignId, ScheduleRequestDto scheduleRequestDto)
      throws ResourceNotFoundException, ValidationException, DuplicateEntityException, ParseException;

  @Transactional
  Integer updateSchedule(Integer userId, Integer campaignId, Integer scheduleId,
      ScheduleRequestDto scheduleRequestDto)
      throws ResourceNotFoundException, ValidationException, OperationNotImplementException, DuplicateEntityException, ParseException;

  Integer updateScheduleStatus(Integer userId, Integer id, ScheduleUpdateStatusDto dto) throws
      ResourceNotFoundException, OperationNotImplementException, ParseException;
}
