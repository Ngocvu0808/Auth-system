package com.ttt.mar.notify.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.ttt.mar.notify.config.Constants;
import com.ttt.mar.notify.config.NotifyMessageCode;
import com.ttt.mar.notify.config.ServiceMessageCode;
import com.ttt.mar.notify.controller.NotifyHistoryController;
import com.ttt.mar.notify.dto.notify.DataRequestNotifyResponse;
import com.ttt.mar.notify.dto.notify.NotifyHistoryDetailResponseDto;
import com.ttt.mar.notify.dto.notify.NotifyHistoryResponseDto;
import com.ttt.mar.notify.dto.notify.PayLoadSendToSmsService;
import com.ttt.mar.notify.dto.notify.email.ContentEmail;
import com.ttt.mar.notify.dto.notify.email.EmailAttachment;
import com.ttt.mar.notify.dto.notify.email.PayLoadSendToEmailService;
import com.ttt.mar.notify.entities.NotifyHistory;
import com.ttt.mar.notify.entities.NotifyHistoryStatus;
import com.ttt.mar.notify.entities.StatusNotification;
import com.ttt.mar.notify.entities.TypeSend;
import com.ttt.mar.notify.entities.template.Template;
import com.ttt.mar.notify.entities.template.TemplateAttachment;
import com.ttt.mar.notify.filter.NotifyHistoryFilter;
import com.ttt.mar.notify.mapper.NotifyHistoryMapper;
import com.ttt.mar.notify.repositories.NotifyHistoryRepository;
import com.ttt.mar.notify.repositories.NotifyHistoryStatusRepository;
import com.ttt.mar.notify.repositories.StatusNotifiCationRepository;
import com.ttt.mar.notify.repositories.TypeSendRepository;
import com.ttt.mar.notify.repositories.template.TemplateAttachmentRepository;
import com.ttt.mar.notify.repositories.template.TemplateRepository;
import com.ttt.mar.notify.service.iface.NotifyHistoryService;
import com.ttt.mar.notify.utils.DateUtil;
import com.ttt.mar.notify.utils.Utils;
import com.ttt.rnd.common.config.ServiceInfo;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import com.ttt.rnd.common.utils.Convertor;
import com.ttt.rnd.common.utils.JsonUtils;
import com.ttt.rnd.common.utils.SortingUtils;
import com.ttt.rnd.lib.dto.logrequest.TemplateLogRequest;
import com.ttt.rnd.lib.entities.User;
import com.ttt.rnd.lib.entities.UserStatus;
import com.ttt.rnd.lib.repositories.UserRepository;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

@Service
public class NotifyHistoryServiceImpl implements NotifyHistoryService {

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
      .getLogger(NotifyHistoryController.class);

  @Value("${notify-history.export.dir:export/}")
  private String PATH_FOLDER_EXPORT_NOTIFY_HISTORY;

  public static final String NOTIFY_HISTORY_NAME = "notify_history_name";

  @Autowired
  private NotifyHistoryRepository notifyHistoryRepository;

  @Autowired
  private NotifyHistoryFilter notifyHistoryFilter;

  @Autowired
  private NotifyHistoryMapper notifyHistoryMapper;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private TemplateRepository templateRepository;

  @Autowired
  private TemplateAttachmentRepository templateAttachmentRepository;

  @Value("${file-service.url.attachment:http://192.168.20.32:9018/attachment/}")
  private String URL_FILE_ATTACHMENT;

  @Value("${file-service.authorization-key:}")
  private String authorizationKeyFile;

  @Value("${file-service.authorization-value:}")
  private String authorizationValueFile;

  @Value("${notify-history.template.config-file-name:template_notify_history.json}")
  private String configTemplateRequestLogFileName;

  public static final String SUFFIX = ".csv";
  private static final Long LONG_TIME_A_DAY = 86400000L;

  private static final String KEY_TYPE_SEND = "typeSend";

  private static final String KEY_ACCOUNT_CODE = "accountCode";

  private static final String KEY_CONTENT = "content";

  private static final String KEY_REQUEST_ID = "requestId";

  private static final String KEY_RECEIVER = "receiver";

  private static final String KEY_TYPE = "type";

  private static final String KEY_BRAND_NAME = "brandName";

  public static final String TEMPLATE_CODE = "templateCode";

  @Value("${code-type-send.sms:SMS}")
  private String CODE_SMS_TYPE;

  @Value("${code-type-send.email:EMAIL}")
  private String CODE_EMAIL_TYPE;

  @Value("${sms-service.url-received-sms}")
  private String URL_SMS_SERVICE_CREATE_NOTIFY;

  @Value("${email-service.url-received-email}")
  private String URL_EMAIL_SERVICE_CREATE_NOTIFY;

