package com.ttt.mar.sms.service.impl;

import com.ttt.mar.sms.config.ServiceMessageCode;
import com.ttt.mar.sms.dto.smpublisher.SmsPublisherCustomResponseDto;
import com.ttt.mar.sms.dto.smpublisher.SmsPublisherRequestDto;
import com.ttt.mar.sms.dto.smpublisher.SmsPublisherResponseDto;
import com.ttt.mar.sms.entities.SmsPublisher;
import com.ttt.mar.sms.filter.SmsPublisherFilter;
import com.ttt.mar.sms.mapper.SmsPublisherMapper;
import com.ttt.mar.sms.repositories.SmsPublisherRepository;
import com.ttt.mar.sms.service.iface.SmsPublisherService;
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
public class SmsPublisherServiceImpl implements SmsPublisherService {

  @Autowired
  public SmsPublisherRepository smsPublisherRepository;

  @Autowired
  public SmsPublisherMapper smsPublisherMapper;

  @Autowired
  private UserRepository userRepository;

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
      .getLogger(SmsPublisherServiceImpl.class);

  @Override
  public Integer createSmsPublisher(SmsPublisherRequestDto smsPublisherRequestDto, Integer userId)
      throws OperationNotImplementException, IdentifyBlankException, DuplicateEntityException, ResourceNotFoundException {

    checkValidSmsPublisher(smsPublisherRequestDto);
    logger.info("smsPublisherRequestDto valid {}", smsPublisherRequestDto);

    User user = checkValidUser(userId);

    SmsPublisher smsPublisherByCode = smsPublisherRepository
        .findByCodeAndIsDeletedFalse(smsPublisherRequestDto.getCode());

    if (smsPublisherByCode != null) {
      throw new DuplicateEntityException("Code publisher exists",
          ServiceInfo.getId() + ServiceMessageCode.CODE_PUBLISHER_EXISTS);
    }

    SmsPublisher smsPublisher = smsPublisherMapper.fromDto(smsPublisherRequestDto);
    smsPublisher.setCreatorUser(user);
    SmsPublisher smsPublisherCreated = smsPublisherRepository.save(smsPublisher);

    return smsPublisherCreated.getId();
  }

  @Override
  public Boolean updateSmsPublisher(SmsPublisherRequestDto smsPublisherRequestDto, Integer userId)
      throws OperationNotImplementException, IdentifyBlankException, ResourceNotFoundException {

    if (smsPublisherRequestDto.getId() == null) {
      throw new IdentifyBlankException("Id null",
          ServiceInfo.getId() + ServiceMessageCode.ID_SMS_PUBLISHER_NULL);
    }

    checkValidSmsPublisher(smsPublisherRequestDto);
    User user = checkValidUser(userId);

    SmsPublisher smsPublisherById = smsPublisherRepository
        .findByIdAndIsDeletedFalse(smsPublisherRequestDto.getId());

    if (smsPublisherById == null) {
      throw new ResourceNotFoundException("publisher not found",
          ServiceInfo.getId() + ServiceMessageCode.RESOURCE_NOT_FOUND);
    }

    smsPublisherMapper.updateModel(smsPublisherById, smsPublisherRequestDto);
    smsPublisherById.setUpdaterUser(user);
    smsPublisherRepository.save(smsPublisherById);
    return true;
  }

  @Override
  public Boolean deleteSmsPublisher(Integer idPublisher, Integer userId)
      throws OperationNotImplementException, IdentifyBlankException, ResourceNotFoundException {
    if (idPublisher == null) {
      throw new IdentifyBlankException("Id null",
          ServiceInfo.getId() + ServiceMessageCode.ID_SMS_PUBLISHER_NULL);
    }
    User user = checkValidUser(userId);
    SmsPublisher smsPublisherById = smsPublisherRepository
        .findByIdAndIsDeletedFalse(idPublisher);
    if (smsPublisherById == null) {
      throw new ResourceNotFoundException("publisher not found",
          ServiceInfo.getId() + ServiceMessageCode.RESOURCE_NOT_FOUND);
    }
    smsPublisherById.setIsDeleted(Boolean.TRUE);
    smsPublisherById.setDeleterUser(user);
    smsPublisherRepository.save(smsPublisherById);
    return true;
  }

  @Override
  public DataPagingResponse<SmsPublisherResponseDto> getPublisherPaging(String search, Integer page,
      String sort,
      Integer limit) {

    Map<String, String> map = SortingUtils.detectSortType(sort);
    Page<SmsPublisher> smsPublisherPages = smsPublisherRepository
        .findAll(new SmsPublisherFilter().getByFilter(search, false, map),
            PageRequest.of(page - 1, limit));

    List<SmsPublisherResponseDto> smsPublisherResponseDtos = new ArrayList<>();

    smsPublisherPages.getContent().forEach(
        smsPublisher -> smsPublisherResponseDtos.add(smsPublisherMapper.toDto(smsPublisher)));

    logger.info("smPubliserResponseDto: {}", smsPublisherResponseDtos);
    DataPagingResponse<SmsPublisherResponseDto> dataPagingResponse = new DataPagingResponse();
    dataPagingResponse.setList(smsPublisherResponseDtos);
    dataPagingResponse.setTotalPage(smsPublisherPages.getTotalPages());
    dataPagingResponse.setNum(smsPublisherPages.getTotalElements());
    dataPagingResponse.setCurrentPage(page);
    return dataPagingResponse;
  }

  @Override
  public List<SmsPublisherCustomResponseDto> getAllPublisher() {
    List<SmsPublisher> smsPublishers = smsPublisherRepository.findAllByIsDeletedFalse();

    List<SmsPublisherCustomResponseDto> smsPublisherCustomResponseDtos = new ArrayList<>();

    smsPublishers.forEach(smsPublisher -> smsPublisherCustomResponseDtos
        .add(smsPublisherMapper.toSmsPublisherCustomResponseDto(smsPublisher)));
    return smsPublisherCustomResponseDtos;
  }

  public void checkValidSmsPublisher(SmsPublisherRequestDto smsPublisherRequestDto)
      throws OperationNotImplementException {

    if (smsPublisherRequestDto == null) {
      throw new OperationNotImplementException("Operation not implement",
          ServiceInfo.getId() + ServiceMessageCode.BODY_REQUEST_NULL);
    }
    if (smsPublisherRequestDto.getCode() == null || smsPublisherRequestDto.getCode().isEmpty()) {
      throw new OperationNotImplementException("Code null",
          ServiceInfo.getId() + ServiceMessageCode.OPERATION_NOT_IMPLEMENT);
    }
    if (smsPublisherRequestDto.getName() == null || smsPublisherRequestDto.getName().isEmpty()) {
      throw new OperationNotImplementException("Code null",
          ServiceInfo.getId() + ServiceMessageCode.OPERATION_NOT_IMPLEMENT);
    }
    if (smsPublisherRequestDto.getEndPoint() == null || smsPublisherRequestDto.getEndPoint()
        .isEmpty()) {
      throw new OperationNotImplementException("Endpoint null",
          ServiceInfo.getId() + ServiceMessageCode.OPERATION_NOT_IMPLEMENT);
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
}
