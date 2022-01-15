package com.ttt.mar.leads.controller;

import com.ttt.mar.leads.config.PermissionObjectCode;
import com.ttt.mar.leads.config.ServicePermissionCode;
import com.ttt.mar.leads.dto.ProjectDetailResponse;
import com.ttt.mar.leads.dto.ProjectDtoToCampaignManage;
import com.ttt.mar.leads.dto.ProjectRequestDto;
import com.ttt.mar.leads.dto.ProjectResponseDto;
import com.ttt.mar.leads.dto.ProjectUpdateRequest;
import com.ttt.mar.leads.dto.ProjectUpdateStatusDto;
import com.ttt.mar.leads.entities.ProjectStatus;
import com.ttt.mar.leads.service.iface.ProjectService;
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
 * @author kietdt
 * @created_date 20/04/2021
 */
@Api(tags = "API for Project")
@RestController
public class ProjectController {

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
      .getLogger(ProjectController.class);

  private final ProjectService projectService;
  private final AuthGuard authGuard;

  public ProjectController(ProjectService projectService, AuthGuard authGuard) {
    this.projectService = projectService;
    this.authGuard = authGuard;
  }


  @ApiOperation(value = "Api create new Project")
  @PostMapping("/project")
  public ResponseEntity<?> createProject(
      HttpServletRequest request, HttpServletResponse response,
      @RequestBody @Valid ProjectRequestDto dto) {
    try {
      if (!authGuard.checkPermission(request, null, PermissionObjectCode.LEADS,
          ServicePermissionCode.ADD_PROJECT)) {
        return new ResponseEntity(
            PostMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build(), HttpStatus.OK);
      }
      Integer userId = authGuard.getUserId(request);
      Integer id = projectService.createProject(userId, dto);
      return new ResponseEntity<>(
          PostMethodResponse.builder().status(true).id(id).message(Constants.SUCCESS_MSG)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value()).build()
          , HttpStatus.OK);
    } catch (ResourceNotFoundException e) {
      logger.warn(e.getMessage(), e);
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.NOT_FOUND.value()).build()
          , HttpStatus.OK);
    } catch (OperationNotImplementException e) {
      logger.warn(e.getMessage(), e);
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.BAD_REQUEST.value()).build(),
          HttpStatus.OK);
    } catch (ProxyAuthenticationException e) {
      logger.warn(e.getMessage(), e);
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.UNAUTHORIZED.value()).build(),
          HttpStatus.OK);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      return new ResponseEntity<>(BaseMethodResponse.builder().status(false)
          .message(Constants.INTERNAL_SERVER_ERROR)
          .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
          .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build(), HttpStatus.OK);
    }
  }

  @ApiOperation(value = "Api update status for project")
  @PutMapping("/project/{id}/status")
  public ResponseEntity<PutMethodResponse<Integer>> updateStatusProject(
      HttpServletRequest request, HttpServletResponse response,
      @PathVariable(name = "id") Integer id, @RequestBody @Valid ProjectUpdateStatusDto dto) {
    try {
      if (!authGuard.checkPermission(request, null, PermissionObjectCode.LEADS,
          ServicePermissionCode.UPDATE_STATUS_PROJECT)) {
        return new ResponseEntity(
            PostMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build(), HttpStatus.OK);
      }
      Integer userId = authGuard.getUserId(request);
      dto.setId(id);
      projectService.updateProjectStatus(userId, dto);
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

  @ApiOperation(value = "Api detail Project")
  @GetMapping("/project/{id}")
  public ResponseEntity<GetMethodResponse<ProjectDetailResponse>> getProjectById(
      HttpServletRequest request, HttpServletResponse response,
      @PathVariable(name = "id") Integer id) {
    try {
      if (!authGuard.checkPermission(request, null, PermissionObjectCode.LEADS,
          ServicePermissionCode.GET_PROJECT_BY_ID)) {
        return new ResponseEntity(
            PostMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build(), HttpStatus.OK);
      }
      ProjectDetailResponse responseDto = projectService.getById(id);
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

  @ApiOperation(value = "Api get all project with pagination")
  @GetMapping("/projects")
  public ResponseEntity<?> getAll(HttpServletRequest request,
      @ApiParam(value = "Page number", example = "1")
      @RequestParam(value = "page", defaultValue = "1", required = false) Integer page,
      @ApiParam(value = "Record limit", example = "10")
      @RequestParam(value = "limit", defaultValue = "10", required = false) Integer limit,
      @ApiParam(value = "Start date", example = "10")
      @RequestParam(value = "startDate", required = false) Long startDate,
      @ApiParam(value = "End date", example = "10")
      @RequestParam(value = "endDate", required = false) Long endDate,
      @ApiParam(value = "Project status", example = "[ACTIVE, DEACTIVE]")
      @RequestParam(value = "status", required = false) ProjectStatus status,
      @ApiParam(value = "Search value: search by name, code and partner_code", example = "prject A")
      @RequestParam(value = "search", defaultValue = "", required = false) String search,
      @ApiParam(value = "sort format", example = "name_asc")
      @RequestParam(value = "sort", defaultValue = "", required = false) String sort) {
    try {
      if (!authGuard.checkPermission(request, null, PermissionObjectCode.LEADS,
          ServicePermissionCode.GET_ALL_PROJECT)) {
        return new ResponseEntity<>(
            PostMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build(), HttpStatus.OK);
      }
      DataPagingResponse<ProjectResponseDto> data = projectService
          .getAll(page, limit, startDate, endDate, status, search, sort);
      return new ResponseEntity<>(GetMethodResponse.builder()
          .status(true).message(Constants.SUCCESS_MSG).data(data)
          .errorCode(HttpStatus.OK.name().toLowerCase())
          .httpCode(HttpStatus.OK.value())
          .build(), HttpStatus.OK);
    } catch (ProxyAuthenticationException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(BaseMethodResponse.builder()
          .status(false).message(e.getMessage())
          .errorCode(e.getMessageCode())
          .httpCode(HttpStatus.UNAUTHORIZED.value())
          .build(), HttpStatus.OK);
    } catch (Exception e) {
      logger.error(e.getMessage());
      return new ResponseEntity<>(BaseMethodResponse.builder()
          .status(false).message(Constants.INTERNAL_SERVER_ERROR)
          .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
          .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
          .build(), HttpStatus.OK);
    }
  }

  @ApiOperation(value = "Api update project")
  @PutMapping("/project/{id}")
  public ResponseEntity<PutMethodResponse<Integer>> updateProject(
      HttpServletRequest request, HttpServletResponse response,
      @PathVariable(name = "id") Integer id, @RequestBody @Valid ProjectUpdateRequest dto) {
    try {
      if (!authGuard.checkPermission(request, null, PermissionObjectCode.LEADS,
          ServicePermissionCode.UPDATE_PROJECT)) {
        return new ResponseEntity(
            PostMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build(), HttpStatus.OK);
      }
      Integer userId = authGuard.getUserId(request);
      dto.setId(id);
      projectService.updateProject(userId, dto);
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

  @ApiOperation(value = "Api xóa dự án ")
  @DeleteMapping("/project/{id}")
  public ResponseEntity<DeleteMethodResponse> deleteProject(HttpServletRequest request,
      HttpServletResponse response,
      @PathVariable(name = "id") Integer id) {
    try {
      Integer userId = authGuard.getUserId(request);
      Boolean result = projectService.deleteProject(userId, id);
      return new ResponseEntity(
          DeleteMethodResponse.builder().status(result).message(Constants.SUCCESS_MSG)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value())
              .build(),
          HttpStatus.OK);
    } catch (ResourceNotFoundException e) {
      logger.warn(e.getMessage(), e);
      return new ResponseEntity(BaseMethodResponse.builder().status(false).message(e.getMessage())
          .errorCode(e.getMessageCode()).httpCode(HttpStatus.NOT_FOUND.value()).build(),
          HttpStatus.OK);
    } catch (OperationNotImplementException e) {
      logger.warn(e.getMessage(), e);
      return new ResponseEntity(BaseMethodResponse.builder().status(false).message(e.getMessage())
          .errorCode(e.getMessageCode()).httpCode(HttpStatus.BAD_REQUEST.value()).build(),
          HttpStatus.OK);
    } catch (ProxyAuthenticationException e) {
      logger.warn(e.getMessage(), e);
      return new ResponseEntity(BaseMethodResponse.builder().status(false).message(e.getMessage())
          .errorCode(e.getMessageCode()).httpCode(HttpStatus.UNAUTHORIZED.value()).build(),
          HttpStatus.OK);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      return new ResponseEntity(
          BaseMethodResponse.builder().status(false).message(Constants.INTERNAL_SERVER_ERROR)
              .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
              .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build(),
          HttpStatus.OK);
    }
  }

  @ApiOperation(value = "Api get Project for campaign manage")
  @GetMapping("/project/filter")
  public ResponseEntity<List<ProjectDtoToCampaignManage>> getProjectForCampaignManage(
      @ApiParam(value = "status", example = "ACTIVE,DEACTIVE")
      @RequestParam(value = "status", required = false) ProjectStatus status,
      HttpServletRequest request) {
    try {
//      authGuard.checkAuthorization(request);
//      Integer userId = authGuard.getUserId(request);
      List<ProjectDtoToCampaignManage> responseDto = projectService
          .getProjectToCampaignManage(status);
      return new ResponseEntity(
          GetMethodResponse.builder().status(true).message(Constants.SUCCESS_MSG).data(responseDto)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value()).build()
          , HttpStatus.OK);
    }
//    catch (ProxyAuthenticationException e) {
//      logger.warn(e.getMessage(), e);
//      return new ResponseEntity(
//          BaseMethodResponse.builder().status(false).message(e.getMessage())
//              .errorCode(e.getMessageCode()).httpCode(HttpStatus.UNAUTHORIZED.value()).build(),
//          HttpStatus.OK);
//    }
    catch (Exception e) {
      logger.error(e.getMessage(), e);
      return new ResponseEntity(BaseMethodResponse.builder().status(false)
          .message(Constants.INTERNAL_SERVER_ERROR)
          .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
          .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build(), HttpStatus.OK);
    }
  }
}
