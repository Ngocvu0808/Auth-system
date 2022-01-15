package com.ttt.mar.leads.service.impl;

import com.ttt.mar.leads.config.ServiceMessageCode;
import com.ttt.mar.leads.dto.CampaignFilterInCampaignResponseDto;
import com.ttt.mar.leads.dto.CampaignOfferInCampaignResponseDto;
import com.ttt.mar.leads.dto.CampaignRequestDto;
import com.ttt.mar.leads.dto.CampaignResponse;
import com.ttt.mar.leads.dto.CampaignResponseDto;
import com.ttt.mar.leads.dto.CampaignStatusDto;
import com.ttt.mar.leads.dto.CampaignUpdateDto;
import com.ttt.mar.leads.dto.CampaignsCodeResponseDto;
import com.ttt.mar.leads.dto.DistributeResponseDto;
import com.ttt.mar.leads.dto.LeadSourceResponseDto;
import com.ttt.mar.leads.dto.OfferResponseListDto;
import com.ttt.mar.leads.dto.UserResponseDto;
import com.ttt.mar.leads.entities.Campaign;
import com.ttt.mar.leads.entities.CampaignDistribute;
import com.ttt.mar.leads.entities.CampaignFilter;
import com.ttt.mar.leads.entities.CampaignOffer;
import com.ttt.mar.leads.entities.CampaignSource;
import com.ttt.mar.leads.entities.CampaignStatus;
import com.ttt.mar.leads.entities.DistributeApi;
import com.ttt.mar.leads.entities.DistributeApiStatus;
import com.ttt.mar.leads.entities.LeadSource;
import com.ttt.mar.leads.entities.LeadSourceStatus;
import com.ttt.mar.leads.entities.Project;
import com.ttt.mar.leads.entities.ProjectDistribute;
import com.ttt.mar.leads.entities.ProjectSource;
import com.ttt.mar.leads.entities.ProjectStatus;
import com.ttt.mar.leads.filter.CampaignsFilter;
import com.ttt.mar.leads.filter.CampaignOfferFilter;
import com.ttt.mar.leads.filter.FilterOfCampaignFilter;
import com.ttt.mar.leads.mapper.CampaignFilterMapper;
import com.ttt.mar.leads.mapper.CampaignMapper;
import com.ttt.mar.leads.mapper.CampaignOfferMapper;
import com.ttt.mar.leads.mapper.DistributeApiMapper;
import com.ttt.mar.leads.mapper.FilterMapper;
import com.ttt.mar.leads.mapper.LeadSourceMapper;
import com.ttt.mar.leads.mapper.OfferMapper;
import com.ttt.mar.leads.mapper.UserMapper;
import com.ttt.mar.leads.repositories.CampaignDistributeRepository;
import com.ttt.mar.leads.repositories.CampaignFilterRepository;
import com.ttt.mar.leads.repositories.CampaignOfferRepository;
import com.ttt.mar.leads.repositories.CampaignRepository;
import com.ttt.mar.leads.repositories.CampaignSourceRepository;
import com.ttt.mar.leads.repositories.DistributeApiRepository;
import com.ttt.mar.leads.repositories.LeadSourceRepository;
import com.ttt.mar.leads.repositories.OfferRepository;
import com.ttt.mar.leads.repositories.ProjectDistributeRepository;
import com.ttt.mar.leads.repositories.ProjectRepository;
import com.ttt.mar.leads.repositories.ProjectSourceRepository;
import com.ttt.mar.leads.service.iface.CampaignService;
import com.ttt.mar.leads.service.iface.UserService;
import com.ttt.mar.leads.utils.DateUtil;
import com.ttt.rnd.common.config.ServiceInfo;
import com.ttt.rnd.common.dto.DataPagingResponse;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import com.ttt.rnd.common.utils.SortingUtils;
import com.ttt.rnd.lib.entities.User;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author KietDT
 * @created_date 09/06/2021
 */
@Service
public class CampaignServiceImpl implements CampaignService {

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
      .getLogger(CampaignServiceImpl.class);

