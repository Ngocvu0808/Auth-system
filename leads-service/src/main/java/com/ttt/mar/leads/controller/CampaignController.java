package com.ttt.mar.leads.controller;

import com.ttt.mar.leads.config.PermissionObjectCode;
import com.ttt.mar.leads.config.ServicePermissionCode;
import com.ttt.mar.leads.dto.CampaignRequestDto;
import com.ttt.mar.leads.dto.CampaignResponse;
import com.ttt.mar.leads.dto.CampaignResponseDto;
import com.ttt.mar.leads.dto.CampaignStatusDto;
import com.ttt.mar.leads.dto.CampaignUpdateDto;
import com.ttt.mar.leads.dto.CampaignsCodeResponseDto;
import com.ttt.mar.leads.dto.OfferCampaignResponseDto;
import com.ttt.mar.leads.dto.OfferResponseListDto;
import com.ttt.mar.leads.entities.Filter;
import com.ttt.mar.leads.entities.Offer;
import com.ttt.mar.leads.entities.ProjectStatus;
import com.ttt.mar.leads.service.iface.CampaignService;
import com.ttt.rnd.common.config.Constants;
import com.ttt.rnd.common.dto.BaseMethodResponse;
import com.ttt.rnd.common.dto.DataPagingResponse;
import com.ttt.rnd.common.dto.GetMethodResponse;
import com.ttt.rnd.common.dto.PostMethodResponse;
import com.ttt.rnd.common.dto.PutMethodResponse;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ProxyAuthenticationException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import com.ttt.rnd.common.exception.UserNotFoundException;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author NamBN
 * @created_date 08/06/2021
 */
@RestController
@Api(tags = "API for Campaign")
public class CampaignController {

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
      .getLogger(CampaignController.class);


  private final CampaignService campaignService;

  private final AuthGuard authGuard;

  public CampaignController(CampaignService campaignService, AuthGuard authGuard) {
    this.campaignService = campaignService;
    this.authGuard = authGuard;
  }

