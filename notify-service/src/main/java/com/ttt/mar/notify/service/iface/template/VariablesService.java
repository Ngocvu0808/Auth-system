package com.ttt.mar.notify.service.iface.template;


import com.ttt.mar.notify.dto.template.TemplateVariableResponseForTemplate;
import com.ttt.mar.notify.dto.template.VariableResponse;
import com.ttt.mar.notify.dto.template.VariablesRequestDto;
import com.ttt.rnd.common.dto.DataPagingResponse;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import java.util.List;

public interface VariablesService {

  Integer createVariable(Integer userId, VariablesRequestDto dto)
      throws OperationNotImplementException, ResourceNotFoundException;

  Boolean deleteVariable(Integer userId, Integer iD)
      throws OperationNotImplementException, ResourceNotFoundException;

  DataPagingResponse<VariableResponse> getListVariableFilter(String search, String sort,
      Integer page, Integer limit);

  List<TemplateVariableResponseForTemplate> getAllVariableForTemplate();
}
