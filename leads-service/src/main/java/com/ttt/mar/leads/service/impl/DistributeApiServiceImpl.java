package com.ttt.mar.leads.service.impl;

import com.ttt.mar.leads.config.ServiceMessageCode;
import com.ttt.mar.leads.dto.DistributeApiDataRequestDto;
import com.ttt.mar.leads.dto.DistributeApiDataResponseDto;
import com.ttt.mar.leads.dto.DistributeApiDetailResponse;
import com.ttt.mar.leads.dto.DistributeApiHeaderRequestDto;
import com.ttt.mar.leads.dto.DistributeApiHeaderResponseDto;
import com.ttt.mar.leads.dto.DistributeApiRequestDto;
import com.ttt.mar.leads.dto.DistributeApiResponseDto;
import com.ttt.mar.leads.dto.DistributeApiUpdateStatusDto;
import com.ttt.mar.leads.dto.DistributeFieldMappingRequestDto;
import com.ttt.mar.leads.dto.DistributeFieldMappingResponseDto;
import com.ttt.mar.leads.dto.DistributeMethodPOSTResponseDto;
import com.ttt.mar.leads.dto.DistributeResponseDto;
import com.ttt.mar.leads.dto.UserResponseDto;
import com.ttt.mar.leads.entities.ApiMethod;
import com.ttt.mar.leads.entities.ApiSecureMethod;
import com.ttt.mar.leads.entities.DistributeApi;
import com.ttt.mar.leads.entities.DistributeApiData;
import com.ttt.mar.leads.entities.DistributeApiHeader;
import com.ttt.mar.leads.entities.DistributeApiStatus;
import com.ttt.mar.leads.entities.DistributeFieldMapping;
import com.ttt.mar.leads.entities.Project;
import com.ttt.mar.leads.entities.ProjectDistribute;
import com.ttt.mar.leads.filter.DistributeApiFilter;
import com.ttt.mar.leads.mapper.DistributeApiDataMapper;
import com.ttt.mar.leads.mapper.DistributeApiHeaderMapper;
import com.ttt.mar.leads.mapper.DistributeApiMapper;
import com.ttt.mar.leads.mapper.DistributeFieldMappingMapper;
import com.ttt.mar.leads.repositories.CampaignDistributeRepository;
import com.ttt.mar.leads.repositories.CampaignRepository;
import com.ttt.mar.leads.repositories.DistributeApiDataRepository;
import com.ttt.mar.leads.repositories.DistributeApiHeaderRepository;
import com.ttt.mar.leads.repositories.DistributeApiRepository;
import com.ttt.mar.leads.repositories.DistributeFieldMappingRepository;
import com.ttt.mar.leads.repositories.ProjectDistributeRepository;
import com.ttt.mar.leads.repositories.ProjectRepository;
import com.ttt.mar.leads.service.iface.DistributeApiService;
import com.ttt.mar.leads.service.iface.UserService;
import com.ttt.rnd.common.config.ServiceInfo;
import com.ttt.rnd.common.dto.DataPagingResponse;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
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
import org.springframework.util.ObjectUtils;

/**
 * @author kietdt
 * @created_date 20/04/2021
 */
@Service
public class DistributeApiServiceImpl implements DistributeApiService {

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
      .getLogger(DistributeApiServiceImpl.class);

  private final UserService userService;
  private final DistributeApiMapper distributeApiMapper;
  private final DistributeApiDataMapper distributeApiDataMapper;
  private final DistributeApiHeaderMapper distributeApiHeaderMapper;
  private final DistributeFieldMappingMapper distributeFieldMappingMapper;
  private final DistributeApiDataRepository distributeApiDataRepository;
  private final DistributeApiRepository distributeApiRepository;
  private final DistributeApiHeaderRepository distributeApiHeaderRepository;
  private final DistributeFieldMappingRepository distributeFieldMappingRepository;
  private final DistributeApiFilter distributeApiFilter;
  private final ProjectDistributeRepository projectDistributeRepository;
  private final ProjectRepository projectRepository;
  private final CampaignRepository campaignRepository;
  private final CampaignDistributeRepository campaignDistributeRepository;

