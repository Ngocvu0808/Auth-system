package com.ttt.mar.leads.service.impl;

import com.ttt.mar.leads.config.ServiceMessageCode;
import com.ttt.mar.leads.dto.CampaignOfferRequestDto;
import com.ttt.mar.leads.dto.OfferResponseDto;
import com.ttt.mar.leads.dto.OfferResponseListDto;
import com.ttt.mar.leads.entities.Campaign;
import com.ttt.mar.leads.entities.CampaignOffer;
import com.ttt.mar.leads.entities.Offer;
import com.ttt.mar.leads.mapper.OfferMapper;
import com.ttt.mar.leads.repositories.CampaignOfferRepository;
import com.ttt.mar.leads.repositories.CampaignRepository;
import com.ttt.mar.leads.repositories.OfferRepository;
import com.ttt.mar.leads.service.iface.CampaignOfferService;
import com.ttt.mar.leads.service.iface.UserService;
import com.ttt.rnd.common.config.ServiceInfo;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * @author nguyen
 * @create_date 06/08/2021
 */

@Service
public class CampaignOfferServiceImpl implements CampaignOfferService {

  private final UserService userService;
  private final CampaignRepository campaignRepository;
  private final CampaignOfferRepository campaignOfferRepository;
  private final OfferRepository offerRepository;
  private final OfferMapper offerMapper;

  public CampaignOfferServiceImpl(UserService userService,
      CampaignRepository campaignRepository,
      CampaignOfferRepository campaignOfferRepository,
      OfferRepository offerRepository,
      OfferMapper offerMapper) {
    this.userService = userService;
    this.campaignRepository = campaignRepository;
    this.campaignOfferRepository = campaignOfferRepository;
    this.offerRepository = offerRepository;
    this.offerMapper = offerMapper;
  }


  @Override
  public Boolean deleteCampaignOffer(Integer userId, Integer campaignId, Integer campaignOfferId)
      throws ResourceNotFoundException, OperationNotImplementException {
    userService.checkValidUser(userId);
    Campaign campaign = campaignRepository.findByIdAndIsDeletedFalse(campaignId);
    if (campaign == null) {
      throw new ResourceNotFoundException("Campaign Not Found",
          ServiceInfo.getId() + ServiceMessageCode.CAMPAIGN_NOT_FOUND);
    }
    CampaignOffer campaignOffer = campaignOfferRepository.
        findByCampaignIdAndIdAndIsDeletedFalse(campaignId, campaignOfferId);
    if (campaignOffer == null) {
      throw new ResourceNotFoundException("CampaignOffer Not Found",
          ServiceInfo.getId() + ServiceMessageCode.CAMPAIGN_OFFER_NOT_FOUND);
    }
    campaignOffer.setIsDeleted(true);
    campaignOffer.setDeleterUserId(userId);
    campaignOfferRepository.save(campaignOffer);

    return true;
  }

  @Override
  public List<OfferResponseListDto> findOfferNotExistCampaign(Integer campaignId)
      throws ResourceNotFoundException {
    Campaign campaign = campaignRepository.findByIdAndIsDeletedFalse(campaignId);
    if (campaign == null) {
      throw new ResourceNotFoundException("Campaign Not Found",
          ServiceInfo.getId() + ServiceMessageCode.CAMPAIGN_NOT_FOUND);
    }
    List<Offer> offers;
    List<CampaignOffer> campaignOffers = campaignOfferRepository
        .findCampaignOfferByCampaignIdAndIsDeletedIsFalse(
            campaignId);
    if (campaignOffers.isEmpty()) {
      offers = offerRepository.findByIsDeletedFalse();
    } else {
      Set<Integer> offerIdExists = campaignOffers.stream().map(CampaignOffer::getOfferId)
          .collect(Collectors.toSet());
      offers = offerRepository.findByIdNotInAndIsDeletedFalse(offerIdExists);
    }

    return offers.stream().map(offerMapper::toDto).collect(Collectors.toList());
  }

  /**
   * @param userId
   * @param dto
   * @return ID
   * @throws ResourceNotFoundException
   * @throws OperationNotImplementException
   * @apiNote API them moi offer cho chien dich
   */
  @Override
  public Integer createCampaignOffer(Integer userId, CampaignOfferRequestDto dto)
      throws ResourceNotFoundException, OperationNotImplementException {
    Campaign campaign = campaignRepository.findByIdAndIsDeletedFalse(dto.getCampaignId());
    if (campaign == null) {
      throw new ResourceNotFoundException("Campaign Not Found",
          ServiceInfo.getId() + ServiceMessageCode.CAMPAIGN_NOT_FOUND);
    }
    Offer offer = offerRepository.findByIdAndIsDeletedFalse(dto.getOfferId());
    if (offer == null) {
      throw new ResourceNotFoundException("Offer Not Found",
          ServiceInfo.getId() + ServiceMessageCode.OFFER_NOT_FOUND);
    }
    List<CampaignOffer> campaignOffers = campaignOfferRepository.
        findByCampaignIdAndOfferIdAndIsDeletedFalse(dto.getCampaignId(), dto.getOfferId());
    if (campaignOffers != null && !campaignOffers.isEmpty()) {
      throw new OperationNotImplementException("CampaignOffer is exits.",
          ServiceInfo.getId() + ServiceMessageCode.CAMPAIGN_OFFER_EXIST);
    }
    CampaignOffer campaignOffer = new CampaignOffer();
    campaignOffer.setCampaign(campaign);
    campaignOffer.setOffer(offer);
    campaignOffer.setCreatorUserId(userId);
    campaignOfferRepository.save(campaignOffer);

    return campaignOffer.getId();
  }
}