  private final CampaignRepository campaignRepository;

  private final ProjectRepository projectRepository;

  private final DistributeApiRepository distributeApiRepository;

  private final ProjectDistributeRepository projectDistributeRepository;

  private final LeadSourceRepository leadSourceRepository;

  private final ProjectSourceRepository projectSourceRepository;

  private final CampaignDistributeRepository campaignDistributeRepository;

  private final CampaignSourceRepository campaignSourceRepository;

  private final CampaignMapper campaignMapper;

  private final CampaignsFilter campaignFilter;

  private final UserService userService;

  private final UserMapper userMapper;

  private final LeadSourceMapper leadSourceMapper;

  private final DistributeApiMapper distributeApiMapper;

  private final CampaignOfferRepository campaignOfferRepository;

  private final OfferRepository offerRepository;

  private final OfferMapper offerMapper;

  private final CampaignFilterRepository campaignFilterRepository;

  private final FilterMapper filterMapper;

  private final CampaignFilterMapper campaignFilterMapper;

  private final CampaignOfferMapper campaignOfferMapper;


  public CampaignServiceImpl(CampaignRepository campaignRepository,
      ProjectRepository projectRepository,
      DistributeApiRepository distributeApiRepository,
      ProjectDistributeRepository projectDistributeRepository,
      LeadSourceRepository leadSourceRepository,
      ProjectSourceRepository projectSourceRepository,
      CampaignDistributeRepository campaignDistributeRepository,
      CampaignSourceRepository campaignSourceRepository,
      CampaignMapper campaignMapper, CampaignsFilter campaignFilter,
      UserService userService, LeadSourceMapper leadSourceMapper,
      DistributeApiMapper distributeApiMapper, UserMapper userMapper,
      CampaignOfferRepository campaignOfferRepository,
      OfferRepository offerRepository, OfferMapper offerMapper,
      CampaignFilterRepository campaignFilterRepository,
      FilterMapper filterMapper, CampaignFilterMapper campaignFilterMapper,
      CampaignOfferMapper campaignOfferMapper) {
    this.campaignRepository = campaignRepository;
    this.projectRepository = projectRepository;
    this.distributeApiRepository = distributeApiRepository;
    this.projectDistributeRepository = projectDistributeRepository;
    this.leadSourceRepository = leadSourceRepository;
    this.projectSourceRepository = projectSourceRepository;
    this.campaignDistributeRepository = campaignDistributeRepository;
    this.campaignSourceRepository = campaignSourceRepository;
    this.campaignMapper = campaignMapper;
    this.campaignFilter = campaignFilter;
    this.userService = userService;
    this.leadSourceMapper = leadSourceMapper;
    this.distributeApiMapper = distributeApiMapper;
    this.userMapper = userMapper;
    this.campaignOfferRepository = campaignOfferRepository;
    this.offerRepository = offerRepository;
    this.offerMapper = offerMapper;
    this.campaignFilterRepository = campaignFilterRepository;
    this.filterMapper = filterMapper;
    this.campaignFilterMapper = campaignFilterMapper;
    this.campaignOfferMapper = campaignOfferMapper;
  }

  @Override
  public DataPagingResponse<CampaignResponse> getListCampaignFilter(Integer projectId,
      Integer leadSourceId, Integer distributeId, ProjectStatus projectStatus, String search,
      String sort, int page, int limit)
      throws ResourceNotFoundException {
    logger.info("--- getListCampaignFilter() ---");
    Map<String, String> map = SortingUtils.detectSortType(sort);
    if (map.isEmpty()) {
      map.put("createdTime", "desc");
    }
    Page<Campaign> campaignPage = campaignRepository
        .findAll(campaignFilter
                .filter(projectId, leadSourceId, distributeId, projectStatus, search, map),
            PageRequest.of(page - 1, limit));
    List<CampaignResponse> campaignResponses = mappingCampaignDto(campaignPage.getContent());
    DataPagingResponse<CampaignResponse> dataPagingResponses = new DataPagingResponse<>();
    dataPagingResponses.setList(campaignResponses);
    dataPagingResponses.setTotalPage(campaignPage.getTotalPages());
    dataPagingResponses.setNum(campaignPage.getTotalElements());
    dataPagingResponses.setCurrentPage(page);

    return dataPagingResponses;
  }

