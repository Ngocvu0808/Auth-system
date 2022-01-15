package com.ttt.mar.email.controller;

import com.ttt.mar.email.config.PermissionObjectCode;
import com.ttt.mar.email.config.ServicePermissionCode;
import com.ttt.mar.email.dto.emailconfig.EmailConfigCreateRequestDto;
import com.ttt.mar.email.dto.emailconfig.EmailConfigDetailResponseDto;
import com.ttt.mar.email.dto.emailconfig.EmailConfigResponseDto;
import com.ttt.mar.email.dto.emailconfig.EmailConfigUpdateRequestDto;
import com.ttt.mar.email.service.iface.EmailConfigService;
import com.ttt.rnd.common.config.Constants;
import com.ttt.rnd.common.dto.BaseMethodResponse;
import com.ttt.rnd.common.dto.DataPagingResponse;
import com.ttt.rnd.common.dto.DeleteMethodResponse;
import com.ttt.rnd.common.dto.GetMethodResponse;
import com.ttt.rnd.common.dto.PostMethodResponse;
import com.ttt.rnd.common.dto.PutMethodResponse;
import com.ttt.rnd.common.exception.IdentifyBlankException;
import com.ttt.rnd.common.exception.OperationNotImplementException;
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
public class EmailConfigController {

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
      .getLogger(EmailConfigController.class);

  @Autowired
  private EmailConfigService emailConfigService;

  @Autowired
  private AuthGuard authGuard;

  @PostMapping("/config")
  public ResponseEntity<?> createEmailConfig(
      @RequestBody @Valid EmailConfigCreateRequestDto dto,
      HttpServletRequest request, HttpServletResponse response) {
    try {
      if (!authGuard.checkPermission(request, null, PermissionObjectCode.EMAIL,
          ServicePermissionCode.EMAIL_CONFIG_ADD)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build(), HttpStatus.OK);
      }
      Integer userId = authGuard.getUserId(request);
      Integer emailConfigId = emailConfigService
          .createEmailConfigService(dto, userId);
      return new ResponseEntity<>(
          PostMethodResponse.builder().status(true).message(Constants.SUCCESS_MSG)
              .id(emailConfigId)
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
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.BAD_REQUEST.value())
              .build(), HttpStatus.OK);
    } catch (OperationNotImplementException e) {
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).httpCode(HttpStatus.BAD_REQUEST.value())
              .message(e.getMessage()).errorCode(e.getMessageCode()).build(), HttpStatus.OK);
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
  public ResponseEntity<?> getEmailConfigsPaging(
      @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
      @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
      @RequestParam(value = "search", required = false, defaultValue = "") String search,
      @RequestParam(value = "publisherId", required = false) Integer publisherId,
      @RequestParam(value = "sort", required = false, defaultValue = "") String sort,
      HttpServletRequest request, HttpServletResponse response) {
    try {
      if (!authGuard
          .checkPermission(request, null, PermissionObjectCode.EMAIL,
              ServicePermissionCode.EMAIL_CONFIG_GETS)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build(), HttpStatus.OK);
      }
      if (!SortingUtils.validateSort(sort, EmailConfigResponseDto.class)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message("Sort invalid")
                .errorCode(HttpStatus.BAD_REQUEST.name().toLowerCase())
                .httpCode(HttpStatus.BAD_REQUEST.value()).build(), HttpStatus.OK);
      }
      DataPagingResponse<EmailConfigResponseDto> smsConfigs = emailConfigService
          .getEmailConfigs(publisherId, search, sort, page - 1, limit);
      return new ResponseEntity<>(
          GetMethodResponse.builder().status(true).message(Constants.SUCCESS_MSG).data(smsConfigs)
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

  @PutMapping("/config/{id}")
  public ResponseEntity<?> updateEmailConfigById(
      @RequestBody @Valid EmailConfigUpdateRequestDto dto,
      @PathVariable("id") Integer id, HttpServletRequest request,
      HttpServletResponse response) {
    try {
      if (!authGuard
          .checkPermission(request, null, PermissionObjectCode.EMAIL,
              ServicePermissionCode.EMAIL_CONFIG_EDIT)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build(), HttpStatus.OK);
      }
      Integer userId = authGuard.getUserId(request);
      dto.setId(id);
      emailConfigService.updateEmailConfig(dto, userId);
      return new ResponseEntity<>(
          PutMethodResponse.builder().status(true).message(Constants.SUCCESS_MSG).id(id)
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
    } catch (Exception e) {
      logger.error(e.getMessage());
      return new ResponseEntity<>(BaseMethodResponse.builder().status(false)
          .message(Constants.INTERNAL_SERVER_ERROR)
          .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
          .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build(), HttpStatus.OK);
    }
  }


  @DeleteMapping("/config/{id}")
  public ResponseEntity<?> deleteEmailConfigById(
      @PathVariable("id") Integer emailConfigId, HttpServletRequest request,
      HttpServletResponse response) {
    try {
      if (!authGuard
          .checkPermission(request, null, PermissionObjectCode.EMAIL,
              ServicePermissionCode.EMAIL_CONFIG_DELETE)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build(), HttpStatus.OK);
      }
      Integer userId = authGuard.getUserId(request);
      Boolean result = emailConfigService.deleteEmailConfigById(emailConfigId, userId);
      return new ResponseEntity<>(
          DeleteMethodResponse.builder().status(result).message(Constants.SUCCESS_MSG)
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

  @GetMapping("/config/{id}")
  public ResponseEntity<?> getEmailConfigById(
      @PathVariable("id") Integer id, HttpServletRequest request,
      HttpServletResponse response) {
    try {
      if (!authGuard
          .checkPermission(request, null, PermissionObjectCode.EMAIL,
              ServicePermissionCode.EMAIL_CONFIG_GET_BY_ID)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build(), HttpStatus.OK);
      }
      EmailConfigDetailResponseDto data = emailConfigService.getEmailConfigById(id);
      return new ResponseEntity<>(
          GetMethodResponse.builder().status(true).message(Constants.SUCCESS_MSG).data(data)
              .httpCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
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
