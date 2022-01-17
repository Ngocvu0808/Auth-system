package com.ttt.mar.config.controller;

import com.ttt.mar.config.config.PermissionObjectCode;
import com.ttt.mar.config.config.ServicePermissionCode;
import com.ttt.mar.config.dto.FieldConfigDto;
import com.ttt.mar.config.dto.FilterConfigDto;
import com.ttt.mar.config.dto.MarFieldConfigObjectDto;
import com.ttt.mar.config.service.iface.FieldConfigService;
import com.ttt.mar.config.service.iface.MarFieldConfigObjectService;
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
public class FieldConfigController {

  private static final Logger logger = LoggerFactory.getLogger(FieldConfigController.class);
  private final FieldConfigService configService;
  private final AuthGuard authGuard;
  private final MarFieldConfigObjectService marFieldConfigObjectService;

  public FieldConfigController(FieldConfigService configService,
      AuthGuard authGuard, MarFieldConfigObjectService marFieldConfigObjectService) {
    this.configService = configService;
    this.authGuard = authGuard;
    this.marFieldConfigObjectService = marFieldConfigObjectService;
  }

  @GetMapping("fields")
  public ResponseEntity<?> getAllConfig(HttpServletRequest request, HttpServletResponse response,
      @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
      @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
      @RequestParam(name = "type", required = false, defaultValue = "") String type,
      @RequestParam(name = "search", required = false, defaultValue = "") String search,
      @RequestParam(name = "sort", required = false, defaultValue = "") String sort) {
    try {
      if (!authGuard
          .checkPermission(request, null, PermissionObjectCode.FIELD_CONFIG,
              ServicePermissionCode.FIELD_CONFIG_GET_ALL)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build(), HttpStatus.OK);
      }
      if (!SortingUtils.validateSort(sort, FieldConfigDto.class)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.SORTING_INVALID)
                .errorCode(HttpStatus.BAD_REQUEST.name().toLowerCase())
                .httpCode(HttpStatus.BAD_REQUEST.value()).build(), HttpStatus.OK);
      }
      DataPagingResponse<FieldConfigDto> configs = configService
          .findAll(page - 1, limit, type, search, sort);
      return new ResponseEntity<>(
          GetMethodResponse.builder().data(configs).status(true)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value())
              .message(Constants.SUCCESS_MSG).build(), HttpStatus.OK);
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

  @GetMapping("field/{id}")
  public ResponseEntity<?> getConfig(HttpServletRequest request, HttpServletResponse response,
      @PathVariable("id") int id) {
    try {
      if (!authGuard
          .checkPermission(request, null, PermissionObjectCode.FIELD_CONFIG,
              ServicePermissionCode.FIELD_CONFIG_GET_BY_ID)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build(), HttpStatus.OK);
      }
      FieldConfigDto config = configService.findById(id);
      return new ResponseEntity<>(GetMethodResponse.builder().status(true).data(config)
          .errorCode(HttpStatus.OK.name().toLowerCase())
          .httpCode(HttpStatus.OK.value()).message(Constants.SUCCESS_MSG)
          .build(), HttpStatus.OK);
    } catch (IdentifyBlankException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.BAD_REQUEST.value())
              .build(), HttpStatus.OK);
    } catch (ResourceNotFoundException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          GetMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.NOT_FOUND.value()).build(),
          HttpStatus.NOT_FOUND);
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

  @PostMapping("field")
  public ResponseEntity<?> addNewConfig(HttpServletRequest request, HttpServletResponse response,
      @RequestBody @Valid FieldConfigDto dto) {
    try {
      if (!authGuard
          .checkPermission(request, null, PermissionObjectCode.FIELD_CONFIG,
              ServicePermissionCode.FIELD_CONFIG_ADD)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build(), HttpStatus.OK);
      }
      Integer uid = authGuard.getUserId(request);
      FieldConfigDto config = configService.addConfig(dto, uid);
      return new ResponseEntity<>(
          PostMethodResponse.builder().status(true).message(Constants.SUCCESS_MSG)
              .errorCode(HttpStatus.OK.name().toLowerCase())
              .id(config.getId()).httpCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
    } catch (ResourceNotFoundException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.NOT_FOUND.value())
              .build(), HttpStatus.OK);
    } catch (ProxyAuthenticationException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.UNAUTHORIZED.value())
              .build(), HttpStatus.OK);
    } catch (DuplicateEntityException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.CONFLICT.value())
              .build(), HttpStatus.OK);
    } catch (Exception e) {
      logger.error(e.getMessage());
      return new ResponseEntity<>(BaseMethodResponse.builder().status(false)
          .message(Constants.INTERNAL_SERVER_ERROR)
          .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
          .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build(), HttpStatus.OK);
    }
  }

  @PutMapping("field/{id}")
  public ResponseEntity<?> updateConfig(HttpServletRequest request, HttpServletResponse response,
      @RequestBody @Valid FieldConfigDto dto, @PathVariable("id") int id) {
    try {
      if (!authGuard
          .checkPermission(request, null, PermissionObjectCode.FIELD_CONFIG,
              ServicePermissionCode.FIELD_CONFIG_UPDATE)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build(), HttpStatus.OK);
      }
      Integer uid = authGuard.getUserId(request);
      dto.setId(id);
      FieldConfigDto res = configService.update(dto, uid);
      return new ResponseEntity<>(
          PutMethodResponse.builder().status(true).message(Constants.SUCCESS_MSG).id(res.getId())
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value())
              .build(), HttpStatus.OK);
    } catch (IdentifyBlankException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).httpCode(HttpStatus.BAD_REQUEST.value())
              .message(e.getMessage()).errorCode(e.getMessageCode()).build(), HttpStatus.OK);
    } catch (ResourceNotFoundException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).httpCode(HttpStatus.NOT_FOUND.value())
              .message(e.getMessage()).errorCode(e.getMessageCode()).build(), HttpStatus.OK);
    } catch (ProxyAuthenticationException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).httpCode(HttpStatus.UNAUTHORIZED.value())
              .message(e.getMessage()).errorCode(e.getMessageCode()).build(), HttpStatus.OK);
    } catch (DuplicateEntityException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).httpCode(HttpStatus.CONFLICT.value())
              .message(e.getMessage()).errorCode(e.getMessageCode()).build(), HttpStatus.OK);
    } catch (OperationNotImplementException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).httpCode(HttpStatus.LOCKED.value())
              .message(e.getMessage()).errorCode(e.getMessageCode()).build(), HttpStatus.OK);
    } catch (Exception e) {
      logger.error(e.getMessage());
      return new ResponseEntity<>(BaseMethodResponse.builder().status(false)
          .message(Constants.INTERNAL_SERVER_ERROR)
          .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
          .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build(), HttpStatus.OK);
    }
  }

  @DeleteMapping("field/{id}")
  public ResponseEntity<?> deleteConfig(HttpServletRequest request, HttpServletResponse response,
      @PathVariable("id") int id) {
    try {
      if (!authGuard
          .checkPermission(request, null, PermissionObjectCode.FIELD_CONFIG,
              ServicePermissionCode.FIELD_CONFIG_DELETE)) {
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
    } catch (IdentifyBlankException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).httpCode(HttpStatus.BAD_REQUEST.value())
              .message(e.getMessage()).errorCode(e.getMessageCode()).build(), HttpStatus.OK);
    } catch (ResourceNotFoundException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).httpCode(HttpStatus.NOT_FOUND.value())
              .message(e.getMessage()).errorCode(e.getMessageCode()).build(), HttpStatus.OK);
    } catch (OperationNotImplementException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).httpCode(HttpStatus.LOCKED.value())
              .message(e.getMessage()).errorCode(e.getMessageCode()).build(), HttpStatus.OK);
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

  @GetMapping("field/filters")
  public ResponseEntity<?> getFilter(HttpServletRequest request, HttpServletResponse response,
      @RequestParam(name = "service") String service, @RequestParam(name = "type") String type) {
    try {
      authGuard.checkAuthorization(request);
      List<FilterConfigDto> filters = configService.getFilter(service, type);
      return new ResponseEntity<>(GetMethodResponse.builder().status(true).data(filters)
          .errorCode(HttpStatus.OK.name().toLowerCase())
          .httpCode(HttpStatus.OK.value()).message(Constants.SUCCESS_MSG).build(), HttpStatus.OK);
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

  @GetMapping("field")
  public ResponseEntity<?> getAllConfigByType(HttpServletRequest request,
      HttpServletResponse response, @RequestParam(name = "type") String type) {
    try {
      authGuard.checkAuthorization(request);
      List<FieldConfigDto> dtos = configService.getAllByType(type);
      return new ResponseEntity<>(GetMethodResponse.builder().status(true).data(dtos)
          .errorCode(HttpStatus.OK.name().toLowerCase())
          .httpCode(HttpStatus.OK.value()).message(Constants.SUCCESS_MSG).build(), HttpStatus.OK);
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

  @GetMapping("field/objects")
  public ResponseEntity<?> getFieldConfigObjects(HttpServletRequest request,
      HttpServletResponse response) {
    try {
      List<MarFieldConfigObjectDto> dtos = marFieldConfigObjectService.findAll();
      return new ResponseEntity<>(GetMethodResponse.builder().status(true).data(dtos)
          .errorCode(HttpStatus.OK.name().toLowerCase())
          .httpCode(HttpStatus.OK.value()).message(Constants.SUCCESS_MSG).build(), HttpStatus.OK);
    } catch (Exception e) {
      logger.error(e.getMessage());
      return new ResponseEntity<>(BaseMethodResponse.builder().status(false)
          .message(Constants.INTERNAL_SERVER_ERROR)
          .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
          .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build(), HttpStatus.OK);
    }
  }

  @GetMapping("field/filter")
  public ResponseEntity<?> getConfigByTypeAndObject(HttpServletRequest request,
      @RequestParam(name = "type") String type, @RequestParam(name = "objects") String objects) {
    try {
      authGuard.checkAuthorization(request);
      List<FieldConfigDto> data = configService.getAllByTypeAndObject(type, objects);
      return new ResponseEntity<>(GetMethodResponse.builder().status(true).data(data)
          .errorCode(HttpStatus.OK.name().toLowerCase())
          .httpCode(HttpStatus.OK.value()).message(Constants.SUCCESS_MSG).build(), HttpStatus.OK);
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
}
