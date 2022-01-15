package com.ttt.mar.sms.controller;

import com.ttt.mar.sms.config.PermissionObjectCode;
import com.ttt.mar.sms.config.ServicePermissionCode;
import com.ttt.mar.sms.dto.inforsms.PayloadRequestFromNotifyServiceDto;
import com.ttt.mar.sms.service.iface.SmsEnQueueService;
import com.ttt.rnd.common.config.Constants;
import com.ttt.rnd.common.dto.BaseMethodResponse;
import com.ttt.rnd.common.dto.GetMethodResponse;
import com.ttt.rnd.common.exception.OperationNotImplementException;
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

@RestController
public class SmsEnqueueController {

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
      .getLogger(SmsEnqueueController.class);

  @Autowired
  private SmsEnQueueService smsEnQueueService;

  @Autowired
  private AuthGuard authGuard;

  @PostMapping("/receive-sms")
  public ResponseEntity<?> sendSms(HttpServletRequest request, HttpServletResponse response,
      @RequestBody @Valid
          PayloadRequestFromNotifyServiceDto smsRequest) {

    try {
//      if (!authGuard
//          .checkPermission(request, null, PermissionObjectCode.SMS,
//              ServicePermissionCode.SMS_RECEIVE_SMS_FROM_NOTIFY_SERVICE)) {
//        return new ResponseEntity<>(
//            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
//                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
//                .httpCode(HttpStatus.FORBIDDEN.value()).build(), HttpStatus.OK);
//      }

      Map<String, Object> result = smsEnQueueService
          .receiveInforSmsFromNotifyService(smsRequest);
      return new ResponseEntity<>(GetMethodResponse.builder().status(true).status(true)
          .message(Constants.SUCCESS_MSG).errorCode(
              HttpStatus.OK.name().toLowerCase()).data(result).httpCode(HttpStatus.OK.value())
          .build(),
          HttpStatus.OK);

    } catch (OperationNotImplementException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.BAD_REQUEST.value())
              .build(),
          HttpStatus.OK);
    } catch (ResourceNotFoundException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.NOT_FOUND.value())
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
