package com.ttt.mar.leads.service.impl;

import com.ttt.mar.leads.config.ServiceMessageCode;
import com.ttt.mar.leads.dto.ScheduleRequestDto;
import com.ttt.mar.leads.dto.ScheduleResponseDto;
import com.ttt.mar.leads.dto.ScheduleResponseListDto;
import com.ttt.mar.leads.dto.ScheduleUpdateStatusDto;
import com.ttt.mar.leads.entities.Campaign;
import com.ttt.mar.leads.entities.Schedule;
import com.ttt.mar.leads.entities.ScheduleValue;
import com.ttt.mar.leads.filter.ScheduleFilter;
import com.ttt.mar.leads.mapper.CampaignScheduleMapper;
import com.ttt.mar.leads.mapper.CampaignScheduleValueMapper;
import com.ttt.mar.leads.repositories.CampaignRepository;
import com.ttt.mar.leads.repositories.ScheduleRepository;
import com.ttt.mar.leads.repositories.ScheduleValueRepository;
import com.ttt.mar.leads.service.iface.ScheduleService;
import com.ttt.mar.leads.service.iface.UserService;
import com.ttt.rnd.common.config.ServiceInfo;
import com.ttt.rnd.common.dto.DataPagingResponse;
import com.ttt.rnd.common.exception.DuplicateEntityException;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import com.ttt.rnd.common.exception.ValidationException;
import com.ttt.rnd.common.utils.SortingUtils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * @author Chien Chill
 * @create_date 06/09/2021
 */
@Service
public class ScheduleServiceImpl implements ScheduleService {

  SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
      .getLogger(OfferServiceImpl.class);

  private final ScheduleRepository scheduleRepository;

  private final UserService userService;

  private final CampaignScheduleMapper campaignScheduleMapper;

  private final CampaignRepository campaignRepository;

  private final CampaignScheduleValueMapper campaignScheduleValueMapper;

  private final ScheduleValueRepository scheduleValueRepository;

  public ScheduleServiceImpl(ScheduleRepository scheduleRepository,
      UserService userService,
      CampaignScheduleMapper campaignScheduleMapper,
      CampaignRepository campaignRepository,
      CampaignScheduleValueMapper campaignScheduleValueMapper,
      ScheduleValueRepository scheduleValueRepository) {
    this.scheduleRepository = scheduleRepository;
    this.userService = userService;
    this.campaignScheduleMapper = campaignScheduleMapper;
    this.campaignRepository = campaignRepository;
    this.campaignScheduleValueMapper = campaignScheduleValueMapper;
    this.scheduleValueRepository = scheduleValueRepository;
  }

  @Override
  public Boolean deleteSchedule(Integer userId, Integer campaignId, Integer scheduleId)
      throws ResourceNotFoundException, OperationNotImplementException {
    userService.checkValidUser(userId);
    Campaign campaign = campaignRepository.findByIdAndIsDeletedFalse(campaignId);
    if (campaign == null) {
      throw new ResourceNotFoundException("Campaign Not Found",
          ServiceInfo.getId() + ServiceMessageCode.CAMPAIGN_NOT_FOUND);
    }
    Schedule schedule = scheduleRepository.findByIdAndIsDeletedFalse(scheduleId);
    if (schedule == null) {
      throw new ResourceNotFoundException("Schedule Not Found",
          ServiceInfo.getId() + ServiceMessageCode.SCHEDULE_NOT_FOUND);
    }
    if (!schedule.getCampaignId().equals(campaignId)) {
      throw new ResourceNotFoundException("CampaignId not match",
          ServiceInfo.getId() + ServiceMessageCode.CAMPAIGN_ID_NOT_MATCH);
    }
    schedule.setIsDeleted(true);
    schedule.setDeleterUserId(userId);

    if (schedule.getType().equals("ALWAYS")) {
      scheduleRepository.save(schedule);
      return true;

    }
    scheduleRepository.save(schedule);
    ScheduleValue scheduleValue = scheduleValueRepository.
        findByScheduleIdAndIsDeletedFalse(scheduleId);
    if (scheduleValue == null) {
      throw new ResourceNotFoundException("ScheduleValue not found",
          ServiceInfo.getId() + ServiceMessageCode.SCHEDULE_VALUE_NOT_FOUND);
    }
    scheduleValue.setIsDeleted(true);
    scheduleValueRepository.save(scheduleValue);
    return true;
  }

