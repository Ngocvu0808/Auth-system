package com.ttt.mar.auth.controller;

import com.ttt.mar.auth.dto.app.RenewTokenDto;
import com.ttt.mar.auth.dto.app.TokenRequestDto;
import com.ttt.mar.auth.dto.auth.GuestAccessRequestDto;
import com.ttt.mar.auth.dto.auth.GuestAccessResponseDto;
import com.ttt.mar.auth.dto.auth.LoginRequestDto;
import com.ttt.mar.auth.dto.auth.LoginResponseDto;
import com.ttt.mar.auth.service.iface.AuthService;
import com.ttt.rnd.common.config.Constants;
import com.ttt.rnd.common.dto.BaseMethodResponse;
import com.ttt.rnd.common.dto.GetMethodResponse;
import com.ttt.rnd.common.dto.PostMethodResponse;
import com.ttt.rnd.common.exception.CryptoException;
import com.ttt.rnd.common.exception.IdentifyBlankException;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ProxyAuthenticationException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import com.ttt.rnd.common.exception.UnAuthorizedException;
import com.ttt.rnd.common.exception.UserNotFoundException;
import com.ttt.rnd.common.service.iface.AuthGuard;
import com.ttt.rnd.lib.dto.IdListDto;
import java.util.Arrays;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AuthController {

  private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
  private final AuthService authService;

  @Autowired
  private AuthGuard authGuard;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping(path = "/login", produces = {MediaType.APPLICATION_JSON_VALUE,
      MediaType.APPLICATION_OCTET_STREAM_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE,
      MediaType.APPLICATION_OCTET_STREAM_VALUE})
  public ResponseEntity<?> login(HttpServletRequest request, HttpServletResponse response,
      @RequestBody LoginRequestDto dataLogin) {
    try {
      LoginResponseDto data = authService.login(request, dataLogin);
      return new ResponseEntity<>(
          GetMethodResponse.builder().status(true).data(data)
              .message(Constants.SUCCESS_MSG).errorCode(HttpStatus.OK.name().toLowerCase())
              .httpCode(HttpStatus.OK.value()).build()
          , HttpStatus.OK);
    } catch (ResourceNotFoundException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.NOT_FOUND.value()).build()
          , HttpStatus.OK);
    } catch (UnAuthorizedException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.UNAUTHORIZED.value()).build()
          , HttpStatus.OK);
    } catch (IdentifyBlankException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.BAD_REQUEST.value()).build()
          , HttpStatus.OK);
    } catch (CryptoException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.BAD_REQUEST.value()).build()
          , HttpStatus.OK);
    } catch (Exception e) {
      logger.error(Arrays.toString(e.getStackTrace()));
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(Constants.INTERNAL_SERVER_ERROR)
              .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
              .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build()
          , HttpStatus.OK);
    }
  }

  @PostMapping(path = "/check", produces = {MediaType.APPLICATION_JSON_VALUE,
      MediaType.APPLICATION_OCTET_STREAM_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE,
      MediaType.APPLICATION_OCTET_STREAM_VALUE})
  public ResponseEntity<?> checkAuthenticate(HttpServletRequest request) {
    try {
      LoginResponseDto responseData = authService.checkAuthenticate(request);
      return new ResponseEntity<>(
          GetMethodResponse.builder().status(true).data(responseData).message(Constants.SUCCESS_MSG)
              .httpCode(HttpStatus.OK.value()).errorCode(HttpStatus.OK.name().toLowerCase()).build()
          , HttpStatus.OK);
    } catch (UnAuthorizedException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(HttpStatus.UNAUTHORIZED.name().toLowerCase())
              .httpCode(HttpStatus.UNAUTHORIZED.value()).build()
          , HttpStatus.OK);
    } catch (Exception e) {
      logger.error(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(Constants.INTERNAL_SERVER_ERROR)
              .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
              .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build()
          , HttpStatus.OK);
    }
  }

  @GetMapping(path = "/validate", produces = {MediaType.APPLICATION_JSON_VALUE,
      MediaType.APPLICATION_OCTET_STREAM_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE,
      MediaType.APPLICATION_OCTET_STREAM_VALUE})
  public ResponseEntity<?> validateToken(HttpServletRequest request) {
    try {
      Boolean responseData = authService.validateToken(request);
      return new ResponseEntity<>(
          GetMethodResponse.builder().status(true).data(responseData).message(Constants.SUCCESS_MSG)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value()).build()
          , HttpStatus.OK);
    } catch (UnAuthorizedException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          GetMethodResponse.builder().status(false).data(false)
              .message(e.getMessage()).errorCode(HttpStatus.UNAUTHORIZED.name().toLowerCase())
              .httpCode(HttpStatus.UNAUTHORIZED.value()).build()
          , HttpStatus.OK);
    } catch (Exception e) {
      logger.error(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(Constants.INTERNAL_SERVER_ERROR)
              .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
              .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build()
          , HttpStatus.OK);
    }
  }

  @PostMapping(path = "/logout", produces = {MediaType.APPLICATION_JSON_VALUE,
      MediaType.APPLICATION_OCTET_STREAM_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE,
      MediaType.APPLICATION_OCTET_STREAM_VALUE})
  public ResponseEntity<?> logOut(HttpServletRequest request) {
    try {
      int userId = authGuard.getUserId(request);
      authService.logout(request, userId);
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(true).message(Constants.SUCCESS_MSG)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value()).build()
          , HttpStatus.OK);
    } catch (UnAuthorizedException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.UNAUTHORIZED.value()).build()
          , HttpStatus.OK);
    } catch (ServletException e) {
      logger.error(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(Constants.INTERNAL_SERVER_ERROR)
              .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
              .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build()
          , HttpStatus.OK);
    } catch (ProxyAuthenticationException | UserNotFoundException e) {
      logger.warn(e.getMessage(), e);
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.UNAUTHORIZED.value()).build(),
          HttpStatus.OK);
    } catch (Exception e) {
      logger.error(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(Constants.INTERNAL_SERVER_ERROR)
              .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
              .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build()
          , HttpStatus.OK);
    }
  }

  @PostMapping(path = "/reload", produces = {MediaType.APPLICATION_JSON_VALUE,
      MediaType.APPLICATION_OCTET_STREAM_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE,
      MediaType.APPLICATION_OCTET_STREAM_VALUE})
  public ResponseEntity<?> reloadPermission(HttpServletRequest request,
      @RequestBody IdListDto<Integer> idList) {
    try {
      authService.reloadPermission(idList.getIds());
      return new ResponseEntity<>(
          PostMethodResponse.builder().status(true).message(Constants.SUCCESS_MSG)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value()).build()
          , HttpStatus.OK);
    } catch (Exception e) {
      logger.error(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(Constants.INTERNAL_SERVER_ERROR)
              .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
              .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build()
          , HttpStatus.OK);
    }
  }

  /**
   * API sinh refresh_token & access_token
   *
   * @param request:         HttpServletRequest
   * @param tokenRequestDto: {client_id, client_secret, api_key}
   * @return {access_token ,access_token_exp, refresh_token, refresh_token_exp}
   */
  @PostMapping("/authorized")
  public ResponseEntity<?> getToken(HttpServletRequest request,
      @RequestBody TokenRequestDto tokenRequestDto) {
    try {
      Map<String, Object> map = authService.getToken(request, tokenRequestDto);
      return new ResponseEntity<>(
          GetMethodResponse.builder().status(true).message(Constants.SUCCESS_MSG).data(map)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value()).build()
          , HttpStatus.OK);
    } catch (ResourceNotFoundException | OperationNotImplementException | IdentifyBlankException | UnAuthorizedException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.BAD_REQUEST.value()).build()
          , HttpStatus.OK);
    } catch (Exception e) {
      logger.error(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(Constants.INTERNAL_SERVER_ERROR)
              .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
              .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build()
          , HttpStatus.OK);
    }
  }

  /**
   * @param request:       HttpServletRequest
   * @param renewTokenDto: {refresh_token}
   * @return {access_token ,access_token_exp}
   */
  @PostMapping("/renew-token")
  public ResponseEntity<?> getAccessTokenFromRefreshToken(HttpServletRequest request,
      @RequestBody RenewTokenDto renewTokenDto) {
    try {
      Map<String, Object> accessToken = authService
          .getAccessTokenFromRefreshToken(request, renewTokenDto.getRefreshToken());
      return new ResponseEntity<>(
          GetMethodResponse.builder().status(true).data(accessToken)
              .message(Constants.SUCCESS_MSG).errorCode(HttpStatus.OK.name().toLowerCase())
              .httpCode(HttpStatus.OK.value()).build()
          , HttpStatus.OK);
    } catch (IdentifyBlankException | OperationNotImplementException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.BAD_REQUEST.value()).build()
          , HttpStatus.OK);
    } catch (ResourceNotFoundException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.NOT_FOUND.value()).build()
          , HttpStatus.OK);
    } catch (UnAuthorizedException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.UNAUTHORIZED.value()).build()
          , HttpStatus.OK);
    } catch (Exception e) {
      logger.error(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(Constants.INTERNAL_SERVER_ERROR)
              .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
              .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build()
          , HttpStatus.OK);
    }
  }

  @GetMapping(path = "/refresh")
  public ResponseEntity<?> refreshPermission(HttpServletRequest request) {
    try {
      LoginResponseDto responseData = authService.reloadPermission(request);
      return new ResponseEntity<>(
          GetMethodResponse.builder().status(true).data(responseData).message(Constants.SUCCESS_MSG)
              .httpCode(HttpStatus.OK.value()).errorCode(HttpStatus.OK.name().toLowerCase()).build()
          , HttpStatus.OK);
    } catch (UnAuthorizedException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(HttpStatus.UNAUTHORIZED.name().toLowerCase())
              .httpCode(HttpStatus.UNAUTHORIZED.value()).build()
          , HttpStatus.OK);
    } catch (Exception e) {
      logger.error(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(Constants.INTERNAL_SERVER_ERROR)
              .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
              .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build()
          , HttpStatus.OK);
    }
  }

  @PostMapping(path = "/temporary")
  public ResponseEntity<?> virtualUser(HttpServletRequest request,
      @RequestBody GuestAccessRequestDto dto) {
    try {
      GuestAccessResponseDto resp = authService.guestAccess(dto);
      return new ResponseEntity<>(
          GetMethodResponse.builder().status(true).data(resp).message(Constants.SUCCESS_MSG)
              .httpCode(HttpStatus.OK.value()).errorCode(HttpStatus.OK.name().toLowerCase()).build()
          , HttpStatus.OK);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(Constants.INTERNAL_SERVER_ERROR)
              .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
              .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build()
          , HttpStatus.OK);
    }
  }

  @PostMapping(path = "/temporary/close")
  public ResponseEntity<?> temporaryClose(HttpServletRequest request) {
    try {
      authGuard.checkAuthorization(request);
      authService.temporaryClose(request);
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(true).message(Constants.SUCCESS_MSG)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value()).build()
          , HttpStatus.OK);
    } catch (ResourceNotFoundException e) {
      logger.warn(e.getMessage());
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.NOT_FOUND.value()).build()
          , HttpStatus.OK);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(Constants.INTERNAL_SERVER_ERROR)
              .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
              .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build()
          , HttpStatus.OK);
    }
  }
}