  public DistributeApiServiceImpl(UserService userService, DistributeApiMapper distributeApiMapper,
      DistributeApiDataMapper distributeApiDataMapper,
      DistributeApiHeaderMapper distributeApiHeaderMapper,
      DistributeFieldMappingMapper distributeFieldMappingMapper,
      DistributeApiDataRepository distributeApiDataRepository,
      DistributeApiRepository distributeApiRepository,
      DistributeApiHeaderRepository distributeApiHeaderRepository,
      DistributeFieldMappingRepository distributeFieldMappingRepository,
      DistributeApiFilter distributeApiFilter,
      ProjectDistributeRepository projectDistributeRepository,
      ProjectRepository projectRepository,
      CampaignRepository campaignRepository,
      CampaignDistributeRepository campaignDistributeRepository) {
    this.userService = userService;
    this.distributeApiMapper = distributeApiMapper;
    this.distributeApiDataMapper = distributeApiDataMapper;
    this.distributeApiHeaderMapper = distributeApiHeaderMapper;
    this.distributeFieldMappingMapper = distributeFieldMappingMapper;
    this.distributeApiDataRepository = distributeApiDataRepository;
    this.distributeApiRepository = distributeApiRepository;
    this.distributeApiHeaderRepository = distributeApiHeaderRepository;
    this.distributeFieldMappingRepository = distributeFieldMappingRepository;
    this.distributeApiFilter = distributeApiFilter;
    this.projectDistributeRepository = projectDistributeRepository;
    this.projectRepository = projectRepository;
    this.campaignRepository = campaignRepository;
    this.campaignDistributeRepository = campaignDistributeRepository;
  }

  public Integer createDistributeApi(Integer userId, DistributeApiRequestDto dto)
      throws ResourceNotFoundException, OperationNotImplementException {
    logger.info("--- createDistributeApi() ---");
    checkValidateDistribute(null, dto.getName());
    userService.checkValidUser(userId);
    DistributeApi distributeApi = distributeApiMapper.fromDto(dto);
    distributeApi.setCreatorUserId(userId);
    distributeApiRepository.save(distributeApi);

    saveApiDataRequestDtosNew(userId, distributeApi, dto.getApiDataRequestDtos());
    saveApiHeaderRequestDtosNew(userId, distributeApi, dto.getApiHeaderRequestDtos());
    saveFieldMappingRequestDtosNew(userId, distributeApi, dto.getFieldMappingRequestDtos());

    return distributeApi.getId();
  }

  public DataPagingResponse<DistributeApiResponseDto> getDistributeFilter(
      DistributeApiStatus status, String search,
      String sort, Integer page, Integer limit) {
    logger.info("--- getDistributeFilter() ---");
    Map<String, String> map = SortingUtils.detectSortType(sort);
    if (map.isEmpty()) {
      map.put("createdTime", "desc");
    }
    Page<DistributeApi> distributeApiPage = distributeApiRepository
        .findAll(distributeApiFilter.filter(status, search, map), PageRequest.of(page - 1, limit));
    List<DistributeApiResponseDto> distributeApiResponseDtos = mappingDistributeDto(
        distributeApiPage.getContent());

    DataPagingResponse<DistributeApiResponseDto> dataPagingResponses = new DataPagingResponse<>();
    dataPagingResponses.setList(distributeApiResponseDtos);
    dataPagingResponses.setTotalPage(distributeApiPage.getTotalPages());
    dataPagingResponses.setNum(distributeApiPage.getTotalElements());
    dataPagingResponses.setCurrentPage(page);

    return dataPagingResponses;
  }

  public DistributeApiDetailResponse getById(Integer id) throws ResourceNotFoundException {
    DistributeApi distributeApi = distributeApiRepository.findByIdAndIsDeletedFalse(id);
    if (distributeApi == null) {
      throw new ResourceNotFoundException("Distribute Not Found",
          ServiceInfo.getId() + ServiceMessageCode.DISTRIBUTE_NOT_FOUND);
    }

    return mappingDistributeApiDto(distributeApi);
  }

