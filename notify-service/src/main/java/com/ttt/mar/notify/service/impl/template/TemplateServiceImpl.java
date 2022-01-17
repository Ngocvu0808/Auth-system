package com.ttt.mar.notify.service.impl.template;

import com.ttt.mar.notify.utils.Utils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.ttt.mar.notify.config.Constants;
import com.ttt.mar.notify.config.ServiceMessageCode;
import com.ttt.mar.notify.dto.template.TemplateAttachmentRequestDto;
import com.ttt.mar.notify.dto.template.TemplateAttachmentResponse;
import com.ttt.mar.notify.dto.template.TemplateRequestDto;
import com.ttt.mar.notify.dto.template.TemplateResponseDetailDto;
import com.ttt.mar.notify.dto.template.TemplateResponseDto;
import com.ttt.mar.notify.dto.template.TemplateUpdateRequestDto;
import com.ttt.mar.notify.dto.template.UserResponseDto;
import com.ttt.mar.notify.entities.template.Template;
import com.ttt.mar.notify.entities.template.TemplateAttachment;
import com.ttt.mar.notify.entities.template.TemplateSemantic;
import com.ttt.mar.notify.entities.template.TemplateSemanticDetail;
import com.ttt.mar.notify.entities.template.TemplateSemanticType;
import com.ttt.mar.notify.entities.template.TemplateType;
import com.ttt.mar.notify.filter.TemplateFilter;
import com.ttt.mar.notify.mapper.template.TemplateAttachmentMapper;
import com.ttt.mar.notify.mapper.template.TemplateMapper;
import com.ttt.mar.notify.mapper.template.TemplateSemanticMapper;
import com.ttt.mar.notify.repositories.template.TemplateAttachmentRepository;
import com.ttt.mar.notify.repositories.template.TemplateRepository;
import com.ttt.mar.notify.repositories.template.TemplateSemanticDetailRepository;
import com.ttt.mar.notify.repositories.template.TemplateSemanticRepository;
import com.ttt.mar.notify.repositories.template.TemplateTypeRepository;
import com.ttt.mar.notify.service.iface.UserService;
import com.ttt.mar.notify.service.iface.template.TemplateService;
import com.ttt.rnd.common.config.ServiceInfo;
import com.ttt.rnd.common.dto.DataPagingResponse;
import com.ttt.rnd.common.exception.IdentifyBlankException;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import com.ttt.rnd.common.utils.SortingUtils;
import com.ttt.rnd.lib.entities.User;

@Service
public class TemplateServiceImpl implements TemplateService {

  @Value("${template.attachment.max-file: 5}")
  private Integer MAX_FILE_TEMPLATE;

  private static final Logger logger = LoggerFactory.getLogger(VariablesServiceImpl.class);

  private final UserService userService;
  private final TemplateRepository templateRepository;
  private final TemplateSemanticRepository templateSemanticRepository;
  private final TemplateAttachmentRepository templateAttachmentRepository;
  private final TemplateSemanticDetailRepository templateSemanticDetailRepository;
  private final TemplateTypeRepository templateTypeRepository;
  private final TemplateMapper templateMapper;
  private final TemplateAttachmentMapper templateAttachmentMapper;
  private final TemplateFilter templateFilter;
  private final TemplateSemanticMapper templateSemanticMapper;

  public TemplateServiceImpl(UserService userService, TemplateRepository templateRepository,
      TemplateSemanticRepository templateSemanticRepository,
      TemplateAttachmentRepository templateAttachmentRepository,
      TemplateSemanticDetailRepository templateSemanticDetailRepository,
      TemplateTypeRepository templateTypeRepository, TemplateMapper templateMapper,
      TemplateAttachmentMapper templateAttachmentMapper, TemplateFilter templateFilter,
      TemplateSemanticMapper templateSemanticMapper) {
    this.userService = userService;
    this.templateRepository = templateRepository;
    this.templateSemanticRepository = templateSemanticRepository;
    this.templateAttachmentRepository = templateAttachmentRepository;
    this.templateSemanticDetailRepository = templateSemanticDetailRepository;
    this.templateTypeRepository = templateTypeRepository;
    this.templateMapper = templateMapper;
    this.templateAttachmentMapper = templateAttachmentMapper;
    this.templateFilter = templateFilter;
    this.templateSemanticMapper = templateSemanticMapper;
  }

