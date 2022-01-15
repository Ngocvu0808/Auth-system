package com.ttt.mar.leads.controller;

import com.ttt.mar.leads.dto.LeadConditionResponseDto;
import com.ttt.mar.leads.service.iface.ConditionService;
import com.ttt.rnd.common.config.Constants;
import com.ttt.rnd.common.dto.BaseMethodResponse;
import com.ttt.rnd.common.dto.GetMethodResponse;
import com.ttt.rnd.common.service.iface.AuthGuard;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "API for condition")
public class ConditionController {

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
      .getLogger(ConditionController.class);

  @Autowired
  private ConditionService conditionService;

  @Autowired
  private AuthGuard authGuard;

  @ApiOperation(value = "Api get list condition")
  @GetMapping("/conditions")
  public ResponseEntity<GetMethodResponse<List<LeadConditionResponseDto>>> getListCondition(
      HttpServletRequest request) {
    try {
      authGuard.checkAuthorization(request);
      List<LeadConditionResponseDto> responseDto = conditionService.getAllCondition();
      return new ResponseEntity(
          GetMethodResponse.builder().status(true).message(Constants.SUCCESS_MSG).data(responseDto)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value()).build()
          , HttpStatus.OK);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      return new ResponseEntity(BaseMethodResponse.builder().status(false)
          .message(Constants.INTERNAL_SERVER_ERROR)
          .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
          .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build(), HttpStatus.OK);
    }
  }
}