  @Override
  public ScheduleResponseDto getSchedule(Integer campaignId, Integer scheduleId)
      throws ResourceNotFoundException {

    Campaign campaign = campaignRepository.findByIdAndIsDeletedFalse(campaignId);
    if (campaign == null) {
      throw new ResourceNotFoundException("Campaign Not Found",
          ServiceInfo.getId() + ServiceMessageCode.CAMPAIGN_NOT_FOUND);
    }
    Schedule schedule = scheduleRepository.findByIdAndIsDeletedFalse(scheduleId);
    if (schedule == null) {
      throw new ResourceNotFoundException("Schedule not found",
          ServiceInfo.getId() + ServiceMessageCode.SCHEDULE_NOT_FOUND);
    }
//    Schedule schedule = optional.get();
    if (!campaignId.equals(schedule.getCampaignId())) {
      throw new ResourceNotFoundException("CampaignId not match",
          ServiceInfo.getId() + ServiceMessageCode.CAMPAIGN_ID_NOT_MATCH);
    }

    ScheduleResponseDto scheduleResponseDto = new ScheduleResponseDto();

    scheduleResponseDto = campaignScheduleMapper.toScheduleResponseDto(schedule);

    if (schedule.getType().equals("CALENDER")) {

      ScheduleValue scheduleValue = scheduleValueRepository.findByScheduleIdAndIsDeletedFalse(
          scheduleId);
      if (scheduleValue == null) {
        throw new ResourceNotFoundException("ScheduleValue not found",
            ServiceInfo.getId() + ServiceMessageCode.SCHEDULE_VALUE_NOT_FOUND);
      } else {
//        scheduleResponseDto.setValue(scheduleValue.getValue());
        scheduleResponseDto = campaignScheduleMapper.toDto(schedule, scheduleValue);
      }
    }
    return scheduleResponseDto;
  }

  @Override
  public DataPagingResponse<ScheduleResponseListDto> getListSchedule(Integer campaignId,
      Integer limit, Integer page, String sort)
      throws ResourceNotFoundException {
    Campaign campaign = campaignRepository.findByIdAndIsDeletedFalse(campaignId);
    if (campaign == null) {
      throw new ResourceNotFoundException("Campaign Not Found",
          ServiceInfo.getId() + ServiceMessageCode.CAMPAIGN_NOT_FOUND);
    }
    logger.info(" <<< getAllSchedule in Campaign()");
    Map<String, String> map = SortingUtils.detectSortType(sort);
    if (map.isEmpty()) {
      map.put("createdTime", "desc");
    }

    Page<Schedule> schedulePage = scheduleRepository.findAll(
        new ScheduleFilter().filter(campaignId, map), PageRequest.of(page - 1, limit));
    List<Schedule> scheduleList = schedulePage.getContent();

    List<ScheduleResponseListDto> schedules = scheduleList.stream()
        .map(campaignScheduleMapper::toScheduleDto)
        .collect(Collectors.toList());
    DataPagingResponse<ScheduleResponseListDto> dataPagingResponse = new DataPagingResponse<>();
    dataPagingResponse.setList(schedules);
    dataPagingResponse.setCurrentPage(page);
    dataPagingResponse.setNum(schedulePage.getTotalElements());
    dataPagingResponse.setTotalPage(schedulePage.getTotalPages());

    return dataPagingResponse;
  }

  @Override
  public Integer createSchedule(Integer userId, Integer campaignId,
      ScheduleRequestDto scheduleRequestDto)
      throws ResourceNotFoundException, ValidationException, DuplicateEntityException, ParseException {
    Date requestTime = new Date();
    long requestTimeToLong = requestTime.getTime();
    Campaign campaign = campaignRepository.findByIdAndIsDeletedFalse(campaignId);
    if (campaign == null) {
      throw new ResourceNotFoundException("Campaign Not Found",
          ServiceInfo.getId() + ServiceMessageCode.CAMPAIGN_NOT_FOUND);
    }
    Schedule scheduleExist = scheduleRepository
        .findByNameAndIsDeletedIsFalse(scheduleRequestDto.getName());

    if (scheduleExist != null) {
      throw new DuplicateEntityException("Name input is illegal",
          ServiceInfo.getId() + ServiceMessageCode.NAME_ILLEGAL);
    }

    Schedule schedule = campaignScheduleMapper.fromDto(scheduleRequestDto);
    schedule.setCampaignId(campaignId);
    schedule.setLimit(scheduleRequestDto.getIsLimit());
    if (!schedule.isLimit()) {
      schedule.setDayLimit(0);
    }
    schedule.setCampaign(campaign);
    schedule.setCreatorId(userId);

    if (scheduleRequestDto.getType().equals("ALWAYS")) {
      schedule.setStartTime(new Date(scheduleRequestDto.get_start()));
      scheduleRepository.save(schedule);
      return schedule.getId();
    }

    if (!validateStartTime(scheduleRequestDto.get_start(), simpleDateFormat, requestTimeToLong)) {
      throw new ValidationException("Start time input is not valid!",
          ServiceInfo.getId() + ServiceMessageCode.BAD_START_TIME_INPUT);
    }

    schedule.setStartTime(new Date(scheduleRequestDto.get_start()));
    if (!validateCrontab(scheduleRequestDto.getValue())) {
      throw new ValidationException("Value is illegal",
          ServiceInfo.getId() + ServiceMessageCode.VALUE_NOT_LEGAL);
    }

    scheduleRepository.save(schedule);
    ScheduleValue scheduleValue = campaignScheduleValueMapper.fromDto(scheduleRequestDto);
    scheduleValue.setScheduleId(schedule.getId());
    scheduleValue.setSchedule(schedule);
    scheduleValueRepository.save(scheduleValue);

    return schedule.getId();
  }