  /**
   * apiNote Api them moi template
   *
   * @param userId ID of User
   * @param dto    RequestBody khoi tao Template
   * @return
   * @throws OperationNotImplementException
   * @throws ResourceNotFoundException
   */
  @Override
  public Integer createTemplate(Integer userId, TemplateRequestDto dto)
      throws OperationNotImplementException, ResourceNotFoundException, IdentifyBlankException {
    // step1: kiểm tra user có tồn tại
    userService.checkValidUser(userId);
    Template templateByCode = templateRepository.findByCodeAndIsDeletedFalse(dto.getCode());
    if (templateByCode != null) {
      throw new OperationNotImplementException("Template Code is exist.",
          ServiceInfo.getId() + ServiceMessageCode.TEMPLATE_CODE_EXIST);
    }
    // step2: kiểm tra TemplateType có tồn tại
    getTemplateByCode(dto.getType());
    validateTemplateRequestDto(dto);
    Template template = templateMapper.fromDto(dto);
    // step3: Validate thông tin cho Template
    List<TemplateAttachment> templateAttachments = validateTemplateAttachment(userId, template,
        dto.getId(),
        dto.getAttachmentRequestDtos());
    List<TemplateSemanticDetail> templateSemanticDetails = getTemplateSemanticDetail(userId,
        template, dto);

    // step4: lưu thông tin
    template.setCreatorUserId(userId);
    templateRepository.save(template);
    if (!templateAttachments.isEmpty()) {
      templateAttachmentRepository.saveAll(templateAttachments);
    }
    if (!templateSemanticDetails.isEmpty()) {
      templateSemanticDetailRepository.saveAll(templateSemanticDetails);
    }

    return template.getId();
  }

  private void validateTemplateRequestDto(TemplateRequestDto dto)
      throws OperationNotImplementException {
    if (Constants.EMAIL.equals(dto.getType())) {
      String to = dto.getTo();
      if (StringUtils.isNotBlank(to)) {
        if (to.length() > 5000) {
          throw new OperationNotImplementException("To longer than 5000 characters.",
              ServiceInfo.getId() + ServiceMessageCode.TO_INVALID);
        }
        Utils.validateEmails(to);
      }
      String cc = dto.getCc();
      if (StringUtils.isNotBlank(cc)) {
        Utils.validateEmails(cc);
      }
      String bcc = dto.getBcc();
      if (StringUtils.isNotBlank(bcc)) {
        Utils.validateEmails(bcc);
      }
    }
    if (Constants.SMS.equals(dto.getType())) {
      String to = dto.getTo();
      if (StringUtils.isNotBlank(to)) {
        if (to.length() > 255) {
          throw new OperationNotImplementException("To longer than 255 characters.",
              ServiceInfo.getId() + ServiceMessageCode.TO_INVALID);
        }
        Utils.validatePhones(to);
      }
    }
  }

  private List<TemplateSemanticDetail> getTemplateSemanticDetail(Integer userId, Template template,
      TemplateRequestDto dto) throws ResourceNotFoundException {
    List<TemplateSemanticDetail> templateSemanticDetails = new ArrayList<>();
    if (Constants.EMAIL.equals(template.getType())) {
      if (dto.getHeaderId() != null) {
        List<TemplateSemanticDetail> headerDetails = validateTemplateSemantic(userId, template,
            dto.getHeaderId(), TemplateSemanticType.HEADER);
        if (!headerDetails.isEmpty()) {
          templateSemanticDetails.addAll(headerDetails);
        }
      }
      if (dto.getFooterId() != null) {
        List<TemplateSemanticDetail> footerDetails = validateTemplateSemantic(userId, template,
            dto.getFooterId(), TemplateSemanticType.FOOTER);
        if (!footerDetails.isEmpty()) {
          templateSemanticDetails.addAll(footerDetails);
        }
      }
    }

    return templateSemanticDetails;
  }

