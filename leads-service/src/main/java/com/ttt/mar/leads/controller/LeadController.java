package com.ttt.mar.leads.controller;

import com.mongodb.BasicDBObject;
import com.ttt.mar.leads.config.PermissionObjectCode;
import com.ttt.mar.leads.config.ServicePermissionCode;
import com.ttt.mar.leads.dto.DataPagingTemplateResponse;
import com.ttt.mar.leads.dto.distributeLead.LeadDistributeHistoryResponseDto;
import com.ttt.mar.leads.dto.LeadDistributeResponseDto;
import com.ttt.mar.leads.dto.LeadImportResponseDto;
import com.ttt.mar.leads.service.iface.LeadsService;
import com.ttt.rnd.common.config.Constants;
import com.ttt.rnd.common.dto.BaseMethodResponse;
import com.ttt.rnd.common.dto.GetMethodResponse;
import com.ttt.rnd.common.dto.PutMethodResponse;
import com.ttt.rnd.common.exception.ProxyAuthenticationException;
import com.ttt.rnd.common.exception.UserNotFoundException;
import com.ttt.rnd.common.exception.ValidationException;
import com.ttt.rnd.common.service.iface.AuthGuard;
import com.ttt.rnd.common.utils.MediaTypeUtils;
import com.ttt.rnd.lib.dto.logrequest.TemplateLogRequest;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Api(tags = "API for Leads")
public class LeadController {

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
      .getLogger(LeadController.class);
  @Value("${template.leads.config-file-name}")
  private String configTemplateRequestLogFileName;
  @Autowired
  private LeadsService leadsService;
  @Autowired
  private AuthGuard authGuard;

  @Autowired
  private ServletContext servletContext;

