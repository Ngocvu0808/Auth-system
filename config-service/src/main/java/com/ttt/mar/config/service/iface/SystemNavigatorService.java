package com.ttt.mar.config.service.iface;

import com.ttt.mar.config.dto.SystemNavigatorResponse;
import java.util.List;

public interface SystemNavigatorService {

  List<SystemNavigatorResponse> getAll();
}
