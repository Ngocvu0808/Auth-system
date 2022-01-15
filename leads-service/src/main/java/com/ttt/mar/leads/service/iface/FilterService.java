package com.ttt.mar.leads.service.iface;

import com.ttt.mar.leads.dto.*;
import com.ttt.rnd.common.dto.DataPagingResponse;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.List;

public interface FilterService {

  Integer createFilter(Integer userId, FilterRequestDto dto)
      throws OperationNotImplementException, ResourceNotFoundException;

  Integer updateFilter(Integer userId, FilterUpdateRequestDto dto)
      throws ResourceNotFoundException, OperationNotImplementException, ParseException;

  FilterResponseDto getByID(Integer id)
      throws ResourceNotFoundException, OperationNotImplementException;

  @Transactional
  Boolean deleteFilter(Integer userId, Integer id)
      throws ResourceNotFoundException, OperationNotImplementException;


  DataPagingResponse<FilterResponse> getListFilter(String search,
      String sort, Integer page, Integer limit, Long startDate, Long endDate);
}