  public boolean validateStartTime(long date, SimpleDateFormat sdf, long requestTime)
      throws ParseException {
    if (date < requestTime) {
      return false;
    }
    Date dateGen = new Date(date);
    String _dateGen = sdf.format(dateGen);
    Date newDate = sdf.parse(_dateGen);
    return String.valueOf(dateGen).equals(String.valueOf(newDate));
  }


  public boolean validateCrontab(String crontab) {
    String[] partOfCrontab = crontab.split(" ");
    if (Arrays.stream(partOfCrontab).count() != 5) {
      return false;
    }
    if (!checkSubCrontab(partOfCrontab[0], 0, 59, 1, 60)) {
      return false;
    }
    if (!checkSubCrontab(partOfCrontab[1], 0, 23, 1, 24)) {
      return false;
    }
    if (!checkSubCrontab(partOfCrontab[2], 1, 31, 1, 31)) {
      return false;
    }
    if (!checkSubCrontab(partOfCrontab[3], 1, 12, 1, 12)) {
      return false;
    }
    if (!checkSubCrontab(partOfCrontab[4], 0, 6, 1, 7)) {
      return false;
    }
    return true;
  }

  boolean checkSubCrontab(String input, Integer min, Integer max, Integer _min, Integer _max) {

    // start with @
    final String[] often = {"@yearly", "@annually", "@monthly", "@weekly", "@daily", "@hourly",
        "@reboot"};
    for (String each : often) {
      if (input.equals(each)) {
        return true;
      }
    }
    //Is number
    if (NumberUtils.isNumber(input)) {
      return checkNumber(Integer.parseInt(input), min, max);
    }

    //check each char in input
    final String metaCharacters = "0123456789*/-,";
    for (int i = 0; i < input.length(); i++) {
      if (!metaCharacters.contains(String.valueOf(input.charAt(i)))) {
        return false;
      }
    }
    //if length = 1 --> must be "*" or number -> check valid input number
    if (input.length() == 1) {
      if (input.charAt(0) != '*') {
        if (!NumberUtils.isNumber(input)) {
          return false;
        } else {
          if (!checkNumber(Integer.parseInt(input), min, max)) {
            return false;
          }
        }
      } else {
        return true;
      }
    }
    //if length =2 --> must be number
    if (input.length() == 2) {
      if (!NumberUtils.isNumber(input)) {
        return false;
      }
      if (!checkNumber(Integer.parseInt(input), min, max)) {
        return false;
      }
    }
    //if length >2 --> must be "*/{number}" or "{number},{number},..." or "{number}-{number}"
    if (input.length() > 2) {
      if (input.charAt(0) == '*' && input.charAt(1) == '/') {
        String[] partOfElement = input.split("/");
        if (!NumberUtils.isNumber(partOfElement[1])) {
          return false;
        } else {
          if (Integer.parseInt(partOfElement[1]) > _max) {
            return false;
          } else {
            return Integer.parseInt(partOfElement[1]) >= _min;
          }
        }
      } else {
        //todo: check with "{number},{number},..." and "{number}-{number}"
        String[] groupNumber = input.split(",");
        for (String number : groupNumber) {
          if (!NumberUtils.isNumber(number)) {
            String[] eachNumber = number.split("-");
            for (String numberFound : eachNumber) {
              if (!checkNumber(Integer.parseInt(numberFound), min, max)) {
                return false;
              }
            }
          } else {
            if (!checkNumber(Integer.parseInt(number), min, max)) {
              return false;
            }
          }
        }
      }
    }
    return true;
  }

  boolean checkNumber(int number, int min, int max) {
    if (number > max) {
      return false;
    } else {
      return number >= min;
    }
  }

