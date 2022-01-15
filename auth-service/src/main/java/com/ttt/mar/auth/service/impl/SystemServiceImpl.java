package com.ttt.mar.auth.service.impl;

import com.ttt.mar.auth.dto.appservice.SystemCustomDto;
import com.ttt.mar.auth.entities.service.System;
import com.ttt.mar.auth.mapper.SystemMapper;
import com.ttt.mar.auth.repositories.SystemRepository;
import com.ttt.mar.auth.service.iface.SystemService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SystemServiceImpl implements SystemService {

  @Autowired
  private SystemRepository systemRepository;

  @Autowired
  private SystemMapper systemMapper;

  @Override
  public List<SystemCustomDto> getSystems() {
    List<System> systems = systemRepository.findAll();
    List<SystemCustomDto> systemCustomDtos = new ArrayList<>();
    systems.forEach(system -> systemCustomDtos.add(systemMapper.toDto(system)));
    return systemCustomDtos;
  }
}
