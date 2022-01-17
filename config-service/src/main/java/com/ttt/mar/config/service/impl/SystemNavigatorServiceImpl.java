package com.ttt.mar.config.service.impl;

import com.ttt.mar.config.controller.SystemNavigatorController;
import com.ttt.mar.config.dto.SystemNavigatorResponse;
import com.ttt.mar.config.entities.SystemNavigator;
import com.ttt.mar.config.mapper.SystemNavigatorMapper;
import com.ttt.mar.config.repositories.SystemNavigatorRepository;
import com.ttt.mar.config.service.iface.SystemNavigatorService;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SystemNavigatorServiceImpl implements SystemNavigatorService {

  private static final Logger logger = LoggerFactory.getLogger(SystemNavigatorController.class);

  @Autowired
  private SystemNavigatorRepository systemNavigatorRepository;
  @Autowired
  private SystemNavigatorMapper systemNavigatorMapper;

  SystemNavigatorServiceImpl(SystemNavigatorRepository systemNavigatorRepository,
      SystemNavigatorMapper systemNavigatorMapper) {
    this.systemNavigatorRepository = systemNavigatorRepository;
    this.systemNavigatorMapper = systemNavigatorMapper;
  }

  @Override
  public List<SystemNavigatorResponse> getAll() {
    List<SystemNavigator> systemNavigators = systemNavigatorRepository.findAllByIsDeletedFalse();
    logger.info("Size: {}", systemNavigators.size());
    return systemNavigators.stream()
        .map(entity -> systemNavigatorMapper.toDto(entity))
        .sorted(Comparator.comparing(SystemNavigatorResponse::getIndex))
        .collect(Collectors.toList());
  }
}
