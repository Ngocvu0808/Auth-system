package com.ttt.mar.email.service.impl;


import com.ttt.mar.email.config.ServiceMessageCode;
import com.ttt.mar.email.dto.emailhistory.DataPagingEmailHistoryResponse;
import com.ttt.mar.email.dto.emailhistory.EmailHistoryDtoResponse;
import com.ttt.mar.email.dto.emailhistory.Template;
import com.ttt.mar.email.dto.mail.EmailRequestDto;
import com.ttt.mar.email.entities.EmailConfig;
import com.ttt.mar.email.entities.EmailHistory;
import com.ttt.mar.email.entities.StatusEmail;
import com.ttt.mar.email.filter.EmailHistoryFilter;
import com.ttt.mar.email.mapper.EmailHistoryMapper;
import com.ttt.mar.email.repositories.EmailConfigRepository;
import com.ttt.mar.email.repositories.EmailHistoryRepository;
import com.ttt.mar.email.service.iface.EmailHistoryService;
import com.ttt.mar.email.utils.DateUtil;
import com.ttt.rnd.common.config.ServiceInfo;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import com.ttt.rnd.common.utils.SortingUtils;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.prefs.CsvPreference;


@Service
public class EmailHistoryServiceImpl implements EmailHistoryService {

  @Autowired
  private EmailHistoryRepository emailHistoryRepository;

  @Autowired
  private EmailConfigRepository emailConfigRepository;

  @Autowired
  private EmailHistoryMapper emailHistoryMapper;

  private static final Long LONG_TIME_A_DAY = 86400000L;

  public static final String EMAIL_HISTORY_NAME = "email_history";

  public static final String SUFFIX = ".csv";

  @Value("${email-history.export.dir: ./export}")
  private String PATH_FOLDER_EXPORT_HISTORY_EMAIL;


  @Override
  public DataPagingEmailHistoryResponse<EmailHistoryDtoResponse> getEmailHistories(String search,
      String status, String idHistoryConfigs, Long fromDate, Long toDate, String sort, Integer page,
      Integer limit) throws ParseException, OperationNotImplementException {

    checkValidFromDateAndToDate(fromDate, toDate);
    Set<StatusEmail> listStatusEmail = new HashSet<>();
    if (status != null && !status.isEmpty()) {
      String[] statusArrays = status.trim().split(",");
      for (String statusArray : statusArrays) {
        if (statusArray.equals(StatusEmail.FAIL.name())) {
          listStatusEmail.add(StatusEmail.FAIL);
        }
        if (statusArray.equals(StatusEmail.SUCCESS.name())) {
          listStatusEmail.add(StatusEmail.SUCCESS);
        }
      }
    }

    List<Integer> listIdHistoryConfig = new ArrayList<>();
    if (idHistoryConfigs != null && !idHistoryConfigs.isEmpty()) {
      String[] idHistoryConfigArrays = idHistoryConfigs.trim().split(",");
      for (String idHistoryArray : idHistoryConfigArrays) {
        listIdHistoryConfig.add(Integer.parseInt(idHistoryArray));
      }
    }

    Map<String, String> map = SortingUtils.detectSortType(sort);

    Page<EmailHistory> emailHistoryPages = emailHistoryRepository.findAll(new EmailHistoryFilter()
            .getByFilter(search, listStatusEmail, listIdHistoryConfig, fromDate, toDate, map),
        PageRequest.of(page - 1, limit));

    List<EmailHistoryDtoResponse> listEmailHistoryDto = new ArrayList<>();
    emailHistoryPages.getContent()
        .forEach(emailHistory -> listEmailHistoryDto
            .add(emailHistoryMapper.toEmailHistoryDtoResponse(emailHistory)));

    DataPagingEmailHistoryResponse<EmailHistoryDtoResponse> dataPagingResponses = new DataPagingEmailHistoryResponse<>();
    dataPagingResponses.setList(listEmailHistoryDto);
    dataPagingResponses.setTotalPage(emailHistoryPages.getTotalPages());
    dataPagingResponses.setNum(emailHistoryPages.getTotalElements());
    dataPagingResponses.setCurrentPage(page + 1);
    dataPagingResponses.setTemplates(getTemplates());
    return dataPagingResponses;
  }