  public List<CampaignResponse> mappingCampaignDto(List<Campaign> campaigns)
      throws ResourceNotFoundException {
    logger.info("--- mappingCampaignDto() ---");
    List<CampaignResponse> campaignResponse = new ArrayList<>();
    if (campaigns == null || campaigns.isEmpty()) {
      return campaignResponse;
    }
    Set<Integer> userIdSet = campaigns.stream().map(Campaign::getCreatorUserId)
        .filter(Objects::nonNull).collect(Collectors.toSet());
    Map<Integer, UserResponseDto> userResponseDtoMap = new HashMap<>();
    if (!userIdSet.isEmpty()) {
      userResponseDtoMap = userService.mapByListId(userIdSet);
    }
    for (Campaign campaign : campaigns) {
      CampaignResponse response = campaignMapper.toDto(campaign);
      Project project = projectRepository.findByIdAndIsDeletedFalse(campaign.getProjectId());
      if (project == null) {
        throw new ResourceNotFoundException("Project not found.",
            ServiceInfo.getId() + ServiceMessageCode.PROJECT_NOT_FOUND);
      }
      response.setProjectCode(project.getCode());
      List<CampaignSource> campaignSource = campaignSourceRepository
          .findByCampaignIdAndIsDeletedFalse(campaign.getId());
      if (!campaignSource.isEmpty()) {
        LeadSource leadSource = leadSourceRepository
            .findByIdAndIsDeletedFalse(campaignSource.get(0).getSourceId());
        if (leadSource != null) {
          response.setLeadSourceName(leadSource.getName());
        }
      }
      List<CampaignDistribute> campaignDistributes = campaignDistributeRepository
          .findByCampaignIdAndIsDeletedFalse(campaign.getId());
      if (!campaignDistributes.isEmpty()) {
        DistributeApi distributeApi = distributeApiRepository
            .findByIdAndIsDeletedFalse(campaignDistributes.get(0).getDistributeId());
        if (distributeApi != null) {
          response.setDistributeName(distributeApi.getName());
        }
      }
      Integer creatorId = campaign.getCreatorUserId();
      if (creatorId != null && userResponseDtoMap.containsKey(creatorId)) {
        UserResponseDto userResponseDto = userResponseDtoMap.get(creatorId);
        response.setCreatedUser(userResponseDto);
      }
      campaignResponse.add(response);
    }

    return campaignResponse;
  }

  /**
   * @param userId Ma nguoi khoi tao
   * @param dto    Request Body khoi tao chien dich
   * @return Ma chien dich
   * @throws ResourceNotFoundException
   * @throws OperationNotImplementException
   * @apiNote : Tao chien dich
   */
  @Transactional
  @Override
  public Integer createCampaign(Integer userId, CampaignRequestDto dto)
      throws ResourceNotFoundException, OperationNotImplementException, ParseException {
    validateCampaignExistCode(dto.getCode());
    validateStartAndEndDate(dto.getStartDate(), dto.getEndDate());
    // Step 1: Kiem tra project co ton tai va dang active
    Project project = validCampaignByProject(dto.getProjectId());
    // Step 2: Kiem tra kenh phan phoi co ton tai theo project
    List<CampaignDistribute> campaignDistributeNews =
        validateCreateCampaignByDistribute(userId, dto.getDistributeId(), dto.getProjectId());
    // Step 3: Kiem tra nguon cap co ton tai theo Project
    List<CampaignSource> campaignSourceNews = validateCreateCampaignBySource(userId,
        dto.getLeadSourceId(), dto.getProjectId());

    Campaign campaign = campaignMapper.fromDto(dto);
    campaign.setProject(project);
    campaign.setCreatorUserId(userId);
    campaignRepository.save(campaign);

    campaignDistributeNews.forEach(campaignDistribute -> campaignDistribute.setCampaign(campaign));
    campaignDistributeRepository.saveAll(campaignDistributeNews);

    campaignSourceNews.forEach(campaignSource -> campaignSource.setCampaign(campaign));
    campaignSourceRepository.saveAll(campaignSourceNews);

    return campaign.getId();
  }