  public Integer updateStatusDistributeApi(Integer userId, DistributeApiUpdateStatusDto dto)
      throws ResourceNotFoundException, OperationNotImplementException {
    userService.checkValidUser(userId);
    DistributeApi distributeApi = distributeApiRepository.findByIdAndIsDeletedFalse(dto.getId());
    if (distributeApi == null) {
      throw new ResourceNotFoundException("Lead Source Not Found",
          ServiceInfo.getId() + ServiceMessageCode.DISTRIBUTE_NOT_FOUND);
    }
    distributeApi.setUpdaterUserId(userId);
    distributeApi.setStatus(dto.getStatus());
    distributeApiRepository.save(distributeApi);

    return distributeApi.getId();
  }

  public Boolean deleteDistribute(Integer userId, Integer id)
      throws ResourceNotFoundException, OperationNotImplementException {
    userService.checkValidUser(userId);
    DistributeApi distributeApi = distributeApiRepository.findByIdAndIsDeletedFalse(id);
    if (distributeApi == null) {
      throw new ResourceNotFoundException("Distribute Not Found",
          ServiceInfo.getId() + ServiceMessageCode.DISTRIBUTE_NOT_FOUND);
    }
    if (DistributeApiStatus.ACTIVE.equals(distributeApi.getStatus())) {
      throw new OperationNotImplementException("Invalid Status Distribute",
          ServiceInfo.getId() + ServiceMessageCode.INVALID_STATUS_DISTRIBUTE);
    }
    distributeApi.setDeleterUserId(userId);
    distributeApi.setIsDeleted(true);
    distributeApiRepository.save(distributeApi);

    return true;
  }

  public Integer updateDistributeApi(Integer userId, DistributeApiRequestDto dto)
      throws ResourceNotFoundException, OperationNotImplementException {
    checkValidateDistribute(dto.getId(), dto.getName());
    userService.checkValidUser(userId);
    // Step 1: Kiểm tra kênh phân phối tồn tại
    DistributeApi distributeApi = distributeApiRepository.findByIdAndIsDeletedFalse(dto.getId());
    if (distributeApi == null) {
      throw new ResourceNotFoundException("Distribute Not Found",
          ServiceInfo.getId() + ServiceMessageCode.DISTRIBUTE_NOT_FOUND);
    }
    BeanUtils.copyProperties(dto, distributeApi, "username", "password");
    distributeApi.setUpdaterUserId(userId);
    if (ApiSecureMethod.BASIC.equals(distributeApi.getSecureMethod())) {
      distributeApi.setUsername(dto.getUsername());
      distributeApi.setPassword(dto.getPassword());
    }

    // Step 2: Kiểm tra các trường thông tin đi kèm kênh phân phối
    List<DistributeApiData> distributeApiDataList = validationDistributeApiDatas(dto.getId(),
        userId,
        dto.getApiDataRequestDtos());
    List<DistributeApiHeader> distributeApiHeaders = validationDistributeApiHeaders(dto.getId(),
        userId,
        dto.getApiHeaderRequestDtos());
    List<DistributeFieldMapping> distributeFieldMappings = validationDistributeFieldMappings(
        dto.getId(), userId,
        dto.getFieldMappingRequestDtos());

    // Step 3: Lưu thông tin
    distributeApiRepository.save(distributeApi);
    saveDistributeApiDatas(distributeApi, distributeApiDataList);
    saveDistributeApiHeaders(distributeApi, distributeApiHeaders);
    saveDistributeFieldMappings(distributeApi, distributeFieldMappings);

    return distributeApi.getId();
  }

  public void saveDistributeFieldMappings(DistributeApi distributeApi,
      List<DistributeFieldMapping> distributeFieldMappings) {
    logger.info("--- saveDistributeFieldMappings() ---");
    if (distributeFieldMappings == null || distributeFieldMappings.isEmpty()) {
      return;
    }

    for (DistributeFieldMapping entity : distributeFieldMappings) {
      entity.setDistribute(distributeApi);
    }
    distributeFieldMappingRepository.saveAll(distributeFieldMappings);
  }

