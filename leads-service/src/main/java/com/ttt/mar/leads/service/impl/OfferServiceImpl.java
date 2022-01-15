package com.ttt.mar.leads.service.impl;

import com.ttt.mar.leads.config.ServiceMessageCode;
import com.ttt.mar.leads.dto.LeadConditionRequestDto;
import com.ttt.mar.leads.dto.OfferConditionResponseDto;
import com.ttt.mar.leads.dto.OfferInfoRequestDto;
import com.ttt.mar.leads.dto.OfferInfoResponseDto;
import com.ttt.mar.leads.dto.OfferRequestDto;
import com.ttt.mar.leads.dto.OfferResponseDto;
import com.ttt.mar.leads.dto.OfferResponseListDto;
import com.ttt.mar.leads.dto.OfferUpdateRequestDto;
import com.ttt.mar.leads.entities.Condition;
import com.ttt.mar.leads.entities.Offer;
import com.ttt.mar.leads.entities.OfferCondition;
import com.ttt.mar.leads.entities.OfferConditionValue;
import com.ttt.mar.leads.entities.OfferInfo;
import com.ttt.mar.leads.filter.OfferFilter;
import com.ttt.mar.leads.mapper.OfferConditionMapper;
import com.ttt.mar.leads.mapper.OfferInfoMapper;
import com.ttt.mar.leads.mapper.OfferMapper;
import com.ttt.mar.leads.repositories.ConditionRepository;
import com.ttt.mar.leads.repositories.OfferConditionRepository;
import com.ttt.mar.leads.repositories.OfferConditionValueRepository;
import com.ttt.mar.leads.repositories.OfferInfoRepository;
import com.ttt.mar.leads.repositories.OfferRepository;
import com.ttt.mar.leads.service.iface.OfferService;
import com.ttt.mar.leads.service.iface.UserService;
import com.ttt.rnd.common.config.ServiceInfo;
import com.ttt.rnd.common.dto.DataPagingResponse;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import com.ttt.rnd.common.utils.JsonUtils;
import com.ttt.rnd.common.utils.SortingUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class OfferServiceImpl implements OfferService {

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
      .getLogger(OfferServiceImpl.class);

  private final OfferRepository offerRepository;

  private final OfferConditionRepository offerConditionRepository;

  private final OfferInfoRepository offerInfoRepository;

  private final OfferConditionValueRepository offerConditionValueRepository;

  private final ConditionRepository conditionRepository;

  private final UserService userService;

  private final OfferConditionMapper offerConditionMapper;

  private final OfferInfoMapper offerInfoMapper;

  private final OfferMapper offerMapper;

  private final OfferFilter offerFilter;


  public OfferServiceImpl(OfferRepository offerRepository,
      OfferConditionRepository offerConditionRepository,
      OfferConditionValueRepository offerConditionValueRepository,
      ConditionRepository conditionRepository,
      UserService userService,
      OfferConditionMapper offerConditionMapper,
      OfferInfoMapper offerInfoMapper,
      OfferInfoRepository offerInfoRepository,
      OfferMapper offerMapper,
      OfferFilter offerFilter) {
    this.offerRepository = offerRepository;
    this.offerConditionRepository = offerConditionRepository;
    this.offerConditionValueRepository = offerConditionValueRepository;
    this.conditionRepository = conditionRepository;
    this.userService = userService;
    this.offerConditionMapper = offerConditionMapper;
    this.offerInfoMapper = offerInfoMapper;
    this.offerInfoRepository = offerInfoRepository;
    this.offerMapper = offerMapper;
    this.offerFilter = offerFilter;
  }

  /**
   * @param userId
   * @param dto
   * @return
   * @apiNote API them moi offer
   */
  @Override
  @Transactional
  public Integer createOffer(Integer userId, OfferRequestDto dto)
      throws OperationNotImplementException, ResourceNotFoundException {
    logger.info("<<< createOffer(), userId = {}, OfferRequestDto = {}", userId, dto);
    Offer offerByCode = offerRepository.findByCodeAndIsDeletedFalse(dto.getCode());
    if (offerByCode != null) {
      throw new OperationNotImplementException("Offer By Code is Exist.",
          ServiceInfo.getId() + ServiceMessageCode.OFFER_BY_CODE_EXIST);
    }
    Offer offer = offerMapper.fromDto(dto);
    offer.setCreatorUserId(userId);
    List<OfferConditionValue> offerConditionValues = new ArrayList<>();
    List<OfferCondition> offerConditions = validateOfferConditionCreateOffer(userId, offer,
        dto.getLeadConditions(), offerConditionValues);
    List<OfferInfo> offerInfos = validateOfferInfoCreateOffer(
        userId, offer, dto.getOfferInfos());
    offerRepository.save(offer);
    if (!offerConditions.isEmpty()) {
      offerConditionRepository.saveAll(offerConditions);
    }
    if (!offerConditionValues.isEmpty()) {
      offerConditionValueRepository.saveAll(offerConditionValues);
    }
    if (!offerInfos.isEmpty()) {
      offerInfoRepository.saveAll(offerInfos);
    }
    return offer.getId();
  }

  private List<OfferInfo> validateOfferInfoCreateOffer(Integer userId, Offer offer,
      List<OfferInfoRequestDto> offerInfoRequestDtos) throws OperationNotImplementException {
    logger.info("<<< validateOfferInfoCreateOffer()");
    List<OfferInfo> offerInfos = new ArrayList<>();
    if (offerInfoRequestDtos == null || offerInfoRequestDtos.isEmpty()) {
      return offerInfos;
    }
    Set<Integer> fieldIds = new HashSet<>();
    for (OfferInfoRequestDto offerInfoRequestDto : offerInfoRequestDtos) {
//      if (fieldIds.contains(offerInfoRequestDto.getFieldId())) {
//        throw new OperationNotImplementException("FieldCode is Exist.",
//            ServiceInfo.getId() + ServiceMessageCode.FIELD_CODE_OFFER_INFO_EXIST);
//      }
      fieldIds.add(offerInfoRequestDto.getFieldId());
      OfferInfo offerInfo = offerInfoMapper.fromDto(offerInfoRequestDto);
      offerInfo.setOffer(offer);
      offerInfo.setCreatorUserId(userId);
      offerInfos.add(offerInfo);
    }
    return offerInfos;
  }

  private List<OfferCondition> validateOfferConditionCreateOffer(
      Integer userId, Offer offer, List<LeadConditionRequestDto> leadConditionRequestDtos,
      List<OfferConditionValue> offerConditionValues)
      throws OperationNotImplementException, ResourceNotFoundException {
    logger.info("<<< validateOfferConditionCreateOffer()");
    List<OfferCondition> offerConditions = new ArrayList<>();
    if (leadConditionRequestDtos == null || leadConditionRequestDtos.isEmpty()) {
      return offerConditions;
    }
    Map<Integer, Integer> conditionFieldMap = new HashMap<>();
    for (LeadConditionRequestDto leadConditionRequestDto : leadConditionRequestDtos) {
      Integer conditionId = leadConditionRequestDto.getConditionId();
      Integer fieldId = leadConditionRequestDto.getFieldId();
      // Kiem tra xem da ton tai FieldCode And ConditionId.
      if (conditionFieldMap.containsKey(conditionId)
          && fieldId.equals(conditionFieldMap.get(conditionId))) {
        throw new OperationNotImplementException("FieldId And ConditionId is Duplicate.",
            ServiceInfo.getId() + ServiceMessageCode.FIELD_ID_CONDITION_ID_DUPLICATE);
      }
      conditionFieldMap.put(conditionId, fieldId);
    }
    // Kiểm tra danh sach condition co ton tai.
    Set<Integer> conditionIdSet = conditionFieldMap.keySet();
    Set<Condition> conditions = conditionRepository.findByIdInAndIsDeletedFalse(conditionIdSet);
    if (conditions.size() != conditionIdSet.size()) {
      throw new ResourceNotFoundException("Condition Not Found.",
          ServiceInfo.getId() + ServiceMessageCode.CONDITION_NOT_FOUND);
    }
    Map<Integer, Condition> conditionMap = new HashMap<>();
    conditionMap.putAll(conditions.stream()
        .collect(Collectors.toMap(Condition::getId, entity -> entity)));
    for (LeadConditionRequestDto leadConditionRequestDto : leadConditionRequestDtos) {
      OfferCondition offerCondition = offerConditionMapper.fromDto(leadConditionRequestDto);
      Condition condition = conditionMap.get(leadConditionRequestDto.getConditionId());
      offerCondition.setCondition(condition);
      offerCondition.setOffer(offer);
      offerCondition.setCreatorUserId(userId);
      offerConditions.add(offerCondition);

      List<String> values = leadConditionRequestDto.getValues();
      if (values != null && !values.isEmpty()) {
        offerConditionValues.addAll(values.stream()
            .map(value -> new OfferConditionValue(offerCondition, value, userId))
            .collect(Collectors.toList()));
      }
    }

    return offerConditions;
  }

  /**
   * @param userId
   * @param offerId
   * @param dto
   * @return
   * @throws ResourceNotFoundException
   * @throws OperationNotImplementException
   * @apiNote API cap nhat offer
   */
  @Override
  public Integer updateOffer(Integer userId, Integer offerId, OfferUpdateRequestDto dto)
      throws ResourceNotFoundException, OperationNotImplementException {
    logger.info("<<< updateOffer(), userId = {}, offerId = {}, OfferUpdateRequestDto = {}",
        userId, offerId, JsonUtils.toJson(dto));
    userService.checkValidUser(userId);
    Offer offerExist = offerRepository.findByIdAndIsDeletedFalse(offerId);
    if (offerExist == null) {
      throw new ResourceNotFoundException("Offer Not Found.",
          ServiceInfo.getId() + ServiceMessageCode.OFFER_NOT_FOUND);
    }
    BeanUtils.copyProperties(dto, offerExist);
    List<OfferConditionValue> offerConditionValues = new ArrayList<>();
    List<OfferCondition> offerConditions = validateOfferConditionUpdateOffer(userId,
        offerExist, dto.getOfferConditions(), offerConditionValues);
    List<OfferInfo> offerInfos = validateOfferInfoUpdateOffer(userId, offerExist,
        dto.getOfferInfos());
    offerRepository.save(offerExist);
    if (!offerConditions.isEmpty()) {
      offerConditionRepository.saveAll(offerConditions);
    }
    if (!offerConditionValues.isEmpty()) {
      offerConditionValueRepository.saveAll(offerConditionValues);
    }
    if (!offerInfos.isEmpty()) {
      offerInfoRepository.saveAll(offerInfos);
    }
    return offerId;
  }

  private List<OfferInfo> validateOfferInfoUpdateOffer(Integer userId, Offer offer,
      List<OfferInfoRequestDto> offerInfoRequestDtos) throws OperationNotImplementException {
    logger.info("<<< validateOfferInfoUpdateOffer()");
    List<OfferInfo> offerInfos = new ArrayList<>();
    Integer offerId = offer.getId();
    List<OfferInfo> offerInfoDeletes = offerInfoRepository.findByOfferIdAndIsDeletedFalse(offerId);
    if (!offerInfoDeletes.isEmpty()) {
      for (OfferInfo offerInfo : offerInfoDeletes) {
        offerInfo.setIsDeleted(true);
        offerInfo.setUpdaterUserId(userId);
        offerInfos.add(offerInfo);
      }
    }
    offerInfos.addAll(validateOfferInfoCreateOffer(userId, offer, offerInfoRequestDtos));

    return offerInfos;
  }

  private List<OfferCondition> validateOfferConditionUpdateOffer(Integer userId, Offer offer,
      List<LeadConditionRequestDto> leadConditionRequestDtos,
      List<OfferConditionValue> offerConditionValues)
      throws OperationNotImplementException, ResourceNotFoundException {
    logger.info(" <<< validateOfferConditionUpdateOffer()");
    List<OfferCondition> offerConditions = new ArrayList<>();
    Integer offerId = offer.getId();
    List<OfferCondition> offerConditionDeletes =
        offerConditionRepository.findByOfferIdAndIsDeletedFalse(offerId);
    if (!offerConditionDeletes.isEmpty()) {
      for (OfferCondition offerCondition : offerConditionDeletes) {
        offerCondition.setUpdaterUserId(userId);
        offerCondition.setIsDeleted(true);

        offerConditions.add(offerCondition);
      }
      Set<Integer> offerConditionIds =
          offerConditionDeletes.stream().map(OfferCondition::getId).collect(Collectors.toSet());
      List<OfferConditionValue> offerConditionValueDeletes =
          offerConditionValueRepository
              .findByOfferConditionIdInAndIsDeletedFalse(offerConditionIds);
      for (OfferConditionValue offerConditionValue : offerConditionValueDeletes) {
        offerConditionValue.setIsDeleted(true);
        offerConditionValue.setUpdaterUserId(userId);

        offerConditionValues.add(offerConditionValue);
      }
    }

    offerConditions.addAll(validateOfferConditionCreateOffer(userId,
        offer, leadConditionRequestDtos, offerConditionValues));

    return offerConditions;
  }

  @Override
  public OfferResponseDto getbyId(Integer id)
      throws ResourceNotFoundException, OperationNotImplementException {
    OfferResponseDto responseDto = new OfferResponseDto();
    Optional<Offer> optional = offerRepository.findById(id);
    if (optional.isEmpty()) {
      throw new ResourceNotFoundException("Offer not found",
          ServiceInfo.getId() + ServiceMessageCode.OFFER_NOT_FOUND);
    }
    Offer offer = optional.get();
    responseDto.setId(offer.getId());
    responseDto.setName(offer.getName());
    responseDto.setCode(offer.getCode());
    // find offer condition
    List<OfferConditionResponseDto> offerConditionResponseDtos = new ArrayList<>();
    List<OfferCondition> listOfferCondition = offerConditionRepository
        .findOfferConditionByOfferIdAndAndIsDeletedIsFalse(
            offer.getId());
    if (listOfferCondition.size() > 0) {
      offerConditionResponseDtos = listOfferCondition.stream()
          .map(offerConditionMapper::toDto)
          .collect(Collectors.toList());
      offerConditionResponseDtos.forEach(offerCondition -> offerCondition.setValues(
          offerConditionValueRepository.listConditionValueByOfferConditionId(
              offerCondition.getId())));
    }
    responseDto.setLeadConditions(offerConditionResponseDtos);
    //find Offer Info
    List<OfferInfoResponseDto> offerInfoResponseDtos = new ArrayList<>();
    List<OfferInfo> listOfferInfo = offerInfoRepository.findOfferInfoByOfferIdAndIsDeletedIsFalse(
        offer.getId());
    offerInfoResponseDtos = listOfferInfo.stream()
        .map(offerInfoMapper::toDto)
        .collect(Collectors.toList());
    responseDto.setOfferInfos(offerInfoResponseDtos);
    return responseDto;
  }

  @Override
  public Boolean deleteOffer(Integer userId, Integer id)
      throws ResourceNotFoundException, OperationNotImplementException {
    userService.checkValidUser(userId);
    Offer offer = offerRepository.findByIdAndIsDeletedFalse(id);
    if (offer == null) {
      throw new ResourceNotFoundException("offer Not Found",
          ServiceInfo.getId() + ServiceMessageCode.OFFER_NOT_FOUND);
    }
    offer.setIsDeleted(true);
    offer.setDeleterUserId(userId);
    offerRepository.save(offer);
    return true;
  }

  @Override
  public DataPagingResponse<OfferResponseListDto> getAll(Integer page, Integer limit, Long fromDate,
      Long toDate, String search, String sort) {
    logger.info(" <<< getAllOffer()");
    Map<String, String> map = SortingUtils.detectSortType(sort);
    if (map.isEmpty()) {
      map.put("createdTime", "desc");
    }

    Page<Offer> offerPages = offerRepository.findAll(
        offerFilter.filter(search, map, fromDate, toDate), PageRequest.of(page - 1, limit));
    List<OfferResponseListDto> responseListDtos =
        offerPages.getContent().stream().map(offerMapper::toDto).collect(Collectors.toList());
    DataPagingResponse<OfferResponseListDto> dataPagingResponse = new DataPagingResponse<>();
    dataPagingResponse.setList(responseListDtos);
    dataPagingResponse.setTotalPage(offerPages.getTotalPages());
    dataPagingResponse.setNum(offerPages.getTotalElements());
    dataPagingResponse.setCurrentPage(page);

    return dataPagingResponse;
  }
}
