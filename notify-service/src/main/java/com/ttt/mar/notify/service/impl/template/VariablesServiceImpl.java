package com.ttt.mar.notify.service.impl.template;


import com.ttt.mar.notify.config.ServiceMessageCode;
import com.ttt.mar.notify.dto.template.TemplateVariableResponseForTemplate;
import com.ttt.mar.notify.dto.template.UserResponseDto;
import com.ttt.mar.notify.dto.template.VariableResponse;
import com.ttt.mar.notify.dto.template.VariablesRequestDto;
import com.ttt.mar.notify.entities.template.TemplateVariables;
import com.ttt.mar.notify.filter.TemplateVariableFilter;
import com.ttt.mar.notify.mapper.template.VariablesMapper;
import com.ttt.mar.notify.repositories.template.TemplateVariablesRepository;
import com.ttt.mar.notify.service.iface.UserService;
import com.ttt.mar.notify.service.iface.template.VariablesService;
import com.ttt.rnd.common.config.ServiceInfo;
import com.ttt.rnd.common.dto.DataPagingResponse;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import com.ttt.rnd.common.utils.SortingUtils;
import com.ttt.rnd.lib.entities.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * @author KietDT
 * @created_date 05/05/2021
 */
@Service
public class VariablesServiceImpl implements VariablesService {

  private static final Logger logger = LoggerFactory.getLogger(VariablesServiceImpl.class);

  private final UserService userService;
  private final TemplateVariablesRepository templateVariablesRepository;
  private final VariablesMapper variablesMapper;
  private final TemplateVariableFilter templateVariablesFilter;

  public VariablesServiceImpl(UserService userService,
      TemplateVariablesRepository templateVariablesRepository,
      VariablesMapper variablesMapper, TemplateVariableFilter templateVariablesFilter) {
    this.userService = userService;
    this.templateVariablesRepository = templateVariablesRepository;
    this.variablesMapper = variablesMapper;
    this.templateVariablesFilter = templateVariablesFilter;
  }

  /**
   * @param userId ID of User
   * @param dto    RequestBody them moi Bien
   * @return ID of Variables
   * @throws OperationNotImplementException
   * @throws ResourceNotFoundException
   * @apiNote API them moi bien
   */
  @Override
  public Integer createVariable(Integer userId, VariablesRequestDto dto)
      throws OperationNotImplementException, ResourceNotFoundException {
    logger.info("--- createVariable() ---");

    User user = userService.checkValidUser(userId);
    String code = dto.getCode();
    List<TemplateVariables> templateVariablesList = templateVariablesRepository
        .findByCodeAndIsDeletedFalse(code);
    if (templateVariablesList != null && !templateVariablesList.isEmpty()) {
      throw new OperationNotImplementException("Variables Code is exist",
          ServiceInfo.getId() + ServiceMessageCode.VARIABLES_CODE_EXIST);
    }
    TemplateVariables templateVariables = variablesMapper.fromDto(dto);
    templateVariables.setCreatorUserId(userId);
    templateVariablesRepository.save(templateVariables);
    return templateVariables.getId();
  }

  /**
   * apiNote API xoa bien
   *
   * @param userId     ID of User
   * @param variableId ID of Variable
   * @return
   * @throws OperationNotImplementException
   * @throws ResourceNotFoundException
   */
  @Transactional
  @Override
  public Boolean deleteVariable(Integer userId, Integer variableId)
      throws OperationNotImplementException, ResourceNotFoundException {
    logger.info("--- deleteVariable() ---");

    userService.checkValidUser(userId);
    TemplateVariables templateVariables = templateVariablesRepository
        .findByIdAndIsDeletedFalse(variableId);
    if (templateVariables == null) {
      throw new OperationNotImplementException("Variables not found",
          ServiceInfo.getId() + ServiceMessageCode.VARIABLES_NOT_FOUND);
    }
    templateVariables.setIsDeleted(true);
    templateVariables.setDeleterUserId(userId);
    templateVariablesRepository.save(templateVariables);

    return true;
  }