  public void saveDistributeApiHeaders(DistributeApi distributeApi,
      List<DistributeApiHeader> distributeApiHeaders) {
    logger.info("--- saveDistributeApiHeaders() ---");
    if (distributeApiHeaders == null || distributeApiHeaders.isEmpty()) {
      return;
    }

    for (DistributeApiHeader entity : distributeApiHeaders) {
      entity.setDistribute(distributeApi);
    }
    distributeApiHeaderRepository.saveAll(distributeApiHeaders);
  }

  public void saveDistributeApiDatas(DistributeApi distributeApi,
      List<DistributeApiData> distributeApiDataList) {
    logger.info("--- saveDistributeApiDatas() ---");
    if (distributeApiDataList == null || distributeApiDataList.isEmpty()) {
      return;
    }

    for (DistributeApiData entity : distributeApiDataList) {
      entity.setDistribute(distributeApi);
    }
    distributeApiDataRepository.saveAll(distributeApiDataList);
  }

  public List<DistributeApiData> validationDistributeApiDatas(Integer distributeId, Integer userId,
      List<DistributeApiDataRequestDto> apiDataRequestDtos) throws ResourceNotFoundException {
    logger.info("--- validationDistributeApiDatas() ---");
    List<DistributeApiData> distributeApiDataList = new ArrayList<>();

    Set<Integer> idDeletes = new HashSet<>();
    if (apiDataRequestDtos != null && !apiDataRequestDtos.isEmpty()) {
      for (DistributeApiDataRequestDto dto : apiDataRequestDtos) {
        DistributeApiData distributeApiData = new DistributeApiData();
        if (dto.getId() != null) {
          // Cập nhật bản ghi cũ
          distributeApiData = distributeApiDataRepository
              .findByIdAndDistributeIdAndIsDeletedFalse(dto.getId(), distributeId);
          if (distributeApiData == null) {
            throw new ResourceNotFoundException("Distribute Data Not Found",
                ServiceInfo.getId() + ServiceMessageCode.DISTRIBUTE_DATA_NOT_FOUND);
          }
          BeanUtils.copyProperties(dto, distributeApiData);
          distributeApiData.setUpdaterUserId(userId);
          idDeletes.add(dto.getId());
        } else {
          // Tạo bản ghi mới
          distributeApiData = distributeApiDataMapper.fromDto(dto);
          distributeApiData.setCreatorUserId(userId);
          distributeApiDataList.add(distributeApiData);
        }
      }
    }
    List<DistributeApiData> distributeApiDataDeletes;
    if (!idDeletes.isEmpty()) {
      // Xóa các thông tin
      distributeApiDataDeletes = distributeApiDataRepository.findByIdNotIn(idDeletes);
    } else {
      distributeApiDataDeletes = distributeApiDataRepository
          .findByDistributeIdAndIsDeletedFalse(distributeId);
    }
    if (!distributeApiDataDeletes.isEmpty()) {
      for (DistributeApiData data : distributeApiDataDeletes) {
        data.setDeleterUserId(userId);
        data.setIsDeleted(true);
        distributeApiDataList.add(data);
      }
    }
    return distributeApiDataList;
  }

