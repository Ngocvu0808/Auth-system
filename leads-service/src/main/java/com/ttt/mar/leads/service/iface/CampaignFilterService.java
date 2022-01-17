package com.ttt.mar.leads.service.iface;

import com.ttt.mar.leads.dto.CampaignFilterRequestDto;
import com.ttt.mar.leads.entities.CampaignFilter;
import com.ttt.mar.leads.dto.FilterResponseListDto;
import com.ttt.mar.leads.dto.OfferResponseListDto;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import com.ttt.rnd.common.exception.ValidationException;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public interface CampaignFilterService {

  @Transactional
  Boolean deleteCampaignFilter(Integer userId, Integer campaignId, Integer campaignFilterId)
      throws ResourceNotFoundException, OperationNotImplementException;

  List<FilterResponseListDto> findFilterNotExistCampaign(Integer campaignId)
      throws ResourceNotFoundException;

  Integer createCampaignFilter(Integer userId, CampaignFilterRequestDto dto)
      throws ResourceNotFoundException, OperationNotImplementException, ValidationException;

}
