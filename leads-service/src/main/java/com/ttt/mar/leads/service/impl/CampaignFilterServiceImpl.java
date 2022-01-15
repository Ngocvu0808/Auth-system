package com.ttt.mar.leads.service.impl;

import com.ttt.mar.leads.config.ServiceMessageCode;
import com.ttt.mar.leads.dto.CampaignFilterRequestDto;
import com.ttt.mar.leads.dto.FilterResponseListDto;
import com.ttt.mar.leads.entities.Campaign;
import com.ttt.mar.leads.entities.CampaignFilter;
import com.ttt.mar.leads.entities.Filter;
import com.ttt.mar.leads.mapper.CampaignFilterMapper;
import com.ttt.mar.leads.mapper.FilterMapper;
import com.ttt.mar.leads.repositories.CampaignFilterRepository;
import com.ttt.mar.leads.repositories.CampaignRepository;
import com.ttt.mar.leads.repositories.FilterRepository;
import com.ttt.mar.leads.service.iface.CampaignFilterService;
import com.ttt.mar.leads.service.iface.UserService;
import com.ttt.rnd.common.config.ServiceInfo;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import com.ttt.rnd.common.exception.ValidationException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * @author nguyen
 * @create_date 06/08/2021
 */

@Service
public class CampaignFilterServiceImpl implements CampaignFilterService {

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
      .getLogger(CampaignFilterServiceImpl.class);

  private final UserService userService;

  private final CampaignRepository campaignRepository;

  private final CampaignFilterRepository campaignFilterRepository;

  private final FilterRepository filterRepository;

  private final CampaignFilterMapper campaignFilterMapper;

  private final FilterMapper filterMapper;

  public CampaignFilterServiceImpl(UserService userService,
      CampaignRepository campaignRepository,
      CampaignFilterRepository campaignFilterRepository,
      FilterRepository filterRepository,
      CampaignFilterMapper campaignFilterMapper,
      FilterMapper filterMapper) {
    this.userService = userService;
    this.campaignRepository = campaignRepository;
    this.campaignFilterRepository = campaignFilterRepository;
    this.filterRepository = filterRepository;
    this.campaignFilterMapper = campaignFilterMapper;
    this.filterMapper = filterMapper;
  }

  @Override
  public Boolean deleteCampaignFilter(Integer userId, Integer campaignId, Integer campaignFilterId)
      throws ResourceNotFoundException, OperationNotImplementException {
    userService.checkValidUser(userId);
    Campaign campaign = campaignRepository.findByIdAndIsDeletedFalse(campaignId);
    if (campaign == null) {
      throw new ResourceNotFoundException("Campaign Not Found",
          ServiceInfo.getId() + ServiceMessageCode.CAMPAIGN_NOT_FOUND);
    }
    CampaignFilter campaignFilter = campaignFilterRepository.
        findByCampaignIdAndIdAndIsDeletedFalse(campaignId, campaignFilterId);
    if (campaignFilter == null) {
      throw new ResourceNotFoundException("CampaignFilter Not Found",
          ServiceInfo.getId() + ServiceMessageCode.CAMPAIGN_FILTER_NOT_FOUND);
    }
    campaignFilter.setIsDeleted(true);
    campaignFilter.setDeleterUserId(userId);
    campaignFilterRepository.save(campaignFilter);
    return true;
  }

  @Override
  public Integer createCampaignFilter(Integer userId, CampaignFilterRequestDto dto)
      throws ResourceNotFoundException, OperationNotImplementException, ValidationException {
    userService.checkValidUser(userId);
    Campaign campaign = campaignRepository.findByIdAndIsDeletedFalse(dto.getCampaignId());
    if (campaign == null) {
      throw new ResourceNotFoundException("Campaign Not Found",
          ServiceInfo.getId() + ServiceMessageCode.CAMPAIGN_NOT_FOUND);
    }
    Filter filter = filterRepository.findByIdAndIsDeletedIsFalse(dto.getFilterId());
    if (filter == null) {
      throw new ResourceNotFoundException("Filter Not Found",
          ServiceInfo.getId() + ServiceMessageCode.FILTER_NOT_FOUND);
    }
    List<CampaignFilter> campaignFilters = campaignFilterRepository.
        findByCampaignIdAndFilterIdAndIsDeletedFalse(dto.getCampaignId(),
            dto.getFilterId());
    if (campaignFilters != null && campaignFilters.size() != 0) {
      throw new OperationNotImplementException("CampaignFilter is exits.",
          ServiceInfo.getId() + ServiceMessageCode.CAMPAIGN_FILTER_EXIST);

    }

    CampaignFilter campaignFilter = new CampaignFilter();
    campaignFilter.setCreatorUserId(userId);
    campaignFilter.setCampaign(campaign);
    campaignFilter.setFilter(filter);
    campaignFilterRepository.save(campaignFilter);

    return campaignFilter.getId();
  }

  @Override
  public List<FilterResponseListDto> findFilterNotExistCampaign(Integer campaignId)
      throws ResourceNotFoundException {
    Campaign campaign = campaignRepository.findByIdAndIsDeletedFalse(campaignId);
    if (campaign == null) {
      throw new ResourceNotFoundException("Campaign Not Found",
          ServiceInfo.getId() + ServiceMessageCode.CAMPAIGN_NOT_FOUND);
    }
    List<Filter> filters;
    List<CampaignFilter> campaignFilters = campaignFilterRepository
        .findCampaignFilterByCampaignIdAndIsDeletedIsFalse(campaignId);
    if (campaignFilters.isEmpty()) {
      filters = filterRepository.findByIsDeletedFalse();
    } else {
      Set<Integer> offerIdExists = campaignFilters.stream().map(CampaignFilter::getFilterId)
          .collect(Collectors.toSet());
      filters = filterRepository.findByIdNotInAndIsDeletedFalse(offerIdExists);
    }

    return filters.stream().map(filterMapper::toDtoList).collect(Collectors.toList());
  }
}