  public List<DistributeApiHeader> validationDistributeApiHeaders(Integer distributeId,
      Integer userId,
      List<DistributeApiHeaderRequestDto> apiHeaderRequestDtos) throws ResourceNotFoundException {
    logger.info("--- validationDistributeApiHeaders() ---");

    List<DistributeApiHeader> distributeApiHeaders = new ArrayList<>();

    Set<Integer> idDeletes = new HashSet<>();
    if (apiHeaderRequestDtos != null && !apiHeaderRequestDtos.isEmpty()) {
      for (DistributeApiHeaderRequestDto dto : apiHeaderRequestDtos) {
        DistributeApiHeader distributeApiHeader = new DistributeApiHeader();
        if (dto.getId() != null) {
          // Update thông tin đã tồn tại
          distributeApiHeader = distributeApiHeaderRepository
              .findByIdAndDistributeIdAndIsDeletedFalse(dto.getId(), distributeId);
          if (distributeApiHeader == null) {
            throw new ResourceNotFoundException("Distribute Header Not Found",
                ServiceInfo.getId() + ServiceMessageCode.DISTRIBUTE_HEADER_NOT_FOUND);
          }
          BeanUtils.copyProperties(dto, distributeApiHeader);
          distributeApiHeader.setUpdaterUserId(userId);
          idDeletes.add(dto.getId());
        } else {
          // Tạo mới thông tin
          distributeApiHeader = distributeApiHeaderMapper.fromDto(dto);
          distributeApiHeader.setCreatorUserId(userId);
          distributeApiHeaders.add(distributeApiHeader);
        }
      }
    }

    List<DistributeApiHeader> distributeApiHeaderDeletes;
    if (!idDeletes.isEmpty()) {
      // Xóa các thông tin cũ
      distributeApiHeaderDeletes = distributeApiHeaderRepository.findByIdNotIn(idDeletes);
    } else {
      distributeApiHeaderDeletes = distributeApiHeaderRepository
          .findByDistributeIdAndIsDeletedFalse(distributeId);
    }
    if (!distributeApiHeaderDeletes.isEmpty()) {
      for (DistributeApiHeader data : distributeApiHeaderDeletes) {
        data.setDeleterUserId(userId);
        data.setIsDeleted(true);
        distributeApiHeaders.add(data);
      }
    }

    return distributeApiHeaders;
  }

  public List<DistributeFieldMapping> validationDistributeFieldMappings(Integer distributeId,
      Integer userId,
      List<DistributeFieldMappingRequestDto> fieldMappingRequestDtos)
      throws ResourceNotFoundException {
    logger.info("--- validationDistributeFieldMappings() ---");
    List<DistributeFieldMapping> distributeFieldMappings = new ArrayList<>();

    Set<Integer> idDeletes = new HashSet<>();
    if (fieldMappingRequestDtos != null && !fieldMappingRequestDtos.isEmpty()) {
      for (DistributeFieldMappingRequestDto dto : fieldMappingRequestDtos) {
        DistributeFieldMapping distributeFieldMapping = new DistributeFieldMapping();
        if (dto.getId() != null) {
          distributeFieldMapping = distributeFieldMappingRepository
              .findByIdAndDistributeIdAndIsDeletedFalse(dto.getId(), distributeId);
          if (distributeFieldMapping == null) {
            throw new ResourceNotFoundException("Distribute Field Mapping Not Found",
                ServiceInfo.getId() + ServiceMessageCode.DISTRIBUTE_FIELD_MAPPING_NOT_FOUND);
          }
          BeanUtils.copyProperties(dto, distributeFieldMapping);
          distributeFieldMapping.setUpdaterUserId(userId);
          idDeletes.add(dto.getId());
        } else {
          distributeFieldMapping = distributeFieldMappingMapper.fromDto(dto);
          distributeFieldMapping.setCreatorUserId(userId);
          distributeFieldMappings.add(distributeFieldMapping);
        }
      }
    }
    List<DistributeFieldMapping> distributeFieldMappingDeletes;
    if (!idDeletes.isEmpty()) {
      distributeFieldMappingDeletes = distributeFieldMappingRepository.findByIdNotIn(idDeletes);
    } else {
      distributeFieldMappingDeletes = distributeFieldMappingRepository
          .findByDistributeIdAndIsDeletedFalse(distributeId);
    }
    if (!distributeFieldMappingDeletes.isEmpty()) {
      for (DistributeFieldMapping data : distributeFieldMappingDeletes) {
        data.setDeleterUserId(userId);
        data.setIsDeleted(true);
        distributeFieldMappings.add(data);
      }
    }

    return distributeFieldMappings;
  }

