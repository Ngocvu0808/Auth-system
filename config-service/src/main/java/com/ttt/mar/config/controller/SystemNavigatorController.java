package com.ttt.mar.config.controller;

import com.ttt.mar.config.config.PermissionObjectCode;
import com.ttt.mar.config.config.ServicePermissionCode;
import com.ttt.mar.config.dto.SystemNavigatorResponse;
import com.ttt.mar.config.service.iface.SystemNavigatorService;
import com.ttt.rnd.common.config.Constants;
import com.ttt.rnd.common.dto.BaseMethodResponse;
import com.ttt.rnd.common.dto.GetMethodResponse;
import com.ttt.rnd.common.exception.ProxyAuthenticationException;
import com.ttt.rnd.common.service.iface.AuthGuard;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SystemNavigatorController {

  private static final Logger logger = LoggerFactory.getLogger(SystemNavigatorController.class);
  private final SystemNavigatorService systemNavigatorService;
  private final AuthGuard authGuard;

  public SystemNavigatorController(SystemNavigatorService systemNavigatorService,
      AuthGuard authGuard) {
    this.systemNavigatorService = systemNavigatorService;
    this.authGuard = authGuard;
  }

  @GetMapping("/system-navigators")
  public ResponseEntity<?> getSystemNavigators(HttpServletRequest request,
      HttpServletResponse response) {
    try {
      List<SystemNavigatorResponse> data = systemNavigatorService.getAll();
      return new ResponseEntity<>(GetMethodResponse.builder().status(true).data(data)
          .errorCode(HttpStatus.OK.name().toLowerCase())
          .httpCode(HttpStatus.OK.value()).message(Constants.SUCCESS_MSG)
          .build(), HttpStatus.OK);
    } catch (Exception e) {
      logger.error(e.getMessage());
      return new ResponseEntity<>(BaseMethodResponse.builder().status(false)
          .message(Constants.INTERNAL_SERVER_ERROR)
          .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
          .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build(), HttpStatus.OK);
    }
  }
}
