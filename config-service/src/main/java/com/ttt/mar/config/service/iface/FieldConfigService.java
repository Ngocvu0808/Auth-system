package com.ttt.mar.config.service.iface;

import com.ttt.mar.config.dto.FieldConfigDto;
import com.ttt.mar.config.dto.FilterConfigDto;
import com.ttt.rnd.common.dto.DataPagingResponse;
import com.ttt.rnd.common.exception.DuplicateEntityException;
import com.ttt.rnd.common.exception.IdentifyBlankException;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import java.util.List;

public interface FieldConfigService {

  DataPagingResponse<FieldConfigDto> findAll(Integer page, Integer limit, String type,
      String search, String sort);

  FieldConfigDto findById(Integer id) throws IdentifyBlankException, ResourceNotFoundException;

  FieldConfigDto update(FieldConfigDto dto, Integer userId)
      throws IdentifyBlankException, ResourceNotFoundException, DuplicateEntityException, OperationNotImplementException;

  void delete(Integer id, Integer userId)
      throws IdentifyBlankException, ResourceNotFoundException, OperationNotImplementException;

  FieldConfigDto addConfig(FieldConfigDto dto, Integer userId)
      throws ResourceNotFoundException, DuplicateEntityException;

  List<FieldConfigDto> getAllByType(String type);

  List<FilterConfigDto> getFilter(String service, String type);

  List<FieldConfigDto> getAllByTypeAndObject(String type, String object);
}
