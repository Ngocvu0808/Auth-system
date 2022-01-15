package com.ttt.mar.email.controller;

import com.ttt.mar.email.config.PermissionObjectCode;
import com.ttt.mar.email.config.ServicePermissionCode;
import com.ttt.mar.email.dto.emailpublisher.EmailPublisherCreateRequestDto;
import com.ttt.mar.email.dto.emailpublisher.EmailPublisherPublicResponseDto;
import com.ttt.mar.email.dto.emailpublisher.EmailPublisherUpdateRequestDto;
import com.ttt.mar.email.service.iface.EmailPublisherService;
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
public class EmailPublisherController {

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
      .getLogger(EmailPublisherController.class);

  @Autowired
  private EmailPublisherService emailPublisherService;

  @Autowired
  private AuthGuard authGuard;

  @PostMapping("/publisher")
  public ResponseEntity<?> createEmailPublisher(
      @RequestBody @Valid EmailPublisherCreateRequestDto dto,
      HttpServletRequest request, HttpServletResponse response) {
    try {
      if (!authGuard
          .checkPermission(request, null, PermissionObjectCode.EMAIL,
              ServicePermissionCode.EMAIL_PUBLISHER_ADD)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build(), HttpStatus.OK);
      }
      Integer userId = authGuard.getUserId(request);
      Integer id = emailPublisherService.createEmailPublisher(dto, userId);
      return new ResponseEntity<>(
          PostMethodResponse.builder().status(true).message(Constants.SUCCESS_MSG)
              .id(id)
              .httpCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
    } catch (IdentifyBlankException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.BAD_REQUEST.value()).build(),
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
    } catch (OperationNotImplementException e) {
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).httpCode(HttpStatus.BAD_REQUEST.value())
              .message(e.getMessage()).errorCode(e.getMessageCode()).build(), HttpStatus.OK);
    } catch (Exception e) {
      logger.error(e.getMessage());
      return new ResponseEntity<>(BaseMethodResponse.builder().status(false)
          .message(Constants.INTERNAL_SERVER_ERROR)
          .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
          .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build(), HttpStatus.OK);
    }
  }

  @PutMapping("/publisher/{id}")
  public ResponseEntity<?> updateEmailPublisher(
      @RequestBody @Valid EmailPublisherUpdateRequestDto dto,
      @PathVariable("id") Integer id,
      HttpServletRequest request, HttpServletResponse response) {
    try {
      if (!authGuard
          .checkPermission(request, null, PermissionObjectCode.EMAIL,
              ServicePermissionCode.EMAIL_PUBLISHER_EDIT)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build(), HttpStatus.OK);
      }
      Integer userId = authGuard.getUserId(request);
      dto.setId(id);
      Integer idPublisher = emailPublisherService.updateEmailPublisher(dto, userId);
      return new ResponseEntity<>(
          PutMethodResponse.builder().status(true).message(Constants.SUCCESS_MSG)
              .id(idPublisher)
              .httpCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
    } catch (IdentifyBlankException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.BAD_REQUEST.value()).build(),
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
    } catch (OperationNotImplementException e) {
      return new ResponseEntity<>(
          PutMethodResponse.builder().status(false).httpCode(HttpStatus.BAD_REQUEST.value())
              .message(e.getMessage()).errorCode(e.getMessageCode()).build(), HttpStatus.OK);
    } catch (Exception e) {
      logger.error(e.getMessage());
      return new ResponseEntity<>(BaseMethodResponse.builder().status(false)
          .message(Constants.INTERNAL_SERVER_ERROR)
          .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
          .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build(), HttpStatus.OK);
    }
  }

  @GetMapping("/publishers")
  public ResponseEntity<?> getPublisherPaging(
      HttpServletRequest request, HttpServletResponse response,
      @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
      @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
      @RequestParam(name = "search", required = false, defaultValue = "") String search,
      @RequestParam(name = "sort", required = false, defaultValue = "") String sort) {
    try {
      if (!authGuard.checkPermission(request, null, PermissionObjectCode.EMAIL,
          ServicePermissionCode.EMAIL_PUBLISHER_GETS)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build(), HttpStatus.OK);
      }
      DataPagingResponse<?> data = emailPublisherService
          .getEmailPublisherPaging(search, sort, page, limit);
      return new ResponseEntity<>(
          GetMethodResponse.builder().status(true).data(data).message(Constants.SUCCESS_MSG)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value())
              .build(), HttpStatus.OK);
    } catch (Exception e) {
      logger.error(e.getMessage());
      return new ResponseEntity<>(BaseMethodResponse.builder().status(false)
          .message(Constants.INTERNAL_SERVER_ERROR)
          .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
          .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build(), HttpStatus.OK);
    }
  }

  @DeleteMapping("/publisher/{id}")
  public ResponseEntity<?> deleteEmailPublisher(
      @PathVariable("id") Integer id, HttpServletRequest request,
      HttpServletResponse response) {
    try {
      if (!authGuard
          .checkPermission(request, null, PermissionObjectCode.EMAIL,
              ServicePermissionCode.EMAIL_PUBLISHER_DELETE)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build(), HttpStatus.OK);
      }
      Integer userId = authGuard.getUserId(request);
      Boolean result = emailPublisherService.deleteEmailPublisher(id, userId);
      return new ResponseEntity<>(
          DeleteMethodResponse.builder().status(true).message(Constants.SUCCESS_MSG).errorCode(
              HttpStatus.OK.name().toLowerCase())
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

  @GetMapping("/publisher-all")
  public ResponseEntity<?> getPublicEmailPublishers(HttpServletRequest request,
      HttpServletResponse response) {
    try {
      List<EmailPublisherPublicResponseDto> data = emailPublisherService.getListPublishers();
      return new ResponseEntity<>(
          GetMethodResponse.builder()
              .status(true)
              .data(data)
              .message(Constants.SUCCESS_MSG)
              .errorCode(HttpStatus.OK.name().toLowerCase())
              .httpCode(HttpStatus.OK.value())
              .build(), HttpStatus.OK);
    } catch (Exception e) {
      logger.error(e.getMessage());
      return new ResponseEntity<>(
          GetMethodResponse.builder()
              .status(false)
              .message(Constants.INTERNAL_SERVER_ERROR)
              .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
              .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
              .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