  public List<EmailHistoryDtoResponse> getEmailHistoriesExport(String search, String status,
      String idHistoryConfigs, Long fromDate, Long toDate, String sort)
      throws ParseException, OperationNotImplementException {

    checkValidFromDateAndToDate(fromDate, toDate);
    Set<StatusEmail> listStatusEmail = new HashSet<>();
    if (status != null && !status.isEmpty()) {
      String[] statusArrays = status.trim().split(",");
      for (String statusArray : statusArrays) {
        if (statusArray.equals(StatusEmail.FAIL.name())) {
          listStatusEmail.add(StatusEmail.FAIL);
        }
        if (statusArray.equals(StatusEmail.SUCCESS.name())) {
          listStatusEmail.add(StatusEmail.SUCCESS);
        }
      }
    }

    List<Integer> listIdHistoryConfig = new ArrayList<>();
    if (idHistoryConfigs != null && !idHistoryConfigs.isEmpty()) {
      String[] idHistoryConfigArrays = idHistoryConfigs.trim().split(",");
      for (String idHistoryArray : idHistoryConfigArrays) {
        listIdHistoryConfig.add(Integer.parseInt(idHistoryArray));
      }
    }

    Map<String, String> map = SortingUtils.detectSortType(sort);

    List<EmailHistoryDtoResponse> listEmailHistoryDto = new ArrayList<>();

    List<EmailHistory> emailHistories = emailHistoryRepository.findAll(new EmailHistoryFilter()
        .getByFilter(search, listStatusEmail, listIdHistoryConfig, fromDate, toDate, map));

    emailHistories.forEach(emailHistory -> listEmailHistoryDto
        .add(emailHistoryMapper.toEmailHistoryDtoResponse(emailHistory)));

    return listEmailHistoryDto;
  }

  @Override
  public String exportEmailHistory(String search, String status, String idHistoryConfigs,
      Long fromDate, Long toDate, String sort)
      throws ParseException, OperationNotImplementException, IOException {
    List<EmailHistoryDtoResponse> listEmailHistory = getEmailHistoriesExport(search, status,
        idHistoryConfigs, fromDate,
        toDate, sort);
    List<Template> templates = this.getTemplates();

    String[] keyFromTemplates = getKeyFromTemplate(templates);
    String[] nameFromTemplates = getNameFromTemplate(templates);

    //create file
    String fileName =
        PATH_FOLDER_EXPORT_HISTORY_EMAIL + EMAIL_HISTORY_NAME + UUID.randomUUID().toString()
            + SUFFIX;
    if (!Files.exists(Paths.get(PATH_FOLDER_EXPORT_HISTORY_EMAIL))) {
      File file = new File(PATH_FOLDER_EXPORT_HISTORY_EMAIL);
      file.mkdir();
    }

    //set name for header file csv
    CsvBeanWriter csvBeanWriter = new CsvBeanWriter(new FileWriter(fileName),
        CsvPreference.STANDARD_PREFERENCE);
    csvBeanWriter.writeHeader(nameFromTemplates);

    //write content
    for (EmailHistoryDtoResponse emailHistory : listEmailHistory) {
      csvBeanWriter.write(emailHistory, keyFromTemplates);
    }
    csvBeanWriter.close();
    return fileName;
  }