  @ApiOperation(value = "Api get all leads")
  @GetMapping("/leads")
  public ResponseEntity<GetMethodResponse<DataPagingTemplateResponse<BasicDBObject>>> getDistributesPaging(
      @ApiParam(value = "tu ngay - ngay tao")
      @RequestParam(value = "fromDate", required = false) Long fromDate,
      @ApiParam(value = "den ngay - ngay tao")
      @RequestParam(value = "toDate", required = false) Long toDate,
      @ApiParam(value = "tim kiem")
      @RequestParam(value = "search", required = false) String search,
      @ApiParam(value = "page data", example = "1")
      @RequestParam(value = "page", required = true, defaultValue = "1") Integer page,
      @ApiParam(value = "Limit data", example = "10")
      @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
      HttpServletRequest request) {
    try {
      Map<String, String[]> params = request.getParameterMap();
      Map<String, String> allParams=new HashMap<>();
      Set<String> keys=params.keySet();
      for(String key: keys){
        String value = params.get(key)[0];
        allParams.put(key,value);
      }
//      if (!authGuard
//          .checkPermission(request, null, PermissionObjectCode.LEADS,
//              ServicePermissionCode.GET_ALL_LEAD)) {
//        return new ResponseEntity(
//            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
//                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
//                .httpCode(HttpStatus.FORBIDDEN.value()).build(), HttpStatus.OK);
//      }
      DataPagingTemplateResponse<TemplateLogRequest> dataPagingResponse = leadsService
          .getListData(fromDate, toDate, search, page, limit, null, allParams);
      return new ResponseEntity(
          GetMethodResponse.builder().status(true).message(Constants.SUCCESS_MSG)
              .data(dataPagingResponse)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value()).build()
          , HttpStatus.OK);
//    } catch (ProxyAuthenticationException e) {
//      logger.warn(e.getMessage(), e);
//      return new ResponseEntity(
//          BaseMethodResponse.builder().status(false).message(e.getMessage())
//              .errorCode(e.getMessageCode()).httpCode(HttpStatus.UNAUTHORIZED.value()).build(),
//          HttpStatus.OK);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      return new ResponseEntity(BaseMethodResponse.builder().status(false)
          .message(Constants.INTERNAL_SERVER_ERROR)
          .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
          .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build(), HttpStatus.OK);
    }
  }

  @ApiOperation(value = "Api detail lead")
  @GetMapping("/lead/{id}")
  public ResponseEntity<GetMethodResponse<BasicDBObject>> getLeadById(
      HttpServletRequest request, HttpServletResponse response,
      @PathVariable(name = "id") String id) {
    try {
//      if (!authGuard.checkPermission(request, null, PermissionObjectCode.LEADS,
//          ServicePermissionCode.GET_LEAD_BY_ID)) {
//        return new ResponseEntity(
//            GetMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
//                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
//                .httpCode(HttpStatus.FORBIDDEN.value()).build(), HttpStatus.OK);
//      }
      BasicDBObject responseDto = leadsService.getById(id);
      return new ResponseEntity(
          GetMethodResponse.builder().status(true).message(Constants.SUCCESS_MSG).data(responseDto)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value()).build()
          , HttpStatus.OK);
//    } catch (ResourceNotFoundException e) {
//      logger.warn(e.getMessage(), e);
//      return new ResponseEntity(
//          BaseMethodResponse.builder().status(false).message(e.getMessage())
//              .errorCode(e.getMessageCode()).httpCode(HttpStatus.NOT_FOUND.value()).build()
//          , HttpStatus.OK);
    } catch (OperationNotImplementException e) {
      logger.warn(e.getMessage(), e);
      return new ResponseEntity(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.BAD_REQUEST.value()).build(),
          HttpStatus.OK);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      return new ResponseEntity(BaseMethodResponse.builder().status(false)
          .message(Constants.INTERNAL_SERVER_ERROR)
          .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
          .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build(), HttpStatus.OK);
    }
  }

  @ApiOperation(value = "Api get history lead")
  @GetMapping("/lead/{id}/history/status")
  public ResponseEntity<GetMethodResponse<List<BasicDBObject>>> getHistory(
      HttpServletRequest request, HttpServletResponse response,
      @PathVariable(name = "id") String id) {
    try {
      if (!authGuard.checkPermission(request, null, PermissionObjectCode.LEADS,
          ServicePermissionCode.GET_HISTORY_LEAD)) {
        return new ResponseEntity(
            GetMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build(), HttpStatus.OK);
      }
      List<BasicDBObject> responseDto = leadsService.getListHistory(id);
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

  @ApiOperation(value = "Api get all status lead")
  @GetMapping("/lead/status")
  public ResponseEntity<GetMethodResponse<List<BasicDBObject>>> getListStatus(
      HttpServletRequest request, HttpServletResponse response) {
    try {
      if (!authGuard.checkPermission(request, null, PermissionObjectCode.LEADS,
          ServicePermissionCode.GET_ALL_STATUS_LEAD)) {
        return new ResponseEntity(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build(), HttpStatus.OK);
      }
      List<BasicDBObject> responseDto = leadsService.getListStatus();
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

  @ApiOperation(value = "Get csv file to download")
  @GetMapping(path = "/lead/export")
  public ResponseEntity<?> getExportLead(
      @RequestParam List<Object> leadIds,
      @RequestParam(value = "createTime", required = false, defaultValue = "desc") String sort,
      @RequestParam(value = "isBlackList",
          required = false, defaultValue = "true") boolean isBlackList,
      HttpServletRequest request, HttpServletResponse response) {
    List<TemplateLogRequest> template = this.leadsService
        .getTemplates(configTemplateRequestLogFileName);
    try {
      if (!authGuard
          .checkPermission(request, null, PermissionObjectCode.LEADS,
              ServicePermissionCode.DOWNLOAD_LEAD)) {
        logger.warn("Request forbidden!");
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build()
            , HttpStatus.OK);
      }
      String filePath = this.leadsService.exportCsv(template, leadIds, isBlackList);
      File file = new File(filePath);
      MediaType mediaType = MediaTypeUtils
          .getMediaTypeForFileName(this.servletContext, file.getName());
      FileInputStream fis = new FileInputStream(file.getAbsolutePath());
      InputStreamResource resource = new InputStreamResource(fis);
      return ResponseEntity.ok()
          // Content-Disposition
          .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
          // Content-Type
          .contentType(mediaType)
          // Contet-Length
          .contentLength(file.length()) //
          .body(resource);
    } catch (FileNotFoundException e) {
      return new ResponseEntity<>(
          HttpStatus.NOT_FOUND);

    } catch (Exception e) {
      logger.error(e.getMessage());
      return new ResponseEntity<>(
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }


  @ApiOperation("Api upload leads")
  @PostMapping(path = "/lead/import")
  public ResponseEntity<?> importLead(HttpServletRequest request,
      HttpServletResponse response,
      @RequestParam("file") MultipartFile multipartFile,
      @RequestParam(name = "data", required = false, defaultValue = "") String leadInfoRequests) {
    try {
      if (!authGuard.checkPermission(request, null, PermissionObjectCode.LEADS,
          ServicePermissionCode.IMPORT_LEAD)) {
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build(), HttpStatus.OK);
      }
      Integer userId = authGuard.getUserId(request);
//      Integer userId = 1;
      LeadImportResponseDto result = leadsService
          .importLead(multipartFile, userId, leadInfoRequests);
      return new ResponseEntity<>(
          GetMethodResponse.builder().status(true).message(Constants.SUCCESS_MSG).data(result)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value()).build()
          , HttpStatus.OK);
    } catch (ProxyAuthenticationException | UserNotFoundException e) {
      logger.warn(e.getMessage(), e);
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.UNAUTHORIZED.value()).build(),
          HttpStatus.OK);

    } catch (ValidationException e) {
      logger.error(e.getMessage(), e);
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.BAD_REQUEST.value()).build(),
          HttpStatus.OK);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false)
              .message(Constants.INTERNAL_SERVER_ERROR)
              .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
              .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build(), HttpStatus.OK);
    }
  }

  @ApiOperation(value = "Get file pattern")
  @GetMapping(path = "/lead/get-pattern")
  public ResponseEntity<?> getFilePattern(HttpServletRequest request,
      HttpServletResponse response) {
    logger.info("Get file pattern !!!!!");
    try {
      if (!authGuard.checkPermission(request, null, PermissionObjectCode.LEADS,
          ServicePermissionCode.DOWNLOAD_PATTERN)) {
        logger.warn("Request forbidden!");
        return new ResponseEntity<>(
            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build(), HttpStatus.OK);
      }
      logger.info("Get file pattern");
      String filePath = this.leadsService.getFilePattern();
      File file = new File(filePath);
      MediaType mediaType = MediaTypeUtils
          .getMediaTypeForFileName(this.servletContext, file.getName());
      FileInputStream fis = new FileInputStream(file.getAbsolutePath());
      InputStreamResource resource = new InputStreamResource(fis);
      return ResponseEntity.ok()
          // Content-Disposition
          .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
          // Content-Type
          .contentType(mediaType)
          // Contet-Length
          .contentLength(file.length()) //
          .body(resource);
    } catch (FileNotFoundException e) {
      return new ResponseEntity<>(
          HttpStatus.NOT_FOUND);

    } catch (Exception e) {
      logger.error(e.getMessage());
      return new ResponseEntity<>(
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @ApiOperation(value = "check campaign exist for lead")
  @GetMapping(path = "/lead/campaign-exist")
  public ResponseEntity<?> checkCampaignExisted(HttpServletRequest request,
      HttpServletResponse response,
      @RequestParam(value = "campaignCode", required = true) String campaignCode) {
    try {
      logger.info("Check Campaign Exist!");
      boolean result = leadsService.checkCampaignExist(campaignCode);
      return new ResponseEntity<>(
          GetMethodResponse.builder().status(result).message(Constants.SUCCESS_MSG)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value()).build()
          , HttpStatus.OK);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      return new ResponseEntity<>(BaseMethodResponse.builder().status(false)
          .message(Constants.INTERNAL_SERVER_ERROR)
          .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
          .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build(), HttpStatus.OK);
    }
  }

  @GetMapping(path = "/lead/distribute-imediate")
  @ApiOperation(value = "API distribute lead imediately")
  public ResponseEntity<?> imediateDistributeLead(HttpServletRequest request,
      HttpServletResponse response,
      @RequestParam(required = true, name = "leads") List<Object> leadids,
      @RequestParam(required = true, name = "isBlackList") boolean isBlackList,
      @RequestParam(required = true, name = "distributer") Integer distributeId,
      @ApiParam(value = "tu ngay - ngay tao")
      @RequestParam(value = "fromDate", required = false) Long fromDate,
      @ApiParam(value = "den ngay - ngay tao")
      @RequestParam(value = "toDate", required = false) Long toDate,
      @ApiParam(value = "tim kiem")
      @RequestParam(value = "search", required = false) String search
      ) {
    try {
//      if (!authGuard.checkPermission(request, null, PermissionObjectCode.LEADS,
//          ServicePermissionCode.LEAD_DISTRIBUTE)) {
//        logger.warn("Request forbidden!");
//        return new ResponseEntity<>(
//            BaseMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
//                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
//                .httpCode(HttpStatus.FORBIDDEN.value()).build(), HttpStatus.OK);
//      }
//      Integer userId = authGuard.getUserId(request);
      Integer userId = 1;
      Map<String, String[]> params = request.getParameterMap();
      Map<String, String> allParams=new HashMap<>();
      Set<String> keys=params.keySet();
      for(String key: keys){
        String value = params.get(key)[0];
        allParams.put(key,value);
      }
      LeadDistributeResponseDto responseDto = leadsService.distribuleLead(leadids,
          distributeId, isBlackList, userId,allParams,fromDate,toDate,search);
      return new ResponseEntity<>(
          GetMethodResponse.builder().status(true).message(Constants.SUCCESS_MSG).data(responseDto)
              .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value()).build()
          , HttpStatus.OK);
//    } catch (ProxyAuthenticationException | UserNotFoundException e) {
//      logger.warn(e.getMessage(), e);
//      return new ResponseEntity<>(
//          BaseMethodResponse.builder().status(false).message(e.getMessage())
//              .errorCode(e.getMessageCode()).httpCode(HttpStatus.UNAUTHORIZED.value()).build(),
//          HttpStatus.OK);
    } catch (OperationNotImplementException e) {
      logger.warn(e.getMessage(), e);
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.BAD_REQUEST.value()).build(),
          HttpStatus.OK);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      return new ResponseEntity<>(
          BaseMethodResponse.builder().status(false)
              .message(Constants.INTERNAL_SERVER_ERROR)
              .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
              .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build(), HttpStatus.OK);
    }
  }

  @PutMapping("/lead/{id}/status")
  public ResponseEntity<?> updateStatusLeadDistribute(HttpServletRequest request,
      HttpServletResponse response,
      @RequestParam(name = "leadId", required = false, defaultValue = "") String leadId,
      @RequestParam(name = "value", required = false) Integer value) {
    try {
      if (!authGuard.checkPermission(request, null, PermissionObjectCode.LEADS,
          ServicePermissionCode.UPDATE_STATUS_LEAD_DISTRIBUTE)) {
        return new ResponseEntity<>(
            PutMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                .httpCode(HttpStatus.FORBIDDEN.value()).build(), HttpStatus.OK);
      }
      String result = leadsService.updateStatusleadDistribite(leadId, value);
      return new ResponseEntity<>(
          PutMethodResponse.builder().status(true).id(result).message(Constants.SUCCESS_MSG)
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
      return new ResponseEntity(
          BaseMethodResponse.builder().status(false).message(e.getMessage())
              .errorCode(e.getMessageCode()).httpCode(HttpStatus.NOT_FOUND.value())
              .build(), HttpStatus.OK);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      return new ResponseEntity<>(BaseMethodResponse.builder().status(false)
          .message(Constants.INTERNAL_SERVER_ERROR)
          .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
          .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build(), HttpStatus.OK);
    }
  }
  @ApiOperation(value = "Api get lead distribute history")
  @GetMapping("/lead/{id}/distribute-history")
  public ResponseEntity<GetMethodResponse<BasicDBObject>> getLeadDistributeHistoryById(
          HttpServletRequest request, HttpServletResponse response,
          @PathVariable(name = "id",required = false) String leadId) {
    try {
      if (!authGuard.checkPermission(request, null, PermissionObjectCode.LEADS,
              ServicePermissionCode.GET_LEAD_BY_ID)) {
        return new ResponseEntity(
                GetMethodResponse.builder().status(false).message(Constants.FORBIDDEN)
                        .errorCode(HttpStatus.FORBIDDEN.name().toLowerCase())
                        .httpCode(HttpStatus.FORBIDDEN.value()).build(), HttpStatus.OK);
      }
      List<LeadDistributeHistoryResponseDto> responseDto = leadsService.getLeadDistributeHistory(leadId);
      return new ResponseEntity(
              GetMethodResponse.builder().status(true).message(Constants.SUCCESS_MSG).data(responseDto)
                      .errorCode(HttpStatus.OK.name().toLowerCase()).httpCode(HttpStatus.OK.value()).build()
              , HttpStatus.OK);
//    } catch (ResourceNotFoundException e) {
//      logger.warn(e.getMessage(), e);
//      return new ResponseEntity(
//          BaseMethodResponse.builder().status(false).message(e.getMessage())
//              .errorCode(e.getMessageCode()).httpCode(HttpStatus.NOT_FOUND.value()).build()
//          , HttpStatus.OK);
//    } catch (OperationNotImplementException e) {
//      logger.warn(e.getMessage(), e);
//      return new ResponseEntity(
//          BaseMethodResponse.builder().status(false).message(e.getMessage())
//              .errorCode(e.getMessageCode()).httpCode(HttpStatus.BAD_REQUEST.value()).build(),
//          HttpStatus.OK);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      return new ResponseEntity(BaseMethodResponse.builder().status(false)
              .message(Constants.INTERNAL_SERVER_ERROR)
              .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name().toLowerCase())
              .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build(), HttpStatus.OK);
    }
  }

}

