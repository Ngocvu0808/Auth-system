package com.ttt.mar.leads.service.impl;

import com.ttt.mar.leads.config.ServiceMessageCode;
import com.ttt.mar.leads.dto.LeadSourceDetailResponseDto;
import com.ttt.mar.leads.dto.LeadSourceFieldValidationRequestDto;
import com.ttt.mar.leads.dto.LeadSourceRequestDto;
import com.ttt.mar.leads.dto.LeadSourceResponse;
import com.ttt.mar.leads.dto.LeadSourceResponseDto;
import com.ttt.mar.leads.dto.LeadSourceUpdateStatusDto;
import com.ttt.mar.leads.dto.UserResponseDto;
import com.ttt.mar.leads.dto.ValidationDto;
import com.ttt.mar.leads.entities.LeadSource;
import com.ttt.mar.leads.entities.LeadSourceFieldValidation;
import com.ttt.mar.leads.entities.LeadSourceStatus;
import com.ttt.mar.leads.entities.ProjectSource;
import com.ttt.mar.leads.entities.Validation;
import com.ttt.mar.leads.filter.LeadSourceFilter;
import com.ttt.mar.leads.mapper.LeadSourceFieldValidationMapper;
import com.ttt.mar.leads.mapper.LeadSourceMapper;
import com.ttt.mar.leads.mapper.ValidationMapper;
import com.ttt.mar.leads.repositories.CampaignRepository;
import com.ttt.mar.leads.repositories.CampaignSourceRepository;
import com.ttt.mar.leads.repositories.LeadSourceFieldValidationRepository;
import com.ttt.mar.leads.repositories.LeadSourceRepository;
import com.ttt.mar.leads.repositories.ProjectSourceRepository;
import com.ttt.mar.leads.repositories.ValidationRepository;
import com.ttt.mar.leads.service.iface.LeadSourceService;
import com.ttt.mar.leads.service.iface.UserService;
import com.ttt.rnd.common.config.ServiceInfo;
import com.ttt.rnd.common.dto.DataPagingResponse;
import com.ttt.rnd.common.exception.IdentifyBlankException;
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
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * @author bontk
 * @created_date 22/03/2021
 */

@Service
public class LeadSourceServiceImpl implements LeadSourceService {

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
      .getLogger(LeadSourceServiceImpl.class);

  private final ValidationRepository validationRepository;
  private final ValidationMapper validationMapper;
  private final LeadSourceMapper leadSourceMapper;
  private final LeadSourceRepository leadSourceRepository;
  private final LeadSourceFieldValidationRepository leadSourceFieldValidationRepository;
  private final LeadSourceFieldValidationMapper leadSourceFieldValidationMapper;
  private final LeadSourceFilter leadSourceFilter;
  private final ProjectSourceRepository projectSourceRepository;
  private final UserService userService;
  private final CampaignRepository campaignRepository;
  private final CampaignSourceRepository campaignSourceRepository;

  public LeadSourceServiceImpl(
      ValidationRepository validationRepository, ValidationMapper validationMapper,
      LeadSourceMapper leadSourceMapper, LeadSourceRepository leadSourceRepository,
      LeadSourceFieldValidationRepository leadSourceFieldValidationRepository,
      LeadSourceFieldValidationMapper leadSourceFieldValidationMapper,
      LeadSourceFilter leadSourceFilter, UserService userService,
      ProjectSourceRepository projectSourceRepository,
      CampaignRepository campaignRepository,
      CampaignSourceRepository campaignSourceRepository) {
    this.validationRepository = validationRepository;
    this.validationMapper = validationMapper;
    this.leadSourceMapper = leadSourceMapper;
    this.leadSourceRepository = leadSourceRepository;
    this.leadSourceFieldValidationRepository = leadSourceFieldValidationRepository;
    this.leadSourceFieldValidationMapper = leadSourceFieldValidationMapper;
    this.leadSourceFilter = leadSourceFilter;
    this.userService = userService;
    this.projectSourceRepository = projectSourceRepository;
    this.campaignRepository = campaignRepository;
    this.campaignSourceRepository = campaignSourceRepository;
  }

  public List<ValidationDto> findAllValidation() {
    logger.info("-- findAll list Validation --");
    List<Validation> validations = validationRepository.findAll();
    return validations.stream().map(validationMapper::toDto)
        .collect(Collectors.toList());
  }

