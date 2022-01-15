package com.ttt.mar.leads.service.iface;

import com.ttt.mar.leads.dto.CampaignRequestDto;
import com.ttt.mar.leads.dto.CampaignResponse;
import com.ttt.mar.leads.dto.CampaignResponseDto;
import com.ttt.mar.leads.dto.CampaignStatusDto;
import com.ttt.mar.leads.dto.CampaignUpdateDto;
import com.ttt.mar.leads.dto.CampaignsCodeResponseDto;
import com.ttt.mar.leads.dto.OfferCampaignResponseDto;
import com.ttt.mar.leads.entities.ProjectStatus;
import com.ttt.rnd.common.dto.DataPagingResponse;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import java.text.ParseException;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author KietDT
 * @created_date 09/06/2021
 */
public interface CampaignService {

  DataPagingResponse<CampaignResponse> getListCampaignFilter(Integer projectId,
      Integer leadSourceId,
      Integer distributeId, ProjectStatus projectStatus, String sort, String search, int page,
      int limit)
      throws ResourceNotFoundException;

  Integer createCampaign(Integer userId, CampaignRequestDto dto)
      throws ResourceNotFoundException, OperationNotImplementException, ParseException;

  Integer updateCampaign(Integer userId, CampaignUpdateDto dto)
      throws ResourceNotFoundException, OperationNotImplementException, ParseException;

  CampaignResponseDto getByID(Integer id)
      throws ResourceNotFoundException, OperationNotImplementException;

  @Transactional
  Integer updateStatusCampaign(Integer userId, Integer id, CampaignStatusDto campaignStatusDto)
      throws OperationNotImplementException, ResourceNotFoundException, ParseException;

  DataPagingResponse<?> getAllOfferCampaign(Integer campaignId, String sort, Integer page,
      Integer limit);

  DataPagingResponse<?> getAllFilterCampaign(Integer campaignId, String sort, Integer page,
      Integer limit);

  List<CampaignsCodeResponseDto> getCampaignsCode();
}