  private void validateStartAndEndDate(Date startDateTime, Date endDateTime)
      throws OperationNotImplementException, ParseException {

    if (startDateTime != null) {
      Long currentDate = DateUtil.getOnlyDateFromTimeStamp(System.currentTimeMillis());
      Long startDate = startDateTime.getTime();
      if (startDate < currentDate) {
        throw new OperationNotImplementException(
            "start time must be greater than or equal to current time.",
            ServiceInfo.getId() + ServiceMessageCode.CAMPAIGN_START_DATE_INVALID);
      }
      if (endDateTime != null) {

        Long endDate = endDateTime.getTime();
        if (startDate > endDate) {
          throw new OperationNotImplementException(
              "start time must be less than or equal to end time.",
              ServiceInfo.getId() + ServiceMessageCode.CAMPAIGN_START_DATE_INVALID);
        }
      }
    }
  }

  private Project validCampaignByProject(Integer projectId) throws ResourceNotFoundException {
    Project project = projectRepository
        .findByIdAndStatusAndIsDeletedFalse(projectId, ProjectStatus.ACTIVE);
    if (project == null) {
      throw new ResourceNotFoundException("Project Active not found.",
          ServiceInfo.getId() + ServiceMessageCode.PROJECT_NOT_FOUND);
    }

    return project;
  }

  private List<CampaignSource> validateCreateCampaignBySource(Integer userId,
      Integer sourceIdNew, Integer projectId) throws ResourceNotFoundException {
    List<CampaignSource> campaignSources = new ArrayList<>();

    LeadSource leadSource =
        leadSourceRepository.findByIdAndIsDeletedFalse(sourceIdNew);
    if (leadSource == null) {
      throw new ResourceNotFoundException("LeadSource not found.",
          ServiceInfo.getId() + ServiceMessageCode.LEAD_SOURCE_NOT_FOUND);
    }

    List<ProjectSource> projectSources = projectSourceRepository
        .findByProjectIdAndSourceIdInAndIsDeletedFalse(projectId,
            Collections.singleton(sourceIdNew));
    if (projectSources == null || projectSources.isEmpty()) {
      throw new ResourceNotFoundException("ProjectSource not found.",
          ServiceInfo.getId() + ServiceMessageCode.PROJECT_SOURCE_NOT_FOUND);
    }
    CampaignSource campaignSource = new CampaignSource();
    campaignSource.setLeadSource(leadSource);
    campaignSource.setCreatorUserId(userId);

    campaignSources.add(campaignSource);

    return campaignSources;
  }

  private List<CampaignDistribute> validateCreateCampaignByDistribute(Integer userId,
      Integer distributeIdNew, Integer projectId) throws ResourceNotFoundException {
    List<CampaignDistribute> campaignDistributeNews = new ArrayList<>();

    DistributeApi distributeApi =
        distributeApiRepository.findByIdAndIsDeletedFalse(distributeIdNew);
    if (distributeApi == null) {
      throw new ResourceNotFoundException("Distribute not found.",
          ServiceInfo.getId() + ServiceMessageCode.DISTRIBUTE_NOT_FOUND);
    }

    List<ProjectDistribute> projectDistributes = projectDistributeRepository
        .findByProjectIdAndDistributeApiIdInAndIsDeletedFalse(projectId,
            Collections.singleton(distributeIdNew));
    if (projectDistributes == null || projectDistributes.isEmpty()) {
      throw new ResourceNotFoundException("ProjectDistribute not found.",
          ServiceInfo.getId() + ServiceMessageCode.PROJECT_DISTRIBUTE_NOT_FOUND);
    }
    CampaignDistribute campaignDistribute = new CampaignDistribute();
    campaignDistribute.setDistributeApi(distributeApi);
    campaignDistribute.setCreatorUserId(userId);

    campaignDistributeNews.add(campaignDistribute);

    return campaignDistributeNews;
  }