  public Integer createLeadSource(Integer userId, LeadSourceRequestDto dto)
      throws OperationNotImplementException, ResourceNotFoundException {
    logger.info("createLeadSource(), Info: userId = {}. dto = {}", userId, JsonUtils.toJson(dto));
    checkValidateLeadSource(null, dto.getName(), dto.getSource(), dto.getUtmSource());
    userService.checkValidUser(userId);
    validateSource(dto.getSource());

    LeadSource leadSource = leadSourceMapper.fromDto(dto);
    List<LeadSourceFieldValidation> leadSourceFieldValidations = leadSource
        .getLeadSourceFieldValidations();

    leadSource.setCreatorUserId(userId);
    leadSourceRepository.save(leadSource);
    saveLeadSourceFieldValidations(userId, leadSource, leadSourceFieldValidations);

    return leadSource.getId();
  }

  public void validateSource(String source) {
    //todo
  }

  public LeadSourceDetailResponseDto getById(Integer id)
      throws IdentifyBlankException, ResourceNotFoundException, OperationNotImplementException {
    logger.info("--- Get LeadSource By Id ---");
    if (id == null) {
      throw new IdentifyBlankException("Id not null",
          ServiceInfo.getId() + ServiceMessageCode.ID_NOT_NULL);
    }
    LeadSource leadSource = leadSourceRepository.findByIdAndIsDeletedFalse(id);
    if (leadSource == null) {
      throw new ResourceNotFoundException("Lead Source Not Found",
          ServiceInfo.getId() + ServiceMessageCode.LEAD_SOURCE_NOT_FOUND);
    }

    return mappingLeadSourceDto(leadSource);
  }

  public List<LeadSourceResponse> mappingLeadSourcesDto(List<LeadSource> leadSources) {
    logger.info("--- mappingLeadSourcesDto() ---");
    List<LeadSourceResponse> leadSourceResponses = new ArrayList<>();
    if (leadSources == null || leadSources.isEmpty()) {
      return leadSourceResponses;
    }

    Set<Integer> userIdSet = leadSources.stream().map(LeadSource::getCreatorUserId)
        .filter(Objects::nonNull).collect(Collectors.toSet());
    Map<Integer, UserResponseDto> userResponseDtoMap = new HashMap<>();
    if (!userIdSet.isEmpty()) {
      userResponseDtoMap = userService.mapByListId(userIdSet);
    }
    for (LeadSource leadSource : leadSources) {
      LeadSourceResponse response = leadSourceMapper.toDto(leadSource);
      Integer creatorId = leadSource.getCreatorUserId();
      if (creatorId != null && userResponseDtoMap.containsKey(creatorId)) {
        UserResponseDto userResponseDto = userResponseDtoMap.get(creatorId);
        response.setUserResponseDto(userResponseDto);
      }
      leadSourceResponses.add(response);
    }

    return leadSourceResponses;
  }

  public LeadSourceDetailResponseDto mappingLeadSourceDto(LeadSource leadSource) {
    logger.info("--- mappingLeadSourceDto() ---");
    LeadSourceDetailResponseDto responseDto = leadSourceMapper.toDetailDto(leadSource);
    Set<Integer> userIdSet = new HashSet<>();

    Integer creatorUserId = leadSource.getCreatorUserId();
    if (creatorUserId != null) {
      userIdSet.add(creatorUserId);
    }
    Integer updaterUserId = leadSource.getUpdaterUserId();
    if (updaterUserId != null) {
      userIdSet.add(updaterUserId);
    }

    if (!userIdSet.isEmpty()) {
      Map<Integer, UserResponseDto> userResponseDtoMap = userService.mapByListId(userIdSet);
      if (!userResponseDtoMap.isEmpty()) {
        if (creatorUserId != null && userResponseDtoMap.containsKey(creatorUserId)) {
          UserResponseDto creatorUser = userResponseDtoMap.get(creatorUserId);
          responseDto.setCreatorName(creatorUser.getUsername());
        }
        if (updaterUserId != null && userResponseDtoMap.containsKey(updaterUserId)) {
          UserResponseDto updaterUser = userResponseDtoMap.get(updaterUserId);
          responseDto.setUpdaterName(updaterUser.getUsername());
        }
      }
    }

    return responseDto;
  }

  public void checkValidateLeadSource(Integer id, String name, String source, String utmSource)
      throws OperationNotImplementException {
    logger.info("--- checkValidateLeadSource() ---");
    if (StringUtils.isNotBlank(name)) {
      List<LeadSource> leadSources = leadSourceRepository
          .findAll(leadSourceFilter.checkExistFilter(id, name, null, null));
      if (!leadSources.isEmpty()) {
        throw new OperationNotImplementException("Name of LeadSource is exist",
            ServiceInfo.getId() + ServiceMessageCode.NAME_LEAD_SOURCE_EXIST);
      }
    }

    if (StringUtils.isNotBlank(source) && StringUtils.isNotBlank(utmSource)) {
      List<LeadSource> leadSources = leadSourceRepository
          .findAll(leadSourceFilter.checkExistFilter(id, null, source, utmSource));
      if (!leadSources.isEmpty()) {
        throw new OperationNotImplementException("Utm Source of LeadSource is exist",
            ServiceInfo.getId() + ServiceMessageCode.UTM_SOURCE_LEAD_SOURCE_EXIST);
      }
    }
  }

