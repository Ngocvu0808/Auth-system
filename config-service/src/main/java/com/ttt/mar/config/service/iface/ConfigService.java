package com.ttt.mar.config.service.iface;

import com.ttt.mar.config.dto.ConfigCustomDto;
import com.ttt.mar.config.dto.ConfigDto;
import com.ttt.rnd.common.dto.DataPagingResponse;
import com.ttt.rnd.common.exception.DuplicateEntityException;
import com.ttt.rnd.common.exception.IdentifyBlankException;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import java.util.List;

public interface ConfigService {

  DataPagingResponse<ConfigDto> findAll(Integer page, Integer limit, String key, String search,
      String sort);

  ConfigDto findById(Integer id) throws ResourceNotFoundException, IdentifyBlankException;

  ConfigDto update(ConfigDto dto, Integer userId)
      throws IdentifyBlankException, ResourceNotFoundException;

  void delete(Integer id, Integer userId)
      throws ResourceNotFoundException, OperationNotImplementException, IdentifyBlankException;

  ConfigDto addConfig(ConfigDto dto, Integer userId) throws ResourceNotFoundException;

  List<String> findAllKey();

  List<ConfigCustomDto> findAllByKey(String key);
}