  private void validateCampaignExistCode(String code) throws OperationNotImplementException {
    List<Campaign> campaigns = campaignRepository.findByCodeAndIsDeletedFalse(code);
    if (campaigns != null && !campaigns.isEmpty()) {
      throw new OperationNotImplementException("Code of Campaign is exist",
          ServiceInfo.getId() + ServiceMessageCode.CAMPAIGN_CODE_EXIST);
    }
  }

  /**
   * @param userId Ma nguoi cap nhat
   * @param dto    Request body chinh sua chien dich
   * @return
   * @throws ResourceNotFoundException
   * @apiNote API chinh sua thong tin chien dich
   */
  @Transactional
  @Override
  public Integer updateCampaign(Integer userId, CampaignUpdateDto dto)
      throws ResourceNotFoundException, OperationNotImplementException, ParseException {
    validateStartAndEndDate(dto.getStartDate(), dto.getEndDate());
    Campaign campaign = getById(dto.getId());
    Project projectByCampaign = campaign.getProject();
    if (projectByCampaign != null && (projectByCampaign.getIsDeleted()) || !ProjectStatus.ACTIVE
        .equals(Objects.requireNonNull(projectByCampaign).getStatus())) {
      throw new ResourceNotFoundException("Campaign for Project Invalid.",
          ServiceInfo.getId() + ServiceMessageCode.PROJECT_FOR_CAMPAIGN_INVALID);
    }

    List<CampaignDistribute> campaignDistributes = validateUpdateCampaignByDistribute(userId,
        campaign, dto.getDistributeId(), projectByCampaign.getId());

    List<CampaignSource> campaignSources = validateUpdateCampaignBySource(userId, campaign,
        dto.getLeadSourceId(), projectByCampaign.getId());

    BeanUtils.copyProperties(dto, campaign);
    campaign.setUpdaterUserId(userId);
    campaignRepository.save(campaign);

    if (!campaignDistributes.isEmpty()) {
      campaignDistributeRepository.saveAll(campaignDistributes);
    }
    if (!campaignSources.isEmpty()) {
      campaignSourceRepository.saveAll(campaignSources);
    }

    return campaign.getId();
  }

  private List<CampaignSource> validateUpdateCampaignBySource(Integer userId,
      Campaign campaign, Integer sourceIdNew, Integer projectId)
      throws ResourceNotFoundException, OperationNotImplementException {
    List<CampaignSource> campaignSourceList = new ArrayList<>();

    List<CampaignSource> campaignSources = campaignSourceRepository
        .findByCampaignIdAndIsDeletedFalse(campaign.getId());
    if (campaignSources == null || campaignSources.isEmpty()) {
      throw new ResourceNotFoundException("CampaignSource for campaign not exist.",
          ServiceInfo.getId() + ServiceMessageCode.CAMPAIGN_SOURCE_NOT_EXIST);
    }

    CampaignSource campaignSourceOld = campaignSources.get(0);
    LeadSource leadSourceOld = campaignSourceOld.getLeadSource();
    if (!sourceIdNew.equals(leadSourceOld.getId())) {
      List<CampaignSource> campaignSourceNews = validateCreateCampaignBySource(userId,
          sourceIdNew, projectId);
      campaignSourceNews.forEach(campaignDistribute -> campaignDistribute.setCampaign(campaign));
      campaignSourceList.addAll(campaignSourceNews);

      campaignSourceOld.setDeleterUserId(userId);
      campaignSourceOld.setIsDeleted(true);
      campaignSourceList.add(campaignSourceOld);

      return campaignSourceList;
    }
    if (leadSourceOld.getIsDeleted()) {
      throw new OperationNotImplementException("Source for campaign invalid.",
          ServiceInfo.getId() + ServiceMessageCode.SOURCE_FOR_CAMPAIGN_INVALID);
    }
    List<ProjectSource> projectSources = projectSourceRepository
        .findByProjectIdAndSourceIdInAndIsDeletedFalse(projectId,
            Collections.singleton(leadSourceOld.getId()));
    if (projectSources == null || projectSources.isEmpty()) {
      throw new ResourceNotFoundException("ProjectSource not found.",
          ServiceInfo.getId() + ServiceMessageCode.PROJECT_SOURCE_NOT_FOUND);
    }

    return campaignSourceList;
  }

