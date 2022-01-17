package com.ttt.mar.notify.controller;

import com.ttt.mar.notify.dto.template.TemplateSemanticResponse;
import com.ttt.mar.notify.entities.template.TemplateSemanticType;
import com.ttt.mar.notify.service.iface.template.TemplateSemanticService;
import com.ttt.rnd.common.config.Constants;
import com.ttt.rnd.common.dto.BaseMethodResponse;
import com.ttt.rnd.common.dto.GetMethodResponse;
import com.ttt.rnd.common.service.iface.AuthGuard;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TemplateSemanticController {

  private static final Logger logger = LoggerFactory.getLogger(TemplateController.class);
  private final TemplateSemanticService templateSemanticService;
  private final AuthGuard authGuard;

  public TemplateSemanticController(TemplateSemanticService templateSemanticService,
      AuthGuard authGuard) {
    this.templateSemanticService = templateSemanticService;
    this.authGuard = authGuard;
  }

  @ApiOperation(value = "Api lay danh sach header va footer")
  @GetMapping("/semantic")
  public ResponseEntity<GetMethodResponse<List<TemplateSemanticResponse>>> getAllTemplateSemanticByType(
      HttpServletRequest request,
      @ApiParam(value = "Search by type", example = "[FOOTER,HEADER]")
      @RequestParam(value = "type", required = true) TemplateSemanticType type) {
    try {
//      if (!authGuard
//          .checkPermission(request, null, PermissionObjectCode.TEMPLATE_SEMANTIC,
//              ServicePermissionCode.GET_ALL_TEMPLATE_SEMANTIC)) {
//        return new ResponseEntity(
//            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
//                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
//                .httpCode(HttpStatus.FORBIDDEN.value()).build(), HttpStatus.OK);
//      }
      List<TemplateSemanticResponse> templateSematicResponse = templateSemanticService
          .getTemplateSemanticByType(type);
      return new ResponseEntity(
          GetMethodResponse.builder().status(true).message(Constants.SUCCESS_MSG)
              .data(templateSematicResponse)
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
      logger.error(e.getMessage());
      return new ResponseEntity(
          BaseMethodResponse.builder().status(false).message(Constants.INTERNAL_SERVER_ERROR)
              .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
              .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build()
          , HttpStatus.OK);
    }
  }


}