  private List<TemplateAttachment> validateTemplateAttachment(Integer userId, Template template,
      Integer templateId,
      List<TemplateAttachmentRequestDto> attachmentRequestDtos)
      throws OperationNotImplementException, ResourceNotFoundException {
    List<TemplateAttachment> templateAttachments = new ArrayList<>();
    if (Constants.SMS.equals(template.getType())) {
      return templateAttachments;
    }
    if (attachmentRequestDtos.size() > MAX_FILE_TEMPLATE) {
      throw new OperationNotImplementException("Attachment out of limit.",
          ServiceInfo.getId() + ServiceMessageCode.TEMPLATE_ATTACHMENT_LIMIT_INVALID);
    }

    Set<Integer> templateAttachmentIds = attachmentRequestDtos.stream()
        .map(TemplateAttachmentRequestDto::getId)
        .filter(Objects::nonNull).collect(Collectors.toSet());
    if (templateId == null && !templateAttachmentIds.isEmpty()) {
      throw new OperationNotImplementException("Add Attachment for Template Invalid.",
          ServiceInfo.getId() + ServiceMessageCode.ADD_TEMPLATE_ATTACHMENT_INVALID);
    }
    for (TemplateAttachmentRequestDto dto : attachmentRequestDtos) {
      TemplateAttachment templateAttachment;
      if (dto.getId() != null) {
        templateAttachment = templateAttachmentRepository
            .findByIdAndTemplateIdAndIsDeletedFalse(dto.getId(),
                templateId);
        if (templateAttachment == null) {
          throw new ResourceNotFoundException("TemplateAttachment not found",
              ServiceInfo.getId() + ServiceMessageCode.TEMPLATE_ATTACHMENT_NOT_FOUND);
        }
      } else {
        templateAttachment = templateAttachmentMapper.fromDto(dto);
        templateAttachment.setTemplate(template);
        templateAttachment.setCreatorUserId(userId);
      }
      templateAttachments.add(templateAttachment);
    }

    List<TemplateAttachment> templateAttachmentDeletes = new ArrayList<>();
    if (!templateAttachmentIds.isEmpty()) {
      templateAttachmentDeletes = templateAttachmentRepository
          .findByIdIsNotInAndTemplateIdAndIsDeletedFalse(templateAttachmentIds, templateId);
    } else if (templateId != null) {
      templateAttachmentDeletes = templateAttachmentRepository
          .findByTemplateIdAndIsDeletedFalse(templateId);
    }
    if (!templateAttachmentDeletes.isEmpty()) {
      for (TemplateAttachment templateAttachment : templateAttachmentDeletes) {
        templateAttachment.setDeleterUserId(userId);
        templateAttachment.setIsDeleted(true);
        templateAttachments.add(templateAttachment);
      }
    }

    return templateAttachments;
  }

  private List<TemplateSemanticDetail> validateTemplateSemantic(Integer userId, Template template,
      Integer semanticId,
      TemplateSemanticType type) throws ResourceNotFoundException {
    logger.info(" --- validateTemplateSemantic() --- ");
    List<TemplateSemanticDetail> templateSemanticDetails = new ArrayList<>();
    Integer templateId = template.getId();
    if (semanticId != null) {
      TemplateSemantic header = templateSemanticRepository
          .findByIdAndTypeAndIsDeletedFalse(semanticId, type);
      if (header == null) {
        throw new ResourceNotFoundException("TemplateSemantic not found",
            ServiceInfo.getId() + ServiceMessageCode.TEMPLATE_SEMANTIC_NOT_FOUND);
      }
      TemplateSemanticDetail templateSemanticDetail = new TemplateSemanticDetail();
      if (templateId != null) {
        List<TemplateSemanticDetail> headerDetails = templateSemanticDetailRepository
            .getTemplateSemanticDetailByTemplateId(type, templateId);
        if (headerDetails == null || headerDetails.isEmpty()) {
          templateSemanticDetail.setCreatorUserId(userId);
          templateSemanticDetail.setTemplate(template);
          templateSemanticDetail.setTemplateSemantic(header);

          templateSemanticDetails.add(templateSemanticDetail);
        } else if (!semanticId.equals(headerDetails.get(0).getTemplateSemanticId())) {
          headerDetails.get(0).setIsDeleted(true);
          headerDetails.get(0).setDeleterUserId(userId);
          templateSemanticDetails.add(headerDetails.get(0));

          templateSemanticDetail.setCreatorUserId(userId);
          templateSemanticDetail.setTemplate(template);
          templateSemanticDetail.setTemplateSemantic(header);

          templateSemanticDetails.add(templateSemanticDetail);
        }
      } else {
        templateSemanticDetail.setCreatorUserId(userId);
        templateSemanticDetail.setTemplate(template);
        templateSemanticDetail.setTemplateSemantic(header);

        templateSemanticDetails.add(templateSemanticDetail);
      }
    }

    return templateSemanticDetails;
  }

  private TemplateType getTemplateByCode(String code)
      throws ResourceNotFoundException, IdentifyBlankException {
    if (StringUtils.isBlank(code)) {
      throw new IdentifyBlankException("TemplateType not Blank",
          ServiceInfo.getId() + ServiceMessageCode.TEMPLATE_TYPE_NOT_BLANK);
    }
    TemplateType templateType = templateTypeRepository.findByCode(code);
    if (templateType == null) {
      throw new ResourceNotFoundException("TemplateType not found",
          ServiceInfo.getId() + ServiceMessageCode.TEMPLATE_TYPE_NOT_FOUND);
    }

    return templateType;
  }

