package com.ttt.mar.leads.controller;

import com.ttt.mar.leads.config.PermissionObjectCode;
import com.ttt.mar.leads.config.ServicePermissionCode;
import com.ttt.mar.leads.dto.LeadSourceDetailResponseDto;
import com.ttt.mar.leads.dto.LeadSourceRequestDto;
import com.ttt.mar.leads.dto.LeadSourceResponse;
import com.ttt.mar.leads.dto.LeadSourceResponseDto;
import com.ttt.mar.leads.dto.LeadSourceUpdateStatusDto;
import com.ttt.mar.leads.dto.ValidationDto;
import com.ttt.mar.leads.entities.LeadSourceStatus;
import com.ttt.mar.leads.service.iface.LeadSourceService;
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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
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

/**
 * @author bontk
 * @created_date 22/03/2021
 */

@Api(tags = "API for Lead Source")
@RestController
public class LeadSourceController {

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
      .getLogger(LeadSourceController.class);

  private final LeadSourceService leadSourceService;
  private final AuthGuard authGuard;

  public LeadSourceController(LeadSourceService leadSourceService, AuthGuard authGuard) {
    this.leadSourceService = leadSourceService;
    this.authGuard = authGuard;
  }

  @ApiOperation(value = "Api get all Validation")
  @GetMapping("/validations")
  public ResponseEntity<GetMethodResponse<List<ValidationDto>>> getValidations(
      HttpServletRequest request, HttpServletResponse response) {
    try {
      authGuard.checkAuthorization(request);
      List<ValidationDto> validationDtos = leadSourceService.findAllValidation();
      return new ResponseEntity(
          GetMethodResponse.builder().status(true).message(Constants.SUCCESS_MSG)
              .data(validationDtos)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value())
              .build(), HttpStatus.OK);
    } catch (ProxyAuthenticationException e) {
      logger.warn(e.getMessage(), e);
      return new ResponseEntity(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.UNAUTHORIZED.value()).build(),
          HttpStatus.OK);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      return new ResponseEntity(BaseMethodResponse.builder().status(false)
          .message(Constants.INTERNAL_SERVER_ERROR)
          .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
          .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build(), HttpStatus.OK);
    }
  }

  @ApiOperation(value = "Api create new lead-source")
  @PostMapping("/source")
  public ResponseEntity<PostMethodResponse<Integer>> createLeadsSource(
      HttpServletRequest request, HttpServletResponse response,
      @RequestBody @Valid LeadSourceRequestDto dto) {
    try {
      if (!authGuard.checkPermission(request, null, PermissionObjectCode.LEADS,
          ServicePermissionCode.ADD_LEAD_SOURCE)) {
        return new ResponseEntity(
            PostMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build(), HttpStatus.OK);
      }
      Integer userId = authGuard.getUserId(request);
      Integer id = leadSourceService.createLeadSource(userId, dto);
      return new ResponseEntity(
          PostMethodResponse.builder().status(true).id(id).message(Constants.SUCCESS_MSG)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value()).build()
          , HttpStatus.OK);
    } catch (ResourceNotFoundException e) {
      logger.warn(e.getMessage(), e);
      return new ResponseEntity(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.NOT_FOUND.value()).build()
          , HttpStatus.OK);
    } catch (OperationNotImplementException e) {
      logger.warn(e.getMessage(), e);
      return new ResponseEntity(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.BAD_REQUEST.value()).build(),
          HttpStatus.OK);
    } catch (ProxyAuthenticationException e) {
      logger.warn(e.getMessage(), e);
      return new ResponseEntity(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.UNAUTHORIZED.value()).build(),
          HttpStatus.OK);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      return new ResponseEntity(BaseMethodResponse.builder().status(false)
          .message(Constants.INTERNAL_SERVER_ERROR)
          .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
          .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build(), HttpStatus.OK);
    }
  }

  @ApiOperation(value = "Api detail lead-source")
  @GetMapping("/source/{id}")
  public ResponseEntity<GetMethodResponse<LeadSourceDetailResponseDto>> getLeadSourceById(
      HttpServletRequest request, HttpServletResponse response,
      @PathVariable(name = "id") Integer id) {
    try {
      if (!authGuard.checkPermission(request, null, PermissionObjectCode.LEADS,
          ServicePermissionCode.GET_LEAD_SOURCE_BY_ID)) {
        return new ResponseEntity(
            PostMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build(), HttpStatus.OK);
      }
      LeadSourceDetailResponseDto responseDto = leadSourceService.getById(id);
      return new ResponseEntity(
          GetMethodResponse.builder().status(true).message(Constants.SUCCESS_MSG).data(responseDto)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value()).build()
          , HttpStatus.OK);
    } catch (IdentifyBlankException e) {
      logger.warn(e.getMessage(), e);
      return new ResponseEntity(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.BAD_REQUEST.value()).build(),
          HttpStatus.OK);
    } catch (ProxyAuthenticationException e) {
      logger.warn(e.getMessage(), e);
      return new ResponseEntity(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.UNAUTHORIZED.value()).build(),
          HttpStatus.OK);
    } catch (ResourceNotFoundException e) {
      logger.warn(e.getMessage(), e);
      return new ResponseEntity(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.NOT_FOUND.value()).build()
          , HttpStatus.OK);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      return new ResponseEntity(BaseMethodResponse.builder().status(false)
          .message(Constants.INTERNAL_SERVER_ERROR)
          .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
          .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build(), HttpStatus.OK);
    }
  }

  @ApiOperation(value = "Api update lead-source")
  @PutMapping("/source/{id}")
  public ResponseEntity<PutMethodResponse<Integer>> updateLeadsSource(
      HttpServletRequest request, HttpServletResponse response,
      @PathVariable(name = "id") Integer id, @RequestBody @Valid LeadSourceRequestDto dto) {
    try {
      if (!authGuard.checkPermission(request, null, PermissionObjectCode.LEADS,
          ServicePermissionCode.UPDATE_LEAD_SOURCE)) {
        return new ResponseEntity(
            PostMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build(), HttpStatus.OK);
      }
      Integer userId = authGuard.getUserId(request);
      dto.setId(id);
      leadSourceService.updateLeadSource(userId, dto);
      return new ResponseEntity(
          PutMethodResponse.builder().status(true).id(id).message(Constants.SUCCESS_MSG)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value()).build()
          , HttpStatus.OK);
    } catch (IdentifyBlankException e) {
      logger.warn(e.getMessage(), e);
      return new ResponseEntity(
          GetMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.BAD_REQUEST.value()).build(),
          HttpStatus.OK);
    } catch (ResourceNotFoundException e) {
      logger.warn(e.getMessage(), e);
      return new ResponseEntity(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.NOT_FOUND.value()).build()
          , HttpStatus.OK);
    } catch (OperationNotImplementException e) {
      logger.warn(e.getMessage(), e);
      return new ResponseEntity(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.BAD_REQUEST.value()).build(),
          HttpStatus.OK);
    } catch (ProxyAuthenticationException e) {
      logger.warn(e.getMessage(), e);
      return new ResponseEntity(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.UNAUTHORIZED.value()).build(),
          HttpStatus.OK);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      return new ResponseEntity(BaseMethodResponse.builder().status(false)
          .message(Constants.INTERNAL_SERVER_ERROR)
          .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
          .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build(), HttpStatus.OK);
    }
  }

  @GetMapping("/sources")
  public ResponseEntity<GetMethodResponse<DataPagingResponse<LeadSourceResponse>>> getLeadSourcesPaging(
      @ApiParam(value = "Status LeadSource", example = "[ACTIVE, DEACTIVE]")
      @RequestParam(value = "status", required = false) LeadSourceStatus status,
      @ApiParam(value = "Search for name source Lead", example = "UTM-Source")
      @RequestParam(value = "search", required = false) String search,
      @ApiParam(value = "Sort format", example = "name_asc")
      @RequestParam(value = "sort", required = false, defaultValue = "") String sort,
      @ApiParam(value = "Page Number", example = "1")
      @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
      @ApiParam(value = "Limit data", example = "10")
      @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
      HttpServletRequest request) {
    try {
      if (!authGuard
          .checkPermission(request, null, PermissionObjectCode.LEADS,
              ServicePermissionCode.GET_ALL_LEAD_SOURCE)) {
        return new ResponseEntity(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build(), HttpStatus.OK);
      }
      if (!SortingUtils.validateSort(sort, LeadSourceResponse.class)) {
        return new ResponseEntity(
            BaseMethodResponse.builder().status(false).message("Sort invalid")
                .errorCode(HttpStatus.BAD_REQUEST.name().toLowerCase())
                .httpCode(HttpStatus.BAD_REQUEST.value()).build(), HttpStatus.OK);
      }
      DataPagingResponse<LeadSourceResponse> leadSourcePaging = leadSourceService
          .getLeadSourceFilter(status, search, sort, page, limit);

      return new ResponseEntity(
          GetMethodResponse.builder().status(true).message(Constants.SUCCESS_MSG)
              .data(leadSourcePaging)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value()).build()
          , HttpStatus.OK);
    } catch (ProxyAuthenticationException e) {
      logger.warn(e.getMessage(), e);
      return new ResponseEntity(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.UNAUTHORIZED.value()).build(),
          HttpStatus.OK);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      return new ResponseEntity(BaseMethodResponse.builder().status(false)
          .message(Constants.INTERNAL_SERVER_ERROR)
          .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
          .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build(), HttpStatus.OK);
    }
  }

  @ApiOperation(value = "Api update status lead-source")
  @PutMapping("/source/{id}/status")
  public ResponseEntity<PutMethodResponse<Integer>> updateStatusLeadsSource(
      HttpServletRequest request, HttpServletResponse response,
      @PathVariable(name = "id") Integer id, @RequestBody @Valid LeadSourceUpdateStatusDto dto) {
    try {
      if (!authGuard.checkPermission(request, null, PermissionObjectCode.LEADS,
          ServicePermissionCode.UPDATE_STATUS_LEAD_SOURCE)) {
        return new ResponseEntity(
            PostMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build(), HttpStatus.OK);
      }
      Integer userId = authGuard.getUserId(request);
      dto.setId(id);
      leadSourceService.updateStatusLeadSource(userId, dto);
      return new ResponseEntity(
          PutMethodResponse.builder().status(true).id(id).message(Constants.SUCCESS_MSG)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value()).build()
          , HttpStatus.OK);
    } catch (ResourceNotFoundException e) {
      logger.warn(e.getMessage(), e);
      return new ResponseEntity(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.NOT_FOUND.value()).build()
          , HttpStatus.OK);
    } catch (OperationNotImplementException e) {
      logger.warn(e.getMessage(), e);
      return new ResponseEntity(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.BAD_REQUEST.value()).build(),
          HttpStatus.OK);
    } catch (ProxyAuthenticationException e) {
      logger.warn(e.getMessage(), e);
      return new ResponseEntity(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.UNAUTHORIZED.value()).build(),
          HttpStatus.OK);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      return new ResponseEntity(BaseMethodResponse.builder().status(false)
          .message(Constants.INTERNAL_SERVER_ERROR)
          .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
          .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build(), HttpStatus.OK);
    }
  }

  @ApiOperation(value = "Api delete lead-source")
  @DeleteMapping("/source/{id}")
  public ResponseEntity<DeleteMethodResponse> deleteLeadsSource(
      HttpServletRequest request, HttpServletResponse response,
      @PathVariable(name = "id") Integer id) {
    try {
      if (!authGuard.checkPermission(request, null, PermissionObjectCode.LEADS,
          ServicePermissionCode.DELETE_LEAD_SOURCE)) {
        return new ResponseEntity(
            PostMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build(), HttpStatus.OK);
      }
      Integer userId = authGuard.getUserId(request);
      Boolean result = leadSourceService.deleteLeadSource(userId, id);
      return new ResponseEntity(
          DeleteMethodResponse.builder().status(result).message(Constants.SUCCESS_MSG)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value()).build()
          , HttpStatus.OK);
    } catch (ResourceNotFoundException e) {
      logger.warn(e.getMessage(), e);
      return new ResponseEntity(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.NOT_FOUND.value()).build()
          , HttpStatus.OK);
    } catch (OperationNotImplementException e) {
      logger.warn(e.getMessage(), e);
      return new ResponseEntity(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.BAD_REQUEST.value()).build(),
          HttpStatus.OK);
    } catch (ProxyAuthenticationException e) {
      logger.warn(e.getMessage(), e);
      return new ResponseEntity(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.UNAUTHORIZED.value()).build(),
          HttpStatus.OK);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      return new ResponseEntity(BaseMethodResponse.builder().status(false)
          .message(Constants.INTERNAL_SERVER_ERROR)
          .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
          .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build(), HttpStatus.OK);
    }
  }

  @ApiOperation(value = "Api get lead source for campaign manage")
  @GetMapping("/source/filter")
  public ResponseEntity<List<LeadSourceResponseDto>> getProjectForCampaignManage(
      @ApiParam(value = "status", example = "ACTIVE,DEACTIVE")
      @RequestParam(value = "status", required = false) LeadSourceStatus status,
      @ApiParam(value = "projectId")
      @RequestParam(value = "projectId", required = false) Integer projectId,
      HttpServletRequest request) {
    try {
      authGuard.checkAuthorization(request);
      List<LeadSourceResponseDto> responseDto = leadSourceService
          .leadSourceToCampaignManage(status, projectId);
      return new ResponseEntity(
          GetMethodResponse.builder().status(true).message(Constants.SUCCESS_MSG).data(responseDto)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value()).build()
          , HttpStatus.OK);
    } catch (ProxyAuthenticationException e) {
      logger.warn(e.getMessage(), e);
      return new ResponseEntity(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.UNAUTHORIZED.value()).build(),
          HttpStatus.OK);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      return new ResponseEntity(BaseMethodResponse.builder().status(false)
          .message(Constants.INTERNAL_SERVER_ERROR)
          .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
          .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build(), HttpStatus.OK);
    }
  }
}
