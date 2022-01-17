package com.ttt.mar.config.service.impl;

import com.ttt.mar.config.config.ServiceMessageCode;
import com.ttt.mar.config.dto.ConfigCustomDto;
import com.ttt.mar.config.dto.ConfigDto;
import com.ttt.mar.config.entities.MarConfig;
import com.ttt.mar.config.filter.ConfigFilter;
import com.ttt.mar.config.mapper.ConfigMapper;
import com.ttt.mar.config.repositories.MarConfigRepository;
import com.ttt.mar.config.service.iface.ConfigService;
import com.ttt.rnd.common.config.ServiceInfo;
import com.ttt.rnd.common.dto.DataPagingResponse;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ConfigServiceImpl implements ConfigService {

  private static final Logger log = LoggerFactory.getLogger(ConfigService.class);

  private final MarConfigRepository marConfigRepository;
  private final UserRepository marUserRepository;
  private final ConfigMapper configMapper;

  public ConfigServiceImpl(MarConfigRepository marConfigRepository,
      UserRepository marUserRepository, ConfigMapper configMapper) {
    this.marConfigRepository = marConfigRepository;
    this.marUserRepository = marUserRepository;
    this.configMapper = configMapper;
  }

  @Override
  public DataPagingResponse<ConfigDto> findAll(Integer page, Integer limit, String key,
      String search, String sort) {
    List<String> keys = new ArrayList<>();
    if (key != null && !key.isEmpty()) {
      keys = Arrays.asList(key.split(","));
    }
    Map<String, String> map = SortingUtils.detectSortType(sort);
    Page<MarConfig> pageRepo = marConfigRepository
        .findAll(new ConfigFilter().getByFilter(keys, search, map, false),
            PageRequest.of(page, limit));
    List<MarConfig> list = pageRepo.getContent();
    List<ConfigDto> data = list.stream().map(configMapper::toDto).collect(
        Collectors.toList());
    DataPagingResponse<ConfigDto> dataPagingResponse = new DataPagingResponse<>();
    dataPagingResponse.setList(data);
    dataPagingResponse.setNum(pageRepo.getTotalElements());
    dataPagingResponse.setTotalPage(pageRepo.getTotalPages());
    dataPagingResponse.setCurrentPage(page + 1);
    return dataPagingResponse;
  }

  @Override
  public ConfigDto findById(Integer id) throws ResourceNotFoundException, IdentifyBlankException {
    if (id == null) {
      throw new IdentifyBlankException("Id cannot be null",
          ServiceInfo.getId() + ServiceMessageCode.ID_NOT_NULL);
    }
    Optional<MarConfig> optional = marConfigRepository.findById(id);
    if (optional.isEmpty()) {
      throw new ResourceNotFoundException("Config does not exist",
          ServiceInfo.getId() + ServiceMessageCode.CONFIG_INVALID);
    }
    MarConfig config = optional.get();
    return configMapper.toDto(config);
  }

  @Override
  public ConfigDto update(ConfigDto dto, Integer userId)
      throws ResourceNotFoundException, IdentifyBlankException {
    if (dto.getId() == null) {
      throw new IdentifyBlankException("Id cannot be null",
          ServiceInfo.getId() + ServiceMessageCode.ID_NOT_NULL);
    }
    Optional<User> userOptional = marUserRepository.findById(userId);
    if (userOptional.isEmpty() || userOptional.get().getIsDeleted().equals(Boolean.TRUE)) {
      throw new ResourceNotFoundException("User invalid",
          ServiceInfo.getId() + ServiceMessageCode.USER_INVALID);
    }
    Optional<MarConfig> optional = marConfigRepository.findById(dto.getId());
    if (optional.isEmpty() || optional.get().getIsDeleted().equals(Boolean.TRUE)) {
      throw new ResourceNotFoundException("Config does not exist",
          ServiceInfo.getId() + ServiceMessageCode.CONFIG_INVALID);
    }
    MarConfig config = optional.get();
//    if (!dto.getKey().equals(config.getKey())) {
//      Optional<MarConfig> configOptional = marConfigRepository.findActiveConfigByKey(dto.getKey());
//      if (configOptional.isPresent()) {
//        throw new DuplicateEntityException("Config with key " + dto.getKey() + " already exist");
//      }
//    }
    configMapper.updateModel(config, dto);
    config.setUpdaterUser(userOptional.get());
    MarConfig marConfig = marConfigRepository.save(config);

    return configMapper.toDto(marConfig);
  }

  @Override
  public void delete(Integer id, Integer userId)
      throws ResourceNotFoundException, OperationNotImplementException, IdentifyBlankException {
    if (id == null) {
      throw new IdentifyBlankException("Id cannot be null",
          ServiceInfo.getId() + ServiceMessageCode.ID_NOT_NULL);
    }
    Optional<MarConfig> optional = marConfigRepository.findById(id);
    if (optional.isEmpty()) {
      throw new ResourceNotFoundException("Config does not exist",
          ServiceInfo.getId() + ServiceMessageCode.CONFIG_INVALID);
    }
    Optional<User> userOptional = marUserRepository.findById(userId);
    if (userOptional.isEmpty() || userOptional.get().getIsDeleted().equals(Boolean.TRUE)) {
      throw new ResourceNotFoundException("User invalid",
          ServiceInfo.getId() + ServiceMessageCode.USER_INVALID);
    }
    MarConfig config = optional.get();
    if (config.getIsDeleted().equals(Boolean.TRUE)) {
      throw new OperationNotImplementException("Config has been deleted",
          ServiceInfo.getId() + ServiceMessageCode.CONFIG_DELETED);
    }
    config.setDeleterUser(userOptional.get());
    config.setIsDeleted(Boolean.TRUE);
    marConfigRepository.save(config);
  }

  @Override
  public ConfigDto addConfig(ConfigDto dto, Integer userId) throws ResourceNotFoundException {
    Optional<User> userOptional = marUserRepository.findById(userId);
    if (userOptional.isEmpty() || userOptional.get().getIsDeleted().equals(Boolean.TRUE)) {
      throw new ResourceNotFoundException("User invalid",
          ServiceInfo.getId() + ServiceMessageCode.USER_INVALID);
    }
//    MarConfig c = configMapper.fromDto(dto);
    MarConfig c = new MarConfig();
    c.setKey(dto.getKey());
    c.setValue(dto.getValue());
    c.setNote(dto.getNote());
    c.setCreatorUser(userOptional.get());
    c.setCreatedTime(new Date());
    MarConfig config = marConfigRepository.save(c);
    return configMapper.toDto(config);
  }

  @Override
  public List<String> findAllKey() {
    return marConfigRepository.findDistinctKey();
  }

  @Override
  public List<ConfigCustomDto> findAllByKey(String key) {
    List<MarConfig> configList = marConfigRepository.findAllByKeyAndIsDeletedFalse(key);
    return configList.stream().map(configMapper::toCustomDto).collect(Collectors.toList());
  }
}
