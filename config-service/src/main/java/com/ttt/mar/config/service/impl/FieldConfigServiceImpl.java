package com.ttt.mar.config.service.impl;

import com.ttt.mar.config.config.ServiceMessageCode;
import com.ttt.mar.config.dto.FieldConfigDto;
import com.ttt.mar.config.dto.FilterConfigDto;
import com.ttt.mar.config.entities.MarFieldConfig;
import com.ttt.mar.config.entities.MarFieldConfigType;
import com.ttt.mar.config.entities.MarFilterConfig;
import com.ttt.mar.config.filter.FieldConfigFilter;
import com.ttt.mar.config.filter.FilterConfigFilter;
import com.ttt.mar.config.mapper.FieldConfigMapper;
import com.ttt.mar.config.mapper.FilterConfigMapper;
import com.ttt.mar.config.repositories.MarFieldConfigRepository;
import com.ttt.mar.config.repositories.MarFilterConfigRepository;
import com.ttt.mar.config.service.iface.FieldConfigService;
import com.ttt.mar.config.service.iface.MarFieldConfigObjectService;
import com.ttt.rnd.common.config.ServiceInfo;
import com.ttt.rnd.common.dto.DataPagingResponse;
import com.ttt.rnd.common.exception.DuplicateEntityException;
import com.ttt.rnd.common.exception.IdentifyBlankException;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import com.ttt.rnd.common.utils.SortingUtils;
import com.ttt.rnd.lib.entities.User;
import com.ttt.rnd.lib.repositories.UserRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FieldConfigServiceImpl implements FieldConfigService {

  @Autowired
  private MarFieldConfigRepository fieldConfigRepository;


  @Autowired
  private UserRepository marUserRepository;

  @Autowired
  private FieldConfigMapper fieldConfigMapper;

  @Autowired
  private FilterConfigMapper filterConfigMapper;

  @Autowired
  private MarFilterConfigRepository filterConfigRepository;

  @Autowired
  private MarFieldConfigObjectService marFieldConfigObjectService;

  @Override
  public DataPagingResponse<FieldConfigDto> findAll(Integer page, Integer limit, String type,
      String search, String sort) {
    List<String> types = new ArrayList<>();
    if (type != null && !type.isEmpty()) {
      types = Arrays.asList(type.split(","));
    }
    Map<String, String> map = SortingUtils.detectSortType(sort);
    Page<MarFieldConfig> pageRepo = fieldConfigRepository
        .findAll(new FieldConfigFilter().getByFilter(types, search, map, false),
            PageRequest.of(page, limit));
    List<MarFieldConfig> list = pageRepo.getContent();
    DataPagingResponse<FieldConfigDto> dataPagingResponse = new DataPagingResponse<>();
    dataPagingResponse.setList(list.stream().map(config -> fieldConfigMapper.toDto(config))
        .collect(Collectors.toList()));
    dataPagingResponse.setNum(pageRepo.getTotalElements());
    dataPagingResponse.setTotalPage(pageRepo.getTotalPages());
    dataPagingResponse.setCurrentPage(page + 1);
    return dataPagingResponse;
  }

  @Override
  public FieldConfigDto findById(Integer id)
      throws IdentifyBlankException, ResourceNotFoundException {
    if (id == null) {
      throw new IdentifyBlankException("Id cannot be null",
          ServiceInfo.getId() + ServiceMessageCode.ID_NOT_NULL);
    }
    Optional<MarFieldConfig> optional = fieldConfigRepository.findById(id);
    if (optional.isEmpty()) {
      throw new ResourceNotFoundException("Field config invalid",
          ServiceInfo.getId() + ServiceMessageCode.FIELD_CONFIG_INVALID);
    }
    MarFieldConfig config = optional.get();
    return fieldConfigMapper.toDto(config);
  }

  @Override
  public FieldConfigDto update(FieldConfigDto dto, Integer userId)
      throws IdentifyBlankException, ResourceNotFoundException, DuplicateEntityException, OperationNotImplementException {
    if (dto.getId() == null) {
      throw new IdentifyBlankException("Id cannot be null",
          ServiceInfo.getId() + ServiceMessageCode.ID_NOT_NULL);
    }
    Optional<User> userOptional = marUserRepository.findById(userId);
    if (userOptional.isEmpty()) {
      throw new ResourceNotFoundException("User invalid",
          ServiceInfo.getId() + ServiceMessageCode.USER_INVALID);
    }
    Optional<MarFieldConfig> optional = fieldConfigRepository.findById(dto.getId());
    if (optional.isEmpty()) {
      throw new ResourceNotFoundException("Field config invalid",
          ServiceInfo.getId() + ServiceMessageCode.FIELD_CONFIG_INVALID);
    }
    MarFieldConfig config = optional.get();
    if (config.getIsDeleted().equals(Boolean.TRUE)) {
      throw new OperationNotImplementException("Field config has been deleted",
          ServiceInfo.getId() + ServiceMessageCode.FIELD_CONFIG_DELETED);
    }
    if (!dto.getKey().equals(config.getKey())) {
      Optional<MarFieldConfig> configOptional = fieldConfigRepository
          .findActiveConfigByKey(dto.getKey());
      if (configOptional.isPresent()) {
        throw new DuplicateEntityException("Config with key " + dto.getKey() + " already exist",
            ServiceInfo.getId() + ServiceMessageCode.FIELD_CONFIG_DUPLICATE);
      }
    }
    String object = dto.getObject();
    if (StringUtils.isNotBlank(object)) {
      if (!marFieldConfigObjectService.checkExistByCode(object)) {
        throw new ResourceNotFoundException("Object not exist",
            ServiceInfo.getId() + ServiceMessageCode.OBJECT_NOT_FOUND);
      }
    }
    config.setUpdaterUser(userOptional.get());
    fieldConfigMapper.updateModel(config, dto);
    MarFieldConfig res = fieldConfigRepository.save(config);
    return fieldConfigMapper.toDto(res);
  }

  @Override
  public void delete(Integer id, Integer userId)
      throws IdentifyBlankException, ResourceNotFoundException, OperationNotImplementException {
    if (id == null) {
      throw new IdentifyBlankException("Id cannot be null",
          ServiceInfo.getId() + ServiceMessageCode.ID_NOT_NULL);
    }
    if (userId == null) {
      throw new IdentifyBlankException("User id cannot be null",
          ServiceInfo.getId() + ServiceMessageCode.USER_ID_NOT_NULL);
    }
    Optional<User> userOptional = marUserRepository.findById(userId);
    if (userOptional.isEmpty()) {
      throw new ResourceNotFoundException("User with id %d not existed",
          ServiceInfo.getId() + ServiceMessageCode.USER_INVALID);
    }
    Optional<MarFieldConfig> optional = fieldConfigRepository.findById(id);
    if (optional.isEmpty()) {
      throw new ResourceNotFoundException("Field with id %d not existed",
          ServiceInfo.getId() + ServiceMessageCode.FIELD_CONFIG_INVALID);
    }
    MarFieldConfig config = optional.get();
    if (config.getIsDeleted().equals(Boolean.TRUE)) {
      throw new OperationNotImplementException("Field config has been deleted",
          ServiceInfo.getId() + ServiceMessageCode.FIELD_CONFIG_DELETED);
    }
    config.setCreatorUser(userOptional.get());
    config.setIsDeleted(true);
    fieldConfigRepository.save(config);
  }

  @Override
  public FieldConfigDto addConfig(FieldConfigDto dto, Integer userId)
      throws ResourceNotFoundException, DuplicateEntityException {
    Optional<User> userOptional = marUserRepository.findById(userId);
    if (userOptional.isEmpty()) {
      throw new ResourceNotFoundException("User with id %d not existed",
          ServiceInfo.getId() + ServiceMessageCode.USER_INVALID);
    }
    Optional<MarFieldConfig> configOptional = fieldConfigRepository
        .findActiveConfigByKey(dto.getKey());
    if (configOptional.isPresent()) {
      throw new DuplicateEntityException("Config with key " + dto.getKey() + " already exist",
          ServiceInfo.getId() + ServiceMessageCode.FIELD_CONFIG_DUPLICATE);
    }
    String object = dto.getObject();
    if (StringUtils.isNotBlank(object)) {
      if (!marFieldConfigObjectService.checkExistByCode(object)) {
        throw new ResourceNotFoundException("Object not exist",
            ServiceInfo.getId() + ServiceMessageCode.OBJECT_NOT_FOUND);
      }
    }
    MarFieldConfig fieldConfig = fieldConfigMapper.fromDto(dto);
    fieldConfig.setCreatorUser(userOptional.get());
    fieldConfig.setCreatedTime(new Date());
    MarFieldConfig config = fieldConfigRepository.save(fieldConfig);
    return fieldConfigMapper.toDto(config);
  }

  @Override
  public List<FieldConfigDto> getAllByType(String type) {
    List<MarFieldConfig> marFieldConfigs = fieldConfigRepository
        .findAllByTypeAndIsDeletedFalse(MarFieldConfigType.valueOf(type));
    List<FieldConfigDto> dtos = marFieldConfigs.stream()
        .map(marFieldConfig -> fieldConfigMapper.toDto(marFieldConfig)).collect(
            Collectors.toList());
    return dtos;
  }

  @Override
  public List<FilterConfigDto> getFilter(String service, String type) {
    List<MarFilterConfig> filterConfigs = filterConfigRepository
        .findAll(new FilterConfigFilter().getByFilter(service, type, false));
    List<FilterConfigDto> dtos = filterConfigs.stream().map(f -> filterConfigMapper.toDto(f))
        .collect(Collectors.toList());
    return dtos;
  }

  @Override
  public List<FieldConfigDto> getAllByTypeAndObject(String type, String objects) {
    List<MarFieldConfig> marFieldConfigs = fieldConfigRepository
        .findAll(new FieldConfigFilter().getByFilter(type, objects));
    return marFieldConfigs.stream().map(fieldConfigMapper::toDto).collect(Collectors.toList());
  }
}
