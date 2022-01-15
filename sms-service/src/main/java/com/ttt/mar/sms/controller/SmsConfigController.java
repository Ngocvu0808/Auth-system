package com.ttt.mar.sms.controller;

import com.ttt.mar.sms.config.PermissionObjectCode;
import com.ttt.mar.sms.config.ServicePermissionCode;
import com.ttt.mar.sms.dto.smsconfig.SmsConfigDtoRequest;
import com.ttt.mar.sms.dto.smsconfig.SmsConfigDtoResponse;
import com.ttt.mar.sms.dto.smsconfig.SmsConfigResponseDetailDto;
import com.ttt.mar.sms.service.iface.SmsConfigService;
import com.ttt.rnd.common.config.Constants;
import com.ttt.rnd.common.dto.BaseMethodResponse;
import com.ttt.rnd.common.dto.DataPagingResponse;
import com.ttt.rnd.common.dto.DeleteMethodResponse;
import com.ttt.rnd.common.dto.GetMethodResponse;
import com.ttt.rnd.common.dto.PostMethodResponse;
import com.ttt.rnd.common.dto.PutMethodResponse;
import com.ttt.rnd.common.exception.DuplicateEntityException;
import com.ttt.rnd.common.exception.IdentifyBlankException;
import com.ttt.rnd.common.exception.ProxyAuthenticationException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import com.ttt.rnd.common.service.iface.AuthGuard;
import com.ttt.rnd.common.utils.SortingUtils;
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
public class SmsConfigController {

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
      .getLogger(SmsConfigController.class);

  @Autowired
  private SmsConfigService smsConfigService;

  @Autowired
  private AuthGuard authGuard;

  @PostMapping("/config")
  public ResponseEntity<?> createSmsConfig(
      @RequestBody @Valid SmsConfigDtoRequest smsConfigDto,
      HttpServletRequest request, HttpServletResponse response) {
    try {
      if (!authGuard
          .checkPermission(request, null, PermissionObjectCode.SMS,
              ServicePermissionCode.SMS_CONFIG_ADD)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build(), HttpStatus.OK);
      }
      Integer userId = authGuard.getUserId(request);
      Integer smsConfigId = smsConfigService.createSmsConfig(smsConfigDto, userId);
      return new ResponseEntity<>(
          PostMethodResponse.builder().status(true).message(Constants.SUCCESS_MSG)
              .id(smsConfigId).errorCode(HttpStatus.OK.name().toLowerCase())
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
    } catch (Exception e) {
      logger.error(e.getMessage());
      return new ResponseEntity<>(BaseMethodResponse.builder().status(false)
          .message(Constants.INTERNAL_SERVER_ERROR)
          .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
          .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build(), HttpStatus.OK);
    }
  }


  @GetMapping("/configs")
  public ResponseEntity<?> getSmsConfigs(
      @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
      @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
      @RequestParam(value = "search", required = false, defaultValue = "") String search,
      @RequestParam(value = "publisherId", required = false, defaultValue = "") Integer publisherId,
      @RequestParam(value = "sort", required = false, defaultValue = "") String sort,
      HttpServletRequest request, HttpServletResponse response) {
    try {
      if (!authGuard
          .checkPermission(request, null, PermissionObjectCode.SMS,
              ServicePermissionCode.SMS_CONFIG_GETS)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build(), HttpStatus.OK);
      }
      if (!SortingUtils.validateSort(sort, SmsConfigDtoResponse.class)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message("Sort invalid")
                .errorCode(HttpStatus.BAD_REQUEST.name().toLowerCase())
                .httpCode(HttpStatus.BAD_REQUEST.value()).build(), HttpStatus.OK);
      }
      DataPagingResponse<SmsConfigDtoResponse> smsConfigs = smsConfigService
          .getSmsConfigs(search, publisherId, sort, page, limit);
      return new ResponseEntity<>(
          GetMethodResponse.builder().status(true).message(Constants.SUCCESS_MSG).data(smsConfigs)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value())
              .build(), HttpStatus.OK);

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


  @DeleteMapping("/config/{id}")
  public ResponseEntity<?> deleteSmsConfigById(
      @PathVariable("id") Integer smsConfigId, HttpServletRequest request,
      HttpServletResponse response) {
    try {
      if (!authGuard
          .checkPermission(request, null, PermissionObjectCode.SMS,
              ServicePermissionCode.SMS_CONFIG_DELETE)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build(), HttpStatus.OK);
      }
      Integer userId = authGuard.getUserId(request);
      Boolean result = smsConfigService.deleteSmsConfigById(smsConfigId, userId);
      return new ResponseEntity<>(
          DeleteMethodResponse.builder().status(result).message(Constants.SUCCESS_MSG)
              .errorCode(HttpStatus.OK.name()).httpCode(HttpStatus.OK.value()).build(),
          HttpStatus.OK);
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


  @PutMapping("/config/{id}")
  public ResponseEntity<?> updateSmsConfigById(
      @RequestBody @Valid SmsConfigDtoRequest smsConfigDtoRequest,
      @PathVariable("id") Integer smsConfigId, HttpServletRequest request,
      HttpServletResponse response) {
    try {
      if (!authGuard
          .checkPermission(request, null, PermissionObjectCode.SMS,
              ServicePermissionCode.SMS_CONFIG_EDIT)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build(), HttpStatus.OK);
      }
      Integer userId = authGuard.getUserId(request);
      smsConfigDtoRequest.setId(smsConfigId);
      Boolean result = smsConfigService.updateSmsConfigById(smsConfigDtoRequest, userId);
      return new ResponseEntity<>(
          PutMethodResponse.builder().status(result).message(Constants.SUCCESS_MSG).id(smsConfigId)
              .errorCode(HttpStatus.OK.name()).httpCode(HttpStatus.OK.value()).build(),
          HttpStatus.OK);
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
    } catch (Exception e) {
      logger.error(e.getMessage());
      return new ResponseEntity<>(BaseMethodResponse.builder().status(false)
          .message(Constants.INTERNAL_SERVER_ERROR)
          .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
          .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build(), HttpStatus.OK);
    }
  }

  @GetMapping("/config/{id}")
  public ResponseEntity<?> getSmsConfigById(
      @PathVariable("id") Integer smsConfigId, HttpServletRequest request,
      HttpServletResponse response) {
    try {
      if (!authGuard
          .checkPermission(request, null, PermissionObjectCode.SMS,
              ServicePermissionCode.SMS_CONFIG_GET_BY_ID)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build(), HttpStatus.OK);
      }
      SmsConfigResponseDetailDto smsConfigById = smsConfigService.getSmsConfigById(smsConfigId);
      return new ResponseEntity<>(
          GetMethodResponse.builder().status(true).message(Constants.SUCCESS_MSG)
              .errorCode(HttpStatus.OK.name().toLowerCase()).data(smsConfigById)
              .httpCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
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
          GetMethodResponse.builder().status(false).message(e.getMessage()).data(smsConfigId)
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
