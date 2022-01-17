package com.ttt.mar.notify.service;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.ttt.mar.notify.dto.template.TemplateSemanticResponse;
import com.ttt.mar.notify.entities.template.TemplateSemantic;
import com.ttt.mar.notify.entities.template.TemplateSemanticType;
import com.ttt.mar.notify.mapper.template.TemplateSemanticMapper;
import com.ttt.mar.notify.repositories.template.TemplateSemanticRepository;
import com.ttt.mar.notify.service.impl.template.TemplateSemanticServiceImpl;
import com.ttt.rnd.common.exception.ResourceNotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class TemplateSemanticServiceImplTest {

  @Mock
  private TemplateSemanticRepository templateSemanticRepository;

  @InjectMocks
  private TemplateSemanticServiceImpl templateSemanticService;

  @Mock
  private TemplateSemanticMapper templateSemanticMapper;

  @Test
  public void getTemplateSemanticByType() throws ResourceNotFoundException {
    when(templateSemanticRepository.getListTemplateSemantic(TemplateSemanticType.HEADER))
        .thenAnswer(invocationOnMock -> {
          List<TemplateSemantic> lstTemplateSemantic = new ArrayList<>();
          for (int i = 1; i <= 5; i++) {
            TemplateSemantic templateSemantic = new TemplateSemantic();
            templateSemantic.setName("Name_" + i);
            templateSemantic.setCode("Code_" + i);
            templateSemantic.setCreatedTime(new Date());
            templateSemantic.setCreatorUserId(1);
            templateSemantic.setType(TemplateSemanticType.HEADER);
            lstTemplateSemantic.add(templateSemantic);
          }
          return lstTemplateSemantic;
        });
    List<TemplateSemanticResponse> empList = templateSemanticService
        .getTemplateSemanticByType(TemplateSemanticType.HEADER);

    assertTrue(empList.size() > 0);
  }

}