  /**
   * @param search is text search
   * @param sort   is type of sort
   * @param page   is page
   * @param limit  is limit
   * @return data paging response
   */
  @Override
  public DataPagingResponse<VariableResponse> getListVariableFilter(String search,
      String sort, Integer page, Integer limit) {
    logger.info("---getListVariableFilter()---");
    Map<String, String> map = SortingUtils.detectSortType(sort);
    if (map.isEmpty()) {
      map.put("createdTime", "desc");
    }
    Page<TemplateVariables> variablePage = templateVariablesRepository
        .findAll(templateVariablesFilter.filter(search, map), PageRequest.of(page - 1, limit));
    List<VariableResponse> listVariableResponse = mappingToVariableReponseDTO(
        variablePage.getContent());

    DataPagingResponse<VariableResponse> dataPagingResponses = new DataPagingResponse<>();
    dataPagingResponses.setList(listVariableResponse);
    dataPagingResponses.setTotalPage(variablePage.getTotalPages());
    dataPagingResponses.setNum(variablePage.getTotalElements());
    dataPagingResponses.setCurrentPage(page);
    return dataPagingResponses;
  }

  /**
   * API get all variables for template
   *
   * @return list VariableResponse
   */
  @Override
  public List<TemplateVariableResponseForTemplate> getAllVariableForTemplate() {
    List<TemplateVariables> listTemplateVariable = templateVariablesRepository
        .findAllAndIsDeletedFalse();
    List<TemplateVariableResponseForTemplate> listDataResponseTemplate = new ArrayList<>();
    if (listTemplateVariable != null && !listTemplateVariable.isEmpty()) {
      listDataResponseTemplate = mappingToVariableReponseForTemplate(listTemplateVariable);
    }
    return listDataResponseTemplate;
  }

  public List<VariableResponse> mappingToVariableReponseDTO(
      List<TemplateVariables> listTemplateVariable) {

    logger.info("--- mappingToVariableReponseDTO() ---");
    List<VariableResponse> variableResponse = new ArrayList<>();
    if (listTemplateVariable == null || listTemplateVariable.isEmpty()) {
      return variableResponse;
    }
    Set<Integer> userIdSet = listTemplateVariable.stream().map(TemplateVariables::getCreatorUserId)
        .filter(Objects::nonNull).collect(Collectors.toSet());
    Map<Integer, UserResponseDto> userResponseDtoMap = new HashMap<>();
    if (!userIdSet.isEmpty()) {
      userResponseDtoMap = userService.mapByListId(userIdSet);
    }
    for (TemplateVariables templateVariable : listTemplateVariable) {
      VariableResponse newVariableRespose = variablesMapper.toDto(templateVariable);
      //get User info
      Integer creatorId = templateVariable.getCreatorUserId();
      if (creatorId != null && userResponseDtoMap.containsKey(creatorId)) {
        UserResponseDto userResponseDTO = userResponseDtoMap.get(creatorId);
        newVariableRespose.setUserName(userResponseDTO.getUsername());
      }
      variableResponse.add(newVariableRespose);
    }
    return variableResponse;
  }

  public List<TemplateVariableResponseForTemplate> mappingToVariableReponseForTemplate(
      List<TemplateVariables> listTemplateVariable) {

    logger.info("--- mappingToVariableReponseDTO() ---");
    List<TemplateVariableResponseForTemplate> variableResponseTemplate = new ArrayList<>();
    if (listTemplateVariable == null || listTemplateVariable.isEmpty()) {
      return variableResponseTemplate;
    }
    for (TemplateVariables templateVariable : listTemplateVariable) {
      TemplateVariableResponseForTemplate newVariableRespose = variablesMapper
          .toVariableForTemplate(templateVariable);
      variableResponseTemplate.add(newVariableRespose);
    }
    return variableResponseTemplate;
  }


}
