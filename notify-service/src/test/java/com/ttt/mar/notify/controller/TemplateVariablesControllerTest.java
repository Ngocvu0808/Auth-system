package com.ttt.mar.notify.controller;

import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ttt.mar.notify.dto.template.VariablesRequestDto;
import com.ttt.rnd.common.exception.ProxyAuthenticationException;
import com.ttt.rnd.common.exception.UserNotFoundException;
import com.ttt.rnd.common.service.iface.AuthGuard;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class TemplateVariablesControllerTest {

  @Autowired
  private MockMvc mvc;

  @Autowired
  WebApplicationContext context;

  @Mock
  private AuthGuard authGuard;

  @Mock
  private HttpServletRequest httpServletRequest;

  @Mock
  private HttpServletResponse httpServletResponse;

  private String mapToJson(Object obj) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.writeValueAsString(obj);
  }

  @Before
  public void setUp() throws UserNotFoundException, ProxyAuthenticationException {
    when(httpServletRequest.getHeader("Authorization")).thenReturn("123");
    when(authGuard.getUserId(httpServletRequest)).thenReturn(1);
  }

  @Test
  public void createTemplateVariable() throws Exception {
    String uri = "/variable";
    VariablesRequestDto newVariable = new VariablesRequestDto();
    newVariable.setCode("NamTest");
    newVariable.setName("Test");
    String inputJson = this.mapToJson(newVariable);
    MvcResult mvcResult = mvc.perform(
        MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(inputJson))
        .andReturn();

    int status = mvcResult.getResponse().getStatus();
    Assertions.assertEquals(200, status);
  }

  @Test
  public void deleteVariable() throws Exception {
    String uri = "/variable/1";
    MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(uri)).andReturn();
    int status = mvcResult.getResponse().getStatus();
    Assertions.assertEquals(200, status);
  }

  @Test
  public void getAllVariables() throws Exception {
    String uri = "/variables?search=a";
    MvcResult mvcResult = mvc
        .perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
        .andReturn();
    int status = mvcResult.getResponse().getStatus();
    Assertions.assertEquals(200, status);
  }

  @Test
  public void getVariablesForTemplate() throws Exception {
    String uri = "/template/variables";
    MvcResult mvcResult = mvc
        .perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
        .andReturn();

    int status = mvcResult.getResponse().getStatus();
    Assertions.assertEquals(200, status);
  }
}
