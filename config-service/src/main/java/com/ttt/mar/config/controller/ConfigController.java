package com.ttt.mar.config.controller;

import com.ttt.mar.config.config.PermissionObjectCode;
import com.ttt.mar.config.config.ServicePermissionCode;
import com.ttt.mar.config.dto.ConfigCustomDto;
import com.ttt.mar.config.dto.ConfigDto;
import com.ttt.mar.config.service.iface.ConfigService;
import com.ttt.rnd.common.config.Constants;
import com.ttt.rnd.common.dto.BaseMethodResponse;
import com.ttt.rnd.common.dto.DataPagingResponse;
import com.ttt.rnd.common.dto.DeleteMethodResponse;
import com.ttt.rnd.common.dto.GetMethodResponse;
import com.ttt.rnd.common.dto.PostMethodResponse;
import com.ttt.rnd.common.dto.PutMethodResponse;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ProxyAuthenticationException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import com.ttt.rnd.common.service.iface.AuthGuard;
import com.ttt.rnd.common.utils.SortingUtils;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class ConfigController {

  private static final Logger logger = LoggerFactory.getLogger(ConfigController.class);
  private final ConfigService configService;
  private final AuthGuard authGuard;

  public ConfigController(ConfigService configService, AuthGuard authGuard) {
    this.configService = configService;
    this.authGuard = authGuard;
  }

  /**
   * Lấy toàn bộ danh sách config
   *
   * @param request
   * @param response
   * @param page
   * @param limit
   * @param key
   * @param search
   * @return
   */
  @GetMapping("/systems")
  public ResponseEntity<?> getAllConfig(HttpServletRequest request, HttpServletResponse response,
      @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
      @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
      @RequestParam(name = "key", required = false, defaultValue = "") String key,
      @RequestParam(name = "search", required = false, defaultValue = "") String search,
      @RequestParam(name = "sort", required = false, defaultValue = "") String sort
  ) {
    try {
      if (!authGuard
          .checkPermission(request, null, PermissionObjectCode.SYS_CONFIG,
              ServicePermissionCode.SYSTEM_CONFIG_GET_ALL)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.OK.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build(), HttpStatus.OK);
      }
      if (!SortingUtils.validateSort(sort, ConfigDto.class)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.SORTING_INVALID)
                .errorCode(HttpStatus.BAD_REQUEST.name().toLowerCase())
                .httpCode(HttpStatus.BAD_REQUEST.value()).build(), HttpStatus.OK);
      }
      DataPagingResponse<ConfigDto> configs = configService
          .findAll(page - 1, limit, key, search, sort);
      return new ResponseEntity<>(
          GetMethodResponse.builder().data(configs).status(true).message(Constants.SUCCESS_MSG)
              .errorCode(HttpStatus.OK.name().toLowerCase())
              .httpCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
    } catch (ProxyAuthenticationException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(BaseMethodResponse.builder().status(false).message(e.getMessage())
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

  /**
   * Lấy thông tin chi tiết config theo id
   *
   * @param request
   * @param response
   * @param id
   * @return ResponseMessage
   */
  @GetMapping("/system/{id}")
  public ResponseEntity<?> getConfig(HttpServletRequest request, HttpServletResponse response,
      @PathVariable("id") int id) {
    try {
      if (!authGuard
          .checkPermission(request, null, PermissionObjectCode.SYS_CONFIG,
              ServicePermissionCode.SYSTEM_CONFIG_GET_BY_ID)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.OK.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build(), HttpStatus.OK);
      }
      ConfigDto config = configService.findById(id);
      return new ResponseEntity<>(
          GetMethodResponse.builder().status(true).data(config).message(Constants.SUCCESS_MSG)
              .errorCode(HttpStatus.OK.name().toLowerCase())
              .httpCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
    } catch (ResourceNotFoundException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(BaseMethodResponse.builder().status(false).message(e.getMessage())
          .errorCode(e.getMessageCode()).httpCode(HttpStatus.NOT_FOUND.value()).build(),
          HttpStatus.OK);
    } catch (ProxyAuthenticationException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(BaseMethodResponse.builder().status(false).message(e.getMessage())
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

  @GetMapping("/system/keys")
  public ResponseEntity<?> findAllKey(HttpServletRequest request, HttpServletResponse response) {
    try {
      authGuard.checkAuthorization(request);
      List<String> keys = configService.findAllKey();
      return new ResponseEntity<>(
          GetMethodResponse.builder().status(true).message(Constants.SUCCESS_MSG).data(keys)
              .errorCode(HttpStatus.OK.name().toLowerCase())
              .httpCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
    } catch (ProxyAuthenticationException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(BaseMethodResponse.builder().status(false).message(e.getMessage())
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

  /**
   * Thêm mới config
   *
   * @param request
   * @param response
   * @param configDto
   * @return ResponseMessage
   */
  @PostMapping("/system")
  public ResponseEntity<?> createNewConfig(HttpServletRequest request, HttpServletResponse response,
      @RequestBody @Valid ConfigDto configDto) {
    try {
      if (!authGuard
          .checkPermission(request, null, PermissionObjectCode.SYS_CONFIG,
              ServicePermissionCode.SYSTEM_CONFIG_ADD)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build(), HttpStatus.OK);
      }
      Integer uid = authGuard.getUserId(request);
      ConfigDto dto = configService.addConfig(configDto, uid);
      return new ResponseEntity<>(
          PostMethodResponse.builder().status(true).message(Constants.SUCCESS_MSG).id(dto.getId())
              .errorCode(HttpStatus.OK.name().toLowerCase())
              .httpCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
    } catch (ResourceNotFoundException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.NOT_FOUND.value()).build(),
          HttpStatus.OK);
    } catch (ProxyAuthenticationException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).httpCode(HttpStatus.UNAUTHORIZED.value())
              .errorCode(e.getMessageCode()).message(e.getMessage()).build(), HttpStatus.OK);
    } catch (Exception e) {
      logger.error(e.getMessage());
      return new ResponseEntity<>(BaseMethodResponse.builder().status(false)
          .message(Constants.INTERNAL_SERVER_ERROR)
          .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
          .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build(), HttpStatus.OK);
    }
  }

  /**
   * Sửa config
   *
   * @param request
   * @param response
   * @param dto
   * @return ResponseMessage
   */
  @PutMapping("/system/{id}")
  public ResponseEntity<?> updateConfig(HttpServletRequest request, HttpServletResponse response,
      @RequestBody @Valid ConfigDto dto, @PathVariable("id") int id) {
    try {
      if (!authGuard
          .checkPermission(request, null, PermissionObjectCode.SYS_CONFIG,
              ServicePermissionCode.SYSTEM_CONFIG_UPDATE)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build(), HttpStatus.OK);
      }
      Integer uid = authGuard.getUserId(request);
      dto.setId(id);
      ConfigDto config = configService.update(dto, uid);
      return new ResponseEntity<>(
          PutMethodResponse.builder().status(true).message(Constants.SUCCESS_MSG)
              .errorCode(HttpStatus.OK.name().toLowerCase())
              .httpCode(HttpStatus.OK.value()).id(config.getId()).build(), HttpStatus.OK);
    } catch (ResourceNotFoundException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).httpCode(HttpStatus.NOT_FOUND.value())
              .errorCode(e.getMessageCode()).message(e.getMessage()).build(), HttpStatus.OK);
    } catch (ProxyAuthenticationException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).httpCode(HttpStatus.UNAUTHORIZED.value())
              .message(e.getMessage()).errorCode(e.getMessageCode()).build(), HttpStatus.OK);
    } catch (Exception e) {
      logger.error(e.getMessage());
      return new ResponseEntity<>(BaseMethodResponse.builder().status(false)
          .message(Constants.INTERNAL_SERVER_ERROR)
          .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
          .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build(), HttpStatus.OK);
    }
  }

  /**
   * Xóa config theo id
   *
   * @param request
   * @param response
   * @param id
   * @return ResponseMessage
   */

  @DeleteMapping("/system/{id}")
  public ResponseEntity<?> deleteConfig(HttpServletRequest request, HttpServletResponse response,
      @PathVariable("id") int id) {
    try {
      if (!authGuard
          .checkPermission(request, null, PermissionObjectCode.SYS_CONFIG,
              ServicePermissionCode.SYSTEM_CONFIG_DELETE)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build(), HttpStatus.OK);
      }
      Integer uid = authGuard.getUserId(request);
      configService.delete(id, uid);
      return new ResponseEntity<>(
          DeleteMethodResponse.builder().status(true).message(Constants.SUCCESS_MSG)
              .errorCode(HttpStatus.OK.name().toLowerCase())
              .httpCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
    } catch (ResourceNotFoundException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(BaseMethodResponse.builder().status(false)
          .errorCode(e.getMessageCode()).message(e.getMessage())
          .httpCode(HttpStatus.NOT_FOUND.value()).build(), HttpStatus.OK);
    } catch (OperationNotImplementException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.BAD_REQUEST.value())
              .build(), HttpStatus.OK);
    } catch (ProxyAuthenticationException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.UNAUTHORIZED.value())
              .build(), HttpStatus.OK);
    } catch (Exception e) {
      logger.error(e.getMessage());
      return new ResponseEntity<>(BaseMethodResponse.builder().status(false)
          .message(Constants.INTERNAL_SERVER_ERROR)
          .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
          .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build(), HttpStatus.OK);
    }
  }


  @GetMapping("/system")
  public ResponseEntity<?> findAllConfigByKey(
      HttpServletRequest request, HttpServletResponse response,
      @RequestParam(name = "key", required = false, defaultValue = "") String key) {
    try {
      List<ConfigCustomDto> configs = configService.findAllByKey(key);
      return new ResponseEntity<>(
          GetMethodResponse.builder().status(true).data(configs)
              .httpCode(HttpStatus.OK.value())
              .errorCode(HttpStatus.OK.name().toLowerCase())
              .message(Constants.SUCCESS_MSG).build(), HttpStatus.OK);
    } catch (Exception e) {
      logger.error(e.getMessage());
      return new ResponseEntity<>(BaseMethodResponse.builder().status(false)
          .message(Constants.INTERNAL_SERVER_ERROR)
          .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
          .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build(), HttpStatus.OK);
    }
  }
}
