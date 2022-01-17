package com.ttt.mar.config.service.impl;

import com.ttt.mar.config.dto.MarFieldConfigObjectDto;
import com.ttt.mar.config.entities.MarFieldConfigObject;
import com.ttt.mar.config.mapper.MarFieldConfigObjectMapper;
import com.ttt.mar.config.repositories.MarFieldConfigObjectRepository;
import com.ttt.mar.config.service.iface.MarFieldConfigObjectService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MarFieldConfigObjectServiceImpl implements MarFieldConfigObjectService {

  @Autowired
  private MarFieldConfigObjectRepository marFieldConfigObjectRepository;
  @Autowired
  private MarFieldConfigObjectMapper marFieldConfigObjectMapper;

  public Boolean checkExistByCode(String code) {
    MarFieldConfigObject marFieldConfigObject = marFieldConfigObjectRepository
        .findByCode(code);
    if (marFieldConfigObject == null) {
      return false;
    }
    return true;
  }

  public List<MarFieldConfigObjectDto> findAll() {
    List<MarFieldConfigObject> marFieldConfigObjects = marFieldConfigObjectRepository.findAll();
    return marFieldConfigObjects.stream().map(entity -> marFieldConfigObjectMapper.toDto(entity))
        .collect(Collectors.toList());
  }
}
