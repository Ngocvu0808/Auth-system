package com.ttt.mar.config.service.impl;

import static org.mockito.Mockito.doReturn;

import com.ttt.mar.config.dto.FieldConfigDto;
import com.ttt.mar.config.entities.MarFieldConfig;
import com.ttt.mar.config.entities.MarFieldConfigType;
import com.ttt.mar.config.entities.MarFieldConfigTypeValue;
import com.ttt.mar.config.mapper.FieldConfigMapper;
import com.ttt.mar.config.repositories.MarFieldConfigRepository;
import com.ttt.mar.config.service.iface.FieldConfigService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class FieldConfigServiceImplTest {

  @Autowired
  private FieldConfigService fieldConfigService;

  @Mock
  private FieldConfigMapper fieldConfigMapper;
  @MockBean
  private MarFieldConfigRepository marFieldConfigRepository;

  @Test
  void getAllByTypeAndObject() {
    String type = "PARAM";
    String objs = "GLOBAL,LEAD";
    List<String> objects = Arrays.asList(objs.split(",").clone());
    List<MarFieldConfig> data = new ArrayList<>();
    MarFieldConfig f = new MarFieldConfig();
    f.setType(MarFieldConfigType.PARAM);
    f.setKey("utm_partner");
    f.setName("UTM Partner");
    f.setTypeValue(MarFieldConfigTypeValue.TEXT);
    f.setCreatedTime(new Date());
    data.add(f);
    doReturn(data).when(marFieldConfigRepository)
        .findAllByTypeAndObjectInAndIsDeletedFalse(MarFieldConfigType.valueOf(type), objects);

    List<FieldConfigDto> resp = fieldConfigService.getAllByTypeAndObject(type, objs);
    Assertions.assertNotNull(resp, "data is empty");
    Assertions.assertSame(data.size(), resp.size());


  }
}