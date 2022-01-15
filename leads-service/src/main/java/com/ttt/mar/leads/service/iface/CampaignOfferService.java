package com.ttt.mar.leads.service.iface;

import com.ttt.mar.leads.dto.CampaignOfferRequestDto;
import com.ttt.mar.leads.dto.OfferResponseListDto;
import com.ttt.mar.leads.entities.Offer;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author nguyen
 * @create_date 06/08/2021
 */
public interface CampaignOfferService {

  @Transactional
  Boolean deleteCampaignOffer(Integer userId, Integer campaignId, Integer campaignOfferId)
      throws ResourceNotFoundException, OperationNotImplementException;

  List<OfferResponseListDto> findOfferNotExistCampaign(Integer campaignId)
      throws ResourceNotFoundException;

  Integer createCampaignOffer(Integer userId, CampaignOfferRequestDto dto)
      throws ResourceNotFoundException, OperationNotImplementException;
}
