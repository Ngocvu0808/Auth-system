package com.ttt.mar.notify.service.iface.template;


import com.ttt.mar.notify.dto.template.TemplateRequestDto;
import com.ttt.mar.notify.dto.template.TemplateResponseDetailDto;
import com.ttt.mar.notify.dto.template.TemplateResponseDto;
import com.ttt.mar.notify.dto.template.TemplateUpdateRequestDto;
import com.ttt.rnd.common.dto.DataPagingResponse;
import com.ttt.rnd.common.exception.IdentifyBlankException;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;

public interface TemplateService {

  Integer createTemplate(Integer userId, TemplateRequestDto dto)
      throws OperationNotImplementException, ResourceNotFoundException, IdentifyBlankException;

  DataPagingResponse<TemplateResponseDto> getTemplates(String search, Long fromDate, Long toDate,
      String type, String sort, Integer page, Integer limit)
      throws ResourceNotFoundException, IdentifyBlankException;

  TemplateResponseDetailDto getTemplateById(Integer id) throws ResourceNotFoundException;

  Integer updateTemplate(Integer userId, TemplateUpdateRequestDto dto)
      throws OperationNotImplementException, ResourceNotFoundException;

  boolean deleteTemplate(Integer userId, Integer tempId)
      throws OperationNotImplementException, ResourceNotFoundException;
}
