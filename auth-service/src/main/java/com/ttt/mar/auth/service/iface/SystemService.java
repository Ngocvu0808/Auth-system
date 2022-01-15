package com.ttt.mar.auth.service.iface;

import com.ttt.mar.auth.dto.appservice.SystemCustomDto;
import java.util.List;

public interface SystemService {

  List<SystemCustomDto> getSystems();

}
