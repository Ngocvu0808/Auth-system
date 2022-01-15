package com.ttt.mar.leads.controller;

import com.ttt.mar.leads.config.PermissionObjectCode;
import com.ttt.mar.leads.config.ServicePermissionCode;
import com.ttt.mar.leads.dto.ProjectUserRequestDto;
import com.ttt.mar.leads.dto.ProjectUserResponseDto;
import com.ttt.mar.leads.dto.UserResponseDto;
import com.ttt.mar.leads.service.iface.ProjectService;
import com.ttt.mar.leads.service.iface.ProjectUserService;
import com.ttt.rnd.common.config.Constants;
import com.ttt.rnd.common.dto.BaseMethodResponse;
import com.ttt.rnd.common.dto.DataPagingResponse;
import com.ttt.rnd.common.dto.DeleteMethodResponse;
import com.ttt.rnd.common.dto.GetMethodResponse;
import com.ttt.rnd.common.dto.PostMethodResponse;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ProxyAuthenticationException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import com.ttt.rnd.common.service.iface.AuthGuard;
import com.ttt.rnd.lib.entities.UserStatus;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author kietdt
 * @created_date 22/04/2021
 */
@Api(tags = "API for Project-User")
@RestController
public class ProjectUserController {

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
      .getLogger(ProjectUserController.class);

  private final ProjectUserService projectUserService;
  private final AuthGuard authGuard;

  public ProjectUserController(ProjectUserService projectUserService, AuthGuard authGuard) {
    this.projectUserService = projectUserService;
    this.authGuard = authGuard;
  }

  @ApiOperation(value = "Api create new list Project-User")
  @PostMapping("/project/{projectId}/user")
  public ResponseEntity<?> createProjectSource(
      HttpServletRequest request, HttpServletResponse response,
      @ApiParam(value = "Id of project", example = "1")
      @PathVariable(value = "projectId") Integer projectId,
      @RequestBody @Valid ProjectUserRequestDto dto) {
    try {
      if (!authGuard.checkPermission(request, null, PermissionObjectCode.LEADS,
          ServicePermissionCode.GET_LEAD_SOURCE_BY_ID)) {
        return new ResponseEntity(
            PostMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build(), HttpStatus.OK);
      }
      Integer userId = authGuard.getUserId(request);
      projectUserService.createProjectUsers(userId, projectId, dto);
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(true).message(Constants.SUCCESS_MSG)
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

  @ApiOperation(value = "Api get all User not exist Project")
  @GetMapping("/project/users")
  public ResponseEntity<GetMethodResponse<List<UserResponseDto>>> getUserNotExistProject(
      HttpServletRequest request, HttpServletResponse response,
      @RequestParam(name = "project_id") Integer projectId,
      @RequestParam(name = "status") UserStatus status) {
    try {
      authGuard.checkAuthorization(request);
      List<UserResponseDto> responseDto = projectUserService
          .findProjectUserNotExistProject(projectId, status);
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

  @ApiOperation(value = "Api get all user of Project")
  @GetMapping("/project/{projectId}/users")
  public ResponseEntity<GetMethodResponse<DataPagingResponse<ProjectUserResponseDto>>> getProjectUserFilter(
      HttpServletRequest request, HttpServletResponse response,
      @ApiParam(value = "Id of project", example = "1")
      @PathVariable(value = "projectId") Integer projectId,
      @ApiParam(value = "Sort format", example = "name_asc")
      @RequestParam(value = "sort", required = false, defaultValue = "") String sort,
      @ApiParam(value = "Page Number", example = "1")
      @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
      @ApiParam(value = "Limit data", example = "10")
      @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit) {
    try {
      if (!authGuard.checkPermission(request, null, PermissionObjectCode.LEADS,
          ServicePermissionCode.GET_PROJECT_BY_ID)) {
        return new ResponseEntity(
            PostMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build(), HttpStatus.OK);
      }
      DataPagingResponse<ProjectUserResponseDto> responseDto = projectUserService
          .getProjectUserFilter(projectId, page, limit, sort);
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

  @ApiOperation(value = "Api xóa nhân sự tham gia theo dự án ")
  @DeleteMapping("/project/{id_project}/user/{id}")
  public ResponseEntity<DeleteMethodResponse> deleteProject(HttpServletRequest request,
      HttpServletResponse response,
      @PathVariable(name = "id_project") Integer id_project,
      @PathVariable(name = "id") Integer id) {
    try {
      if (!authGuard.checkPermission(request, null, PermissionObjectCode.LEADS,
          ServicePermissionCode.GET_PROJECT_BY_ID)) {
        return new ResponseEntity(
            PostMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build(), HttpStatus.OK);
      }
      Integer idDeleter = authGuard.getUserId(request);
      Boolean result = projectUserService.deleteUserOfProject(idDeleter, id_project, id);
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
}
