package com.ttt.mar.email.service.impl;

import com.ttt.mar.email.config.PermissionObjectCode;
import com.ttt.mar.email.config.ServiceMessageCode;
import com.ttt.mar.email.dto.emailconfig.EmailConfigCreateRequestDto;
import com.ttt.mar.email.dto.emailconfig.EmailConfigDetailResponseDto;
import com.ttt.mar.email.dto.emailconfig.EmailConfigResponseDto;
import com.ttt.mar.email.dto.emailconfig.EmailConfigUpdateRequestDto;
import com.ttt.mar.email.entities.EmailConfig;
import com.ttt.mar.email.entities.EmailProtocol;
import com.ttt.mar.email.entities.EmailPublisher;
import com.ttt.mar.email.filter.EmailConfigFilter;
import com.ttt.mar.email.mapper.EmailConfigMapper;
import com.ttt.mar.email.repositories.EmailConfigRepository;
import com.ttt.mar.email.repositories.EmailPublisherRepository;
import com.ttt.mar.email.service.iface.EmailConfigService;
import com.ttt.rnd.common.config.ServiceInfo;
import com.ttt.rnd.common.dto.DataPagingResponse;
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
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class EmailConfigServiceImpl implements EmailConfigService {

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
      .getLogger(EmailConfigServiceImpl.class);

  @Autowired
  private EmailConfigRepository emailConfigRepository;

  @Autowired
  private EmailConfigMapper emailConfigMapper;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private EmailPublisherRepository emailPublisherRepository;

  @Autowired
  private EmailConfigFilter emailConfigFilter;

  @Override
  public Integer createEmailConfigService(EmailConfigCreateRequestDto request, Integer userId)
      throws IdentifyBlankException, ResourceNotFoundException, OperationNotImplementException {
    String accountCode = request.getAccountCode();
    if (StringUtils.isBlank(accountCode)) {
      throw new IdentifyBlankException("Account-Code null",
          ServiceInfo.getId() + ServiceMessageCode.EMAIL_CONFIG_ACCOUNT_CODE_NULL);
    }
    // Kiểm tra Acoount-Code tồn tại.
    EmailConfig emailConfig = emailConfigRepository.findByAccountCodeAndIsDeletedFalse(accountCode);
    if (emailConfig != null) {
      throw new IdentifyBlankException("Account-Code already exist",
          ServiceInfo.getId() + ServiceMessageCode.EMAIL_CONFIG_ACCOUNT_CODE_EXIST);
    }
    Integer publisherId = request.getPublisherId();
    EmailPublisher emailPublisher = emailPublisherRepository.findByIdAndIsDeletedFalse(publisherId);
    if (emailPublisher == null) {
      throw new ResourceNotFoundException("Email-Publisher doesn't exist",
          ServiceInfo.getId() + ServiceMessageCode.EMAIL_PUBLISHER_NOT_FOUND);
    }
    // Mapping Dto => Entity.
    EmailConfig emailConfigForCreate = emailConfigMapper.fromDto(request);
    if (EmailProtocol.API.equals(emailPublisher.getProtocol())) {
      // Nếu Email-Publisher có protocol là API => Gán token.
      String token = request.getToken();
      if (StringUtils.isNotBlank(token)) {
        emailConfigForCreate.setToken(token);
      }
    }
    // Gán User.
    emailConfigForCreate.setEmailPublisher(emailPublisher);
    User user = checkValidUser(userId);
    emailConfigForCreate.setCreatorUser(user);

    return emailConfigRepository.save(emailConfigForCreate).getId();
  }

  @Override
   public DataPagingResponse<EmailConfigResponseDto> getEmailConfigs(Integer publisherId, String search,
      String sort, Integer page, Integer limit) {

    Map<String, String> map = SortingUtils.detectSortType(sort);
    if (map.isEmpty()) {
      map.put("createdTime", "desc");
    }
    Page<EmailConfig> emailConfigPage = emailConfigRepository
        .findAll(emailConfigFilter.getByFilter(publisherId, search, map),
            PageRequest.of(page, limit));

    List<EmailConfigResponseDto> emailConfigResponseDtos = new ArrayList<>();
    emailConfigPage.getContent().forEach(emailConfig -> emailConfigResponseDtos
        .add(emailConfigMapper.toEmailConfigResponse(emailConfig)));
    DataPagingResponse<EmailConfigResponseDto> dataPagingResponses = new DataPagingResponse<>();
    dataPagingResponses.setList(emailConfigResponseDtos);
    dataPagingResponses.setTotalPage(emailConfigPage.getTotalPages());
    dataPagingResponses.setNum(emailConfigPage.getTotalElements());
    dataPagingResponses.setCurrentPage(page + 1);

    return dataPagingResponses;
  }

  @Override
  public Integer updateEmailConfig(EmailConfigUpdateRequestDto request, Integer userId)
      throws ResourceNotFoundException, OperationNotImplementException {
    EmailConfig emailConfig = emailConfigRepository.findByIdAndIsDeletedFalse(request.getId());
    if (emailConfig == null) {
      throw new ResourceNotFoundException("Email-Config doesn't exist",
          ServiceInfo.getId() + ServiceMessageCode.EMAIL_CONFIG_NOT_FOUND);
    }
    Integer publisherId = request.getPublisherId();
    EmailPublisher emailPublisher = emailPublisherRepository.findByIdAndIsDeletedFalse(publisherId);
    if (emailPublisher == null) {
      throw new ResourceNotFoundException("Email-Publisher doesn't exist",
          ServiceInfo.getId() + ServiceMessageCode.EMAIL_PUBLISHER_NOT_FOUND);
    }
    // Mapping Dto => Entity.
    emailConfigMapper.updateModel(emailConfig, request);
    if (!EmailProtocol.API.equals(emailPublisher.getProtocol())) {
      emailConfig.setToken(null);
    }
    // Gán User.
    User user = checkValidUser(userId);
    emailConfig.setEmailPublisher(emailPublisher);
    emailConfig.setUpdaterUser(user);
    emailConfigRepository.save(emailConfig);

    return request.getId();
  }

  @Override
  public Boolean deleteEmailConfigById(Integer id, Integer userId)
      throws ResourceNotFoundException, IdentifyBlankException, OperationNotImplementException {
    if (id == null) {
      throw new IdentifyBlankException("Id sms config null",
          ServiceInfo.getId() + ServiceMessageCode.ID_EMAIL_CONFIG_NULL);
    }

    checkValidUser(userId);
    logger.info("Check User success");
    EmailConfig emailConfigById = emailConfigRepository.findByIdAndIsDeletedFalse(id);

    if (emailConfigById == null) {
      throw new ResourceNotFoundException("Email-Config not found",
          ServiceInfo.getId() + ServiceMessageCode.EMAIL_CONFIG_NOT_FOUND);
    }
    logger.info("Email-Publisher is Exist.");

    EmailPublisher emailPublisher = emailConfigById.getEmailPublisher();
    if (emailPublisher == null || emailPublisher.getIsDeleted()) {
      throw new ResourceNotFoundException("Email-Publisher doesn't exist",
          ServiceInfo.getId() + ServiceMessageCode.EMAIL_PUBLISHER_NOT_FOUND);
    }
    logger.info("Email-Publisher: " + emailPublisher.getId());

    emailConfigById.setIsDeleted(Boolean.TRUE);
    emailConfigById.setUpdaterUserId(userId);
    emailConfigRepository.save(emailConfigById);

    return true;
  }

  public EmailConfigDetailResponseDto getEmailConfigById(Integer id)
      throws ResourceNotFoundException {
    logger.info("getEmailConfigById");
    EmailConfig emailConfig = emailConfigRepository.findByIdAndIsDeletedFalse(id);
    if (emailConfig == null) {
      throw new ResourceNotFoundException("Email-Config doesn't exist",
          ServiceInfo.getId() + ServiceMessageCode.EMAIL_CONFIG_NOT_FOUND);
    }
    logger.info("Email-Config is Exist.");
    EmailPublisher emailPublisher = emailConfig.getEmailPublisher();
    if (emailPublisher == null || emailPublisher.getIsDeleted()) {
      throw new ResourceNotFoundException("Email-Publisher doesn't exist",
          ServiceInfo.getId() + ServiceMessageCode.EMAIL_PUBLISHER_NOT_FOUND);
    }
    logger.info("Email-Publisher is Exist.");
    EmailConfigDetailResponseDto responseDto = emailConfigMapper.toDto(emailConfig);
    logger.info("EmailConfigDetailResponseDto: [{}]", responseDto);
    return responseDto;
  }

  public User checkValidUser(Integer userId)
      throws ResourceNotFoundException, OperationNotImplementException {
    Optional<User> userOptionalById = userRepository.findById(userId);
    if (userOptionalById.isEmpty() || userOptionalById.get().getIsDeleted()) {
      throw new ResourceNotFoundException("Resource not found" + userId,
          ServiceInfo.getId() + ServiceMessageCode.RESOURCE_NOT_FOUND);
    }
    if (userOptionalById.get().getStatus().equals(UserStatus.DEACTIVE)) {
      throw new OperationNotImplementException("User deactive",
          ServiceInfo.getId() + ServiceMessageCode.USER_DEACTIVE);
    }
    return userOptionalById.get();
  }

  public void checkAndSetUser(EmailConfig emailConfig, Integer userId)
      throws ResourceNotFoundException, OperationNotImplementException {
    User marUser = checkValidUser(userId);
    emailConfig.setCreatorUser(marUser);
  }
}