  public DistributeApiDetailResponse mappingDistributeApiDto(DistributeApi distributeApi) {
    logger.info("--- mappingDistributeApiDto() ---");
    DistributeApiDetailResponse distributeApiDetailResponse = distributeApiMapper
        .toDetailDto(distributeApi);
    setDistributeApiDataResponseDtos(distributeApi.getId(), distributeApiDetailResponse);
    setDistributeApiHeaderResponseDtos(distributeApi.getId(), distributeApiDetailResponse);
    setDistributeFieldMappingResponseDtos(distributeApi.getId(), distributeApiDetailResponse);

    Set<Integer> userIdSet = new HashSet<>();

    Integer creatorUserId = distributeApi.getCreatorUserId();
    if (creatorUserId != null) {
      userIdSet.add(creatorUserId);
    }
    Integer updaterUserId = distributeApi.getUpdaterUserId();
    if (updaterUserId != null) {
      userIdSet.add(updaterUserId);
    }

    if (!userIdSet.isEmpty()) {
      Map<Integer, UserResponseDto> userResponseDtoMap = userService.mapByListId(userIdSet);
      if (!userResponseDtoMap.isEmpty()) {
        if (creatorUserId != null && userResponseDtoMap.containsKey(creatorUserId)) {
          UserResponseDto creatorUser = userResponseDtoMap.get(creatorUserId);
          distributeApiDetailResponse.setCreatorName(creatorUser.getUsername());
        }
        if (updaterUserId != null && userResponseDtoMap.containsKey(updaterUserId)) {
          UserResponseDto updaterUser = userResponseDtoMap.get(updaterUserId);
          distributeApiDetailResponse.setUpdaterName(updaterUser.getUsername());
        }
      }
    }

    return distributeApiDetailResponse;
  }

  public void setDistributeApiDataResponseDtos(Integer distributeId,
      DistributeApiDetailResponse distributeApiDetailResponse) {
    logger.info("--- setDistributeApiDataResponseDtos() ---");
    List<DistributeApiData> distributeApiDatas = distributeApiDataRepository
        .findByDistributeIdAndIsDeletedFalse(distributeId);
    if (distributeApiDatas.isEmpty()) {
      return;
    }
    List<DistributeApiDataResponseDto> distributeApiDataResponseDtos = distributeApiDatas.stream()
        .map(distributeApiDataMapper::toDto).collect(Collectors.toList());
    distributeApiDetailResponse.setDistributeApiDataResponseDtos(distributeApiDataResponseDtos);
  }

  public void setDistributeApiHeaderResponseDtos(Integer distributeId,
      DistributeApiDetailResponse distributeApiDetailResponse) {
    logger.info("--- setDistributeApiHeaderResponseDtos() ---");
    List<DistributeApiHeader> distributeApiHeaders = distributeApiHeaderRepository
        .findByDistributeIdAndIsDeletedFalse(distributeId);
    if (distributeApiHeaders.isEmpty()) {
      return;
    }
    List<DistributeApiHeaderResponseDto> distributeApiHeaderResponseDtos = distributeApiHeaders
        .stream()
        .map(distributeApiHeaderMapper::toDto).collect(Collectors.toList());
    distributeApiDetailResponse.setDistributeApiHeaderResponseDtos(distributeApiHeaderResponseDtos);
  }

  public void setDistributeFieldMappingResponseDtos(Integer distributeId,
      DistributeApiDetailResponse distributeApiDetailResponse) {
    logger.info("--- setDistributeFieldMappingResponseDtos() ---");
    List<DistributeFieldMapping> distributeFieldMappings = distributeFieldMappingRepository
        .findByDistributeIdAndIsDeletedFalse(distributeId);
    if (distributeFieldMappings.isEmpty()) {
      return;
    }
    List<DistributeFieldMappingResponseDto> distributeFieldMappingResponseDtos = distributeFieldMappings
        .stream()
        .map(distributeFieldMappingMapper::toDto).collect(Collectors.toList());
    distributeApiDetailResponse
        .setDistributeFieldMappingResponseDtos(distributeFieldMappingResponseDtos);
  }