  @Value("${email-service.authorization-key:}")
  private String authorizationEmailKey;

  @Value("${email-service.authorization-value:}")
  private String authorizationEmailValue;

  @Value("${sms-service.authorization-key:}")
  private String authorizationSmsKey;

  @Value("${sms-service.authorization-value:}")
  private String authorizationSmsValue;

  private String CODE_STATUS_NOTIFY_REQUESTED = "REQUESTED";

  @Value("${attach-file.dir:attach-file/}")
  private String PATH_FOLDER_ATTACH_FILE;

  @Autowired
  private TypeSendRepository typeSendRepository;

  @Autowired
  private StatusNotifiCationRepository statusNotifyCationRepository;

  @Autowired
  private NotifyHistoryStatusRepository notifyHistoryStatusRepository;

  public DataRequestNotifyResponse<?> getNotifyHistoryPaging(Long fromDate, Long toDate,
      Integer statusId, Integer typeSendId, String search, String sort, Integer page, Integer limit)
      throws ParseException, OperationNotImplementException {
    checkValidFromDateAndToDate(fromDate, toDate);
    logger.info("fromDate : {}, toDate : {}", fromDate, toDate);
    Map<String, String> map = SortingUtils.detectSortType(sort);
    List<NotifyHistory> notifyHistories = notifyHistoryRepository
        .findAll(notifyHistoryFilter.getByFilter(fromDate, toDate, typeSendId, search, map));
    logger.info("NotifyHistories Size {}", notifyHistories.size());
    List<NotifyHistoryResponseDto> notifyHistoryResponseDtos = notifyHistories
        .stream().map(entity -> notifyHistoryMapper.toDto(entity)).collect(Collectors.toList());
    if (statusId != null) {
      notifyHistoryResponseDtos = notifyHistoryResponseDtos.stream()
          .filter(dto -> statusId.equals(dto.getStatusId())).collect(Collectors.toList());
    }
    logger.info("NotifyHistories Size Last: {}", notifyHistories.size());
    Page<NotifyHistoryResponseDto> responseDtoPage = convertListToPage(notifyHistoryResponseDtos,
        page, limit);
    List<TemplateLogRequest> temples = getTemplates();
    List<String> fieldNames = temples.stream().map(TemplateLogRequest::getKey)
        .collect(Collectors.toList());

    logger.info("fieldNames : {}", fieldNames);
    List<NotifyHistoryResponseDto> responseDtos = responseDtoPage.getContent();

    this.convertData(fieldNames, responseDtos);
    DataRequestNotifyResponse<NotifyHistoryResponseDto> dataRequestNotifyResponse
        = new DataRequestNotifyResponse<>();

    dataRequestNotifyResponse.setList(responseDtos);
    dataRequestNotifyResponse.setCurrentPage(page);
    dataRequestNotifyResponse.setNum(responseDtoPage.getTotalElements());
    dataRequestNotifyResponse.setTotalPage(responseDtoPage.getTotalPages());
    dataRequestNotifyResponse.setTemplate(getTemplates());
    return dataRequestNotifyResponse;
  }

  public String exportNotifyHistory(Long fromDate, Long toDate, Integer statusId,
      Integer typeSendId, String search, String sort)
      throws ParseException, OperationNotImplementException, IOException {
    checkValidFromDateAndToDate(fromDate, toDate);
    logger.info("fromDate : {}, toDate : {}", fromDate, toDate);
    Map<String, String> map = SortingUtils.detectSortType(sort);
    List<NotifyHistory> notifyHistories = notifyHistoryRepository
        .findAll(notifyHistoryFilter.getByFilter(fromDate, toDate, typeSendId, search, map));
    List<NotifyHistoryResponseDto> notifyHistoryResponseDtos = notifyHistories
        .stream().map(entity -> notifyHistoryMapper.toDto(entity)).collect(Collectors.toList());
    if (statusId != null) {
      notifyHistoryResponseDtos = notifyHistoryResponseDtos.stream()
          .filter(dto -> statusId.equals(dto.getStatusId())).collect(Collectors.toList());
    }

    List<TemplateLogRequest> templates = this.getTemplates();
    String[] keyFromTemplates = getKeyFromTemplate(templates);
    logger.info("keyFromTemplates: {}", (Object) keyFromTemplates);
    String[] nameFromTemplates = getNameFromTemplate(templates);
    logger.info("nameFromTemplates: {}", (Object) nameFromTemplates);
    //create file
    String fileName = PATH_FOLDER_EXPORT_NOTIFY_HISTORY.concat(NOTIFY_HISTORY_NAME).concat("_")
        .concat(UUID.randomUUID().toString()).concat(SUFFIX);
    logger.info("fileName: {}", fileName);
    if (!Files.exists(Paths.get(PATH_FOLDER_EXPORT_NOTIFY_HISTORY))) {
      logger.info("Path {} not exist", PATH_FOLDER_EXPORT_NOTIFY_HISTORY);
      File file = new File(PATH_FOLDER_EXPORT_NOTIFY_HISTORY);
      file.mkdir();
      logger.info("Path {} exist", PATH_FOLDER_EXPORT_NOTIFY_HISTORY);
    }

    //set name for header file csv
    CsvBeanWriter csvBeanWriter = new CsvBeanWriter(new FileWriter(fileName),
        CsvPreference.STANDARD_PREFERENCE);
    csvBeanWriter.writeHeader(nameFromTemplates);

    //write content
    for (NotifyHistoryResponseDto log : notifyHistoryResponseDtos) {
      csvBeanWriter.write(log, keyFromTemplates);
    }
    csvBeanWriter.close();
    return fileName;
  }

