package com.ttt.mar.notify.controller;

import com.ttt.mar.notify.config.PermissionObjectCode;
import com.ttt.mar.notify.config.ServicePermissionCode;
import com.ttt.mar.notify.dto.notify.PayLoadStatusHistoryUpdateDto;
import com.ttt.mar.notify.service.iface.NotifyHistoryStatusService;
import com.ttt.rnd.common.config.Constants;
import com.ttt.rnd.common.dto.BaseMethodResponse;
import com.ttt.rnd.common.dto.GetMethodResponse;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ProxyAuthenticationException;
import com.ttt.rnd.common.service.iface.AuthGuard;
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
public class NotifyHistoryStatusController {

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
      .getLogger(NotifyHistoryController.class);

  @Autowired
  private NotifyHistoryStatusService notifyHistoryStatusService;

  @Autowired
  private AuthGuard authGuard;

  @PostMapping("/status-history")
  public ResponseEntity<?> updateStatusHistoryNotification(HttpServletRequest request,
      HttpServletResponse response,
      @RequestBody @Valid PayLoadStatusHistoryUpdateDto payLoadStatusHistoryUpdateDto) {
    try {
//      if (!authGuard
//          .checkPermission(request, null, PermissionObjectCode.NOTIFY,
//              ServicePermissionCode.UPDATE_STATUS_HISTORY_NOTIFY)) {
//        return new ResponseEntity<>(
//            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
//                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
//                .httpCode(HttpStatus.FORBIDDEN.value()).build(), HttpStatus.OK);
//      }
      Boolean result = notifyHistoryStatusService
          .updateStatusHistory(payLoadStatusHistoryUpdateDto);

      return new ResponseEntity<>(GetMethodResponse.builder().status(true).status(true)
          .message(Constants.SUCCESS_MSG).errorCode(HttpStatus.OK.name().toLowerCase()).data(result)
          .httpCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
//    } catch (ProxyAuthenticationException e) {
//      logger.warn(e.getMessage());
//      return new ResponseEntity<>(
//          BaseMethodResponse.builder().status(false).message(e.getMessage())
//              .errorCode(e.getMessageCode()).httpCode(HttpStatus.UNAUTHORIZED.value()).build(),
//          HttpStatus.OK);
    } catch (OperationNotImplementException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.NOT_FOUND.value()).build(),
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