  public void saveApiDataRequestDtosNew(Integer userId, DistributeApi distributeApi,
      List<DistributeApiDataRequestDto> apiDataRequestDtos) {
    logger.info("--- saveApiDataRequestDtosNew() ---");
    if (apiDataRequestDtos == null || apiDataRequestDtos.isEmpty()) {
      return;
    }
    List<DistributeApiData> distributeApiData = apiDataRequestDtos.stream()
        .map(distributeApiDataMapper::fromDto)
        .collect(Collectors.toList());
    for (DistributeApiData data : distributeApiData) {
      data.setCreatorUserId(userId);
      data.setDistribute(distributeApi);
    }
    distributeApiDataRepository.saveAll(distributeApiData);
  }

  public void saveApiHeaderRequestDtosNew(Integer userId, DistributeApi distributeApi,
      List<DistributeApiHeaderRequestDto> apiHeaderRequestDtos) {
    logger.info("--- saveApiHeaderRequestDtosNew() ---");
    if (apiHeaderRequestDtos == null || apiHeaderRequestDtos.isEmpty()) {
      return;
    }
    List<DistributeApiHeader> distributeApiHeaders = apiHeaderRequestDtos.stream()
        .map(distributeApiHeaderMapper::fromDto).collect(Collectors.toList());
    for (DistributeApiHeader data : distributeApiHeaders) {
      data.setCreatorUserId(userId);
      data.setDistribute(distributeApi);
    }
    distributeApiHeaderRepository.saveAll(distributeApiHeaders);
  }

  public void saveFieldMappingRequestDtosNew(Integer userId, DistributeApi distributeApi,
      List<DistributeFieldMappingRequestDto> fieldMappingRequestDtos) {
    logger.info("--- saveFieldMappingRequestDtosNew() ---");
    if (fieldMappingRequestDtos == null || fieldMappingRequestDtos.isEmpty()) {
      return;
    }
    List<DistributeFieldMapping> distributeFieldMappings = fieldMappingRequestDtos.stream()
        .map(distributeFieldMappingMapper::fromDto).collect(Collectors.toList());
    for (DistributeFieldMapping data : distributeFieldMappings) {
      data.setCreatorUserId(userId);
      data.setDistribute(distributeApi);
    }
    distributeFieldMappingRepository.saveAll(distributeFieldMappings);
  }

  public List<DistributeApiResponseDto> mappingDistributeDto(List<DistributeApi> distributeApis) {
    logger.info("--- mappingDistributeDto() ---");
    List<DistributeApiResponseDto> distributeApiResponseDtos = new ArrayList<>();
    if (distributeApis == null || distributeApis.isEmpty()) {
      return distributeApiResponseDtos;
    }

    Set<Integer> userIdSet = distributeApis.stream().map(DistributeApi::getCreatorUserId)
        .filter(Objects::nonNull)
        .collect(Collectors.toSet());
    Map<Integer, UserResponseDto> userResponseDtoMap = new HashMap<>();
    if (!userIdSet.isEmpty()) {
      userResponseDtoMap = userService.mapByListId(userIdSet);
    }
    for (DistributeApi distributeApi : distributeApis) {
      DistributeApiResponseDto response = distributeApiMapper.toDto(distributeApi);
      Integer creatorId = distributeApi.getCreatorUserId();
      if (creatorId != null && userResponseDtoMap.containsKey(creatorId)) {
        UserResponseDto userResponseDto = userResponseDtoMap.get(creatorId);
        response.setUserResponseDto(userResponseDto);
      }
      distributeApiResponseDtos.add(response);
    }

    return distributeApiResponseDtos;
  }

  public void checkValidateDistribute(Integer id, String name)
      throws OperationNotImplementException {
    logger.info("--- checkValidateDistribute() ---");
    if (StringUtils.isNotBlank(name)) {
      List<DistributeApi> distributeApis = distributeApiRepository
          .findAll(distributeApiFilter.checkExistFilter(id, name));
      if (!distributeApis.isEmpty()) {
        throw new OperationNotImplementException("Name of DistributeApi is exist",
            ServiceInfo.getId() + ServiceMessageCode.NAME_DISTRIBUTE_EXIST);
      }
    }
  }