  private List<CampaignDistribute> validateUpdateCampaignByDistribute(Integer userId,
      Campaign campaign, Integer distributeIdNew, Integer projectId)
      throws ResourceNotFoundException, OperationNotImplementException {
    List<CampaignDistribute> campaignDistributeList = new ArrayList<>();

    List<CampaignDistribute> campaignDistributes = campaignDistributeRepository
        .findByCampaignIdAndIsDeletedFalse(campaign.getId());
    if (campaignDistributes == null || campaignDistributes.isEmpty()) {
      throw new ResourceNotFoundException("CampaignDistribute for campaign not exist.",
          ServiceInfo.getId() + ServiceMessageCode.CAMPAIGN_DISTRIBUTE_NOT_EXIST);
    }

    CampaignDistribute campaignDistributeOld = campaignDistributes.get(0);
    DistributeApi distributeApiOld = campaignDistributeOld.getDistributeApi();
    if (!distributeIdNew.equals(distributeApiOld.getId())) {
      List<CampaignDistribute> campaignDistributeNews = validateCreateCampaignByDistribute(userId,
          distributeIdNew, projectId);
      campaignDistributeNews
          .forEach(campaignDistribute -> campaignDistribute.setCampaign(campaign));
      campaignDistributeList.addAll(campaignDistributeNews);

      campaignDistributeOld.setDeleterUserId(userId);
      campaignDistributeOld.setIsDeleted(true);
      campaignDistributeList.add(campaignDistributeOld);

      return campaignDistributeList;
    }
    if (distributeApiOld.getIsDeleted()) {
      throw new OperationNotImplementException("Distribute for campaign invalid.",
          ServiceInfo.getId() + ServiceMessageCode.DISTRIBUTE_FOR_CAMPAIGN_INVALID);
    }
    List<ProjectDistribute> projectDistributes = projectDistributeRepository
        .findByProjectIdAndDistributeApiIdInAndIsDeletedFalse(projectId,
            Collections.singleton(distributeApiOld.getId()));
    if (projectDistributes == null || projectDistributes.isEmpty()) {
      throw new ResourceNotFoundException("ProjectDistribute not found.",
          ServiceInfo.getId() + ServiceMessageCode.PROJECT_DISTRIBUTE_NOT_FOUND);
    }

    return campaignDistributeList;
  }

  private Campaign getById(Integer id) throws ResourceNotFoundException {
    Campaign campaign = campaignRepository.findByIdAndIsDeletedFalse(id);
    if (campaign == null) {
      throw new ResourceNotFoundException("Campaign not found.",
          ServiceInfo.getId() + ServiceMessageCode.CAMPAIGN_NOT_FOUND);
    }

    return campaign;
  }

