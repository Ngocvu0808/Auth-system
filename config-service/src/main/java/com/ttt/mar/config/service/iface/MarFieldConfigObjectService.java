package com.ttt.mar.config.service.iface;

import com.ttt.mar.config.dto.MarFieldConfigObjectDto;
import java.util.List;

public interface MarFieldConfigObjectService {

  Boolean checkExistByCode(String code);

  List<MarFieldConfigObjectDto> findAll();
}
