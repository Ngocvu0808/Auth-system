package com.ttt.mar.config.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ttt.mar.config.entities.MarFieldConfig;
import com.ttt.mar.config.entities.MarFieldConfigType;
import com.ttt.mar.config.entities.MarFieldConfigTypeValue;
import com.ttt.mar.config.service.iface.FieldConfigService;
import com.ttt.rnd.common.service.iface.AuthGuard;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
class FieldConfigControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private FieldConfigService fieldConfigService;
  @MockBean
  private AuthGuard authGuard;

  @Test
  void getConfigByTypeAndObject() throws Exception {
    // Setup our mocked service

    String type = "PARAM";
    String objs = "GLOBAL,LEAD";
    Map<String, String> params = new HashMap<>();
    params.put("type", type);
    params.put("objects", objs);
    List<MarFieldConfig> data = new ArrayList<>();
    MarFieldConfig f = new MarFieldConfig();
    f.setType(MarFieldConfigType.PARAM);
    f.setKey("utm_partner");
    f.setName("UTM Partner");
    f.setTypeValue(MarFieldConfigTypeValue.TEXT);
    f.setCreatedTime(new Date());
    data.add(f);
    doReturn(data).when(fieldConfigService).getAllByTypeAndObject(type, objs);

    // Execute the GET request
    mockMvc.perform(MockMvcRequestBuilders.get("/field/filter")
        .param("type", type).param("objects", objs)
        .header("auth", ""))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", is(true)))
        .andExpect(jsonPath("$.httpCode", is(200)))
        .andExpect(jsonPath("$.errorCode", is("ok")));

  }
}