  @Override
  public Integer updateSchedule(Integer userId, Integer campaignId, Integer scheduleId,
      ScheduleRequestDto scheduleRequestDto)
      throws ResourceNotFoundException, OperationNotImplementException, ValidationException, DuplicateEntityException, ParseException {
    Date requestTime = new Date();
    long requestTimeToLong = requestTime.getTime();
    userService.checkValidUser(userId);
    Campaign campaign = campaignRepository.findByIdAndIsDeletedFalse(campaignId);
    if (campaign == null) {
      throw new ResourceNotFoundException("Campaign Not Found",
          ServiceInfo.getId() + ServiceMessageCode.CAMPAIGN_NOT_FOUND);
    }
    Schedule schedule = scheduleRepository.findByIdAndIsDeletedFalse(scheduleId);
    if (schedule == null) {
      throw new ResourceNotFoundException("Schedule Not Found",
          ServiceInfo.getId() + ServiceMessageCode.SCHEDULE_NOT_FOUND);
    }
    if (!schedule.getCampaignId().equals(campaignId)) {
      throw new ResourceNotFoundException("CampaignId not match",
          ServiceInfo.getId() + ServiceMessageCode.CAMPAIGN_ID_NOT_MATCH);
    }

    Schedule scheduleExist = scheduleRepository
        .findByNameAndIsDeletedIsFalse(scheduleRequestDto.getName());

    if (scheduleExist != null && !scheduleRequestDto.getName().equals(schedule.getName())) {
      throw new DuplicateEntityException("Name input is illegal",
          ServiceInfo.getId() + ServiceMessageCode.NAME_ILLEGAL);
    }

    schedule.setType(scheduleRequestDto.getType());
    schedule.setName(scheduleRequestDto.getName());
    schedule.setLimit(scheduleRequestDto.getIsLimit());
    schedule.setDayLimit(scheduleRequestDto.getDayLimit());
    schedule.setCampaignId(campaignId);
    schedule.setCampaign(campaign);
    schedule.setUpdaterId(userId);
    schedule.setStartTime(new Date(scheduleRequestDto.get_start()));

    if (schedule.getType().equals("ALWAYS")) {
      scheduleRepository.save(schedule);
      return scheduleId;
    }
//    if (!validateStartTime(scheduleRequestDto.get_start(), simpleDateFormat, requestTimeToLong)) {
//      throw new ParseException("Time input is not valid!",
//          Integer.parseInt(ServiceInfo.getId() + ServiceMessageCode.BAD_START_TIME_INPUT));
//    }
    if (!validateCrontab(scheduleRequestDto.getValue())) {
      throw new ValidationException("Value is illegal",
          ServiceInfo.getId() + ServiceMessageCode.VALUE_NOT_LEGAL);
    }
    scheduleRepository.save(schedule);
    ScheduleValue scheduleValue = scheduleValueRepository.
        findByScheduleIdAndIsDeletedFalse(scheduleId);
    scheduleValue.setValue(scheduleRequestDto.getValue());
    scheduleValueRepository.save(scheduleValue);

    return scheduleId;
  }

  @Override
  public Integer updateScheduleStatus(Integer userId, Integer id, ScheduleUpdateStatusDto dto)
      throws ResourceNotFoundException, OperationNotImplementException, ParseException {
    userService.checkValidUser(userId);
    if (dto.getStatus() == null) {
      throw new OperationNotImplementException("Status invalid",
          ServiceInfo.getId() + ServiceMessageCode.SCHEDULE_STATUS_INVALID);
    }
    Schedule schedule = scheduleRepository.findByIdAndIsDeletedFalse(id);
    if (schedule == null) {
      throw new ResourceNotFoundException("Schedule not found",
          ServiceInfo.getId() + ServiceMessageCode.SCHEDULE_NOT_FOUND);
    }
    Campaign campaign = campaignRepository.findByIdAndIsDeletedFalse(schedule.getCampaignId());
    if (campaign == null) {
      throw new ResourceNotFoundException("Campaign not found",
          ServiceInfo.getId() + ServiceMessageCode.CAMPAIGN_NOT_FOUND);
    }

    if (schedule.getStatus().equals(dto.getStatus())) {
      throw new OperationNotImplementException(" Schedule status not change",
          ServiceInfo.getId() + ServiceMessageCode.SCHEDULE_STATUS_NOT_CHANGE);
    }
//    if(ScheduleStatus.DEACTIVE.equals(schedule.getStatus()) ){
//      throw new OperationNotImplementException(" Schedule status is deactive",
//          ServiceInfo.getId() + ServiceMessageCode.SCHEDULE_STATUS_IS_DEACTIVE);
//    }
//    if (ScheduleStatus.ACTIVE.equals(dto.getStatus())) {
//      throw new OperationNotImplementException(" Schedule status is deactive",
//          ServiceInfo.getId() + ServiceMessageCode.SCHEDULE_STATUS_IS_ACTIVE);
//    }
    schedule.setStatus(dto.getStatus());
    schedule.setUpdaterId(userId);
    scheduleRepository.save(schedule);
    return id;
  }


}