  public Integer updateLeadSource(Integer userId, LeadSourceRequestDto dto)
      throws ResourceNotFoundException, OperationNotImplementException {
    logger.info("---- updateLeadSource() ----");
    checkValidateLeadSource(dto.getId(), dto.getName(), dto.getSource(), dto.getUtmSource());
    // tep1: Kiểm tra các thông tin.
    userService.checkValidUser(userId);
    LeadSource leadSource = leadSourceRepository.findByIdAndIsDeletedFalse(dto.getId());
    if (leadSource == null) {
      throw new ResourceNotFoundException("Lead Source Not Found",
          ServiceInfo.getId() + ServiceMessageCode.LEAD_SOURCE_NOT_FOUND);
    }
    List<LeadSourceFieldValidation> validationFieldOlds = leadSource
        .getLeadSourceFieldValidations();

    validateSource(dto.getSource());

    // Step2: copy data Dto to Entity
    BeanUtils.copyProperties(dto, leadSource);
    leadSource.setUpdaterUserId(userId);

    List<LeadSourceFieldValidationRequestDto> leadSourceFieldValidationRequestDtos =
        dto.getLeadSourceFieldValidationRequestDtos();
    List<LeadSourceFieldValidation> leadSourceFieldValidationNews = new ArrayList<>();
    if (leadSourceFieldValidationRequestDtos != null
        && !leadSourceFieldValidationRequestDtos.isEmpty()) {
      leadSourceFieldValidationNews = leadSourceFieldValidationRequestDtos.stream().map(
          leadSourceFieldValidationMapper::fromDto).collect(Collectors.toList());
    }

    // Step 3: xóa các data fieldValidation cũ
    deleteLeadSourceFieldValidation(userId, validationFieldOlds);
    leadSourceRepository.save(leadSource);
    // Step4: Thêm mới các fieldValidation mới
    saveLeadSourceFieldValidations(userId, leadSource, leadSourceFieldValidationNews);

    return leadSource.getId();
  }

  public DataPagingResponse<LeadSourceResponse> getLeadSourceFilter(LeadSourceStatus status,
      String search, String sort, Integer page, Integer limit) {
    logger.info("--- getLeadSourceFilter() ---");
    Map<String, String> map = SortingUtils.detectSortType(sort);
    if (map.isEmpty()) {
      map.put("createdTime", "desc");
    }
    Page<LeadSource> leadSourcePage = leadSourceRepository
        .findAll(leadSourceFilter.filter(status, search, map), PageRequest.of(page - 1, limit));
    List<LeadSourceResponse> leadSourceResponses =
        mappingLeadSourcesDto(leadSourcePage.getContent());

    DataPagingResponse<LeadSourceResponse> dataPagingResponses = new DataPagingResponse<>();
    dataPagingResponses.setList(leadSourceResponses);
    dataPagingResponses.setTotalPage(leadSourcePage.getTotalPages());
    dataPagingResponses.setNum(leadSourcePage.getTotalElements());
    dataPagingResponses.setCurrentPage(page);

    return dataPagingResponses;
  }

  public Integer updateStatusLeadSource(Integer userId, LeadSourceUpdateStatusDto dto)
      throws ResourceNotFoundException, OperationNotImplementException {
    userService.checkValidUser(userId);
    LeadSource leadSource = leadSourceRepository.findByIdAndIsDeletedFalse(dto.getId());
    if (leadSource == null) {
      throw new ResourceNotFoundException("Lead Source Not Found",
          ServiceInfo.getId() + ServiceMessageCode.LEAD_SOURCE_NOT_FOUND);
    }
    leadSource.setUpdaterUserId(userId);
    leadSource.setStatus(dto.getStatus());
    leadSourceRepository.save(leadSource);

    return leadSource.getId();
  }

  public Boolean deleteLeadSource(Integer userId, Integer id)
      throws ResourceNotFoundException, OperationNotImplementException {
    userService.checkValidUser(userId);
    LeadSource leadSource = leadSourceRepository.findByIdAndIsDeletedFalse(id);
    if (leadSource == null) {
      throw new ResourceNotFoundException("Lead Source Not Found",
          ServiceInfo.getId() + ServiceMessageCode.LEAD_SOURCE_NOT_FOUND);
    }
    if (LeadSourceStatus.ACTIVE.equals(leadSource.getStatus())) {
      throw new OperationNotImplementException("Invalid Status LeadSource",
          ServiceInfo.getId() + ServiceMessageCode.INVALID_STATUS_LEAD_SOURCE);
    }
    leadSource.setDeleterUserId(userId);
    leadSource.setIsDeleted(true);
    leadSourceRepository.save(leadSource);

    return true;
  }


