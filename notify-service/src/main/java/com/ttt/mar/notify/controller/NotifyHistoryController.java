package com.ttt.mar.notify.controller;

import com.ttt.mar.notify.config.PermissionObjectCode;
import com.ttt.mar.notify.config.ServicePermissionCode;
import com.ttt.mar.notify.dto.notify.DataRequestNotifyResponse;
import com.ttt.mar.notify.dto.notify.NotifyHistoryDetailResponseDto;
import com.ttt.mar.notify.dto.notify.NotifyHistoryResponseDto;
import com.ttt.mar.notify.service.iface.NotifyHistoryService;
import com.ttt.rnd.common.config.Constants;
import com.ttt.rnd.common.dto.BaseMethodResponse;
import com.ttt.rnd.common.dto.GetMethodResponse;
import com.ttt.rnd.common.dto.PostMethodResponse;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ProxyAuthenticationException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import com.ttt.rnd.common.service.iface.AuthGuard;
import com.ttt.rnd.common.utils.SortingUtils;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotifyHistoryController {

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
      .getLogger(NotifyHistoryController.class);

  @Autowired
  private NotifyHistoryService notifyHistoryService;

  @Autowired
  private AuthGuard authGuard;


  @PostMapping("/notification")
  public ResponseEntity<?> createNotify(HttpServletRequest request, HttpServletResponse response,
      @RequestBody Map<String, Object> notifyMap) {
    try {
//      if (!authGuard.checkPermission(request, null, PermissionObjectCode.NOTIFY,
//          Arrays.asList(ServicePermissionCode.NOTIFY_HISTORY_ADD))) {
//        return new ResponseEntity<>(
//            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
//                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
//                .httpCode(HttpStatus.FORBIDDEN.value()).build(), HttpStatus.OK);
//      }

      logger.info("notifyMap: {}", notifyMap);
      Integer userId = 1;//authGuard.getUserId(request);
      Long requestId = notifyHistoryService.createNotification(notifyMap, userId);
      return new ResponseEntity<>(
          PostMethodResponse.builder().status(true).id(requestId).message(Constants.SUCCESS_MSG)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value())
              .build(), HttpStatus.OK);

    } catch (OperationNotImplementException e) {
      logger.error(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.BAD_REQUEST.value()).build(),
          HttpStatus.OK);
    } catch (ProxyAuthenticationException e) {
      logger.error(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.UNAUTHORIZED.value()).build(),
          HttpStatus.OK);
    } catch (ResourceNotFoundException e) {
      logger.error(e.getMessage());
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

  @GetMapping("/histories")
  public ResponseEntity<?> getNotifyHistoriesPaging(
      @RequestParam(value = "statusId", required = false) Integer statusId,
      @RequestParam(value = "typeSendId", required = false) Integer typeSendId,
      @RequestParam(value = "fromDate", required = false) Long fromDate,
      @RequestParam(value = "toDate", required = false) Long toDate,
      @RequestParam(value = "search", required = false, defaultValue = "") String search,
      @RequestParam(value = "sort", required = false, defaultValue = "") String sort,
      @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
      @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
      HttpServletRequest request) {
    try {
      if (!authGuard
          .checkPermission(request, null, PermissionObjectCode.NOTIFY,
              ServicePermissionCode.NOTIFY_HISTORY_GETS)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build(), HttpStatus.OK);
      }
      if (!SortingUtils.validateSort(sort, NotifyHistoryResponseDto.class)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message("Sort invalid")
                .errorCode(HttpStatus.BAD_REQUEST.name().toLowerCase())
                .httpCode(HttpStatus.BAD_REQUEST.value()).build(), HttpStatus.OK);
      }
      DataRequestNotifyResponse<?> notifyHistories = notifyHistoryService
          .getNotifyHistoryPaging(fromDate, toDate, statusId, typeSendId, search, sort, page,
              limit);

      return new ResponseEntity<>(
          GetMethodResponse.builder().status(true).message(Constants.SUCCESS_MSG)
              .data(notifyHistories)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value()).build()
          , HttpStatus.OK);
    } catch (ProxyAuthenticationException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.UNAUTHORIZED.value()).build(),
          HttpStatus.OK);
    } catch (Exception e) {
      logger.error(e.getMessage());
      return new ResponseEntity<>(BaseMethodResponse.builder().status(false)
          .message(Constants.INTERNAL_SERVER_ERROR)
          .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
          .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build(), HttpStatus.OK);
    }
  }

  @GetMapping("/histories/export")
  public ResponseEntity<?> exportNotifyHistories(HttpServletRequest request,
      @RequestParam(value = "statusId", required = false) Integer statusId,
      @RequestParam(value = "typeSendId", required = false) Integer typeSendId,
      @RequestParam(value = "fromDate", required = false) Long fromDate,
      @RequestParam(value = "toDate", required = false) Long toDate,
      @RequestParam(value = "search", required = false, defaultValue = "") String search,
      @RequestParam(value = "sort", required = false, defaultValue = "") String sort) {
    try {
      if (!authGuard.checkPermission(request, null, PermissionObjectCode.NOTIFY,
          ServicePermissionCode.NOTIFY_HISTORY_EXPORT)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build(), HttpStatus.OK);
      }
      String fileName = notifyHistoryService
          .exportNotifyHistory(fromDate, toDate, statusId, typeSendId, search, sort);
      File file = new File(fileName);
      Path path = Paths.get(file.getAbsolutePath());
      ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

      return ResponseEntity.ok()
          .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
          .contentLength(file.length()).contentType(MediaType.APPLICATION_OCTET_STREAM)
          .body(resource);
    } catch (OperationNotImplementException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.BAD_REQUEST.value()).build(),
          HttpStatus.OK);
    } catch (Exception e) {
      logger.error(e.getMessage());
      return new ResponseEntity<>(BaseMethodResponse.builder().status(false)
          .message(Constants.INTERNAL_SERVER_ERROR)
          .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
          .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build(), HttpStatus.OK);
    }
  }

  @GetMapping("/history/{id}")
  public ResponseEntity<?> getNotifyHistoryById(
      @PathVariable("id") Long id, HttpServletRequest request,
      HttpServletResponse response) {
    try {
      if (!authGuard
          .checkPermission(request, null, PermissionObjectCode.NOTIFY,
              ServicePermissionCode.NOTIFY_HISTORY_GET_BY_ID)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build()
            , HttpStatus.OK);
      }
      NotifyHistoryDetailResponseDto data = notifyHistoryService.getNotifyHistoryById(id);
      return new ResponseEntity<>(
          GetMethodResponse.builder().status(true).message(Constants.SUCCESS_MSG).data(data)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value())
              .build(), HttpStatus.OK);
    } catch (ResourceNotFoundException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.NOT_FOUND.value()).build(),
          HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      return new ResponseEntity<>(BaseMethodResponse.builder().status(false)
          .message(Constants.INTERNAL_SERVER_ERROR)
          .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
          .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build(), HttpStatus.OK);
    }
  }
}
