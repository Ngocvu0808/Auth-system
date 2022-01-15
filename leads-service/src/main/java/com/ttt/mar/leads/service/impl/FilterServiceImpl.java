package com.ttt.mar.leads.service.impl;


import com.ttt.mar.leads.config.ServiceMessageCode;
import com.ttt.mar.leads.dto.LeadConditionRequestDto;
import com.ttt.mar.leads.dto.FilterConditionResponseDto;
import com.ttt.mar.leads.dto.FilterRequestDto;
import com.ttt.mar.leads.dto.FilterResponse;
import com.ttt.mar.leads.dto.FilterResponseDto;
import com.ttt.mar.leads.dto.FilterUpdateRequestDto;
import com.ttt.mar.leads.dto.UserResponseDto;
import com.ttt.mar.leads.entities.Campaign;
import com.ttt.mar.leads.entities.CampaignFilter;
import com.ttt.mar.leads.entities.Condition;
import com.ttt.mar.leads.entities.Filter;
import com.ttt.mar.leads.entities.FilterCondition;
import com.ttt.mar.leads.entities.FilterConditionValue;
import com.ttt.mar.leads.filter.FilterOfEntityFilter;
import com.ttt.mar.leads.mapper.FilterConditionMapper;
import com.ttt.mar.leads.mapper.FilterMapper;
import com.ttt.mar.leads.repositories.CampaignFilterRepository;
import com.ttt.mar.leads.repositories.CampaignRepository;
import com.ttt.mar.leads.repositories.ConditionRepository;
import com.ttt.mar.leads.repositories.FilterConditionRepository;
import com.ttt.mar.leads.repositories.FilterConditionValueRepository;
import com.ttt.mar.leads.repositories.FilterRepository;
import com.ttt.mar.leads.service.iface.FilterService;
import com.ttt.mar.leads.service.iface.UserService;
import com.ttt.rnd.common.config.ServiceInfo;
import com.ttt.rnd.common.dto.DataPagingResponse;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import com.ttt.rnd.common.utils.JsonUtils;
import com.ttt.rnd.common.utils.SortingUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class FilterServiceImpl implements FilterService {

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
      .getLogger(FilterServiceImpl.class);

  private final FilterRepository filterRepository;

  private final FilterConditionRepository filterConditionRepository;

  private final FilterConditionValueRepository filterConditionValueRepository;

  private final CampaignRepository campaignRepository;

  private final CampaignFilterRepository campaignFilterRepository;

  private final ConditionRepository conditionRepository;

  private final FilterConditionMapper filterConditionMapper;

  private final FilterMapper filterMapper;

  private final UserService userService;

  private final FilterOfEntityFilter filterOfEntityFilter;

  public FilterServiceImpl(FilterRepository filterRepository,
      FilterConditionRepository filterConditionRepository,
      CampaignFilterRepository campaignFilterRepository,
      ConditionRepository conditionRepository,
      FilterConditionValueRepository filterConditionValueRepository,
      CampaignRepository campaignRepository,
      FilterMapper filterMapper, FilterOfEntityFilter filterOfEntityFilter,
      FilterConditionMapper filterConditionMapper, UserService userService) {
    this.filterRepository = filterRepository;
    this.filterConditionRepository = filterConditionRepository;
    this.campaignFilterRepository = campaignFilterRepository;
    this.conditionRepository = conditionRepository;
    this.campaignRepository = campaignRepository;
    this.filterConditionMapper = filterConditionMapper;
    this.filterMapper = filterMapper;
    this.filterConditionValueRepository = filterConditionValueRepository;
    this.userService = userService;
    this.filterOfEntityFilter = filterOfEntityFilter;
  }


  /**
   * @param userId
   * @param dto
   * @return
   * @throws OperationNotImplementException
   * @throws ResourceNotFoundException
   * @apiNote API thêm mới Bộ lọc
   */
  @Override
  public Integer createFilter(Integer userId, FilterRequestDto dto)
      throws OperationNotImplementException, ResourceNotFoundException {
    logger.info("<<< createFilter, FilterRequestDto = {}", JsonUtils.toJson(dto));
    String code = dto.getCode();
    Filter filterExist = filterRepository.findByCodeAndIsDeletedFalse(code);
    if (filterExist != null) {
      throw new OperationNotImplementException("Filter By Code is Exist.",
          ServiceInfo.getId() + ServiceMessageCode.FILTER_BY_CODE_EXIST);
    }
    Filter filter = filterMapper.fromDto(dto);
    filter.setCreatorUserId(userId);
    List<FilterConditionValue> filterConditionValues = new ArrayList<>();
    List<FilterCondition> filterConditions = validateFilterConditionCreateFilter(
        userId, filter, dto.getConditions(), filterConditionValues);
    filterRepository.save(filter);
    if (!filterConditions.isEmpty()) {
      filterConditionRepository.saveAll(filterConditions);
    }
    if (!filterConditionValues.isEmpty()) {
      filterConditionValueRepository.saveAll(filterConditionValues);
    }

    return filter.getId();
  }

  private List<FilterCondition> validateFilterConditionCreateFilter(
      Integer userId, Filter filter, List<LeadConditionRequestDto> leadConditionRequestDtos,
      List<FilterConditionValue> filterConditionValues)
      throws OperationNotImplementException, ResourceNotFoundException {
    logger.info("<<< validateFilterConditionCreateFilter()");
    List<FilterCondition> filterConditions = new ArrayList<>();
    if (leadConditionRequestDtos == null || leadConditionRequestDtos.isEmpty()) {
      return filterConditions;
    }
    Map<Integer, Integer> conditionFieldMap = new HashMap<>();
    for (LeadConditionRequestDto leadConditionRequestDto : leadConditionRequestDtos) {
      Integer conditionId = leadConditionRequestDto.getConditionId();
      Integer fieldId = leadConditionRequestDto.getFieldId();
      // Kiem tra xem da ton tai FieldCode And ConditionId.
      if (conditionFieldMap.containsKey(conditionId)
          && fieldId.equals(conditionFieldMap.get(conditionId))) {
        throw new OperationNotImplementException("FieldId And ConditionId is Duplicate.",
            ServiceInfo.getId() + ServiceMessageCode.FIELD_ID_CONDITION_ID_DUPLICATE);
      }
      conditionFieldMap.put(conditionId, fieldId);
    }
    // Kiểm tra danh sach condition co ton tai.
    Set<Integer> conditionIdSet = conditionFieldMap.keySet();
    Set<Condition> conditions = conditionRepository.findByIdInAndIsDeletedFalse(conditionIdSet);
    if (conditions.size() != conditionIdSet.size()) {
      throw new ResourceNotFoundException("Condition Not Found.",
          ServiceInfo.getId() + ServiceMessageCode.CONDITION_NOT_FOUND);
    }
    Map<Integer, Condition> conditionMap = new HashMap<>();
    conditionMap.putAll(conditions.stream()
        .collect(Collectors.toMap(Condition::getId, entity -> entity)));
    for (LeadConditionRequestDto leadConditionRequestDto : leadConditionRequestDtos) {
      FilterCondition filterCondition = filterConditionMapper.fromDto(leadConditionRequestDto);
      Condition condition = conditionMap.get(leadConditionRequestDto.getConditionId());
      filterCondition.setCondition(condition);
      filterCondition.setFilter(filter);
      filterCondition.setCreatorUserId(userId);
      filterConditions.add(filterCondition);

      List<String> values = leadConditionRequestDto.getValues();
      if (values != null && !values.isEmpty()) {
        filterConditionValues.addAll(values.stream()
            .map(value -> new FilterConditionValue(filterCondition, value, userId))
            .collect(Collectors.toList()));
      }
    }

    return filterConditions;
  }

  /**
   * @param userId
   * @param dto
   * @return
   * @throws ResourceNotFoundException
   * @throws OperationNotImplementException
   * @apiNote API cap nhat bo loc
   */
  @Override
  public Integer updateFilter(Integer userId, FilterUpdateRequestDto dto)
      throws ResourceNotFoundException, OperationNotImplementException {
    Integer filterId = dto.getId();
    Filter filterExist = filterRepository.findByIdAndIsDeletedIsFalse(filterId);
    if (filterExist == null) {
      throw new ResourceNotFoundException("Filter not found",
          ServiceInfo.getId() + ServiceMessageCode.FILTER_NOT_FOUND);
    }
    BeanUtils.copyProperties(dto, filterExist);
    List<FilterConditionValue> filterConditionValues = new ArrayList<>();
    List<FilterCondition> filterConditions = validateFilterConditionUpdateFilter(userId,
        filterExist, dto.getConditions(), filterConditionValues);
    filterExist.setUpdaterUserId(userId);
    filterExist.setModifiedTime();
    filterRepository.save(filterExist);
    if (!filterConditions.isEmpty()) {
      filterConditionRepository.saveAll(filterConditions);
    }
    if (!filterConditionValues.isEmpty()) {
      filterConditionValueRepository.saveAll(filterConditionValues);
    }
    return filterExist.getId();
  }

  private List<FilterCondition> validateFilterConditionUpdateFilter(Integer userId, Filter filter,
      List<LeadConditionRequestDto> leadConditionRequestDtos,
      List<FilterConditionValue> filterConditionValues)
      throws OperationNotImplementException, ResourceNotFoundException {
    logger.info(" <<< validateFilterConditionUpdateFilter()");
    List<FilterCondition> filterConditions = new ArrayList<>();
    Integer offerId = filter.getId();
    List<FilterCondition> filterConditionDeletes =
        filterConditionRepository.findFilterConditionByFilterIdAndIsDeletedFalse(offerId);
    if (!filterConditionDeletes.isEmpty()) {
      for (FilterCondition filterCondition : filterConditionDeletes) {
        filterCondition.setUpdaterUserId(userId);
        filterCondition.setIsDeleted(true);

        filterConditions.add(filterCondition);
      }
      Set<Integer> filterConditionIds =
          filterConditionDeletes.stream().map(FilterCondition::getId).collect(Collectors.toSet());
      List<FilterConditionValue> filterConditionValueDeletes =
          filterConditionValueRepository.findByFilterConditionIdInAndIsDeletedFalse(
              filterConditionIds);
      for (FilterConditionValue filterConditionValue : filterConditionValueDeletes) {
        filterConditionValue.setIsDeleted(true);
        filterConditionValue.setUpdaterUserId(userId);

        filterConditionValues.add(filterConditionValue);
      }
    }

    filterConditions.addAll(validateFilterConditionCreateFilter(userId,
        filter, leadConditionRequestDtos, filterConditionValues));

    return filterConditions;
  }

  @Override
  public FilterResponseDto getByID(Integer id)
      throws ResourceNotFoundException {
    FilterResponseDto filterResponseDto = new FilterResponseDto();
    Filter filter = filterRepository.findByIdAndIsDeletedIsFalse(id);
    if (filter == null) {
      throw new ResourceNotFoundException("Filter not found",
          ServiceInfo.getId() + ServiceMessageCode.FILTER_NOT_FOUND);
    }

    Integer modifierId = filter.getUpdaterUserId();

    if (modifierId == null) {
      filterResponseDto.setUpdaterName("");
    } else {
      filterResponseDto.setUpdaterName(userService.getUserById(modifierId).getName());
    }

    filterResponseDto.setId(filter.getId());
    filterResponseDto.setCode(filter.getCode());
    filterResponseDto.setName(filter.getName());
    filterResponseDto.setDescription(filter.getDescription());
    filterResponseDto.setCreateTime(filter.getCreatedTime());
    filterResponseDto.setModifiedTime(filter.getModifiedTime());
    List<FilterConditionResponseDto> listFilterConditionResponse = new ArrayList<>();
    List<FilterCondition> listFilterConditionById = filterConditionRepository
        .findFilterConditionByFilterIdAndIsDeletedFalse(
            filter.getId());
    if (listFilterConditionById.size() > 0) {
      listFilterConditionResponse = listFilterConditionById.stream()
          .map(filterConditionMapper::toDto).collect(
              Collectors.toList());
      listFilterConditionResponse.forEach(
          filterConditionResponseDto -> filterConditionResponseDto.setValue(
              filterConditionValueRepository.listFilterValueByConditionId(
                  filterConditionResponseDto.getId())));
    }
    filterResponseDto.setListFilterCondition(listFilterConditionResponse);
    //find User
    Set<Integer> listUser = new HashSet<>();
    Integer creatorUserId = filter.getCreatorUserId();
    if (creatorUserId != null) {
      listUser.add(creatorUserId);
    }
    Integer updaterUserId = filter.getUpdaterUserId();
    if (updaterUserId != null) {
      listUser.add(updaterUserId);
    }
    if (!listUser.isEmpty()) {
      Map<Integer, UserResponseDto> userResponseDtoMap = userService.mapByListId(listUser);
      if (!userResponseDtoMap.isEmpty()) {
        if (creatorUserId != null && userResponseDtoMap.containsKey(creatorUserId)) {
          UserResponseDto creatorUser = userResponseDtoMap.get(creatorUserId);
          filterResponseDto.setCreatorName(creatorUser.getUsername());
        }
        if (updaterUserId != null && userResponseDtoMap.containsKey(updaterUserId)) {
          UserResponseDto updaterUser = userResponseDtoMap.get(updaterUserId);
          filterResponseDto.setUpdaterName(updaterUser.getUsername());
        }
      }
    }
    return filterResponseDto;
  }

  @Override
  public Boolean deleteFilter(Integer userId, Integer id)
      throws ResourceNotFoundException, OperationNotImplementException {
    userService.checkValidUser(userId);
    Filter filter = filterRepository.findByIdAndIsDeletedIsFalse(id);
    if (filter == null) {
      throw new ResourceNotFoundException("Filter not found",
          ServiceInfo.getId() + ServiceMessageCode.FILTER_NOT_FOUND);
    }
    filter.setIsDeleted(true);
    filter.setDeleterUserId(userId);
    List<CampaignFilter> campaignFilters = campaignFilterRepository
        .findCampaignFilterByFilterIdAndIsDeletedIsFalse(id);
    if (campaignFilters != null || campaignFilters.size() == 0) {
      for (CampaignFilter campaignFilter : campaignFilters) {
        campaignFilter.setIsDeleted(true);
        campaignFilter.setDeleterUserId(userId);
//      campaign.setDeleted(true);
//      campaign.setDeleterUserId(userId);
      }
    }
    return true;
  }

  @Override
  public DataPagingResponse<FilterResponse> getListFilter(String search, String sort, Integer page,
      Integer limit,
      Long startDate, Long endDate) {
    logger.info("--- getListFilter() ---");
    Map<String, String> map = SortingUtils.detectSortType(sort);
    if (map.isEmpty()) {
      map.put("createdTime", "desc");
    }
    Page<Filter> filterPage = filterRepository
        .findAll(filterOfEntityFilter.filter(search, map, startDate, endDate),
            PageRequest.of(page - 1, limit));
    List<FilterResponse> listFilterResponses =
        mappingFilter(filterPage.getContent());

    DataPagingResponse<FilterResponse> dataPagingResponses = new DataPagingResponse<>();
    dataPagingResponses.setList(listFilterResponses);
    dataPagingResponses.setTotalPage(filterPage.getTotalPages());
    dataPagingResponses.setNum(filterPage.getTotalElements());
    dataPagingResponses.setCurrentPage(page);
    return dataPagingResponses;
  }

  private List<FilterResponse> mappingFilter(List<Filter> filters) {
    logger.info("--- mappingFilterDTO() ---");
    List<FilterResponse> result = new ArrayList<>();
    if (filters == null || filters.isEmpty()) {
      return result;
    }

    Set<Integer> userIdSet = filters.stream().map(Filter::getCreatorUserId)
        .filter(Objects::nonNull).collect(Collectors.toSet());
    Map<Integer, UserResponseDto> userResponseDtoMap = new HashMap<>();
    if (!userIdSet.isEmpty()) {
      userResponseDtoMap = userService.mapByListId(userIdSet);
    }
    for (Filter filter : filters) {
      FilterResponse filterResponse = filterMapper.toDto(filter);
      Integer creatorId = filter.getCreatorUserId();
      if (creatorId != null && userResponseDtoMap.containsKey(creatorId)) {
        UserResponseDto userResponseDto = userResponseDtoMap.get(creatorId);
        filterResponse.setUserResponseDto(userResponseDto);
      }
      result.add(filterResponse);
    }

    return result;
  }
}