  public List<DistributeApiResponseDto> getListDistributeApiActiveAndNotExist(Integer projectId)
      throws OperationNotImplementException, ResourceNotFoundException {
    if (ObjectUtils.isEmpty(projectId) || projectId == null) {
      throw new OperationNotImplementException("Project Id is null ",
          ServiceInfo.getId() + ServiceMessageCode.ID_NOT_NULL);
    } else {
      Project project = projectRepository.getOne(projectId);
      if (project == null) {
        throw new ResourceNotFoundException("Project not found ",
            ServiceInfo.getId() + ServiceMessageCode.PROJECT_NOT_FOUND);
      }
    }
    List<Integer> idProjectExist = projectDistributeRepository
        .getListProjectDistributeExist(projectId);
    if (idProjectExist.size() == 0) {
      idProjectExist.add(-1);
    }
    Set<Integer> listIdDistributeActive = distributeApiRepository
        .getListIdProjectDistributeByStatus(DistributeApiStatus.ACTIVE, idProjectExist);
    List<DistributeApi> listResultDistributeApiActive = distributeApiRepository
        .getListProjectDistributeByStatus(listIdDistributeActive);
    return this.mappingDistributeDto(listResultDistributeApiActive);

  }

  @Override
  public List<DistributeResponseDto> getDistributeToCampaignManage(Integer projectId,
      DistributeApiStatus status) {
    List<DistributeResponseDto> listDistributeToCampaign = new ArrayList<>();
    List<DistributeApi> listDistributeApi = new ArrayList<>();
    listDistributeApi = distributeApiRepository.findAllByIsDeletedFalse();
    if (status != null && projectId == null) {
      listDistributeApi = distributeApiRepository.findAllByStatusAndIsDeletedFalse(status);
    }
    if (projectId != null) {
      List<ProjectDistribute> listProjectDistribute = projectDistributeRepository
          .findByProjectIdAndIsDeletedFalse(projectId);
      Set<Integer> SetIdsProjectDistribute = listProjectDistribute.stream()
          .map(x -> x.getDistributeId())
          .collect(Collectors.toSet());
      listDistributeApi = distributeApiRepository
          .findByIdInAndIsDeletedFalse(SetIdsProjectDistribute);
      if (status != null) {
        listDistributeApi = distributeApiRepository
            .findByIdInAndStatusAndIsDeletedFalse(SetIdsProjectDistribute, status);
      }

    }
    listDistributeToCampaign = listDistributeApi.stream()
        .map(distributeApiMapper::toDtoResponseToCampaignManage).collect(
            Collectors.toList());
    listDistributeToCampaign.stream().forEach(x -> x.setProjectId(projectId));
    return listDistributeToCampaign;
  }

  @Override
  public List<DistributeMethodPOSTResponseDto> getDistributeForLead(ApiMethod method) {
    List<DistributeMethodPOSTResponseDto> dtos = new ArrayList<>();
//    List<DistributeApi> distributeApis = distributeApiRepository.findByMethodAndIsDeletedFalse("POST");
//    for (int i = 1; i<10; i++){
//      DistributeMethodPOSTResponseDto dto = new DistributeMethodPOSTResponseDto();
//      dto.setId(i);
//      dto.setName("VPB_DISTRIBUTE_0".concat(String.valueOf(i)));
//      dtos.add(dto);
//    }
    List<DistributeMethodPOSTResponseDto> list = new ArrayList<>();
    List<DistributeApi> _dto = distributeApiRepository.
        findByMethodAndStatusAndIsDeletedFalse(method, DistributeApiStatus.ACTIVE);
    for (DistributeApi dto : _dto) {
      DistributeMethodPOSTResponseDto contribute = new DistributeMethodPOSTResponseDto();
      contribute.setId(dto.getId());
      contribute.setName(dto.getName());
      list.add(contribute);
    }
    return list;
  }
}
