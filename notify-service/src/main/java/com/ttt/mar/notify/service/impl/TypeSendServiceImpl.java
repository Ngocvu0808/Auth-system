package com.ttt.mar.notify.service.impl;

import com.ttt.mar.notify.dto.typesend.TypeSendResponseDto;
import com.ttt.mar.notify.entities.TypeSend;
import com.ttt.mar.notify.mapper.TypeSendMapper;
import com.ttt.mar.notify.repositories.TypeSendRepository;
import com.ttt.mar.notify.service.iface.TypeSendService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TypeSendServiceImpl implements TypeSendService {


  @Autowired
  private TypeSendRepository typeSendRepository;

  @Autowired
  private TypeSendMapper typeSendMapper;

  @Override
  public List<TypeSendResponseDto> getAll() {

    List<TypeSendResponseDto> typeSendResponseDtos = new ArrayList<>();
    List<TypeSend> typeSends = typeSendRepository.findAll();
    typeSends.forEach(typeSend -> typeSendResponseDtos.add(typeSendMapper.toDto(typeSend)));
    return typeSendResponseDtos;
  }
}
