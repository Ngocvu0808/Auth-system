package com.ttt.mar.leads.service.iface;

import com.mongodb.BasicDBObject;
import com.ttt.mar.leads.dto.DataPagingTemplateResponse;
import com.ttt.mar.leads.dto.distributeLead.LeadDistributeHistoryResponseDto;
import com.ttt.mar.leads.dto.LeadDistributeResponseDto;
import com.ttt.mar.leads.dto.LeadImportResponseDto;
import com.ttt.rnd.common.exception.BaseException;
import com.ttt.rnd.common.exception.ValidationException;
import com.ttt.rnd.lib.dto.logrequest.TemplateLogRequest;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.web.multipart.MultipartFile;

public interface LeadsService {

  DataPagingTemplateResponse<TemplateLogRequest> getListData(Long fromDate, Long toDate,
      String search, int page,
      int limit, String sort, Map<String, String> allParams);

  BasicDBObject getById(String idLead)
      throws ResourceNotFoundException, OperationNotImplementException;

  List<BasicDBObject> getListHistory(String idLead);

  List<BasicDBObject> getListStatus();

  List<String> getLeadStatus(String search);

  List<TemplateLogRequest> getTemplates(String templateName);

  String exportCsv(List<TemplateLogRequest> template, List<Object> leadIds, boolean isBlackList)
      throws BaseException;

  LeadImportResponseDto importLead(MultipartFile multipartFile, Integer userId, String data)
      throws ValidationException, IOException, ParseException, InvalidFormatException;

  String getFilePattern();

  boolean checkCampaignExist(String campaignCode);

  LeadDistributeResponseDto distribuleLead(List<Object> leadIds,
      Integer distributeId, boolean isBlackList, Integer userId, Map<String, String> allParams,
      Long fromDate, Long toDate, String search)
      throws BaseException, OperationNotImplementException;

  String updateStatusleadDistribite(String leadId, Integer value)
      throws OperationNotImplementException, ResourceNotFoundException;

  List<LeadDistributeHistoryResponseDto> getLeadDistributeHistory(String leadId)
      throws ParseException;
}
