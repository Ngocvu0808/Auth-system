package com.ttt.mar.notify.service;

import static org.mockito.Mockito.when;

import com.ttt.mar.notify.dto.template.VariableResponse;
import com.ttt.mar.notify.dto.template.VariablesRequestDto;
import com.ttt.mar.notify.entities.template.TemplateVariables;
import com.ttt.mar.notify.filter.TemplateVariableFilter;
import com.ttt.mar.notify.mapper.template.VariablesMapper;
import com.ttt.mar.notify.repositories.template.TemplateVariablesRepository;
import com.ttt.mar.notify.service.iface.UserService;
import com.ttt.mar.notify.service.impl.template.VariablesServiceImpl;
import com.ttt.rnd.common.dto.DataPagingResponse;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import com.ttt.rnd.lib.entities.User;
import com.ttt.rnd.lib.entities.UserStatus;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@RunWith(MockitoJUnitRunner.class)
public class VariablesServiceImplTest {

  @InjectMocks
  private VariablesServiceImpl variablesService;

  @Mock
  private TemplateVariablesRepository templateVariablesRepository;

  @Mock
  private UserService userService;

  @Mock
  private VariablesMapper variablesMapper;

  @Mock
  private TemplateVariableFilter templateVariableFilter;

  @Before
  public void setUp() throws OperationNotImplementException, ResourceNotFoundException {
    when(userService.checkValidUser(1)).thenAnswer(invocationOnMock -> {
      User user = new User();
      user.setName("admin");
      user.setStatus(UserStatus.ACTIVE);
      return user;
    });

    when(templateVariablesRepository.findByIdAndIsDeletedFalse(1)).thenAnswer(invocationOnMock -> {
      TemplateVariables templateVariables = new TemplateVariables();
      templateVariables.setId(1);
      templateVariables.setName("Name");
      templateVariables.setCode("Code");

      return templateVariables;
    });
  }

  @Test
  public void testCreateVariable()
      throws OperationNotImplementException, ResourceNotFoundException {
    VariablesRequestDto dto = new VariablesRequestDto();
    dto.setName("Name");
    dto.setCode("Code");

    TemplateVariables templateVariables = new TemplateVariables();
    when(variablesMapper.fromDto(dto)).thenAnswer(invocationOnMock -> {
      templateVariables.setName("Name");
      templateVariables.setCode("Code");
      return templateVariables;
    });

    when(templateVariablesRepository.save(templateVariables)).thenAnswer(invocationOnMock -> {
      templateVariables.setId(1);
      return templateVariables;
    });

    Integer id = variablesService.createVariable(1, dto);
    Assertions.assertEquals(1, id);
  }

  @Test
  public void deleteVariableTest()
      throws OperationNotImplementException, ResourceNotFoundException {
    Boolean result = variablesService.deleteVariable(1, 1);
    Assertions.assertEquals(result, true);
  }

  @Test
  public void getListVariableFilterTest() {
    String search = "search";
    Map<String, String> map = new HashMap<>();
    map.put("createdTime", "desc");

    Integer page = 1;
    Integer limit = 10;

    PageRequest pageRequest = PageRequest.of(page - 1, limit);

    when(templateVariablesRepository
        .findAll(templateVariableFilter.filter(search, map), pageRequest))
        .thenAnswer(invocationOnMock -> {
          List<TemplateVariables> templateVariablesList = new ArrayList<>();
          for (int i = 1; i <= 5; i++) {
            TemplateVariables templateVariables = new TemplateVariables();
            templateVariables.setName("Name_" + i);
            templateVariables.setCode("Code_" + i);
            templateVariables.setCreatedTime(new Date());
            templateVariables.setCreatorUserId(1);

            templateVariablesList.add(templateVariables);
          }
          return new PageImpl<>(templateVariablesList,
              pageRequest, templateVariablesList.size());
        });

    DataPagingResponse<VariableResponse> response = variablesService
        .getListVariableFilter(search, null, page, limit);
    Assertions.assertNotNull(response);
  }
}
