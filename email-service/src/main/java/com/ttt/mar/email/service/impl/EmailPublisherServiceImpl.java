package com.ttt.mar.email.service.impl;

import com.ttt.mar.email.config.ServiceMessageCode;
import com.ttt.mar.email.dto.emailpublisher.EmailPublisherCreateRequestDto;
import com.ttt.mar.email.dto.emailpublisher.EmailPublisherPublicResponseDto;
import com.ttt.mar.email.dto.emailpublisher.EmailPublisherResponseDto;
import com.ttt.mar.email.dto.emailpublisher.EmailPublisherUpdateRequestDto;
import com.ttt.mar.email.entities.EmailProtocol;
import com.ttt.mar.email.entities.EmailPublisher;
import com.ttt.mar.email.filter.EmailPublisherFilter;
import com.ttt.mar.email.mapper.EmailPublisherMapper;
import com.ttt.mar.email.repositories.EmailPublisherRepository;
import com.ttt.mar.email.service.iface.EmailPublisherService;
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
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class EmailPublisherServiceImpl implements EmailPublisherService {

  @Autowired
  private EmailPublisherMapper emailPublisherMapper;

  @Autowired
  private EmailPublisherRepository emailPublisherRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private EmailPublisherFilter emailPublisherFilter;

  public Integer createEmailPublisher(EmailPublisherCreateRequestDto request, Integer userId)
      throws IdentifyBlankException, ResourceNotFoundException, OperationNotImplementException {
    // Kiểm tra điều kiện truyền vào khi thêm mới Email-Publisher.
    validateEmailPublisherRequest(request);
    String code = request.getCode();
    if (StringUtils.isBlank(code)) {
      throw new IdentifyBlankException("Publisher-Code null",
          ServiceInfo.getId() + ServiceMessageCode.EMAIL_PUBLISHER_CODE_NULL);
    }
    // Kiểm tra Code đã tồn tại.
    Optional<EmailPublisher> emailPublisherOptional = emailPublisherRepository
        .findByCodeAndIsDeletedFalse(code);
    if (emailPublisherOptional.isPresent()) {
      throw new IdentifyBlankException("Publisher-Code is exist",
          ServiceInfo.getId() + ServiceMessageCode.EMAIL_PUBLISHER_CODE_EXIST);
    }
    // Mapping from Dto to Entity.
    EmailPublisher emailPublisher = emailPublisherMapper.fromDto(request);
    // Kiểm tra và Set Create-User.
    User user = checkValidUser(userId);
    emailPublisher.setCreatorUser(user);
    // Save Entity.
    emailPublisher = emailPublisherRepository.save(emailPublisher);
    return emailPublisher.getId();
  }

  public Integer updateEmailPublisher(EmailPublisherUpdateRequestDto request, Integer userId)
      throws IdentifyBlankException, ResourceNotFoundException, OperationNotImplementException {
    // Kiểm tra điều kiện truyền vào khi thêm mới Email-Publisher.
    validateEmailPublisherRequest(request);
    if (request.getId() == null) {
      throw new IdentifyBlankException("Publisher-Id null",
          ServiceInfo.getId() + ServiceMessageCode.EMAIL_PUBLISHER_ID_NULL);
    }
    Optional<EmailPublisher> emailPublisherOptional = emailPublisherRepository
        .findById(request.getId());
    if (emailPublisherOptional.isEmpty() || emailPublisherOptional.get().getIsDeleted()) {
      throw new ResourceNotFoundException("Email-Publisher doesn't exist",
          ServiceInfo.getId() + ServiceMessageCode.EMAIL_PUBLISHER_NOT_FOUND);
    }
    EmailPublisher emailPublisher = emailPublisherOptional.get();
    // Mapping Update-dto to Entity.
    emailPublisherMapper.updateFromDto(request, emailPublisher);
    // Set User-Update.
    User user = checkValidUser(userId);
    emailPublisher.setUpdaterUser(user);
    emailPublisherRepository.save(emailPublisher);
    return emailPublisher.getId();
  }

  public DataPagingResponse<EmailPublisherResponseDto> getEmailPublisherPaging(String search,
      String sort, Integer page, Integer limit) {
    Map<String, String> map = SortingUtils.detectSortType(sort);
    if (map.isEmpty()) {
      // Mặc định theo ngày tạo giảm dần.
      map.put("createdTime", "desc");
    }
    // Filter theo điều kiện tìm kiếm.
    Page<EmailPublisher> emailPublisherPage = emailPublisherRepository
        .findAll(emailPublisherFilter.filter(search, map), PageRequest.of(page - 1, limit));
    List<EmailPublisher> emailPublishers = emailPublisherPage.getContent();
    // Mapping Entity => Dto.
    List<EmailPublisherResponseDto> emailPublisherResponseDtos = emailPublishers.stream()
        .map(entity -> emailPublisherMapper.toDto(entity)).collect(Collectors.toList());
    DataPagingResponse<EmailPublisherResponseDto> dataPagingResponses = new DataPagingResponse<>();
    dataPagingResponses.setList(emailPublisherResponseDtos);
    dataPagingResponses.setTotalPage(emailPublisherPage.getTotalPages());
    dataPagingResponses.setNum(emailPublisherPage.getTotalElements());
    dataPagingResponses.setCurrentPage(page);
    return dataPagingResponses;
  }

  public Boolean deleteEmailPublisher(Integer id, Integer userId)
      throws IdentifyBlankException, ResourceNotFoundException, OperationNotImplementException {
    if (id == null) {
      throw new IdentifyBlankException("Publisher-Id null",
          ServiceInfo.getId() + ServiceMessageCode.EMAIL_PUBLISHER_ID_NULL);
    }
    // Kiểm tra Publisher tồn tại.
    Optional<EmailPublisher> emailPublisherOptional = emailPublisherRepository
        .findById(id);
    if (emailPublisherOptional.isEmpty() || emailPublisherOptional.get().getIsDeleted()) {
      throw new ResourceNotFoundException("Email-Publisher doesn't exist",
          ServiceInfo.getId() + ServiceMessageCode.EMAIL_PUBLISHER_NOT_FOUND);
    }
    // Kiểm tra User.
    User user = checkValidUser(userId);
    // Xóa Publisher.
    EmailPublisher emailPublisher = emailPublisherOptional.get();
    emailPublisher.setIsDeleted(true);
    emailPublisher.setDeleterUser(user);
    emailPublisherRepository.save(emailPublisher);
    return true;
  }

  public List<EmailPublisherPublicResponseDto> getListPublishers() {
    List<EmailPublisherPublicResponseDto> emailPublisherPublicResponseDtos = new ArrayList<>();
    List<EmailPublisher> emailPublishers = emailPublisherRepository.findAllByIsDeletedFalse();
    if (emailPublishers == null || emailPublishers.isEmpty()) {
      return emailPublisherPublicResponseDtos;
    }
    emailPublisherPublicResponseDtos = emailPublishers.stream()
        .map(entity -> emailPublisherMapper.toDtoPublic(entity)).collect(Collectors.toList());
    return emailPublisherPublicResponseDtos;
  }

  public <T extends EmailPublisherCreateRequestDto> void validateEmailPublisherRequest(T request)
      throws IdentifyBlankException {
    if (StringUtils.isBlank(request.getName())) {
      throw new IdentifyBlankException("Publisher-Name null",
          ServiceInfo.getId() + ServiceMessageCode.EMAIL_PUBLISHER_NAME_NULL);
    }
    EmailProtocol protocol = request.getProtocol();
    if (protocol == null) {
      throw new IdentifyBlankException("Protocol null",
          ServiceInfo.getId() + ServiceMessageCode.EMAIL_PUBLISHER_PROTOCOL_NULL);
    }
    if (EmailProtocol.API.equals(protocol) && StringUtils.isBlank(request.getEndPoint())) {
      throw new IdentifyBlankException("End-Point null",
          ServiceInfo.getId() + ServiceMessageCode.EMAIL_PUBLISHER_END_POINT_NULL);
    }
    if (!EmailProtocol.API.equals(protocol)) {
      if (StringUtils.isBlank(request.getHost())) {
        throw new IdentifyBlankException("Host null",
            ServiceInfo.getId() + ServiceMessageCode.EMAIL_PUBLISHER_HOST_NULL);
      }
      if (request.getPort() == null) {
        throw new IdentifyBlankException("Port null",
            ServiceInfo.getId() + ServiceMessageCode.EMAIL_PUBLISHER_PORT_NULL);
      }
    }
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
}