  public void checkValidFromDateAndToDate(Long fromDateTimeStamp, Long toDateTimeStamp)
      throws OperationNotImplementException, ParseException {
    Long onlyDateFromTimeStamp = DateUtil.getOnlyDateFromTimeStamp(System.currentTimeMillis());
    if (fromDateTimeStamp != null & toDateTimeStamp != null) {
      if (fromDateTimeStamp.longValue() > toDateTimeStamp.longValue()) {
        throw new OperationNotImplementException("fromDate greater toDate",
            ServiceInfo.getId() + ServiceMessageCode.FROM_DATE_GREATER_TO_DATE);

      }
      if (fromDateTimeStamp.longValue() > onlyDateFromTimeStamp + LONG_TIME_A_DAY) {
        throw new OperationNotImplementException("fromDate greater currentDate",
            ServiceInfo.getId()
                + ServiceMessageCode.FROM_DATE_GREATER_CURRENT_DATE);
      }

      if (toDateTimeStamp.longValue() > onlyDateFromTimeStamp + LONG_TIME_A_DAY) {
        throw new OperationNotImplementException("toDate greater currentDate",
            ServiceInfo.getId()
                + ServiceMessageCode.TO_DATE_GREATER_CURRENT_DATE);
      }
    }
  }

  public Boolean addEmailHistory(EmailRequestDto dto) throws ResourceNotFoundException {
    Integer idEmailConfig = dto.getIdEmailConfig();
    EmailHistory emailHistory = new EmailHistory();
    if (idEmailConfig != null) {
      EmailConfig emailConfig = emailConfigRepository.findByIdAndIsDeletedFalse(idEmailConfig);
      if (emailConfig == null) {
        throw new ResourceNotFoundException("Email Config Not Found",
            ServiceInfo.getId() + ServiceMessageCode.EMAIL_CONFIG_NOT_FOUND);
      }
      emailHistory.setEmailConfig(emailConfig);
    }
    BeanUtils.copyProperties(dto, emailHistory, "idRequest, ");
    emailHistoryRepository.save(emailHistory);
    return true;
  }

  public List<Template> getTemplates() {
    List<Template> templates = new ArrayList<>();

    Template historyId = new Template();
    historyId.setIndex(1);
    historyId.setKey("id");
    historyId.setTypeValue("number");
    historyId.setName("History ID");
    templates.add(historyId);

    Template receiver = new Template();
    receiver.setIndex(3);
    receiver.setKey("receiver");
    receiver.setTypeValue("string");
    receiver.setName("Người nhận");
    templates.add(receiver);

    Template title = new Template();
    title.setIndex(4);
    title.setKey("title");
    title.setName("Tiêu Đề");
    title.setTypeValue("string");
    templates.add(title);

    Template status = new Template();
    status.setIndex(5);
    status.setKey("status");
    status.setName("Trạng thái");
    status.setTypeValue("string");
    templates.add(status);

    Template attachFileUrl = new Template();
    attachFileUrl.setIndex(6);
    attachFileUrl.setKey("attachFileUrl");
    attachFileUrl.setName("File đính kèm");
    attachFileUrl.setTypeValue("string");
    templates.add(attachFileUrl);

    Template content = new Template();
    content.setIndex(7);
    content.setKey("content");
    content.setName("Nội dung");
    content.setTypeValue("string");
    templates.add(content);

    Template creationTime = new Template();
    creationTime.setIndex(8);
    creationTime.setKey("creationTime");
    creationTime.setName("Ngày tạo");
    creationTime.setTypeValue("date");
    templates.add(creationTime);

    Template lastModified = new Template();
    lastModified.setIndex(9);
    lastModified.setKey("lastModified");
    lastModified.setName("Ngày cập nhật gần nhất");
    lastModified.setTypeValue("date");
    templates.add(lastModified);

    return templates;
  }

  public String[] getKeyFromTemplate(List<Template> templates)
      throws OperationNotImplementException {
    List<String> values = new ArrayList<>();
    templates.forEach(template -> values.add(template.getKey()));
    return values.toArray(String[]::new);
  }

  public String[] getNameFromTemplate(List<Template> templates)
      throws OperationNotImplementException {
    List<String> values = new ArrayList<>();
    templates.forEach(template -> values.add(template.getName()));
    return values.toArray(String[]::new);
  }
}
