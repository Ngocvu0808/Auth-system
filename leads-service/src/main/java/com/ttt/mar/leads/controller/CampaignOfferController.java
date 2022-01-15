package com.ttt.mar.leads.controller;

import com.ttt.mar.leads.dto.CampaignOfferRequestDto;
import com.ttt.mar.leads.dto.OfferResponseListDto;
import com.ttt.mar.leads.entities.CampaignOffer;
import com.ttt.mar.leads.entities.Offer;
import com.ttt.mar.leads.service.iface.CampaignOfferService;
import com.ttt.rnd.common.config.Constants;
import com.ttt.rnd.common.dto.BaseMethodResponse;
import com.ttt.rnd.common.dto.DeleteMethodResponse;
import com.ttt.rnd.common.dto.GetMethodResponse;
import com.ttt.rnd.common.dto.PostMethodResponse;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ProxyAuthenticationException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import com.ttt.rnd.common.exception.UserNotFoundException;
import com.ttt.rnd.common.service.iface.AuthGuard;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
 * @author nguyen
 * @create_date 06/08/2021
 */


@RestController
@Api(tags = "API for Offer in Campaign details")
public class CampaignOfferController {

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
      .getLogger(CampaignOfferController.class);


  private final CampaignOfferService campaignOfferService;

  private final AuthGuard authGuard;

  public CampaignOfferController(
      CampaignOfferService campaignOfferService,
      AuthGuard authGuard) {
    this.campaignOfferService = campaignOfferService;
    this.authGuard = authGuard;
  }


  @ApiOperation("Delete Offer in Campaign detail")
  @DeleteMapping("/campaign/{campaignId}/offer/{campaignOfferId}")
  public ResponseEntity<?> deleteOfferInCampaignDetail(HttpServletRequest request,
      HttpServletResponse response,
      @PathVariable(name = "campaignId") Integer campaignId,
      @PathVariable(name = "campaignOfferId") Integer campaignOfferId) {
    try {
      Integer userId = authGuard.getUserId(request);
//        Integer userId = 1;
      Boolean result = campaignOfferService
          .deleteCampaignOffer(userId, campaignId, campaignOfferId);
      return new ResponseEntity<>(
          DeleteMethodResponse.builder().status(result).message(Constants.SUCCESS_MSG)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value())
              .build(), HttpStatus.OK);
    } catch (ProxyAuthenticationException e) {
      logger.warn(e.getMessage(), e);
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.UNAUTHORIZED.value()).build(),
          HttpStatus.OK);
    } catch (OperationNotImplementException e) {
      logger.warn(e.getMessage(), e);
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.BAD_REQUEST.value()).build(),
          HttpStatus.OK);
    } catch (ResourceNotFoundException e) {
      logger.warn(e.getMessage(), e);
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.NOT_FOUND.value()).build()
          , HttpStatus.OK);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      return new ResponseEntity<>(BaseMethodResponse.builder().status(false)
          .message(Constants.INTERNAL_SERVER_ERROR)
          .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
          .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build(), HttpStatus.OK);
    }

  }


  @ApiOperation("API lấy danh sách Offer cho chi tiết chiến dịch")
  @GetMapping("/offer/filter")
  public ResponseEntity<?> findOfferNotExistCampaign(HttpServletRequest request,
      HttpServletResponse response,
      @RequestParam(name = "campaignId") Integer campaignId) {
    try {
      authGuard.checkAuthorization(request);
      List<OfferResponseListDto> offers = campaignOfferService
          .findOfferNotExistCampaign(campaignId);
      return new ResponseEntity<>(
          GetMethodResponse.builder().status(true).message(Constants.SUCCESS_MSG)
              .data(offers)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value()).build()
          , HttpStatus.OK);
    } catch (ProxyAuthenticationException e) {
      logger.warn(e.getMessage(), e);
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.UNAUTHORIZED.value()).build(),
          HttpStatus.OK);
    } catch (ResourceNotFoundException e) {
      logger.warn(e.getMessage(), e);
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.NOT_FOUND.value()).build()
          , HttpStatus.OK);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      return new ResponseEntity<>(BaseMethodResponse.builder().status(false)
          .message(Constants.INTERNAL_SERVER_ERROR)
          .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
          .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build(), HttpStatus.OK);
    }
  }

  @ApiOperation("API them moi offer cho chien dich")
  @PostMapping("/campaign/{campaignId}/offer")
  public ResponseEntity<?> createCampaignOffer(HttpServletRequest request,
      HttpServletResponse response,
      @PathVariable(name = "campaignId") Integer campaignId,
      @RequestBody @Valid CampaignOfferRequestDto dto) {
    try {
      Integer userId = authGuard.getUserId(request);
//      Integer userId = 1;
      dto.setCampaignId(campaignId);
      Integer id = campaignOfferService
          .createCampaignOffer(userId, dto);
      return new ResponseEntity<>(
          PostMethodResponse.builder().status(true).id(id).message(Constants.SUCCESS_MSG)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value()).build()
          , HttpStatus.OK);
    } catch (ProxyAuthenticationException | UserNotFoundException e) {
      logger.warn(e.getMessage(), e);
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.UNAUTHORIZED.value()).build(),
          HttpStatus.OK);
    } catch (OperationNotImplementException e) {
      logger.warn(e.getMessage(), e);
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.BAD_REQUEST.value()).build(),
          HttpStatus.OK);
    } catch (ResourceNotFoundException e) {
      logger.warn(e.getMessage(), e);
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.NOT_FOUND.value()).build()
          , HttpStatus.OK);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      return new ResponseEntity<>(BaseMethodResponse.builder().status(false)
          .message(Constants.INTERNAL_SERVER_ERROR)
          .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
          .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build(), HttpStatus.OK);
    }
  }
}
