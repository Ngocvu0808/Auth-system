package com.ttt.mar.auth.service.impl;

import com.ttt.mar.auth.entities.enums.AccessTokenStatusFilter;
import com.ttt.mar.auth.service.iface.AccessTokenService;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;

@Service
public class AccessTokenServiceImpl implements AccessTokenService {

  @Override
  public List<AccessTokenStatusFilter> getStatusOfAccessToken() {
    return Stream.of(AccessTokenStatusFilter.values()).collect(Collectors.toList());
  }
}