  /**
   * API get all template
   *
   * @param search is text search
   * @param sort   is type of sort
   * @param page   is page
   * @param limit  is limit
   * @param type   is type of template
   * @return data paging response
   */
  @Override
  public DataPagingResponse<TemplateResponseDto> getTemplates(String search, Long fromDate,
      Long toDate, String type,
      String sort, Integer page, Integer limit)
      throws ResourceNotFoundException, IdentifyBlankException {

    logger.info("---getTemplates()---");
    DataPagingResponse<TemplateResponseDto> dataPagingResponses = new DataPagingResponse<>();
    Map<String, String> map = SortingUtils.detectSortType(sort);
    if (map.isEmpty()) {
      map.put("createdTime", "desc");
    }
    this.getTemplateByCode(type);
    Page<Template> templatePage = templateRepository.findAll(
        templateFilter.filterTemplate(fromDate, toDate, type, search, map),
        PageRequest.of(page - 1, limit));
    List<TemplateResponseDto> listTemplateResponse = mappingToTemplateReponseDTO(
        templatePage.getContent());
    dataPagingResponses.setList(listTemplateResponse);
    dataPagingResponses.setTotalPage(templatePage.getTotalPages());
    dataPagingResponses.setNum(templatePage.getTotalElements());
    dataPagingResponses.setCurrentPage(page);
    return dataPagingResponses;
  }

  public List<TemplateResponseDto> mappingToTemplateReponseDTO(List<Template> listTemplate) {
    logger.info("--- mappingToTemplateReponseDTO() ---");
    List<TemplateResponseDto> dataTemplateResponse = new ArrayList<>();
    if (listTemplate == null || listTemplate.isEmpty()) {
      return dataTemplateResponse;
    }
    Set<Integer> userIdSet = listTemplate.stream().map(Template::getCreatorUserId)
        .filter(Objects::nonNull)
        .collect(Collectors.toSet());

    Map<Integer, UserResponseDto> userResponseDtoMap = new HashMap<>();
    if (!userIdSet.isEmpty()) {
      userResponseDtoMap = userService.mapByListId(userIdSet);
    }
    for (Template template : listTemplate) {
      TemplateResponseDto oneTemplateResponse = templateMapper.toDto(template);
      // get User info
      Integer creatorId = template.getCreatorUserId();
      if (creatorId != null && userResponseDtoMap.containsKey(creatorId)) {
        UserResponseDto userResponseDTO = userResponseDtoMap.get(creatorId);
        oneTemplateResponse.setUserName(userResponseDTO.getUsername());
      }
      dataTemplateResponse.add(oneTemplateResponse);
    }
    return dataTemplateResponse;
  }


  /**
   * API get template detail
   *
   * @param id is id of template
   * @return template response detail
   */
  @Override
  public TemplateResponseDetailDto getTemplateById(Integer id) throws ResourceNotFoundException {
    logger.info("--- getTemplateById() ---");
    Template template = getTemplate(id);
    TemplateResponseDetailDto oneTemplateResponseDetail = new TemplateResponseDetailDto();
    oneTemplateResponseDetail = templateMapper.toResponseDetail(template);
    //get user detail 
    Integer creatorId = template.getCreatorUserId();
    if (creatorId != null) {
      Map<Integer, UserResponseDto> userResponseDtoMap = userService
          .mapByListId(Collections.singleton(creatorId));
      UserResponseDto userResponseDTO = userResponseDtoMap.get(creatorId);
      oneTemplateResponseDetail.setUserName(userResponseDTO.getUsername());
    }
    //get attachment detail
    List<TemplateAttachment> listTemplateAttachment = templateAttachmentRepository
        .findByTemplateIdAndIsDeletedFalse(template.getId());
    List<TemplateAttachmentResponse> listAttachmentResponse = new ArrayList<>();
    if (!listTemplateAttachment.isEmpty()) {
      listAttachmentResponse = listTemplateAttachment.stream().map(templateAttachmentMapper::toDto)
          .collect(Collectors.toList());
    }
    logger.info("listAttachmentResponse size: " + listAttachmentResponse.size());
    oneTemplateResponseDetail.setAttachmentResponses(listAttachmentResponse);
    // get template semantic detail
    List<TemplateSemanticDetail> templateSematicDetail = templateSemanticDetailRepository
        .getTemplateSemanticDetailByTemplateId(template.getId());
    logger.info(" templateSematicDetail size: " + templateSematicDetail.size());
    if (!templateSematicDetail.isEmpty()) {
      Set<Integer> idSemantic = templateSematicDetail.stream().map(
          TemplateSemanticDetail::getTemplateSemanticId)
          .collect(Collectors.toSet());
      List<TemplateSemantic> templateSematicFooter = templateSemanticRepository
          .findByListIdAndTypeAndIsDeletedFalse(
              idSemantic, TemplateSemanticType.FOOTER);
      List<TemplateSemantic> templateSematicHeader = templateSemanticRepository
          .findByListIdAndTypeAndIsDeletedFalse(idSemantic, TemplateSemanticType.HEADER);
      if (templateSematicFooter != null && !templateSematicFooter.isEmpty()) {
        logger.info("templateSematicFooter list size: " + templateSematicFooter.size());
        oneTemplateResponseDetail
            .setFooter(templateSemanticMapper.toDto(templateSematicFooter.get(0)));
      }
      if (templateSematicHeader != null && !templateSematicHeader.isEmpty()) {
        logger.info("templateSematicHeader list size: " + templateSematicHeader.size());
        oneTemplateResponseDetail
            .setHeader(templateSemanticMapper.toDto(templateSematicHeader.get(0)));
      }
    }

    return oneTemplateResponseDetail;
  }

