package com.ttt.mar.notify.controller;

import com.ttt.mar.notify.dto.typesend.TypeSendResponseDto;
import com.ttt.mar.notify.service.iface.TypeSendService;
import com.ttt.rnd.common.config.Constants;
import com.ttt.rnd.common.dto.BaseMethodResponse;
import com.ttt.rnd.common.dto.GetMethodResponse;
import com.ttt.rnd.common.exception.ProxyAuthenticationException;
import com.ttt.rnd.common.service.iface.AuthGuard;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TypeSendController {

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
      .getLogger(TypeSendController.class);

  @Autowired
  private TypeSendService typeSendService;

  @Autowired
  private AuthGuard authGuard;

  @GetMapping("/type-sends")
  public ResponseEntity<?> getTypeSends(HttpServletRequest request,
      HttpServletResponse response) {

    try {
      authGuard.checkAuthorization(request);
      List<TypeSendResponseDto> typeSends = typeSendService.getAll();
      return new ResponseEntity<>(
          GetMethodResponse.builder().status(true).message(Constants.SUCCESS_MSG)
              .data(typeSends)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value()).build()
          , HttpStatus.OK);

    } catch (ProxyAuthenticationException e) {
      logger.error(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.UNAUTHORIZED.value()).build(),
          HttpStatus.OK);
    } catch (Exception e) {
      logger.error(e.getMessage());
      return new ResponseEntity<>(
          GetMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name())
              .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build(),
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