  @ApiOperation(value = "Api get list Campaigns")
  @GetMapping("/campaigns")
  public ResponseEntity<GetMethodResponse<DataPagingResponse<CampaignResponse>>> getListCampaigns(
      @ApiParam(value = "filter by project id")
      @RequestParam(value = "projectId", required = false) Integer projectId,
      @ApiParam(value = "filter by lead source ")
      @RequestParam(value = "leadSourceId", required = false) Integer leadSourceId,
      @ApiParam(value = "filter by distribute ")
      @RequestParam(value = "distributeId", required = false) Integer distributeId,
      @ApiParam(value = "filter by project status ")
      @RequestParam(value = "projectStatus", required = false) ProjectStatus projectStatus,
      @ApiParam(value = "full text search ")
      @RequestParam(value = "search", required = false) String search,
      @ApiParam(value = "Sort", example = "name_asc")
      @RequestParam(value = "sort", required = false, defaultValue = "") String sort,
      @ApiParam(value = "Number page", example = "1")
      @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
      @ApiParam(value = "Limit data", example = "10")
      @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
      HttpServletRequest request) {
    try {
      if (!authGuard.checkPermission(request, null, PermissionObjectCode.LEADS,
          ServicePermissionCode.GET_ALL_CAMPAIGN)) {
        return new ResponseEntity(
            PostMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build(), HttpStatus.OK);
      }
      DataPagingResponse<CampaignResponse> dataResponse = campaignService
          .getListCampaignFilter(projectId, leadSourceId, distributeId, projectStatus, search,
              sort,
              page, limit);
      return new ResponseEntity(
          GetMethodResponse.builder().status(true).message(Constants.SUCCESS_MSG)
              .data(dataResponse)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value())
              .build()
          , HttpStatus.OK);
    } catch (ProxyAuthenticationException e) {
      logger.warn(e.getMessage(), e);
      return new ResponseEntity(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.UNAUTHORIZED.value())
              .build(),
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


  @ApiOperation(value = "Api create new campaign")
  @PostMapping("/campaign")
  public ResponseEntity<PostMethodResponse<Integer>> createCampaign(
      HttpServletRequest request, HttpServletResponse response,
      @Valid @RequestBody CampaignRequestDto dto) {
    try {
      if (!authGuard.checkPermission(request, null, PermissionObjectCode.LEADS,
          ServicePermissionCode.ADD_CAMPAIGN)) {
        return new ResponseEntity(
            PostMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build(), HttpStatus.OK);
      }
      Integer userId = authGuard.getUserId(request);
//      Integer userId = 1;
      Integer id = campaignService.createCampaign(userId, dto);
      return new ResponseEntity(
          PostMethodResponse.builder().status(true).id(id).message(Constants.SUCCESS_MSG)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value())
              .build()
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
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.BAD_REQUEST.value())
              .build(),
          HttpStatus.OK);
    } catch (ProxyAuthenticationException | UserNotFoundException e) {
      logger.warn(e.getMessage(), e);
      return new ResponseEntity(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.UNAUTHORIZED.value())
              .build(),
          HttpStatus.OK);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      return new ResponseEntity(BaseMethodResponse.builder().status(false)
          .message(Constants.INTERNAL_SERVER_ERROR)
          .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
          .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build(), HttpStatus.OK);
    }
  }

  @ApiOperation(value = "Api update campaign")
  @PutMapping("/campaign/{id}")
  public ResponseEntity<PutMethodResponse<Integer>> updateCampaign(
      HttpServletRequest request, HttpServletResponse response,
      @PathVariable(name = "id") Integer id, @RequestBody @Valid CampaignUpdateDto dto) {
    try {
      if (!authGuard.checkPermission(request, null, PermissionObjectCode.LEADS,
          ServicePermissionCode.UPDATE_CAMPAIGN)) {
        return new ResponseEntity(
            PostMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build(), HttpStatus.OK);
      }
      Integer userId = authGuard.getUserId(request);
      dto.setId(id);
      campaignService.updateCampaign(userId, dto);
      return new ResponseEntity(PutMethodResponse.builder().status(true)
          .id(id).message(Constants.SUCCESS_MSG).errorCode(HttpStatus.OK.name().toLowerCase())
          .httpCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
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
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.BAD_REQUEST.value())
              .build(),
          HttpStatus.OK);
    } catch (ProxyAuthenticationException e) {
      logger.warn(e.getMessage(), e);
      return new ResponseEntity(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.UNAUTHORIZED.value())
              .build(),
          HttpStatus.OK);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      return new ResponseEntity(BaseMethodResponse.builder().status(false)
          .message(Constants.INTERNAL_SERVER_ERROR)
          .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
          .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build(), HttpStatus.OK);
    }
  }

  @ApiOperation(value = "Api detail campaign")
  @GetMapping("/campaign/{id}")
  public ResponseEntity<GetMethodResponse<CampaignResponseDto>> getLeadSourceById(
      HttpServletRequest request, HttpServletResponse response,
      @PathVariable(name = "id") Integer id) {
    try {
      if (!authGuard.checkPermission(request, null, PermissionObjectCode.LEADS,
          ServicePermissionCode.GET_CAMPAIGN_DETAIL)) {
        return new ResponseEntity(
            PostMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build(), HttpStatus.OK);
      }
      CampaignResponseDto responseDto = campaignService.getByID(id);
      return new ResponseEntity(
          GetMethodResponse.builder().status(true).message(Constants.SUCCESS_MSG)
              .data(responseDto)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value())
              .build()
          , HttpStatus.OK);
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

  @ApiOperation(value = "Api update status")
  @PutMapping("/campaign/{id}/status")
  public ResponseEntity<PutMethodResponse<Integer>> updateStatusCampaign(
      HttpServletRequest request, HttpServletResponse response,
      @PathVariable(name = "id") Integer id, @RequestBody CampaignStatusDto campaignStatusDto) {
    try {
      if (!authGuard.checkPermission(request, null, PermissionObjectCode.LEADS,
          ServicePermissionCode.UPDATE_STATUS_CAMPAIGN)) {
        return new ResponseEntity(
            PostMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build(), HttpStatus.OK);
      }
      Integer userId = authGuard.getUserId(request);
      campaignService.updateStatusCampaign(userId, id, campaignStatusDto);
      return new ResponseEntity(
          PutMethodResponse.builder().status(true).id(id).message(Constants.SUCCESS_MSG)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value())
              .build()
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
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.BAD_REQUEST.value())
              .build(),
          HttpStatus.OK);
    } catch (ProxyAuthenticationException e) {
      logger.warn(e.getMessage(), e);
      return new ResponseEntity(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.UNAUTHORIZED.value())
              .build(),
          HttpStatus.OK);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      return new ResponseEntity(BaseMethodResponse.builder().status(false)
          .message(Constants.INTERNAL_SERVER_ERROR)
          .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
          .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build(), HttpStatus.OK);
    }
  }

  @ApiOperation(value = "Api list offer of campaign")
  @GetMapping("/campaign/{id}/offers")
  public ResponseEntity<?> getOfferOfCampaign(
      HttpServletRequest request, HttpServletResponse response,
      @PathVariable(name = "id") Integer id,
      @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
      @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
      @RequestParam(name = "sort", required = false, defaultValue = "") String sort) {
    try {
      authGuard.checkAuthorization(request);
      if (SortingUtils.validateSort(sort, Offer.class)) {
        ;
      }
      DataPagingResponse<?> data = campaignService.getAllOfferCampaign(id, sort, page, limit);
      return new ResponseEntity<>(
          GetMethodResponse.builder().status(true).data(data).message(Constants.SUCCESS_MSG)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value())
              .build()
          , HttpStatus.OK);
    } catch (ProxyAuthenticationException e) {
      logger.warn(e.getMessage(), e);
      return new ResponseEntity(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.UNAUTHORIZED.value())
              .build(),
          HttpStatus.OK);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      return new ResponseEntity(BaseMethodResponse.builder().status(false)
          .message(Constants.INTERNAL_SERVER_ERROR)
          .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
          .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build(), HttpStatus.OK);
    }
  }

  @ApiOperation(value = "Api list filter of campaign")
  @GetMapping("/campaign/{id}/filters")
  public ResponseEntity<?> getFiltersOfCampaign(
      HttpServletRequest request, HttpServletResponse response,
      @PathVariable(name = "id") Integer id,
      @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
      @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
      @RequestParam(name = "sort", required = false, defaultValue = "") String sort) {
    try {
      authGuard.checkAuthorization(request);
      if (SortingUtils.validateSort(sort, Filter.class)) {
        ;
      }
      DataPagingResponse<?> data = campaignService.getAllFilterCampaign(id, sort, page, limit);
      return new ResponseEntity<>(
          GetMethodResponse.builder().status(true).data(data).message(Constants.SUCCESS_MSG)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value())
              .build()
          , HttpStatus.OK);

    } catch (ProxyAuthenticationException e) {
      logger.warn(e.getMessage(), e);
      return new ResponseEntity(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.UNAUTHORIZED.value())
              .build(),
          HttpStatus.OK);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      return new ResponseEntity(BaseMethodResponse.builder().status(false)
          .message(Constants.INTERNAL_SERVER_ERROR)
          .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
          .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build(), HttpStatus.OK);
    }
  }
  @ApiOperation("get list Campaigns Code")
  @GetMapping("/campaignsCode")
  public ResponseEntity<List<CampaignsCodeResponseDto>> getListCampaignsCode(
      HttpServletRequest request, HttpServletResponse response
  ){

    try {
      if (!authGuard.checkPermission(request, null, PermissionObjectCode.LEADS,
          ServicePermissionCode.GET_ALL_CAMPAIGN)) {
        return new ResponseEntity(
            PostMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build(), HttpStatus.OK);
      }
      List<CampaignsCodeResponseDto> dataResponse = campaignService.getCampaignsCode();

      return new ResponseEntity(
          GetMethodResponse.builder().status(true).message(Constants.SUCCESS_MSG)
              .data(dataResponse)
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
