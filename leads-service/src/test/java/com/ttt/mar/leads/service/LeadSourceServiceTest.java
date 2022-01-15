package com.ttt.mar.leads.service;

import static org.mockito.Mockito.when;

import com.ttt.mar.leads.dto.ValidationDto;
import com.ttt.mar.leads.service.iface.LeadSourceService;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * @author KietDT
 * @created_date 28/04/2021
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class LeadSourceServiceTest {

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
      .getLogger(LeadSourceServiceTest.class);

  @Mock
  private LeadSourceService leadSourceService;

  @Before
  public void setUp() {
    ValidationDto validationDto = new ValidationDto();
    validationDto.setId(1);
    validationDto.setCode("DATE");
    validationDto.setTitle("Date");
    validationDto.setFunctionName("date()");

    when(leadSourceService.findAllValidation())
        .thenReturn(Collections.singletonList(validationDto));
  }

  @Test
  public void findAllValidationTest() {
    logger.info("-- findAll list Validation Test --");

    List<ValidationDto> validationDtos = leadSourceService.findAllValidation();
    Assertions.assertNotNull(validationDtos, "data is empty");
    Assertions.assertSame(validationDtos.size(), 1);
  }

//  @Test
//  public void createLeadSourceTest() throws OperationNotImplementException, ResourceNotFoundException {
//    logger.info("--- createLeadSourceTest() ---");
//
//    LeadSourceRequestDto dto = new LeadSourceRequestDto();
//    dto.setSource("source");
//    dto.setUtmSource("utmSource");
//    dto.setName("name");
//    List<LeadSourceFieldValidationRequestDto> leadSourceFieldValidationRequestDtos = new ArrayList<>();
//
//    LeadSourceFieldValidationRequestDto fieldValidationRequestDto = new LeadSourceFieldValidationRequestDto();
//    fieldValidationRequestDto.setFieldCode("PHONE");
//    fieldValidationRequestDto.setRequire(true);
//    fieldValidationRequestDto.setValidationCode("PHONE");
//    leadSourceFieldValidationRequestDtos.add(fieldValidationRequestDto);
//
//    dto.setLeadSourceFieldValidationRequestDtos(leadSourceFieldValidationRequestDtos);
//
//    LeadSource leadSource = new LeadSource();
//    leadSource.setId(1);
//    leadSource.setName("name");
//    Mockito.when(leadSourceRepository.save(leadSource)).thenReturn(leadSource);
//    Integer id = leadSourceService.createLeadSource(1, dto);
//    Assertions.assertSame(id, 1);
//  }
}
