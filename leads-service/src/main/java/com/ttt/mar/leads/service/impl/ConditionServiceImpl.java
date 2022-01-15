package com.ttt.mar.leads.service.impl;

import com.ttt.mar.leads.dto.LeadConditionResponseDto;
import com.ttt.mar.leads.entities.Condition;
import com.ttt.mar.leads.mapper.ConditionMapper;
import com.ttt.mar.leads.repositories.ConditionRepository;
import com.ttt.mar.leads.service.iface.ConditionService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConditionServiceImpl implements ConditionService {

  @Autowired
  private ConditionRepository conditionRepository;

  @Autowired
  private ConditionMapper conditionMapper;

  @Override
  public List<LeadConditionResponseDto> getAllCondition() {
    List<LeadConditionResponseDto> conditions = new ArrayList<>();
    List<Condition> allCondition = conditionRepository.findAll();
    if (allCondition.size() > 0) {
      conditions = allCondition.stream()
          .filter(it -> !it.getIsDeleted())
          .map(conditionMapper::toDto).collect(Collectors.toList());
    }
    return conditions;
  }
}