  public NotifyHistoryDetailResponseDto getNotifyHistoryById(Long id)
      throws ResourceNotFoundException {
    Optional<NotifyHistory> notifyHistoryOptional = notifyHistoryRepository.findById(id);
    if (notifyHistoryOptional.isEmpty()) {
      throw new ResourceNotFoundException("Notify History Not Found",
          ServiceInfo.getId() + ServiceMessageCode.NOTIFY_HISTORY_NOT_FOUND);
    }
    NotifyHistory notifyHistory = notifyHistoryOptional.get();

    return notifyHistoryMapper.toDtoDetail(notifyHistory);
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public Long createNotification(Map<String, Object> notifyMap, Integer userId)
      throws Exception {
//    convertFileToByte();
    Long requestId = null;
    String typeSend = (String) notifyMap.get(KEY_TYPE_SEND);
    if (StringUtils.isBlank(typeSend)) {
      throw new OperationNotImplementException("type send null",
          ServiceInfo.getId() + ServiceMessageCode.OPERATION_NOT_IMPLEMENT);
    }

    User user = checkValidUser(userId);
    // process connect sms-service
    mapDataForTemplate(notifyMap);
    if (typeSend.equals(CODE_SMS_TYPE)) {
      TypeSend typeSendByCode = typeSendRepository.findByCode(typeSend);
      if (typeSendByCode == null) {
        throw new ResourceNotFoundException("typeSend not found",
            ServiceInfo.getId() + ServiceMessageCode.RESOURCE_NOT_FOUND);
      }
      checkValidBodyNotifySms(notifyMap);
      NotifyHistory notifyHistory = new NotifyHistory();
      notifyHistory.setAccountCode((String) notifyMap.get(KEY_ACCOUNT_CODE));
      notifyHistory.setContent((String) notifyMap.get(KEY_CONTENT));
      notifyHistory.setReceiver((String) notifyMap.get(KEY_RECEIVER));
      notifyHistory.setTypeSend(typeSendByCode);
      notifyHistory.setCreatorUser(user);
      notifyHistory.setRequestTime(new Date(System.currentTimeMillis()));
      NotifyHistory notifyCreated = notifyHistoryRepository.save(notifyHistory);
      requestId = notifyCreated.getId();
      logger.info("create notify-sms success!");

      PayLoadSendToSmsService payLoadSendToSmsService = createPayLoadSendToSmsService(
          notifyCreated);
      logger.info("create payLoadSendToSmsService success: {}", payLoadSendToSmsService);
      Map<String, Object> responseSmsService = sendNotifyToSmsService(payLoadSendToSmsService);

      notifyCreated.setBrandName((String) responseSmsService.get("brandName"));
      notifyCreated.setPublisher((String) responseSmsService.get("publisher"));
      notifyCreated.setSender((String) responseSmsService.get("phoneNumber"));
      notifyCreated.setContent("{" + "body:" + "\"" + notifyMap.get(KEY_CONTENT) + "\"" + "}");
      notifyHistoryRepository.save(notifyCreated);
      logger.info("update notify-email success!");

      StatusNotification statusNotificationByCode = statusNotifyCationRepository
          .findByCode(CODE_STATUS_NOTIFY_REQUESTED);
      if (statusNotificationByCode != null) {
        NotifyHistoryStatus notifyHistoryStatus = new NotifyHistoryStatus();
        notifyHistoryStatus.setCreatedTime(new Date(System.currentTimeMillis()));
        notifyHistoryStatus.setStatusNotification(statusNotificationByCode);
        notifyHistoryStatus.setNotifyHistory(notifyCreated);
        notifyHistoryStatus.setStatus(statusNotificationByCode.getName());
        notifyHistory.setContent("{" + "body:" + "\"" + notifyMap.get(KEY_CONTENT) + "\"" + "}");
        notifyHistoryStatusRepository.save(notifyHistoryStatus);
      }

    } else  // process connect email-service
      if (typeSend.equals(CODE_EMAIL_TYPE)) {
        TypeSend typeSendByCode = typeSendRepository.findByCode(typeSend);
        if (typeSendByCode == null) {
          throw new ResourceNotFoundException("typeSend not found",
              ServiceInfo.getId() + ServiceMessageCode.RESOURCE_NOT_FOUND);
        }
        Map<String, Object> notify = checkValidBodyEmailNotify(notifyMap);

        // create instance notifyHistory
        NotifyHistory notifyHistory = new NotifyHistory();
        notifyHistory.setAccountCode((String) notifyMap.get(KEY_ACCOUNT_CODE));
        String body = (String) notifyMap.get("body");
        String subject = (String) notifyMap.get("subject");
        ContentEmail contentEmail = new ContentEmail();
        contentEmail.setBody(body);
        contentEmail.setSubject(subject);
        contentEmail.setType((String) notifyMap.get(KEY_TYPE));
        notifyHistory.setReceiver((String) notifyMap.get(KEY_RECEIVER));
        notifyHistory.setCc((String) notifyMap.get("cc"));
        notifyHistory.setBcc((String) notifyMap.get("bcc"));
        String brandName = (String) notifyMap.get("brandName");
        if (brandName != null && !brandName.isEmpty()) {
          notifyHistory.setBrandName((String) notifyMap.get("brandName"));
        }
        notifyHistory.setTypeSend(typeSendByCode);
        notifyHistory.setCreatorUser(user);
        NotifyHistory notifyCreated = notifyHistoryRepository.save(notifyHistory);
        requestId = notifyCreated.getId();
        logger.info("create notify-email success!");

        // read file and save file to server
        List<EmailAttachment> emailAttachment = new ArrayList<>();
        if (notifyMap.containsKey("emailAttachment")) {
          logger.info("key emailAttachment is exist!");
          emailAttachment = (List<EmailAttachment>) notifyMap.get("emailAttachment");
          logger.info("emailAttachment in dataMap size: {}", emailAttachment.size());
        }
        if (notifyMap.containsKey("files")) {
          List files = (List) notifyMap.get("files");
          logger.info("files: {}", JsonUtils.toJson(files));
          if (files != null && !files.isEmpty()) {
            for (Object url : files) {
              EmailAttachment attachUrl = getEmailAttachment(Utils.percentEncode(url.toString()));
              if (attachUrl != null) {
                emailAttachment.add(attachUrl);
              }
            }
          }
        }
        PayLoadSendToEmailService payLoadSendToEmailService = createPayLoadSendToEmailService(
            notifyMap, contentEmail, notifyCreated, emailAttachment);

        Map<String, Object> responseEmailService = sendNotifyToEmailService(
            payLoadSendToEmailService);
        List<String> listFileName = new ArrayList<>();
        try {
          listFileName = convertByteArrayToFile(PATH_FOLDER_ATTACH_FILE,
              notifyCreated.getId(), emailAttachment);
        } catch (Exception e) {
          logger.error("convertByteArrayToFile error: {}", e.getMessage());
        }
        logger.info("list file name saved to server: {}", listFileName);

        if (brandName == null || brandName.isEmpty()) {
          notifyCreated.setBrandName((String) responseEmailService.get("brandName"));
        }

        notifyCreated.setContent(contentEmail.toString(listFileName));
        notifyCreated.setPublisher((String) responseEmailService.get("publisher"));
        notifyCreated.setSender((String) responseEmailService.get("from"));
        notifyHistoryRepository.save(notifyCreated);
        logger.info("update notify-email success!");
        StatusNotification statusNotificationByCode = statusNotifyCationRepository
            .findByCode(CODE_STATUS_NOTIFY_REQUESTED);
        if (statusNotificationByCode != null) {
          NotifyHistoryStatus notifyHistoryStatus = new NotifyHistoryStatus();
          notifyHistoryStatus.setCreatedTime(new Date(System.currentTimeMillis()));
          notifyHistoryStatus.setStatusNotification(statusNotificationByCode);
          notifyHistoryStatus.setNotifyHistory(notifyCreated);
          notifyHistoryStatus.setMessage(
              "{" + "invalidEmails:" + responseEmailService.get("invalidEmails") + "}");
          notifyHistoryStatus.setStatus(statusNotificationByCode.getName());
          notifyHistoryStatusRepository.save(notifyHistoryStatus);
        }

      } else {
        throw new OperationNotImplementException("typeSend invalid",
            ServiceInfo.getId() + ServiceMessageCode.OPERATION_NOT_IMPLEMENT);
      }

    return requestId;
  }

  public EmailAttachment getEmailAttachment(String url) {
    logger.info("getEmailAttachment: {}", url);
    try {
      HttpResponse<JsonNode> response = Unirest.get(URL_FILE_ATTACHMENT.concat(url))
          .header("Content-Type", "application/json")
          .header(authorizationKeyFile, authorizationValueFile)
          .asJson();
      Map<String, Object> map = response.getBody().getObject().toMap();
      logger.info("HttpResponse<JsonNode>, httpCode = {}", map.get("httpCode"));
      if (map.containsKey("httpCode")
          && 200 == Integer.parseInt(map.get("httpCode").toString())) {
        Map<String, Object> data = (Map<String, Object>) map.get("data");
        logger.info("data: {}", JsonUtils.toJson(data));
        EmailAttachment emailAttachment = new EmailAttachment();
        emailAttachment.setContent(
            Base64.getDecoder().decode(data.get("content").toString().getBytes(
                StandardCharsets.UTF_8)));
        emailAttachment.setName(data.get("name").toString());
        emailAttachment.setMimeType(data.get("mimeType").toString());

        return emailAttachment;
      }
    } catch (Exception e) {
      logger.error("error getEmailAttachment, {}", e.getMessage());
    }

    return null;
  }

  private void mapDataForTemplate(Map<String, Object> notifyMap)
      throws OperationNotImplementException, ResourceNotFoundException {
    String typeSend = (String) notifyMap.get(KEY_TYPE_SEND);
    if (StringUtils.isBlank(typeSend)) {
      throw new OperationNotImplementException("type send null",
          ServiceInfo.getId() + ServiceMessageCode.OPERATION_NOT_IMPLEMENT);
    }

    String templateCode = (String) notifyMap.get(TEMPLATE_CODE);
    if (StringUtils.isBlank(templateCode)) {
      return;
    }

    String temPlateType;
    if (CODE_SMS_TYPE.equals(typeSend)) {
      temPlateType = Constants.SMS;
    } else {
      temPlateType = Constants.EMAIL;
    }

    Template template = templateRepository
        .findByCodeAndTypeAndIsDeletedFalse(templateCode, temPlateType);
    if (template == null) {
      throw new ResourceNotFoundException("Template not found",
          ServiceInfo.getId() + ServiceMessageCode.TEMPLATE_NOT_FOUND);
    }
    //get attachment detail
    List<TemplateAttachment> listTemplateAttachment = templateAttachmentRepository
        .findByTemplateIdAndIsDeletedFalse(template.getId());
    if (!listTemplateAttachment.isEmpty()) {
      List<String> urls = listTemplateAttachment.stream().map(TemplateAttachment::getUrl)
          .collect(Collectors.toList());
      if (notifyMap.containsKey("files")) {
        List files = (List) notifyMap.get("files");
        urls.addAll(files);
      }
      notifyMap.put("files", urls);
    }
    mapDataToTemplateSMS(template, notifyMap, typeSend);
  }

  private void mapDataToTemplateSMS(Template template,
      Map<String, Object> notifyMap, String typeSend) {
    String to = template.getTo();
    to = concatCharacter(to, Convertor.convertToString(notifyMap.get("receiver")));
    String content = template.getContent();
    String cc = template.getCc();
    cc = concatCharacter(cc, Convertor.convertToString(notifyMap.get("cc")));
    String bcc = template.getBcc();
    bcc = concatCharacter(bcc, Convertor.convertToString(notifyMap.get("bcc")));
    String subject = template.getSubject();
    Pattern pattern = Pattern.compile("\\$([a-zA-Z0-9_]+?)\\$");
    for (String key : notifyMap.keySet()) {
      if (pattern.matcher(key).find()) {
        String value = notifyMap.get(key).toString();
        if (StringUtils.isNotBlank(to) && to.contains(key)) {
          to = to.replace(key, value);
        }
        if (StringUtils.isNotBlank(content) && content.contains(key)) {
          content = content.replace(key, value);
        }
        if (CODE_EMAIL_TYPE.equals(typeSend)) {
          if (StringUtils.isNotBlank(cc) && cc.contains(key)) {
            cc = cc.replace(key, value);
          }
          if (StringUtils.isNotBlank(bcc) && bcc.contains(key)) {
            bcc = bcc.replace(key, value);
          }
          if (StringUtils.isNotBlank(subject) && subject.contains(key)) {
            subject = subject.replace(key, value);
          }
        }
      }
    }
    notifyMap.put(KEY_RECEIVER, to);
    if (CODE_EMAIL_TYPE.equals(typeSend)) {
      notifyMap.put("cc", cc);
      notifyMap.put("bcc", bcc);
      notifyMap.put("subject", subject);
      notifyMap.put("body", content);
    } else if (CODE_SMS_TYPE.equals(typeSend)) {
      notifyMap.put(KEY_CONTENT, content);
    }
  }

  private String concatCharacter(String result, String concat) {
    if (StringUtils.isNotBlank(result) && StringUtils.isNotBlank(concat)) {
      return result + "," + concat;
    } else if (StringUtils.isNotBlank(result)) {
      return result;
    } else if (StringUtils.isNotBlank(concat)) {
      return concat;
    }

    return null;
  }

  public PayLoadSendToEmailService createPayLoadSendToEmailService(Map<String, Object> notifyMap,
      ContentEmail contentEmail, NotifyHistory notifyHistory,
      List<EmailAttachment> emailAttachments) {

    PayLoadSendToEmailService payLoadSendToEmailService = new PayLoadSendToEmailService();
    payLoadSendToEmailService.setAccountCode((String) notifyMap.get(KEY_ACCOUNT_CODE));
    payLoadSendToEmailService.setBcc((String) notifyMap.get("bcc"));
    payLoadSendToEmailService.setCc((String) notifyMap.get("cc"));
    payLoadSendToEmailService.setContent(contentEmail);
    payLoadSendToEmailService.setRequestId(notifyHistory.getId());
    payLoadSendToEmailService.setReceiver((String) notifyMap.get(KEY_RECEIVER));
    payLoadSendToEmailService.setType((String) notifyMap.get(KEY_TYPE));
    payLoadSendToEmailService.setEmailAttachmentList(emailAttachments);
    payLoadSendToEmailService.setType((String) notifyMap.get(KEY_RECEIVER));
    payLoadSendToEmailService.setBrandName((String) notifyMap.get(KEY_BRAND_NAME));

    logger.info("payLoadSendToEmailService: {}", payLoadSendToEmailService);
    return payLoadSendToEmailService;
  }

  // email
  public Map<String, Object> checkValidBodyEmailNotify(Map<String, Object> notifyMap)
      throws OperationNotImplementException {
    Map<String, Object> bodyEmail = new HashMap<>();
    String accountCode = (String) notifyMap.get(KEY_ACCOUNT_CODE);

    if (accountCode == null || accountCode.isEmpty()) {
      throw new OperationNotImplementException("accountCode null",
          ServiceInfo.getId() + ServiceMessageCode.OPERATION_NOT_IMPLEMENT);
    }

    String body = (String) notifyMap.get("body");
    if (body == null || body.isEmpty()) {
      throw new OperationNotImplementException("content null",
          ServiceInfo.getId() + ServiceMessageCode.OPERATION_NOT_IMPLEMENT);
    }

    String subject = (String) notifyMap.get("subject");
//    if (subject == null || subject.isEmpty()) {
//      throw new OperationNotImplementException("subject null",
//          ServiceInfo.getId() + ServiceMessageCode.OPERATION_NOT_IMPLEMENT);
//    }

    String receiver = (String) notifyMap.get(KEY_RECEIVER);
    if (receiver == null || receiver.isEmpty()) {
      throw new OperationNotImplementException("receiver null",
          ServiceInfo.getId() + ServiceMessageCode.OPERATION_NOT_IMPLEMENT);
    }

    bodyEmail.put(KEY_ACCOUNT_CODE, accountCode);
    bodyEmail.put("body", body);
    bodyEmail.put(KEY_RECEIVER, receiver);
    bodyEmail.put("subject", subject);
    return bodyEmail;
  }


  // email
  public Map<String, Object> sendNotifyToEmailService(
      PayLoadSendToEmailService payLoadSendToEmailService)
      throws Exception {

    Map<String, Object> responseSmsService = new HashMap<>();

    logger.info("start send infor to email-service: {} ", payLoadSendToEmailService);

    HttpResponse<JsonNode> response = Unirest.post(URL_EMAIL_SERVICE_CREATE_NOTIFY)
        .header("Content-Type", "application/json")
        .header(authorizationEmailKey, authorizationEmailValue)
        .body(JsonUtils.toJson(payLoadSendToEmailService))
        .asJson();
    if (response != null) {
      JsonNode jsonNode = response.getBody();
      if (jsonNode != null) {
        logger.info("response from email-service: {} {}", response.getStatus(),
            jsonNode.toString());
      }
      if (response.getStatus() != 200) {
        throw new OperationNotImplementException("email-service unreceived sms",
            ServiceInfo.getId() + ServiceMessageCode.OPERATION_NOT_IMPLEMENT);
      }
      Boolean status = (Boolean) response.getBody().getObject().get("status");
      if (status == null || status.equals(Boolean.FALSE)) {
        throw new OperationNotImplementException("email-service unreceived sms",
            ServiceInfo.getId() + ServiceMessageCode.OPERATION_NOT_IMPLEMENT);
      }

      if (response.getBody().getObject().has("data")) {
        JSONObject data = response.getBody().getObject().getJSONObject("data");

        if (data.has("publisher")) {
          responseSmsService
              .put("publisher", data.getString("publisher"));
        }

        if (data.has("brandName")) {
          responseSmsService
              .put("brandName", data.getString("brandName"));
        }

        if (data.has("from")) {
          responseSmsService
              .put("from", data.getString("from"));
        }
        if (data.has("invalidEmails")) {
          responseSmsService
              .put("invalidEmails", data.get("invalidEmails"));
        }

      }

    }
    return responseSmsService;
  }

  // sms
  public PayLoadSendToSmsService createPayLoadSendToSmsService(NotifyHistory notifyHistory) {
    PayLoadSendToSmsService payLoadSendToSmsService = new PayLoadSendToSmsService();
    payLoadSendToSmsService.setRequestId(notifyHistory.getId());
    payLoadSendToSmsService.setAccountCode(notifyHistory.getAccountCode());
    payLoadSendToSmsService.setContent(notifyHistory.getContent());
    payLoadSendToSmsService.setReceiver(notifyHistory.getReceiver());

    return payLoadSendToSmsService;
  }

  // sms
  public Map<String, Object> sendNotifyToSmsService(PayLoadSendToSmsService payLoadSendToSmsService)
      throws Exception {

    Map<String, Object> responseSmsService = new HashMap<>();

    logger.info("start send infor to sms-service: {} ", payLoadSendToSmsService);

    HttpResponse<JsonNode> response = Unirest.post(URL_SMS_SERVICE_CREATE_NOTIFY)
        .header("Content-Type", "application/json")
        .header(authorizationSmsKey, authorizationSmsValue)
        .body(JsonUtils.toJson(payLoadSendToSmsService))
        .asJson();
    if (response != null) {
      JsonNode jsonNode = response.getBody();
      if (jsonNode != null) {
        logger.info("response from sms-service: {} {}", response.getStatus(),
            jsonNode.toString());
      }
      if (response.getStatus() != 200) {
        throw new OperationNotImplementException("sms-service unreceived sms",
            ServiceInfo.getId() + ServiceMessageCode.OPERATION_NOT_IMPLEMENT);
      }
      Boolean status = (Boolean) response.getBody().getObject().get("status");
      if (status == null || status.equals(Boolean.FALSE)) {
        throw new OperationNotImplementException("sms-service unreceived sms",
            ServiceInfo.getId() + ServiceMessageCode.OPERATION_NOT_IMPLEMENT);
      }

      if (response.getBody().getObject().has("data")) {
        JSONObject data = response.getBody().getObject().getJSONObject("data");

        if (data.has("publisher")) {
          responseSmsService
              .put("publisher", data.getString("publisher"));
        }

        if (data.has("brandName")) {
          responseSmsService
              .put("brandName", data.getString("brandName"));
        }

        if (data.has("phoneNumber")) {
          responseSmsService
              .put("phoneNumber", data.getString("phoneNumber"));
        }


      }


    }
    return responseSmsService;
  }

  public void checkValidBodyNotifySms(Map<String, Object> notifyMap)
      throws OperationNotImplementException {

    String accountCode = (String) notifyMap.get(KEY_ACCOUNT_CODE);

    if (accountCode == null || accountCode.isEmpty()) {
      throw new OperationNotImplementException("accountCode null",
          ServiceInfo.getId() + ServiceMessageCode.OPERATION_NOT_IMPLEMENT);
    }

    String content = (String) notifyMap.get(KEY_CONTENT);

    if (content == null || content.isEmpty()) {
      throw new OperationNotImplementException("content null",
          ServiceInfo.getId() + ServiceMessageCode.OPERATION_NOT_IMPLEMENT);
    }

    String receiver = (String) notifyMap.get(KEY_RECEIVER);

    if (receiver == null || receiver.isEmpty()) {
      throw new OperationNotImplementException("receiver null",
          ServiceInfo.getId() + ServiceMessageCode.OPERATION_NOT_IMPLEMENT);
    }

  }

  public String[] getKeyFromTemplate(List<TemplateLogRequest> templates) {
    List<String> values = new ArrayList<>();
    templates.forEach(template -> values.add(template.getKey()));
    return values.toArray(String[]::new);
  }

  public String[] getNameFromTemplate(List<TemplateLogRequest> templates) {
    List<String> values = new ArrayList<>();
    templates.forEach(template -> values.add(template.getName()));
    return values.toArray(String[]::new);
  }

  public Page<NotifyHistoryResponseDto> convertListToPage(
      List<NotifyHistoryResponseDto> notifyHistoryResponseDtos, Integer page, Integer limit) {
    PageRequest pageRequest = PageRequest.of(page - 1, limit);
    int start = (int) pageRequest.getOffset();
    int end = Math.min((start + pageRequest.getPageSize()), notifyHistoryResponseDtos.size());

    return new PageImpl<>(
        notifyHistoryResponseDtos.subList(start, end), pageRequest,
        notifyHistoryResponseDtos.size());
  }

  public void checkValidFromDateAndToDate(Long fromDateTimeStamp, Long toDateTimeStamp)
      throws OperationNotImplementException, ParseException {
    Long onlyDateFromTimeStamp = DateUtil.getOnlyDateFromTimeStamp(System.currentTimeMillis());
    if (fromDateTimeStamp != null & toDateTimeStamp != null) {
      if (fromDateTimeStamp > toDateTimeStamp) {
        throw new OperationNotImplementException("fromDate greater toDate",
            ServiceInfo.getId() + NotifyMessageCode.FROM_DATE_GREATER_TO_DATE);
      }
      if (fromDateTimeStamp > onlyDateFromTimeStamp + LONG_TIME_A_DAY) {
        throw new OperationNotImplementException("fromDate greater currentDate",
            ServiceInfo.getId() + NotifyMessageCode.FROM_DATE_GREATER_CURRENT_DATE);
      }
      if (toDateTimeStamp > onlyDateFromTimeStamp + LONG_TIME_A_DAY) {
        throw new OperationNotImplementException("toDate greater currentDate",
            ServiceInfo.getId() + NotifyMessageCode.TO_DATE_GREATER_CURRENT_DATE);
      }
    }
  }

  public List<TemplateLogRequest> getTemplates() {
    try {
      String data = Utils.readFileAsString(configTemplateRequestLogFileName);
      return Arrays.asList(new Gson().fromJson(data, TemplateLogRequest[].class));
    } catch (Exception e) {
      logger.info("error when read template config, reason: {}", e.getMessage());
      return new ArrayList<>();
    }

  }

  void convertData(List<String> fieldNames, List<NotifyHistoryResponseDto> data) {
    Field[] fields = NotifyHistoryResponseDto.class.getDeclaredFields();
    for (Field f : fields) {
      if (!fieldNames.contains(f.getName())) {
        for (NotifyHistoryResponseDto log : data) {
          try {
            f.setAccessible(true);
            f.set(log, null);
          } catch (Exception ignored) {
          }
        }
      }
    }
  }

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


  public List<String> convertByteArrayToFile(String folder, Long notifyId,
      List<EmailAttachment> attachUrls)
      throws Exception {
    logger.info("writing file to server...");
    List<String> listFileNameSavedToServer = new ArrayList<>();
    for (EmailAttachment attachUrl : attachUrls) {
      if (attachUrl == null) {
        throw new OperationNotImplementException("incorrect format file",
            ServiceInfo.getId() + ServiceMessageCode.OPERATION_NOT_IMPLEMENT);
      }
      if (attachUrl.getContent() == null) {
        throw new OperationNotImplementException("file null",
            ServiceInfo.getId() + ServiceMessageCode.OPERATION_NOT_IMPLEMENT);
      }
      if (attachUrl.getName() == null) {
        throw new OperationNotImplementException("originalFileName null",
            ServiceInfo.getId() + ServiceMessageCode.OPERATION_NOT_IMPLEMENT);
      }
    }
    logger.info("attachUrls size {}...", attachUrls.size());
    if (!Files.exists(Paths.get(folder))) {
      logger.info("Folder is not exist.");
      File file = new File(folder);
      file.mkdir();
    }
    for (EmailAttachment attachUrl : attachUrls) {
      if (StringUtils.isNotBlank(attachUrl.getName())) {
        listFileNameSavedToServer.add(attachUrl.getName());
      }
      File file = new File(folder + notifyId + "_" + attachUrl.getName());
      logger.info("File exist.");
      OutputStream outputStream = new FileOutputStream(file);
      logger.info("outputStream exist.");
      outputStream.write(attachUrl.getContent());
      logger.info("write exist.");
      outputStream.close();
    }
    return listFileNameSavedToServer;
  }

//  public void convertFileToByte() throws Exception {
//    byte[] bytes = Files.readAllBytes(Paths.get("/home/test/Downloads/JD_Frontend.docx"));
//    logger.info("byte: {}", bytes);
//  }
}