  /**
   * apiNote API cập nhật template
   *
   * @param userId ID of User
   * @param dto    RequestBody khởi tạo template
   * @return
   * @throws OperationNotImplementException
   * @throws ResourceNotFoundException
   */
  @Transactional
  @Override
  public Integer updateTemplate(Integer userId, TemplateUpdateRequestDto dto)
      throws OperationNotImplementException, ResourceNotFoundException {
    // step1: kiểm tra user có tồn tại
    userService.checkValidUser(userId);
    Template template = getTemplate(dto.getId());
    validateTemplateRequestDto(dto);
    BeanUtils.copyProperties(dto, template, "type", "code");
    if (Constants.SMS.equals(template.getType())) {
      template.setCc(null);
      template.setBcc(null);
      template.setSubject(null);
    }
    // step3: Validate thông tin cho Template
    List<TemplateAttachment> templateAttachments = validateTemplateAttachment(userId, template,
        dto.getId(),
        dto.getAttachmentRequestDtos());
    List<TemplateSemanticDetail> templateSemanticDetails = getTemplateSemanticDetail(userId,
        template, dto);

    // step4: lưu thông tin
    template.setUpdaterUserId(userId);
    templateRepository.save(template);
    if (!templateAttachments.isEmpty()) {
      templateAttachmentRepository.saveAll(templateAttachments);
    }
    if (!templateSemanticDetails.isEmpty()) {
      templateSemanticDetailRepository.saveAll(templateSemanticDetails);
    }

    return template.getId();
  }

  private Template getTemplate(Integer id) throws ResourceNotFoundException {
    Optional<Template> optionalTemplate = templateRepository.findById(id);
    if (optionalTemplate.isEmpty()) {
      throw new ResourceNotFoundException("Template not found",
          ServiceInfo.getId() + ServiceMessageCode.TEMPLATE_NOT_FOUND);
    }
    Template template = optionalTemplate.get();
    if (template.getIsDeleted()) {
      throw new ResourceNotFoundException("Template not found",
          ServiceInfo.getId() + ServiceMessageCode.TEMPLATE_NOT_FOUND);
    }

    return template;
  }

  /**
   * apiNote API delete template
   *
   * @param userId ID of User
   * @param tempId Id of template
   * @return
   * @throws OperationNotImplementException
   * @throws ResourceNotFoundException
   */
  @Transactional
  @Override
  public boolean deleteTemplate(Integer userId, Integer tempId)
      throws OperationNotImplementException, ResourceNotFoundException {
    userService.checkValidUser(userId);
    //check template exist
    Template template = getTemplate(tempId);

    template.setIsDeleted(true);
    template.setDeleterUserId(userId);
    templateRepository.save(template);
    // check template attachment exist
    List<TemplateAttachment> templateAttachment = templateAttachmentRepository
        .findByTemplateIdAndIsDeletedFalse(tempId);
    if (!templateAttachment.isEmpty()) {
      templateAttachment.forEach(f -> f.setIsDeleted(true));
      templateAttachment.forEach(f -> f.setDeleterUserId(userId));
      templateAttachmentRepository.saveAll(templateAttachment);
    }
    //check template semantic detail exist
    List<TemplateSemanticDetail> templateSemantic = templateSemanticDetailRepository
        .getTemplateSemanticDetailByTemplateId(tempId);
    if (!templateSemantic.isEmpty()) {
      templateSemantic.forEach(f -> f.setIsDeleted(true));
      templateSemantic.forEach(f -> f.setDeleterUserId(userId));
      templateSemanticDetailRepository.saveAll(templateSemantic);
    }
    return true;
  }
}
