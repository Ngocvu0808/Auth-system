package com.ttt.mar.notify.service;

import static org.mockito.Mockito.when;

import com.ttt.mar.notify.dto.template.TemplateRequestDto;
import com.ttt.mar.notify.dto.template.TemplateResponseDetailDto;
import com.ttt.mar.notify.entities.template.Template;
import com.ttt.mar.notify.entities.template.TemplateType;
import com.ttt.mar.notify.mapper.template.TemplateMapper;
import com.ttt.mar.notify.repositories.template.TemplateAttachmentRepository;
import com.ttt.mar.notify.repositories.template.TemplateRepository;
import com.ttt.mar.notify.repositories.template.TemplateSemanticDetailRepository;
import com.ttt.mar.notify.repositories.template.TemplateTypeRepository;
import com.ttt.mar.notify.service.iface.UserService;
import com.ttt.mar.notify.service.impl.template.TemplateServiceImpl;
import com.ttt.rnd.common.exception.IdentifyBlankException;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import com.ttt.rnd.lib.entities.User;
import com.ttt.rnd.lib.entities.UserStatus;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TemplateServiceImplTest {

  @InjectMocks
  private TemplateServiceImpl templateService;

  @Mock
  private UserService userService;

  @Mock
  private TemplateRepository templateRepository;

  @Mock
  private TemplateMapper templateMapper;

  @Mock
  private TemplateTypeRepository templateTypeRepository;

  @Mock
  private TemplateAttachmentRepository templateAttachmentRepository;

  @Mock
  private TemplateSemanticDetailRepository templateSemanticDetailRepository;

  @Spy
  private Template template = new Template();

  @Before
  public void setUp() throws OperationNotImplementException, ResourceNotFoundException {

    template.setId(1);
    template.setName("Name");
    template.setCode("Code");
    template.setType("SMS");
    template.setContent("Chuc mung nam moi !!!");
    template.setCreatedTime(new Date());

    when(templateRepository.findById(1)).thenAnswer(invocationOnMock ->
        Optional.of(template)
    );

    when(userService.checkValidUser(1)).thenAnswer(invocationOnMock -> {
      User user = new User();
      user.setName("admin");
      user.setStatus(UserStatus.ACTIVE);
      return user;
    });
  }

  @Test
  public void createTemplateTest()
      throws OperationNotImplementException, ResourceNotFoundException, IdentifyBlankException {
    TemplateRequestDto dto = new TemplateRequestDto();
    dto.setName("Name");
    dto.setCode("Code");
    dto.setContent("Chuc mung nam moi !!!");
    dto.setType("SMS");

    Template template = new Template();
    template.setName("Name");
    template.setCode("Code");
    template.setContent("Chuc mung nam moi !!!");
    template.setType("SMS");

    when(templateMapper.fromDto(dto)).thenReturn(template);
    when(templateTypeRepository.findByCode("SMS")).thenAnswer(invocationOnMock -> {
      TemplateType templateType = new TemplateType();
      templateType.setCode("SMS");
      templateType.setId(1);
      templateType.setName("Sms");

      return templateType;
    });

    when(templateRepository.save(template)).thenAnswer(invocationOnMock -> {
      template.setId(1);
      return template;
    });

    Integer id = templateService.createTemplate(1, dto);
    Assertions.assertEquals(1, id);
  }

  @Test
  public void getTemplateById() throws ResourceNotFoundException {

    when(templateSemanticDetailRepository.getTemplateSemanticDetailByTemplateId(1))
        .thenReturn(new ArrayList<>());

    when(templateMapper.toResponseDetail(template)).thenAnswer(invocationOnMock -> {
      TemplateResponseDetailDto templateResponseDetailDto = new TemplateResponseDetailDto();
      templateResponseDetailDto.setId(1);
      templateResponseDetailDto.setName("Name");
      templateResponseDetailDto.setCode("Code");
      templateResponseDetailDto.setContent("Chuc mung nam moi !!!");
      templateResponseDetailDto.setCreatedTime(new Date());
      templateResponseDetailDto.setUserName("admin");

      return templateResponseDetailDto;
    });

    TemplateResponseDetailDto responseDetailDto = templateService.getTemplateById(1);
    Assertions.assertNotNull(responseDetailDto);
  }

  @Test
  public void deleteTemplateTest()
      throws OperationNotImplementException, ResourceNotFoundException {
    Boolean result = templateService.deleteTemplate(1, 1);
    Assertions.assertEquals(result, true);
  }
}
