package com.ttt.mar.sms.service.impl;

import com.ttt.mar.sms.config.ServiceMessageCode;
import com.ttt.mar.sms.dto.inforsms.PayloadRequestFromNotifyServiceDto;
import com.ttt.mar.sms.dto.smsconfig.SmsConfigDtoRequest;
import com.ttt.mar.sms.dto.smsconfig.SmsConfigDtoResponse;
import com.ttt.mar.sms.dto.smsconfig.SmsConfigResponseDetailDto;
import com.ttt.mar.sms.entities.SmsConfig;
import com.ttt.mar.sms.entities.SmsPublisher;
import com.ttt.mar.sms.filter.SmsConfigFilter;
import com.ttt.mar.sms.mapper.SmsConfigMapper;
import com.ttt.mar.sms.repositories.SmsConfigRepository;
import com.ttt.mar.sms.repositories.SmsPublisherRepository;
import com.ttt.mar.sms.service.iface.SmsConfigService;
import com.ttt.mar.sms.service.iface.kafka.ProducerService;
import com.ttt.rnd.common.config.ServiceInfo;
import com.ttt.rnd.common.dto.DataPagingResponse;
import com.ttt.rnd.common.exception.DuplicateEntityException;
import com.ttt.rnd.common.exception.IdentifyBlankException;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import com.ttt.rnd.common.utils.SortingUtils;
import com.ttt.rnd.lib.entities.User;
import com.ttt.rnd.lib.entities.UserStatus;
import com.ttt.rnd.lib.repositories.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class SmsServiceConfigImpl implements SmsConfigService {

  @Autowired
  private SmsConfigRepository smsConfigRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private SmsConfigMapper smsConfigMapper;

  @Autowired
  private SmsPublisherRepository smsPublisherRepository;

  @Autowired
  private ProducerService<PayloadRequestFromNotifyServiceDto> producerService;

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
      .getLogger(SmsServiceConfigImpl.class);

  @Override
  public Integer createSmsConfig(SmsConfigDtoRequest smsConfigDtoRequest, Integer userId)
      throws OperationNotImplementException, ResourceNotFoundException, IdentifyBlankException, DuplicateEntityException {

    checkValidSmsConfig(smsConfigDtoRequest);
    User user = checkValidUser(userId);

    SmsConfig smsConfigByBrandCodeAndPhoneNumber = smsConfigRepository
        .findByAccountCodeAndIsDeletedFalse(smsConfigDtoRequest.getAccountCode());

    if (smsConfigByBrandCodeAndPhoneNumber != null) {
      throw new DuplicateEntityException("Sms config exists",
          ServiceInfo.getId() + ServiceMessageCode.DUPLICATE_SMS_CONFIG);
    }

    SmsPublisher smsPublisherById = smsPublisherRepository
        .findByIdAndIsDeletedFalse(smsConfigDtoRequest.getPublisherId());

    if (smsPublisherById == null) {
      throw new ResourceNotFoundException("Publisher not found",
          ServiceInfo.getId() + ServiceMessageCode.ID_SMS_PUBLISHER_NULL);
    }

    SmsConfig smsConfig = smsConfigMapper.fromDto(smsConfigDtoRequest);

    logger.info("smsConfigDtoRequest: {}", smsConfigDtoRequest);
    smsConfig.setSmsPublisher(smsPublisherById);
    smsConfig.setCreatorUser(user);
    smsConfigRepository.save(smsConfig);
    return smsConfigRepository.save(smsConfig).getId();
  }

  public void checkValidSmsConfig(SmsConfigDtoRequest smsConfigDtoRequest)
      throws OperationNotImplementException, IdentifyBlankException {
    if (smsConfigDtoRequest == null) {
      throw new OperationNotImplementException("Operation not implement",
          ServiceInfo.getId() + ServiceMessageCode.BODY_REQUEST_NULL);
    }

    if (smsConfigDtoRequest.getAccountCode() == null || smsConfigDtoRequest.getAccountCode()
        .isEmpty()) {
      throw new OperationNotImplementException("accountCode null",
          ServiceInfo.getId() + ServiceMessageCode.ACCOUNT_CODE_NULL);
    }

    if (smsConfigDtoRequest.getPhoneNumber() == null || smsConfigDtoRequest.getPhoneNumber()
        .isEmpty()) {
      throw new OperationNotImplementException("phoneNumber null",
          ServiceInfo.getId() + ServiceMessageCode.PHONE_NUMBER_NULL);
    }

    if (smsConfigDtoRequest.getPublisherId() == null) {
      throw new OperationNotImplementException("publiserId null",
          ServiceInfo.getId() + ServiceMessageCode.ID_SMS_PUBLISHER_NULL);
    }
  }

  // check valid user
  public User checkValidUser(Integer userId)
      throws ResourceNotFoundException, OperationNotImplementException {
    Optional<User> userOptionalById = userRepository.findById(userId);
    if (userOptionalById.isEmpty() || userOptionalById.get().getIsDeleted()) {
      throw new ResourceNotFoundException("Resource not found" + userId,
          ServiceInfo.getId() + ServiceMessageCode.USER_NOT_FOUND);
    }
    if (userOptionalById.get().getStatus().equals(UserStatus.DEACTIVE)) {
      throw new OperationNotImplementException("User deactive",
          ServiceInfo.getId() + ServiceMessageCode.USER_DEACTIVE);
    }

    return userOptionalById.get();
  }

  @Override
  public DataPagingResponse<SmsConfigDtoResponse> getSmsConfigs(String search, Integer publisherId,
      String sort, Integer page, Integer limit) {

    Map<String, String> map = SortingUtils.detectSortType(sort);
    Page<SmsConfig> smsConfigPage = smsConfigRepository
        .findAll(new SmsConfigFilter().getByFilter(search, publisherId, false, map),
            PageRequest.of(page - 1, limit));
    List<SmsConfigDtoResponse> listSmsConfigDtoResponse = new ArrayList<>();
    smsConfigPage.getContent().forEach(
        smsConfig -> listSmsConfigDtoResponse.add(smsConfigMapper.toSmsConfigResponse(smsConfig)));
    DataPagingResponse<SmsConfigDtoResponse> dataPagingResponses = new DataPagingResponse<>();
    dataPagingResponses.setList(listSmsConfigDtoResponse);
    dataPagingResponses.setTotalPage(smsConfigPage.getTotalPages());
    dataPagingResponses.setNum(smsConfigPage.getTotalElements());
    dataPagingResponses.setCurrentPage(page);
    return dataPagingResponses;
  }

  @Override
  public Boolean deleteSmsConfigById(Integer id, Integer userId)
      throws ResourceNotFoundException, IdentifyBlankException, OperationNotImplementException {
    if (id == null) {
      throw new IdentifyBlankException("Id null",
          ServiceInfo.getId() + ServiceMessageCode.ID_SMS_CONFIG_NULL);
    }
    User user = checkValidUser(userId);
    SmsConfig smsConfigById = smsConfigRepository.findByIdAndIsDeletedFalse(id);
    if (smsConfigById == null) {
      throw new ResourceNotFoundException("Resource not found",
          ServiceInfo.getId() + ServiceMessageCode.RESOURCE_NOT_FOUND);
    }
    SmsPublisher smsPublisher = smsConfigById.getSmsPublisher();
    if (smsPublisher == null || smsPublisher.getIsDeleted()) {
      throw new ResourceNotFoundException("Resource not found",
          ServiceInfo.getId() + ServiceMessageCode.RESOURCE_NOT_FOUND);
    }

    smsConfigById.setIsDeleted(Boolean.TRUE);
    smsConfigById.setDeleterUser(user);
    smsConfigRepository.save(smsConfigById);
    return true;
  }

  @Override
  public Boolean updateSmsConfigById(SmsConfigDtoRequest smsConfigDtoRequest,
      Integer userId)
      throws OperationNotImplementException, ResourceNotFoundException, IdentifyBlankException, DuplicateEntityException {
    if (smsConfigDtoRequest.getId() == null) {
      throw new IdentifyBlankException("Id null",
          ServiceInfo.getId() + ServiceMessageCode.ID_SMS_CONFIG_NULL);
    }
    checkValidSmsConfig(smsConfigDtoRequest);
    User user = checkValidUser(userId);

    SmsConfig smsConfigById = smsConfigRepository
        .findByIdAndIsDeletedFalse(smsConfigDtoRequest.getId());

    if (smsConfigById == null) {
      throw new ResourceNotFoundException("Resource not found",
          ServiceInfo.getId() + ServiceMessageCode.RESOURCE_NOT_FOUND);
    }

    SmsPublisher smsPublisherById = smsPublisherRepository
        .findByIdAndIsDeletedFalse(smsConfigDtoRequest.getPublisherId());

    if (smsPublisherById == null) {
      throw new ResourceNotFoundException("Publisher not found",
          ServiceInfo.getId() + ServiceMessageCode.ID_SMS_PUBLISHER_NULL);
    }

    smsConfigMapper.updateModel(smsConfigById, smsConfigDtoRequest);
    smsConfigById.setSmsPublisher(smsPublisherById);
    smsConfigById.setUpdaterUser(user);
    smsConfigRepository.save(smsConfigById);
    return true;
  }

  @Override
  public SmsConfigResponseDetailDto getSmsConfigById(Integer smsConfigId)
      throws ResourceNotFoundException, IdentifyBlankException {
    if (smsConfigId == null) {
      throw new IdentifyBlankException("Id null",
          ServiceInfo.getId() + ServiceMessageCode.ID_SMS_CONFIG_NULL);
    }
    SmsConfig smsConfigById = smsConfigRepository.findByIdAndIsDeletedFalse(smsConfigId);
    if (smsConfigById == null) {
      throw new ResourceNotFoundException("SmsConfig not found",
          ServiceInfo.getId() + ServiceMessageCode.RESOURCE_NOT_FOUND);
    }
    SmsPublisher smsPublisher = smsConfigById.getSmsPublisher();

    if (smsPublisher == null || smsPublisher.getIsDeleted()) {
      throw new ResourceNotFoundException("SmsPublisher not found",
          ServiceInfo.getId() + ServiceMessageCode.RESOURCE_NOT_FOUND);
    }

    SmsConfigResponseDetailDto smsConfigResponseDetailDto = smsConfigMapper
        .toSmsConfigResponseDetailDto(smsConfigById);

    logger.info("smsConfigResponseDetailDto: {}", smsConfigResponseDetailDto);

    return smsConfigResponseDetailDto;
  }

}