  @Override
  public CampaignResponseDto getByID(Integer id)
      throws ResourceNotFoundException, OperationNotImplementException {
    CampaignResponseDto campaignResponseDto = new CampaignResponseDto();
    Campaign campaign = campaignRepository.findByIdAndIsDeletedFalse(id);
    if (campaign == null) {
      throw new ResourceNotFoundException("Campaign not found",
          ServiceInfo.getId() + ServiceMessageCode.CAMPAIGN_NOT_FOUND);
    }
    if (campaign.getProject() == null) {
      throw new OperationNotImplementException("Project is invalid",
          ServiceInfo.getId() + ServiceMessageCode.PROJECT_FOR_CAMPAIGN_INVALID);
    }
    Project project = projectRepository
        .findByIdAndIsDeletedFalse(campaign.getProjectId());
    if (project == null) {
      throw new OperationNotImplementException("Project status invalid",
          ServiceInfo.getId() + ServiceMessageCode.PROJECT_STATUS_INVALID);
    }
    campaignResponseDto = campaignMapper.toDtoDetail(campaign);
    // Lead Source
    List<CampaignSource> listCampaignSource = campaignSourceRepository
        .findByCampaignIdAndIsDeletedFalse(campaign.getId());
    if (listCampaignSource.isEmpty()) {
      throw new ResourceNotFoundException("Lead source not found",
          ServiceInfo.getId() + ServiceMessageCode.LEAD_SOURCE_NOT_FOUND);
    }
    Set<Integer> setIdLeadSource = listCampaignSource.stream().map(x -> x.getSourceId()).collect(
        Collectors.toSet());
    List<LeadSource> leadSourceList = leadSourceRepository
        .findByIdInAndStatusAndIsDeletedFalse(setIdLeadSource, LeadSourceStatus.ACTIVE);
    if (!leadSourceList.isEmpty()) {
      LeadSourceResponseDto leadSourceResponse = leadSourceMapper
          .toDtoToCampaignManage(leadSourceList.get(0));
      leadSourceResponse.setProjectId(campaign.getProjectId());
      campaignResponseDto.setLeadSource(leadSourceResponse);
    }
    // Distribute API
    List<CampaignDistribute> listCampaignDistribute = campaignDistributeRepository
        .findByCampaignIdAndIsDeletedFalse(campaign.getId());
    if (listCampaignDistribute.isEmpty()) {
      throw new ResourceNotFoundException("Distribute Api not found",
          ServiceInfo.getId() + ServiceMessageCode.DISTRIBUTE_NOT_FOUND);
    }
    Set<Integer> setIdCampaignDistribute = listCampaignDistribute.stream()
        .map(x -> x.getDistributeId()).collect(
            Collectors.toSet());
    List<DistributeApi> distributeApiList = distributeApiRepository
        .findByIdInAndStatusAndIsDeletedFalse(setIdCampaignDistribute, DistributeApiStatus.ACTIVE);
    if (!distributeApiList.isEmpty()) {
      DistributeResponseDto distributeResponseDto = distributeApiMapper
          .toDtoResponseToCampaignManage(distributeApiList.get(0));
      distributeResponseDto.setProjectId(campaign.getProjectId());
      campaignResponseDto.setDistribute(distributeResponseDto);
    }
    // set User
    User user = userService.checkValidUser(campaign.getCreatorUserId());
    UserResponseDto userResponseDto = userMapper.toDto(user);
    campaignResponseDto.setCreatedUser(userResponseDto);
    return campaignResponseDto;
  }

  @Override
  public Integer updateStatusCampaign(Integer userId, Integer id,
      CampaignStatusDto campaignStatusDto)
      throws OperationNotImplementException, ResourceNotFoundException, ParseException {
    userService.checkValidUser(userId);
    Long timeNow = DateUtil.getOnlyDateFromTimeStamp(System.currentTimeMillis());
    if (campaignStatusDto.getCampaignStatus() == null) {
      throw new OperationNotImplementException("Status invalid",
          ServiceInfo.getId() + ServiceMessageCode.CAMPAIGN_STATUS_INVALID);
    }
    Campaign campaign = campaignRepository.findByIdAndIsDeletedFalse(id);
    if (campaign == null) {
      throw new ResourceNotFoundException("Campaign not found",
          ServiceInfo.getId() + ServiceMessageCode.CAMPAIGN_NOT_FOUND);
    }
    Project project = projectRepository.findByIdAndIsDeletedFalse(campaign.getProjectId());
    if (project == null) {
      throw new ResourceNotFoundException("Project not found",
          ServiceInfo.getId() + ServiceMessageCode.PROJECT_NOT_FOUND);
    }
    if (ProjectStatus.DEACTIVE.equals(project.getStatus())) {
      throw new OperationNotImplementException(" Project status is deactive",
          ServiceInfo.getId() + ServiceMessageCode.PROJECT_STATUS_IS_DEACTIVE);
    }
    if (CampaignStatus.ACTIVE.equals(campaignStatusDto.getCampaignStatus())
        && campaign.getEndDate() != null) {
      if (timeNow > campaign.getEndDate().getTime()) {
        throw new OperationNotImplementException("End date of campaign is over",
            ServiceInfo.getId() + ServiceMessageCode.CAMPAIGN_TIME_OVER);
      }
    }
    campaign.setStatus(campaignStatusDto.getCampaignStatus());
    campaign.setUpdaterUserId(userId);
    campaignRepository.save(campaign);
    return id;
  }