  public void deleteLeadSourceFieldValidation(Integer userId,
      List<LeadSourceFieldValidation> validationFields) {
    logger.info("--- deleteLeadSourceFieldValidation() ---");
    if (!validationFields.isEmpty()) {
      validationFields = validationFields.stream()
          .filter(entity -> !entity.getIsDeleted()).collect(Collectors.toList());
      if (!validationFields.isEmpty()) {
        for (LeadSourceFieldValidation validationField : validationFields) {
          validationField.setIsDeleted(true);
          validationField.setDeleterUserId(userId);
        }
        leadSourceFieldValidationRepository.saveAll(validationFields);
      }
    }
  }

  public void saveLeadSourceFieldValidations(Integer userId, LeadSource leadSource,
      List<LeadSourceFieldValidation> leadSourceFieldValidations) {
    logger.info("saveLeadSourceFieldValidations()");
    if (leadSourceFieldValidations == null || leadSourceFieldValidations.isEmpty()) {
      return;
    }
    for (LeadSourceFieldValidation validation : leadSourceFieldValidations) {
      validation.setLeadSource(leadSource);
      validation.setCreatorUserId(userId);
    }
    leadSourceFieldValidationRepository.saveAll(leadSourceFieldValidations);
  }

//  public void validateLeadSourceFieldValidations(
//      List<LeadSourceFieldValidation> leadSourceFieldValidations)
//      throws IdentifyBlankException, ResourceNotFoundException {
//    logger.info("validateLeadSourceFieldValidations()");
//    if (leadSourceFieldValidations == null || leadSourceFieldValidations.isEmpty()) {
//      return;
//    }
//    for (LeadSourceFieldValidation leadSourceFieldValidation : leadSourceFieldValidations) {
//      Integer fieldId = leadSourceFieldValidation.getFieldId();
//      if (fieldId == null) {
//        throw new IdentifyBlankException(
//            "Field-Id not null", ServiceInfo.getId() + ServiceMessageCode.FIELD_ID_NOT_NULL);
//      }
//      MarFieldConfig fieldConfig = marFieldConfigRepository.findByIdAndIsDeletedFalse(fieldId);
//      if (fieldConfig == null) {
//        throw new ResourceNotFoundException(String.format("Field by %s not found", fieldId),
//            ServiceInfo.getId() + ServiceMessageCode.FIELD_ID_NOT_NULL);
//      }
//      leadSourceFieldValidation.setField(fieldConfig);
//      String validationCode = leadSourceFieldValidation.getValidationCode();
//      Validation validation = validationRepository.findByCode(validationCode);
//      if (validation == null) {
//        throw new ResourceNotFoundException("Validation not found",
//            ServiceInfo.getId() + ServiceMessageCode.VALIDATION_NOT_FOUND);
//      }
//    }
//  }

  @Override
  public List<LeadSourceResponseDto> leadSourceToCampaignManage(LeadSourceStatus status,
      Integer projectId) {
    List<LeadSourceResponseDto> listLeadSourceToCampaign = new ArrayList<>();
    List<LeadSource> listLeadSource = new ArrayList<>();
    listLeadSource = leadSourceRepository.findAllByIsDeletedFalse();
    if (status != null & projectId == null) {
      listLeadSource = leadSourceRepository.findAllByStatusAndIsDeletedFalse(status);
    }
    if (projectId != null) {
      List<ProjectSource> listProjectSource = projectSourceRepository
          .findByProjectIdAndIsDeletedFalse(projectId);
      Set<Integer> SetIdsProjectSource = listProjectSource.stream().map(x -> x.getSourceId())
          .collect(Collectors.toSet());
      listLeadSource = leadSourceRepository.findByIdInAndIsDeletedFalse(SetIdsProjectSource);
      if (status != null) {
        listLeadSource = leadSourceRepository
            .findByIdInAndStatusAndIsDeletedFalse(SetIdsProjectSource, status);
      }
    }
    listLeadSourceToCampaign = listLeadSource.stream().map(leadSourceMapper::toDtoToCampaignManage)
        .collect(
            Collectors.toList());
    listLeadSourceToCampaign.forEach(x -> x.setProjectId(projectId));

    return listLeadSourceToCampaign;
  }
}
