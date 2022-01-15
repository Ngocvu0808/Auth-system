package com.ttt.mar.auth.service.iface;

import com.ttt.mar.auth.entities.enums.AccessTokenStatusFilter;
import java.util.List;

public interface AccessTokenService {

  List<AccessTokenStatusFilter> getStatusOfAccessToken();
}
