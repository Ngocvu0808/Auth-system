package com.ttt.mar.email.controller;

import com.ttt.mar.email.config.PermissionObjectCode;
import com.ttt.mar.email.config.ServicePermissionCode;
import com.ttt.mar.email.dto.notifyhistory.InfoEmailRequestFromNotifyServiceDto;
import com.ttt.mar.email.service.iface.MailService;
import com.ttt.rnd.common.config.Constants;
import com.ttt.rnd.common.dto.BaseMethodResponse;
import com.ttt.rnd.common.dto.GetMethodResponse;
import com.ttt.rnd.common.exception.ProxyAuthenticationException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import com.ttt.rnd.common.service.iface.AuthGuard;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author bontk
 * @created_date 17/07/2020
 */
@RestController
public class EmailController {

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
      .getLogger(EmailController.class);

  @Autowired
  private MailService mailService;


  @Autowired
  private AuthGuard authGuard;

  @PostMapping("/receive-email")
  public ResponseEntity<?> sendEmail(HttpServletRequest request, HttpServletResponse response,
      @RequestBody @Valid
          InfoEmailRequestFromNotifyServiceDto emailRequest) {
    try {
      if (!authGuard
          .checkPermission(request, null, PermissionObjectCode.EMAIL,
              ServicePermissionCode.EMAIL_RECEIVE_EMAIL_FROM_NOTIFY_SERVICE)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build(), HttpStatus.OK);
      }
      Map<String, Object> result = mailService.receiveEmail(emailRequest);

      return new ResponseEntity<>(GetMethodResponse.builder().status(true).status(true)
          .message(Constants.SUCCESS_MSG).errorCode(
              HttpStatus.OK.name().toLowerCase()).data(result).httpCode(HttpStatus.OK.value())
          .build(),
          HttpStatus.OK);
    } catch (ResourceNotFoundException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.NOT_FOUND.value())
              .build(),
          HttpStatus.OK);
    } catch (ProxyAuthenticationException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.UNAUTHORIZED.value())
              .build(),
          HttpStatus.OK);
    } catch (Exception e) {
      logger.error(e.getMessage());
      return new ResponseEntity<>(BaseMethodResponse.builder().status(false)
          .message(Constants.INTERNAL_SERVER_ERROR)
          .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
          .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build(), HttpStatus.OK);
    }
  }
}
