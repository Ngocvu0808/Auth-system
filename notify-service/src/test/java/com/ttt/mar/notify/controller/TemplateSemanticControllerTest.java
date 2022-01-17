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

import com.ttt.rnd.common.exception.ProxyAuthenticationException;
import com.ttt.rnd.common.exception.UserNotFoundException;
import com.ttt.rnd.common.service.iface.AuthGuard;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class TemplateSemanticControllerTest {

  @Autowired
  private MockMvc mvc;

  @Autowired
  WebApplicationContext context;

  @Mock
  private HttpServletRequest httpServletRequest;

  @Mock
  private HttpServletResponse httpServletResponse;

  @Mock
  private AuthGuard authGuard;


  @Before
  public void setUp() throws UserNotFoundException, ProxyAuthenticationException {
    when(httpServletRequest.getHeader("Authorization")).thenReturn("123");
    when(authGuard.getUserId(httpServletRequest)).thenReturn(1);
  }

  @Test
  public void getAllTemplateSemanticByType() throws Exception {
    String url = "/semantic?type=FOOTER";
    MvcResult mvcResult = mvc
        .perform(MockMvcRequestBuilders.get(url).accept(MediaType.APPLICATION_JSON_VALUE))
        .andReturn();
    int status = mvcResult.getResponse().getStatus();
    Assertions.assertEquals(200, status);
  }
}
