package com.ttt.mar.sms.controller;

import com.ttt.mar.sms.config.PermissionObjectCode;
import com.ttt.mar.sms.config.ServicePermissionCode;
import com.ttt.mar.sms.dto.smpublisher.SmsPublisherCustomResponseDto;
import com.ttt.mar.sms.dto.smpublisher.SmsPublisherRequestDto;
import com.ttt.mar.sms.dto.smpublisher.SmsPublisherResponseDto;
import com.ttt.mar.sms.service.iface.SmsPublisherService;
import com.ttt.rnd.common.config.Constants;
import com.ttt.rnd.common.dto.BaseMethodResponse;
import com.ttt.rnd.common.dto.DataPagingResponse;
import com.ttt.rnd.common.dto.DeleteMethodResponse;
import com.ttt.rnd.common.dto.GetMethodResponse;
import com.ttt.rnd.common.dto.PostMethodResponse;
import com.ttt.rnd.common.dto.PutMethodResponse;
import com.ttt.rnd.common.exception.DuplicateEntityException;
import com.ttt.rnd.common.exception.IdentifyBlankException;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ProxyAuthenticationException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import com.ttt.rnd.common.service.iface.AuthGuard;
import com.ttt.rnd.common.utils.SortingUtils;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SmsPublisherController {

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
      .getLogger(SmsPublisherController.class);

  @Autowired
  private SmsPublisherService smsPublisherService;

  @Autowired
  private AuthGuard authGuard;

  @PostMapping("/publisher")
  public ResponseEntity<?> createSmsPublisher(
      @RequestBody @Valid SmsPublisherRequestDto smsPublisherRequestDto,
      HttpServletRequest request, HttpServletResponse response) {
    try {
      if (!authGuard
          .checkPermission(request, null, PermissionObjectCode.SMS,
              ServicePermissionCode.SMS_PUBLISHER_ADD)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build(), HttpStatus.OK);
      }
      Integer userId = authGuard.getUserId(request);
      Integer id = smsPublisherService.createSmsPublisher(smsPublisherRequestDto, userId);
      return new ResponseEntity<>(
          PostMethodResponse.builder().status(true).message(Constants.SUCCESS_MSG)
              .errorCode(HttpStatus.OK.name())
              .id(id).errorCode(HttpStatus.OK.name().toLowerCase())
              .httpCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
    } catch (ProxyAuthenticationException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.UNAUTHORIZED.value()).build(),
          HttpStatus.OK);
    } catch (DuplicateEntityException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.CONFLICT.value()).build(),
          HttpStatus.OK);
    } catch (ResourceNotFoundException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.NOT_FOUND.value()).build(),
          HttpStatus.OK);
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

  @PutMapping("/publisher/{id}")
  public ResponseEntity<?> updatePublisher(
      @RequestBody @Valid SmsPublisherRequestDto smsPublisher,
      @PathVariable("id") Integer smsPublisherId, HttpServletRequest request,
      HttpServletResponse response) {
    try {
      if (!authGuard
          .checkPermission(request, null, PermissionObjectCode.SMS,
              ServicePermissionCode.SMS_PUBLISHER_EDIT)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build(), HttpStatus.OK);
      }
      Integer userId = authGuard.getUserId(request);
      smsPublisher.setId(smsPublisherId);
      Boolean result = smsPublisherService.updateSmsPublisher(smsPublisher, userId);
      return new ResponseEntity<>(
          PutMethodResponse.builder().status(result).message(Constants.SUCCESS_MSG)
              .id(smsPublisherId)
              .errorCode(HttpStatus.OK.name().toLowerCase())
              .httpCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
    } catch (ProxyAuthenticationException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.UNAUTHORIZED.value()).build(),
          HttpStatus.OK);
    } catch (ResourceNotFoundException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.NOT_FOUND.value()).build(),
          HttpStatus.OK);
    } catch (IdentifyBlankException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.BAD_REQUEST.value()).build(),
          HttpStatus.OK);
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


  @DeleteMapping("/publisher/{id}")
  public ResponseEntity<?> deleteSmsPublisher(
      @PathVariable("id") Integer smsPublisherId, HttpServletRequest request,
      HttpServletResponse response) {
    try {
      if (!authGuard
          .checkPermission(request, null, PermissionObjectCode.SMS,
              ServicePermissionCode.SMS_PUBLISHER_DELETE)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build(), HttpStatus.OK);
      }
      Integer userId = authGuard.getUserId(request);
      Boolean result = smsPublisherService.deleteSmsPublisher(smsPublisherId, userId);
      return new ResponseEntity<>(
          DeleteMethodResponse.builder().status(result).message(Constants.SUCCESS_MSG).errorCode(
              HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value())
              .build(), HttpStatus.OK);
    } catch (ProxyAuthenticationException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.UNAUTHORIZED.value()).build(),
          HttpStatus.OK);
    } catch (IdentifyBlankException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.BAD_REQUEST.value()).build(),
          HttpStatus.OK);
    } catch (ResourceNotFoundException e) {
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

  @GetMapping("/publishers")
  public ResponseEntity<?> getPublisers(
      @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
      @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
      @RequestParam(value = "search", required = false, defaultValue = "") String search,
      @RequestParam(value = "sort", required = false, defaultValue = "") String sort,
      HttpServletRequest request, HttpServletResponse response) {
    try {
      if (!authGuard
          .checkPermission(request, null, PermissionObjectCode.SMS,
              ServicePermissionCode.SMS_PUBLISHER_GETS)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build(), HttpStatus.OK);
      }
      if (!SortingUtils.validateSort(sort, SmsPublisherResponseDto.class)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message("Sort invalid")
                .errorCode(HttpStatus.BAD_REQUEST.name().toLowerCase())
                .httpCode(HttpStatus.BAD_REQUEST.value()).build(), HttpStatus.OK);
      }
      DataPagingResponse<SmsPublisherResponseDto> smsPublishers = smsPublisherService
          .getPublisherPaging(search, page, sort, limit);
      return new ResponseEntity<>(
          GetMethodResponse.builder().status(true).message(Constants.SUCCESS_MSG)
              .data(smsPublishers).errorCode(HttpStatus.OK.name().toLowerCase())
              .httpCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
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


  @GetMapping("/publisher-all")
  public ResponseEntity<?> getAllPublisher(HttpServletRequest request,
      HttpServletResponse response) {
    try {
      authGuard.checkAuthorization(request);
      List<SmsPublisherCustomResponseDto> smsPublishers = smsPublisherService.getAllPublisher();
      return new ResponseEntity<>(
          GetMethodResponse.builder().status(true).message(Constants.SUCCESS_MSG)
              .data(smsPublishers).errorCode(Constants.SUCCESS_MSG.toLowerCase())
              .httpCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
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
}