  @Override
  public DataPagingResponse<?> getAllOfferCampaign(Integer campaignId, String sort, Integer page,
      Integer limit) {

    logger.info("--- getAllOfferCampaign() ---");
    Map<String, String> sortMap = SortingUtils.detectSortType(sort);
    if (sortMap.isEmpty()) {
      sortMap.put("createdTime", "desc");
    }
    Page<CampaignOffer> campaignOfferPage = campaignOfferRepository.findAll(
        new CampaignOfferFilter().filter(campaignId, sortMap), PageRequest.of(page - 1, limit));
    List<CampaignOffer> campaignOfferList = campaignOfferPage.getContent();

    List<CampaignOfferInCampaignResponseDto> offers = campaignOfferList.stream()
        .map(campaignOfferMapper::toOfferCampaignDto)
        .collect(Collectors.toList());

    DataPagingResponse<CampaignOfferInCampaignResponseDto> data = new DataPagingResponse<>();
    data.setCurrentPage(page);
    data.setList(offers);
    data.setNum(campaignOfferPage.getTotalElements());
    data.setTotalPage(campaignOfferPage.getTotalPages());
    return data;
  }

  @Override
  public DataPagingResponse<?> getAllFilterCampaign(Integer campaignId, String sort, Integer page,
      Integer limit) {
    logger.info("--- getAllFilterCampaign() ---");
    Map<String, String> sortMap = SortingUtils.detectSortType(sort);
    if (sortMap.isEmpty()) {
      sortMap.put("createdTime", "desc");
    }
    Page<CampaignFilter> campaignFilterPage = campaignFilterRepository.findAll(
        new FilterOfCampaignFilter().filter(campaignId, sortMap), PageRequest.of(page - 1, limit));
    List<CampaignFilter> campaignFilterList = campaignFilterPage.getContent();

//    List<CampaignFilter> filterList = campaignFilterList.stream()
//        .map(CampaignFilter::getFilter)
//        .collect(Collectors.toList());

    List<CampaignFilterInCampaignResponseDto> filters = campaignFilterList.stream()
        .map(campaignFilterMapper::toFilterCampaignDto)
        .collect(Collectors.toList());
    DataPagingResponse<CampaignFilterInCampaignResponseDto> data = new DataPagingResponse<>();
    data.setCurrentPage(page);
    data.setList(filters);
    data.setNum(campaignFilterPage.getTotalElements());
    data.setTotalPage(campaignFilterPage.getTotalPages());
    return data;
  }

  @Override
  public List<CampaignsCodeResponseDto> getCampaignsCode() {
    List<CampaignsCodeResponseDto> listCampaignsCode = new ArrayList<>();

    List<Campaign> campaigns = campaignRepository.findAllByIsDeletedFalse();
    for (Campaign campaign : campaigns){
      CampaignsCodeResponseDto campaignsCodeResponseDto = new CampaignsCodeResponseDto();
      campaignsCodeResponseDto.setId(campaign.getId());
      campaignsCodeResponseDto.setCode(campaign.getCode());
      listCampaignsCode.add(campaignsCodeResponseDto);
    }
    return listCampaignsCode;
  }
}
