package com.ttt.mar.leads.service.impl;

import static com.ttt.mar.leads.config.Constants.TEMPLATE_TYPE_KEY;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.ttt.mar.leads.config.LeadImportConfig;
import com.ttt.mar.leads.config.MongoFieldsConfig;
import com.ttt.mar.leads.config.ServiceMessageCode;
import com.ttt.mar.leads.dto.*;
import com.ttt.mar.leads.dto.distributeLead.LeadDistributeApiRequestDto;
import com.ttt.mar.leads.dto.distributeLead.LeadDistributeHistoryResponseDto;
import com.ttt.mar.leads.entities.*;
import com.ttt.mar.leads.filter.FilterOfCampaignFilter;
import com.ttt.mar.leads.repositories.*;
import com.ttt.mar.leads.service.iface.DistributeApiService;
import com.ttt.mar.leads.service.iface.LeadsService;
import com.ttt.mar.leads.service.iface.kafka.consumer.ConsumerService;
import com.ttt.mar.leads.service.impl.kafka.ProducerServiceImpl;
import com.ttt.mar.leads.utils.Utils;
import com.ttt.rnd.common.config.ServiceInfo;
import com.ttt.rnd.common.dto.DataPagingResponse;
import com.ttt.rnd.common.exception.BaseException;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import com.ttt.rnd.common.exception.ValidationException;
import com.ttt.rnd.common.utils.Convertor;
import com.ttt.rnd.common.utils.GenerateUniqueKey;
import com.ttt.rnd.common.utils.JsonUtils;
import com.ttt.rnd.common.utils.SortingUtils;
import com.ttt.rnd.lib.dto.logrequest.TemplateLogRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.io.ICsvMapWriter;
import org.supercsv.prefs.CsvPreference;

@Service
public class LeadsServiceImpl implements LeadsService {

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
      .getLogger(LeadsServiceImpl.class);
  public static final List<String> LIST_IMPORT_FIELDS = _getListImportField();

  DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  @Value("${spring.data.mongodb.lead-collection}")
  private String leadCollection;

  @Value("${spring.data.mongodb.lead_status}")
  private String leadStatusCollection;

  @Value("${spring.data.mongodb.lead-distribute-status-history}")
  private String leadDistributeStatusHistory;

  @Value("${spring.data.mongodb.history-lead-collection}")
  private String history_lead_status;


  @Value("${template.leads.config-file-name}")
  private String configTemplateRequestLogFileName;
  @Value("${template.leads.config-file-download-name}")
  private String configTemplateDownloadLead;

  @Value("${mar.info.export-dir}")
  private String EXPORT_DIR;

  @Value("${mar.info.template}")
  private String TEMPLATE_FILE;

  @Value("${mar.info.import-dir}")
  private String IMPORT_DIR;

  @Value("${spring.data.mongodb.lead-trash}")
  private String leadTrashCollection;

  @Value("${spring.data.mongodb.lead-file}")
  private String leadFileCollection;

  @Value("${partner.kafka.default-topic}")
  private String topic;

  final String CREATED_SUCCESS = "CREATED_SUCCESS";
  final String TRANSFER_CRM_SUCCESS = "TRANSFER_CRM_SUCCESS";
  final String TRANSFER_CRM_FAIL = "TRANSFER_CRM_FAIL";
  final String TRANSFER_CRM_ERROR = "TRANSFER_CRM_ERROR";
  final String LEAD_ENOUGH_CONDITION = "LEAD_ENOUGH_CONDITION";

  private final ProducerServiceImpl producerService;

  private final ConsumerService consumerService;

  private final DistributeApiRepository distributeApiRepository;

  private final MongoTemplate mongoTemplate;

  private final CampaignRepository campaignRepository;

  private LeadSourceRepository leadSourceRepository;

  private final CampaignSourceRepository campaignSourceRepository;

  private CampaignServiceImpl campaignService;

  private FilterConditionRepository filterConditionRepository;

  private FilterConditionValueRepository filterConditionValueRepository;

  private LeadDistributeHistoryResponseDto leadDistributeHistoryResponseDto;


  public LeadsServiceImpl(ProducerServiceImpl producerService,
      ConsumerService consumerService,
      DistributeApiService distributeApiService,
      DistributeApiRepository distributeApiRepository,
      MongoTemplate mongoTemplate,
      CampaignRepository campaignRepository,
      LeadSourceRepository leadSourceRepository,
      CampaignSourceRepository campaignSourceRepository,
      CampaignServiceImpl campaignService,
      FilterConditionRepository filterConditionRepository,
      FilterConditionValueRepository filterConditionValueRepository) {
    this.producerService = producerService;
    this.consumerService = consumerService;
    this.distributeApiRepository = distributeApiRepository;
    this.mongoTemplate = mongoTemplate;
    this.campaignRepository = campaignRepository;
    this.leadSourceRepository = leadSourceRepository;

    this.campaignSourceRepository = campaignSourceRepository;
    this.campaignService = campaignService;
    this.filterConditionRepository = filterConditionRepository;
    this.filterConditionValueRepository = filterConditionValueRepository;
  }

  @Override
  public DataPagingTemplateResponse<TemplateLogRequest> getListData(Long fromDate, Long toDate,
      String search, int page, int limit, String sort, Map<String, String> allParams) {
    DataPagingTemplateResponse<TemplateLogRequest> response = new DataPagingTemplateResponse<>();
    List<TemplateLogRequest> temples = getTemplates(configTemplateRequestLogFileName);
    List<TemplateLogRequest> responseTemplate = getTemplates(configTemplateDownloadLead);
    Map<String, String> mapfiller = new HashMap<>();
    for (Map.Entry<String, String> filter : allParams.entrySet()) {
      Query query = new Query();
      query.addCriteria(Criteria.where(filter.getKey()).exists(true));
      List<BasicDBObject> totalData = this.mongoTemplate
          .find(query, BasicDBObject.class, leadCollection);
      if (!totalData.isEmpty()) {
        mapfiller.put(filter.getKey(), filter.getValue());
      }
    }
    Map<String, String> map = new HashMap<>();
    List<String> statusLeads = new ArrayList<>();
    if (StringUtils.isNotBlank(search)) {
      search = Utils.escapeMetaCharacters(search);
      statusLeads = getLeadStatus(search);
      for (TemplateLogRequest templateLogRequest : temples) {
        if (TEMPLATE_TYPE_KEY.equals(templateLogRequest.getTypeValue())
            && !MongoFieldsConfig.STATUS.equals(templateLogRequest.getKey())) {
          map.put(templateLogRequest.getKey(), search);
        }
      }
    }
    List<BasicDBObject> data = getAllLeads(
        temples, statusLeads, fromDate, toDate, search, limit, page, sort, mapfiller);

    Query queryCount = this.createQuery(fromDate, toDate, map, statusLeads, mapfiller);
    List<BasicDBObject> basicDBObjects = mongoTemplate
        .find(queryCount, BasicDBObject.class, leadCollection);
    Integer count = basicDBObjects.size();
    response.setList(data);
    response.setCurrentPage(page);
    response.setNum(count);
    response.setTotalPage(count % limit == 0 ? count / limit : count / limit + 1);
    response.setTemplate(responseTemplate);
    return response;
  }

  /**
   * @param template
   * @param fromDate
   * @param toDate
   * @param searchKey
   * @param limit
   * @param page
   * @param sort
   * @return
   * @apiNote Lấy danh sách Leads
   */
  public List<BasicDBObject> getAllLeads(List<TemplateLogRequest> template,
      List<String> statusLeads, Long fromDate, Long toDate, String searchKey, Integer limit,
      Integer page, String sort, Map<String, String> mapfillter) {
    Integer total = 0;
    Map<String, String> map = new HashMap<>();
    if (StringUtils.isNotBlank(searchKey)) {
      for (TemplateLogRequest templateLogRequest : template) {
        if (TEMPLATE_TYPE_KEY.equals(templateLogRequest.getTypeValue())
            && !MongoFieldsConfig.STATUS.equals(templateLogRequest.getKey())) {
          map.put(templateLogRequest.getKey(), searchKey);
        }
      }
    }
    Query query = this.createQuery(fromDate, toDate, map, statusLeads, mapfillter);
    logger.info("map query   {}", JsonUtils.toJson(map));
    logger.info("--------------------------------------------------------");
    List<BasicDBObject> totalData = this.mongoTemplate
        .find(query, BasicDBObject.class, leadCollection);
    if (!totalData.isEmpty()) {
      total = totalData.size();
    }

    if (limit != -1) {
      query.limit(limit);
      query.skip(Integer.toUnsignedLong((page - 1) * limit));
    }
    Map<String, String> sortings = SortingUtils.detectSortType(sort);
    if (sortings.isEmpty()) {
      sortings.put(MongoFieldsConfig.CREATED_TIME, "DESC");
    }
    for (Map.Entry<String, String> entry : sortings.entrySet()) {
      if (entry.getValue().equals("ASC")) {
        query.with(Sort.by(Sort.Direction.ASC, entry.getKey()));
      } else {
        query.with(Sort.by(Sort.Direction.DESC, entry.getKey()));
      }
    }
    template.forEach(e -> query.fields().include(e.getKey()));
    List<BasicDBObject> list = this.mongoTemplate
        .find(query, BasicDBObject.class, leadCollection);
    logger.info("getAllLeads output size = {}", list.size());
    List<BasicDBObject> results = new ArrayList<>();
    for (BasicDBObject dbObject : list) {
//      if( dbObject.get(MongoFieldsConfig.SOURCEID) != null){
//        int sourceId= Convertor.convertToInt(dbObject.get(MongoFieldsConfig.SOURCEID) );
//        LeadSource leadSource = leadSourceRepository
//                .findByIdAndIsDeletedFalse(sourceId);
//        if(leadSource!=null){
//          dbObject.append(MongoFieldsConfig.SOURCEID,  leadSource.getName());
//        }
//      }
      if (dbObject.get(MongoFieldsConfig.CAMPAIGNCODE) != null) {
        String campaignCode = Convertor
            .convertToString(dbObject.get(MongoFieldsConfig.CAMPAIGNCODE));
        List<Campaign> campaigns = campaignRepository.findByCodeAndIsDeletedFalse(campaignCode);
        if (campaigns != null && !campaigns.isEmpty()) {
          for (Campaign campaign : campaigns) {
            Project project = campaign.getProject();
            dbObject.append(MongoFieldsConfig.PROJECT_CODE, project.getCode());
          }
        }
      }
      results.add(parseData(dbObject));
    }
    return results;
  }

  public BasicDBObject parseData(BasicDBObject dbObject) {
    BasicDBObject result = new BasicDBObject();
    result.put(MongoFieldsConfig._ID, String.valueOf(dbObject.get(MongoFieldsConfig._ID)));

    if (dbObject.get(MongoFieldsConfig.FULL_NAME) == null) {
      result.append(MongoFieldsConfig.FULL_NAME, dbObject.get("CustomerName"));
    } else {
      result.append(MongoFieldsConfig.FULL_NAME, dbObject.get(MongoFieldsConfig.FULL_NAME));
    }
    if (dbObject.get(MongoFieldsConfig.PHONE) == null) {
      result.append(MongoFieldsConfig.PHONE, dbObject.get("PhoneNumber1"));
    } else {
      result.append(MongoFieldsConfig.PHONE, dbObject.get(MongoFieldsConfig.PHONE));
    }
    result.append(MongoFieldsConfig.PROJECT_CODE, dbObject.get(MongoFieldsConfig.PROJECT_CODE));
    if (dbObject.get(MongoFieldsConfig.CAMPAIGN_CODE) == null) {
      result.append(MongoFieldsConfig.CAMPAIGN_CODE, dbObject.get("CampaignCode"));
    } else {
      result.append(MongoFieldsConfig.CAMPAIGN_CODE, dbObject.get(MongoFieldsConfig.CAMPAIGN_CODE));
    }
    if (dbObject.get(MongoFieldsConfig.SOURCE) == null) {
      result.append(MongoFieldsConfig.SOURCE, dbObject.get("SourceId"));
    } else {
      result.append(MongoFieldsConfig.SOURCE, dbObject.get(MongoFieldsConfig.SOURCE));
    }
    result.append(MongoFieldsConfig.CREATED_TIME, dbObject.get(MongoFieldsConfig.CREATED_TIME));
    result.append(MongoFieldsConfig.STATUS, dbObject.get(MongoFieldsConfig.STATUS));

    return result;
  }

  private Query createQueryStatus(String search) {
    if (StringUtils.isBlank(search)) {
      return null;
    }
    Query query = new Query();
    query.addCriteria(Criteria.where(MongoFieldsConfig.STATUS_NAME).regex(search, "i"));
    return query;
  }

  private Query createQuery(Long fromDate, Long toDate,
      Map<String, String> searchKey, List<String> statusLeads, Map<String, String> mapfillter) {
    Query query = new Query();
    if ((searchKey != null && !searchKey.isEmpty())
        || (statusLeads != null && !statusLeads.isEmpty())) {
      DBObject queryCondition = new BasicDBObject();
      BasicDBList values = new BasicDBList();
      if (searchKey != null && !searchKey.isEmpty()) {
        for (String key : searchKey.keySet()) {
          values.add(new BasicDBObject(key,
              new BasicDBObject("$regex", searchKey.get(key).trim()).append("$options", "i")));
        }
      }
      if (statusLeads != null && !statusLeads.isEmpty()) {
        values.add(
            new BasicDBObject(MongoFieldsConfig.STATUS, new BasicDBObject("$in", statusLeads)));
      }
      queryCondition.put("$or", values);
      query = new BasicQuery(String.valueOf(queryCondition));
    }
    if (fromDate != null && toDate != null) {
      query.addCriteria(Criteria.where(MongoFieldsConfig.CREATED_TIME).gte(new Date(fromDate))
          .lte(new Date(toDate)));
    } else if (fromDate != null) {
      query.addCriteria(Criteria.where(MongoFieldsConfig.CREATED_TIME).gte(new Date(fromDate)));
    } else if (toDate != null) {
      query.addCriteria(Criteria.where(MongoFieldsConfig.CREATED_TIME).lte(new Date(toDate)));
    }
    if (mapfillter != null && !mapfillter.isEmpty()) {
      for (Map.Entry<String, String> filter : mapfillter.entrySet()) {
        query.addCriteria(Criteria.where(filter.getKey()).is(filter.getValue()));
      }
    }
    logger.info("{}", String.valueOf(query));
    return query;
  }

  @Override
  public BasicDBObject getById(String idLead)
      throws ResourceNotFoundException, OperationNotImplementException {
    if (idLead == null) {
      throw new OperationNotImplementException("LEAD ID INVALID ",
          ServiceInfo.getId() + ServiceMessageCode.LEAD_ID_IVALID);
    }
    BasicDBObject basicDBObject = new BasicDBObject();
    Query query = new Query();
    query.addCriteria(Criteria.where("_id").is(idLead));



    List<TemplateLogRequest> listTemplate = getTemplates(configTemplateRequestLogFileName);
    listTemplate.forEach(e -> query.fields().include(e.getKey().toString()));
    List<BasicDBObject> lead = this.mongoTemplate
        .find(query, BasicDBObject.class, this.leadCollection);
    if (lead.isEmpty()) {
      throw new ResourceNotFoundException("LEAD NOT FOUND",
          ServiceInfo.getId() + ServiceMessageCode.LEAD_NOT_FOUND);
    }
    List<BasicDBObject> result = new ArrayList<>();
    for (BasicDBObject item : lead) {
      if (item.get(MongoFieldsConfig.CAMPAIGNCODE) != null) {
        String campaignCode = Convertor
                .convertToString(item.get(MongoFieldsConfig.CAMPAIGNCODE));
        List<Campaign> campaigns = campaignRepository.findByCodeAndIsDeletedFalse(campaignCode);
        if (campaigns != null && !campaigns.isEmpty()) {
          for (Campaign campaign : campaigns) {
            Project project = campaign.getProject();
            item.append(MongoFieldsConfig.PROJECT_CODE, project.getCode());
          }
        }
      }
      result.add(parseData(item));
    }

    basicDBObject.append("list", result);
    basicDBObject.append("template", getTemplates(configTemplateRequestLogFileName));
    return basicDBObject;
  }

  @Override
  public List<BasicDBObject> getListHistory(String idLead) {
    Query query = createQueryHistoryLeadStatus(idLead);
    List<BasicDBObject> listHistory =
        this.mongoTemplate.find(query, BasicDBObject.class, history_lead_status);
    return listHistory;
  }

  private Query createQueryHistoryLeadStatus(String leadId) {
    Query query = new Query();
    if (leadId != null) {
      query
          .addCriteria(Criteria.where("lead_id").regex(leadId));
    }
    logger.info("create query search lead success");
    return query;
  }

  @Override
  public List<BasicDBObject> getListStatus() {
    List<BasicDBObject> listHistory = new ArrayList<>();
    listHistory = this.mongoTemplate
        .findAll(BasicDBObject.class, leadStatusCollection);
    return listHistory;
  }

  @Override
  public List<String> getLeadStatus(String search) {
    Query query = createQueryStatus(search);
    List<String> statusLeads = new ArrayList<>();
    if (query == null) {
      return null;
    }
    List<BasicDBObject> basicDBObjects = this.mongoTemplate
        .find(query, BasicDBObject.class, leadStatusCollection);
    if (!basicDBObjects.isEmpty()) {
      statusLeads = basicDBObjects.stream().map(basicDBObject ->
          Convertor.convertToString(basicDBObject.get(MongoFieldsConfig.STATUS_CODE)))
          .collect(Collectors.toList());
    }
    return statusLeads;
  }

  public BasicDBObject writeLead(BasicDBObject dbObject) throws ParseException {
    BasicDBObject data = new BasicDBObject();

    Map leadMap = new Gson().fromJson(JsonUtils.toJson(dbObject), Map.class);
    if (leadMap.containsKey(MongoFieldsConfig._ID)) {
      data.append(MongoFieldsConfig._ID, leadMap.get(MongoFieldsConfig._ID));
    } else {
      data.append(MongoFieldsConfig._ID, null);
    }
    if (leadMap.containsKey(MongoFieldsConfig.FULL_NAME)) {
      data.append(MongoFieldsConfig.FULL_NAME, leadMap.get(MongoFieldsConfig.FULL_NAME));
    } else {
      data.append(MongoFieldsConfig.FULL_NAME, null);
    }
    if (leadMap.containsKey(MongoFieldsConfig.PHONE)) {
      data.append(MongoFieldsConfig.PHONE, String.valueOf(leadMap.get(MongoFieldsConfig.PHONE)));
    } else {
      data.append(MongoFieldsConfig.PHONE, null);
    }
    if (leadMap.containsKey(MongoFieldsConfig.PROJECT_CODE)) {
      data.append(MongoFieldsConfig.PROJECT_CODE, leadMap.get(MongoFieldsConfig.PROJECT_CODE));
    } else {
      data.append(MongoFieldsConfig.PROJECT_CODE, null);
    }
    if (leadMap.containsKey(MongoFieldsConfig.CAMPAIGN_CODE)) {
      data.append(MongoFieldsConfig.CAMPAIGN_CODE, leadMap.get(MongoFieldsConfig.CAMPAIGN_CODE));
    } else {
      data.append(MongoFieldsConfig.CAMPAIGN_CODE, null);
    }
    if (leadMap.containsKey(MongoFieldsConfig.SOURCE)) {
      data.append(MongoFieldsConfig.SOURCE, leadMap.get(MongoFieldsConfig.SOURCE));
    } else {
      data.append(MongoFieldsConfig.SOURCE, null);
    }

    if (leadMap.containsKey(MongoFieldsConfig.CREATED_TIME)) {
      SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
      SimpleDateFormat sdf2 = new SimpleDateFormat("MMM dd, yyyy, hh:mm:ss a");
      String dateString = String.valueOf(leadMap.get(MongoFieldsConfig.CREATED_TIME));
      Date d = sdf2.parse(dateString);
      String dateShow = sdf.format(d);
      data.append(MongoFieldsConfig.CREATED_TIME, dateShow);
    } else {
      data.append(MongoFieldsConfig.CREATED_TIME, null);
    }
    if (leadMap.containsKey(MongoFieldsConfig.STATUS)) {
      data.append(MongoFieldsConfig.STATUS, leadMap.get(MongoFieldsConfig.STATUS));
    } else {
      data.append(MongoFieldsConfig.STATUS, null);
    }

    return data;
  }

  @Override
  public String exportCsv(List<TemplateLogRequest> template, List<Object> leadIds,
      boolean isBlackList)
      throws BaseException {
    logger.info("exportLead");

    List<TemplateLogRequest> templates = this.getTemplates(configTemplateDownloadLead);
    String[] keyFromTemplates = getKeyFromTemplate(templates);
    String[] nameFromTemplates = getNameFromTemplate(templates);
    List<BasicDBObject> leads = new ArrayList<>();

//     TH isblacklist=false
//    for (Object leadId : leadIds) {
//      String id = String.valueOf(leadId);
//      BasicDBObject dbObject = this.mongoTemplate
//          .findById(id, BasicDBObject.class, this.leadCollection);
//      if (dbObject == null) {
//        throw new ResourceNotFoundException("LEAD NOT FOUND ",
//            ServiceInfo.getId() + ServiceMessageCode.LEAD_NOT_FOUND);
//      }
//      //todo: gen _id from object Id to String
//      BasicDBObject result = new BasicDBObject();
//      result = parseData(dbObject);
//      BasicDBObject leadWrited = writeLead(result);
//      leads.add(leadWrited);
//    }

    Query query = new Query();
    if (String.valueOf(leadIds) == null && String.valueOf(leadIds).isEmpty()) {
      query.addCriteria(Criteria.where("_id").all(leadIds));
    } else {
      if (!isBlackList) {
        query.addCriteria(Criteria.where("_id").in(leadIds));
      } else {
        query.addCriteria(Criteria.where("_id").nin(leadIds));
      }
    }
    leads = this.mongoTemplate.find(query, BasicDBObject.class, this.leadCollection);
    List<BasicDBObject> leadExport = leads.stream()
        .map(it -> {
          BasicDBObject result = parseData(it);
          try {
            return writeLead(result);
          } catch (ParseException ignored) {
            return result;
          }
        })
        .collect(Collectors.toList());

    ICsvMapWriter mapWriter = null;
    String fileName = this.EXPORT_DIR.concat(GenerateUniqueKey.genKey())
        .concat(".csv");
    if (!Files.exists(Paths.get(EXPORT_DIR))) {
      File file = new File(EXPORT_DIR);
      file.mkdir();
    }
    logger.info("Start create csv file: {}", fileName);
    Charset utf8 = StandardCharsets.UTF_8;
    try {
      mapWriter = new CsvMapWriter(new FileWriter(fileName, utf8),
          CsvPreference.STANDARD_PREFERENCE);
      // write the header
      mapWriter.writeHeader(nameFromTemplates);
      // write the customer maps
      for (BasicDBObject lead : leadExport) {
        mapWriter.write(lead, keyFromTemplates);
      }
    } catch (IOException e) {
      logger.error(e.getMessage(), " -- " + e);
      throw new BaseException("Error read/write fife",
          ServiceInfo.getId() + ServiceMessageCode.UNABLE_TO_READ_OR_WRITE_FILE);
    } finally {
      if (mapWriter != null) {
        try {
          mapWriter.close();
        } catch (IOException e) {
          logger.error(e.getMessage(), " " + e);
          throw new BaseException("Error read/write fife",
              ServiceInfo.getId() + ServiceMessageCode.UNABLE_TO_READ_OR_WRITE_FILE);
        }
      }
    }
    logger.info("exportLead created file = {}", fileName);
    return fileName;
  }

  @Override
  public String getFilePattern() {

    String fileName = TEMPLATE_FILE;
    logger.info("Getting pattern file: {}", fileName);
//    String path = fileName.split("/")[0];
//    String name = fileName.split("/")[1];
//    if (!Files.exists(Paths.get(path))) {
//      File file = new File(path);
//      file.mkdir();
//    }
//
//    //todo: move file input to PATTERN_DIR (đừng ai xóa vì sẽ cần đến)
//    try {
//      File file = multipartToFile(multipartFile, name);
//      boolean result = file.renameTo(new File(fileName));
//      if (!result) {
//        logger.error("Failed!");
//      }
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
    return fileName;
  }

  @Override
  public LeadImportResponseDto importLead(MultipartFile multipartFile, Integer userId,
      String data)
      throws ValidationException, IOException, ParseException, InvalidFormatException {
    logger.info("Start import file");
    String CHANNEL = "IMPORT_FILE";
    Date now = new Date();

    BasicDBObject file = new BasicDBObject();
    String FAIL_STATUS = "CONTAIN ERROR LEAD";
    String SUCCESS_STATUS = "NOT CONTAIN ERROR LEAD";
    //todo: kiểm tra định dạng file (regex) -> bỏ
    String filename = multipartFile.getOriginalFilename();
//    String[] _name = filename.split("\\.");
//    logger.info("File name: {}", filename);
//    logger.info("Extension file: {}", _name[_name.length - 1]);
//    if (!_name[_name.length - 1].equals("xlsx") && !_name[_name.length - 1].equals("xls")) {
//      throw new ValidationException("File format is invalid",
//          ServiceInfo.getId() + ServiceMessageCode.FILE_FORMAT_ILLEGAL);
//    }

    //todo: đọc file excel --> get dữ liệu
    List<BasicDBObject> leads = readExcel(multipartFile, filename);

    //todo: fetch dữ liệu, kiểm tra trường thông tin bắt buộc
    int countFail = 0;
    int countSuccess = 0;
    JsonObject infos = new JsonObject();
    try {
      infos = new Gson().fromJson(data, JsonObject.class);
    } catch (Exception e) {
      throw new IllegalStateException("Failed to parse data");
    }
    //todo: check field name for compulsory field
    if (infos != null) {
      Set<String> set = infos.keySet();
      for (BasicDBObject lead : leads) {
        if (lead.size() == 0) {
          break;
        }
        logger.info(lead.toString());
        for (String key : set) {
          if (lead.containsKey((Object) key)) {
            lead.put(key, getContent(String.valueOf(infos.get(key))));
          } else {
            lead.append(key, getContent(String.valueOf(infos.get(key))));
          }
        }
      }
    }
    //todo: get gender & status marriage

    LeadFilterDuplicateDto filterDup = filterDuplidate(leads);
    leads = filterDup.getLead();
    //todo: with each lead contain pair phoneNumber1-campaignCode duplicated -> save to lead_trash
    List<BasicDBObject> duplicate = filterDup.getDuplicate();
    for (BasicDBObject dbObject : duplicate) {
      dbObject.append(MongoFieldsConfig.FAIL_REASON,
          "Trùng lặp số điện thoại trong cùng chiến dịch trong trong file import.");
      this.mongoTemplate.save(dbObject, leadTrashCollection);
      countFail++;
    }
    for (BasicDBObject lead : leads) {
      if (lead.size() == 0) {
        break;
      }
      ValidateLeadDto dto = validateLead(lead);
      lead.append(MongoFieldsConfig.CREATED_TIME, now);
      lead.append(MongoFieldsConfig.CHANNEL, CHANNEL);
      if (dto.isResult()) {
        lead.get(MongoFieldsConfig.CAMPAIGNCODE);
        lead.append(MongoFieldsConfig.STATUS, CREATED_SUCCESS);
        this.mongoTemplate.save(lead, leadCollection);
        countSuccess++;
      } else {
        lead.put(MongoFieldsConfig.FAIL_REASON, dto.getReason());
        this.mongoTemplate.save(lead, leadTrashCollection);
        countFail++;
      }
    }

    if (countFail > 0) {
      file = addCompulsoryFieldAndCheck(file, filename, userId, now, CHANNEL, FAIL_STATUS);
    } else {
      file = addCompulsoryFieldAndCheck(file, filename, userId, now, CHANNEL, SUCCESS_STATUS);
    }
    this.mongoTemplate.save(file, leadFileCollection);
    LeadImportResponseDto result = new LeadImportResponseDto();
    result.setTotal(countFail + countSuccess);
    result.setSuccess(countSuccess);
    result.setFail(countFail);
    return result;
  }

  //todo: function get gender & status marriage
  BasicDBObject getInfoFromNumberInput(BasicDBObject lead) {
    if (lead.containsKey((Object) GENDER)) {
      logger.info("Generate gender");
      switch (String.valueOf(lead.get(GENDER))) {
        case "1":
          lead.put(GENDER, "Nam");
          break;
        case "2":
          lead.put(GENDER, "Nữ");
          break;
        case "3":
          lead.put(GENDER, "Khác");
          break;
        default:
          lead.put(GENDER, "");
      }
    }
    if (lead.containsKey((Object) STATUS_MARRIAGE)) {
      logger.info("Generate Status marriage");
      switch (String.valueOf(lead.get(STATUS_MARRIAGE))) {
        case "1":
          lead.put(STATUS_MARRIAGE, "Độc thân");
          break;
        case "2":
          lead.put(STATUS_MARRIAGE, "Đã kết hôn");
          break;
        case "3":
          lead.put(STATUS_MARRIAGE, "Khác");
          break;
        default:
          lead.put(STATUS_MARRIAGE, "");
      }
    }
    return lead;
  }

  //todo: function check field name for compulsory field
  public static boolean checkFieldName(List<String> header) {
    logger.info("check 3 compulsory field");
    if (!header.contains(CAMPAIGN_CODE)) {
      return false;
    }
//    if (!header.contains(PHONE_NUMBER2)) {
//      return false;
//    }
    if (!header.contains(PHONE_NUMBER1)) {
      return false;
    }
    return true;
  }

  public static List<String> checkFieldValid(List<String> header, List<String> fields) {
    List<String> result = new ArrayList<>(header);
    for (String field : fields) {
      result.remove(field);
    }
    return result;
  }

  //todo: check Campaign exist
  @Override
  public boolean checkCampaignExist(String campaignCode) {
    logger.info("check Campaign exist");
    List<Campaign> campaigns = campaignRepository.findByCodeAndIsDeletedFalse(campaignCode);
    return campaigns.size() != 0;
  }

  //todo: check data type
  String checkDataType(BasicDBObject dbObject) {
    logger.info("Check Data type");
    String result = "";
    String phonePattern = "0123456789+";
    String phoneNumber1 = String.valueOf(dbObject.get(PHONE_NUMBER1));
    String phoneNumber2 = String.valueOf(dbObject.get(PHONE_NUMBER2));
    boolean checkPhoneNumber1 = checkPhone(phoneNumber1, phonePattern);
    boolean checkPhoneNumber2 = checkPhone(phoneNumber2, phonePattern);

    if (!checkPhoneNumber1) {
      result = result.concat("PhoneNumber1 không đúng định dạng ");
    }
    if (phoneNumber2!="null" && !checkPhoneNumber2) {
      //result = result.concat("PhoneNumber2 không đúng định dạng ");
      dbObject.put(PHONE_NUMBER2,"");
    }
    return result;
  }

  boolean checkPhone(String phone, String pattern) {
    for (int i = 0; i < phone.length(); i++) {
      if (!pattern.contains(String.valueOf(phone.charAt(i)))) {
        return false;
      }
    }
    if (phone.indexOf('+', 1) > 1) {
      return false;
    }
    if (!(phone.charAt(0) == '0') && !(phone.charAt(0) == '+')) {
      return false;
    } else {
      if (phone.charAt(0) == '+') {
        if (!phone.startsWith("84", 1)) {
          return false;
        }
        if (phone.length() != 12) {
          return false;
        }
      }
      if (phone.charAt(0) == '0') {
        if (phone.length() != 10) {
          return false;
        }
      }
    }
    return true;
  }

  //todo: check length input
  String checkLength(BasicDBObject lead) {
    String result = "";
    String campaignCode = String.valueOf(lead.get("CampaignCode"));
    String customerName = String.valueOf(lead.get("CustomerName"));
    String phoneNumber1 = String.valueOf(lead.get("PhoneNumber1"));

    if (campaignCode.length() > 255) {
      result = result.concat("CampaignCode không được vượt quá 255 ký tự, ");
    }
    if (customerName.length() > 255) {
      result = result.concat("CustomerName không được vượt quá 255 ký tự, ");
    }
    if (phoneNumber1.length() > 50) {
      result = result.concat("PhoneNumber1 không được vượt quá 50 ký tự, ");
    }
    return result;
  }

  //todo: check duplicate itself
  LeadFilterDuplicateDto filterDuplidate(List<BasicDBObject> leads) {
    LeadFilterDuplicateDto dto = new LeadFilterDuplicateDto();
    List<BasicDBObject> result = new ArrayList<>();
    List<BasicDBObject> duplicate = new ArrayList<>();
    List<String> pair = new ArrayList<>();
    for (BasicDBObject lead : leads) {
      if (lead.size() == 0) {
        break;
      }
      String _pair = String.valueOf(lead.get(PHONE_NUMBER2)).
          concat(String.valueOf(lead.get(CAMPAIGN_CODE)));
      String _pair2 = String.valueOf(lead.get(PHONE_NUMBER2)).
          concat(String.valueOf(lead.get(CAMPAIGN_CODE)));
      if (!pair.contains(_pair) && !pair.contains(_pair2)) {
        pair.add(_pair);
        pair.add(_pair2);
        result.add(lead);
      } else {
        duplicate.add(lead);
      }
    }
    dto.setLead(result);
    dto.setDuplicate(duplicate);
    return dto;
  }

  //todo: validate Lead and save if false
  ValidateLeadDto validateLead(BasicDBObject lead) throws ParseException {
    ValidateLeadDto dto = new ValidateLeadDto();
    dto.setResult(true);
    String reason = "";
    lead = getInfoFromNumberInput(lead);
    String checkCompulsoryImportRecord = checkImportRecord(lead);
    if (!checkCompulsoryImportRecord.equals("")) {
      reason = reason.concat(checkCompulsoryImportRecord).concat(" ");
    }
    String checkValidateLeadImport = checkValidateLeadImport(lead);
    if (!checkValidateLeadImport.equals("")) {
      reason = reason.concat(checkValidateLeadImport.
          concat("đã tồn tại và chưa đủ 90 ngày trong chiến dịch "));
    }

    String checkDataType = checkDataType(lead);
    if (!checkDataType.equals("")) {
      reason = reason.concat(checkDataType).concat(" ");
    }
    String campaignCode = String.valueOf(lead.get("CampaignCode"));
    if (!checkCampaignExist(campaignCode)) {
      reason = reason.concat("CampaignCode không tồn tại trong hệ thống").concat(" ");
    }
    if (!reason.equals("")) {
      dto.setResult(false);
    }
    dto.setReason(reason);
    return dto;
  }


  // todo: add field
  BasicDBObject addCompulsoryFieldAndCheck(BasicDBObject lead, String fileName, Integer userId,
      Date time, String channel, String status) {
    lead.append(MongoFieldsConfig.FILE_NAME, fileName);
    lead.append(MongoFieldsConfig.CREATE_USER_ID, userId);
    lead.append(MongoFieldsConfig.CREATED_TIME, time);
    lead.append(MongoFieldsConfig.CHANNEL, channel);
    lead.append(MongoFieldsConfig.STATUS, status);

    return lead;
  }


  String getContent(String a) {
    return a.substring(1, a.length() - 1);
  }

  public static List<BasicDBObject> readExcel(MultipartFile multipartFile, String filename)
      throws IOException, InvalidFormatException, ValidationException {
    List<BasicDBObject> listLeads = new ArrayList<>();

    // Get file
    InputStream inputStream = new FileInputStream(multipartToFile(multipartFile, filename));

    // Get workbook
    Workbook workbook = getWorkbook(inputStream, filename);

    // Get sheet
    Sheet sheet = workbook.getSheetAt(0);
    //todo: start to get info, header first
    Row header = sheet.getRow(0);

    //todo: get key from header in excel document from header caught
    Iterator<Cell> cellHeader = header.cellIterator();
    List<String> listField = new ArrayList<>();
    while (cellHeader.hasNext()) {
      Cell cell = cellHeader.next();
      Object cellValue = getCellValue(cell);
      listField.add(String.valueOf(cellValue));
    }

    //todo: check field name for 3 compulsory field
    if (!checkFieldName(listField)) {
      throw new ValidationException("One of three compulsory field is not exist.",
          ServiceInfo.getId() + ServiceMessageCode.COMPULSORY_FIELD_NOT_EXIST);
    }
    List<String> compare = checkFieldValid(listField, LIST_IMPORT_FIELDS);
    logger.info("Field errors: {}", compare.size());
    if (compare.size() > 0) {
      logger.info("Field errors: {}", compare.get(0));
      throw new ValidationException("There is one or more field not valid",
          ServiceInfo.getId() + ServiceMessageCode.FILE_FORMAT_ILLEGAL);
    }
    //todo: Get all rows
    Iterator<Row> iterator = sheet.iterator();
    while (iterator.hasNext()) {
      Row nextRow = iterator.next();
      if (nextRow.getRowNum() == 0) {
        continue;
      }
      //todo: Get all cells
      Iterator<Cell> cellIterator = nextRow.cellIterator();

      //todo: Read cells and set value to object
      BasicDBObject dbObject = new BasicDBObject();
      while (cellIterator.hasNext()) {
        //todo: Read cell
        Cell cell = cellIterator.next();
        if (cell.getColumnIndex() >= listField.size()) {
          break;
        }
        Object cellValue = getCellValue(cell);
        if (cellValue == null || cellValue.toString().isEmpty()) {
          continue;
        }
        //todo: Set value for object
        int columnIndex = cell.getColumnIndex();
        dbObject.append(listField.get(columnIndex), String.valueOf(cellValue));
      }
      listLeads.add(dbObject);
    }
    workbook.close();
    inputStream.close();
    return listLeads;
  }

  //todo: convert multipart to file
  public static File multipartToFile(MultipartFile multipart, String fileName)
      throws IllegalStateException, IOException {
    File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + fileName);
    multipart.transferTo(convFile);
    return convFile;
  }

  //todo: Get cell value
  private static Object getCellValue(Cell cell) {
    CellType cellType = cell.getCellTypeEnum();
    Object cellValue = null;
    switch (cellType) {
      case BOOLEAN:
        cellValue = String.valueOf(cell.getBooleanCellValue());
        break;
      case FORMULA:
        Workbook workbook = cell.getSheet().getWorkbook();
        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
        cellValue = String.valueOf(evaluator.evaluate(cell).getNumberValue());
        break;
      case NUMERIC:
        cellValue = NumberToTextConverter.toText(cell.getNumericCellValue());
        break;
      case STRING:
        cellValue = cell.getStringCellValue();
        break;
      case _NONE:
      case BLANK:
      case ERROR:
        break;
      default:
        break;
    }

    return cellValue;
  }

  //todo: Create workbook
  private static Workbook getWorkbook(InputStream inputStream, String excelFilePath)
      throws IOException, InvalidFormatException {
    Workbook workbook = null;

    if (excelFilePath.endsWith("xlsx")) {
      workbook = new XSSFWorkbook();
    } else if (excelFilePath.endsWith("xls")) {
      workbook = new HSSFWorkbook();
    } else {
      throw new IllegalArgumentException("The specified file is not Excel file");
    }

    workbook = WorkbookFactory.create(new PushbackInputStream(inputStream));
    return workbook;
  }

  //todo: check import

  final static String PHONE_NUMBER1 = "PhoneNumber1";
  final static String PHONE_NUMBER2 = "PhoneNumber2";
  final static String CAMPAIGN_CODE = "CampaignCode";
  final static String GENDER = "Gender";
  final static String STATUS_MARRIAGE = "StatusMarriage";


  public String checkImportRecord(BasicDBObject dbObject) {

    String result = "";
    if (!dbObject.containsKey((Object) PHONE_NUMBER1) || dbObject.get(PHONE_NUMBER1) == null) {
      result = result.concat(PHONE_NUMBER1).concat(" không được bỏ trống ");
    }
//    if (!dbObject.containsKey((Object) PHONE_NUMBER2) || dbObject.get(PHONE_NUMBER2) == null) {
//      result = result.concat(PHONE_NUMBER2).concat(" không được bỏ trống ");
//    }
    if (!dbObject.containsKey((Object) CAMPAIGN_CODE) || dbObject.get(CAMPAIGN_CODE) == null) {
      result = result.concat(CAMPAIGN_CODE).concat(" không được bỏ trống ");
    }
    return result;
  }

  //todo: check valid lead
  public String checkValidateLeadImport(BasicDBObject dbObject) throws ParseException {
    //todo: get list<Lead> with the name like name in import data
    String result = "";
    String phone1 = String.valueOf(dbObject.get(PHONE_NUMBER1));
    String phone2 = String.valueOf(dbObject.get(PHONE_NUMBER2));
    String campaignCode = String.valueOf(dbObject.get(CAMPAIGN_CODE));

    long getDaysDiff1 = findLeadExist(phone1, campaignCode);

    if (getDaysDiff1 < 90) {
      result = result.concat(PHONE_NUMBER1).concat(" ");
    }
    if(phone2!="null"){
      long getDaysDiff2 = findLeadExist(phone2, campaignCode);
      if (getDaysDiff2 < 90) {
        result = result.concat(PHONE_NUMBER2).concat(" ");
      }
    }
    return result;
  }

  //todo: find lead.....
  long findLeadExist(String phone, String campaignCode) throws ParseException {
    Query query = new Query();
    query.addCriteria(
        new Criteria().orOperator(Criteria.where(PHONE_NUMBER1).is(phone),
            Criteria.where(PHONE_NUMBER2).is(phone)));
    query.addCriteria(Criteria.where(CAMPAIGN_CODE).is(campaignCode));
    query.with(Sort.by(Sort.Direction.DESC, MongoFieldsConfig.CREATED_TIME));
    List<BasicDBObject> leadExist = this.mongoTemplate.
        find(query, BasicDBObject.class, leadCollection);
    if (leadExist.size() == 0) {
      return 100;
    }
    //todo: from lead exist -> check duplicate
    BasicDBObject exist = leadExist.get(0);
    String lastExistDateString = simpleDateFormat
        .format(exist.get(MongoFieldsConfig.CREATED_TIME));
    Date lastExistDate = simpleDateFormat.parse(lastExistDateString);
    Date now = new Date();
    String nowDateString = simpleDateFormat.format(now);
    Date nowDate = simpleDateFormat.parse(nowDateString);

    long getDiff = nowDate.getTime() - lastExistDate.getTime();
    return TimeUnit.MILLISECONDS.toDays(getDiff);
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


  public List<TemplateLogRequest> getTemplates(String templateName) {
    try {
      String data = Utils.readFileAsString(templateName);
      return Arrays.asList(new Gson().fromJson(data, TemplateLogRequest[].class));
    } catch (Exception e) {
      logger.info("error when read template config, reason: {}", e.getMessage());
      return new ArrayList<>();
    }
  }


  static List<String> _getListImportField() {
    List<String> list = new ArrayList<>();
    list.add(LeadImportConfig.CUSTOMER_NAME);
    list.add(LeadImportConfig.PHONE_NUMBER1);
    list.add(LeadImportConfig.PHONE_NUMBER2);
    list.add(LeadImportConfig.NATIONAL_ID1);
    list.add(LeadImportConfig.NATIONAL_ID2);
    list.add(LeadImportConfig.ID_DATE);
    list.add(LeadImportConfig.ID_PLACE);
    list.add(LeadImportConfig.PROVINCE);
    list.add(LeadImportConfig.DATE_OF_BIRTH);
    list.add(LeadImportConfig.GENDER);
    list.add(LeadImportConfig.CUSTOMER_TYPE);
    list.add(LeadImportConfig.SUB_CUST_TYPE);
    list.add(LeadImportConfig.CONTRACT_ID);
    list.add(LeadImportConfig.PERMANENT_ADDRESS);
    list.add(LeadImportConfig.TEMPORARY_ADDRESS);
    list.add(LeadImportConfig.STATUS_MARRIAGE);
    list.add(LeadImportConfig.JOB);
    list.add(LeadImportConfig.WORKING_PLACE);
    list.add(LeadImportConfig.EMAIL1);
    list.add(LeadImportConfig.EMAIL2);
    list.add(LeadImportConfig.FACEBOOK);
    list.add(LeadImportConfig.SOURCE_ID);
    list.add(LeadImportConfig.PRODUCT);
    list.add(LeadImportConfig.PRODUCT_CODE);
    list.add(LeadImportConfig.INCOME_BAND);
    list.add(LeadImportConfig.INCOME_LEVEL);
    list.add(LeadImportConfig.SCORE_RANGE);
    list.add(LeadImportConfig.PARTNER_CODE);
    list.add(LeadImportConfig.LIMIT);
    list.add(LeadImportConfig.SCORE_CC);
    list.add(LeadImportConfig.BAND_CC);
    list.add(LeadImportConfig.SCORE_UPL);
    list.add(LeadImportConfig.BAND_UPL);
    list.add(LeadImportConfig.CC_LIMIT);
    list.add(LeadImportConfig.UPL_LIMIT);
    list.add(LeadImportConfig.UPL_INTEREST);
    list.add(LeadImportConfig.VALID_DATE);
    list.add(LeadImportConfig.POST_DATE);
    list.add(LeadImportConfig.API_OR_EXCEL);
    list.add(LeadImportConfig.DATA_GROUP);
    list.add(LeadImportConfig.PHONE_NUMBER_WORKING);
    list.add(LeadImportConfig.SPOUSE_PHONE_NUMBER);
    list.add(LeadImportConfig.SPOUSE_NAME);
    list.add(LeadImportConfig.REF_PHONE);
    list.add(LeadImportConfig.DESCRIPTION1);
    list.add(LeadImportConfig.DESCRIPTION2);
    list.add(LeadImportConfig.DESCRIPTION3);
    list.add(LeadImportConfig.DESCRIPTION4);
    list.add(LeadImportConfig.DESCRIPTION5);
    list.add(LeadImportConfig.DESCRIPTION6);
    list.add(LeadImportConfig.CAMPAIGN_CODE);
    list.add(LeadImportConfig.OFFER);
    list.add(LeadImportConfig.TRANSACTION_DATE);
    list.add(LeadImportConfig.BALANCE);
    list.add(LeadImportConfig.AVAILABLE_LIMIT);
    list.add(LeadImportConfig.PARTNER_NAME);

    return list;
  }

  public LeadDistributeResponseDto distribuleLead(List<Object> leadIds,
      Integer distributeId, boolean isBlackList, Integer userId, Map<String, String> allParams,
      Long fromDate, Long toDate, String search)
      throws BaseException, OperationNotImplementException {
    Date start = new Date();
    LeadDistributeResponseDto responsedto = new LeadDistributeResponseDto();
    LeadDistributeApiRequestDto apiRequestDto = new LeadDistributeApiRequestDto();
    DistributeApi distributeApi = distributeApiRepository.findByIdAndIsDeletedFalse(distributeId);
    apiRequestDto.setDistributeId(distributeId);
    List<BasicDBObject> leadFound = getListLead(leadIds, isBlackList, allParams,
        fromDate, toDate, search);
    int countSuccess = 0;
    int count = 0;
    for (BasicDBObject lead : leadFound) {
      BasicDBObject leadStatus = new BasicDBObject();
      leadStatus.append(MongoFieldsConfig.LEAD_ID, lead.get(MongoFieldsConfig._ID));
      leadStatus.append(MongoFieldsConfig.CREATE_USER_ID, userId);
      leadStatus.append(MongoFieldsConfig.CREATED_TIME, String.valueOf(new Date()));
      apiRequestDto.setLead(lead);
      boolean send = producerService.sendMessage(apiRequestDto, topic);
      logger.info("enqueue {}", send);
      HttpResponse<JsonNode> response = consumerService.consume(apiRequestDto, topic);
      Integer responseStatus = response.getStatus();
      String nameDistribute = distributeApi.getName();
      if (responseStatus == 900) {
        String responseBody = String.valueOf(response.getBody());
        logger.info("responBody {}", responseBody);
        //todo: get fail mess
        String failMessage = getFailMessage(responseBody)+"{"+nameDistribute+"}";
        lead.put(MongoFieldsConfig.STATUS, TRANSFER_CRM_ERROR);
        this.mongoTemplate.save(lead, leadCollection);
        leadStatus.append(MongoFieldsConfig.RESPONSE_DATA, responseBody);
        leadStatus.append(MongoFieldsConfig.STATUS, TRANSFER_CRM_ERROR);
        leadStatus.append(MongoFieldsConfig.DISTRIBUTE_ID, distributeId);
        leadStatus.append(MongoFieldsConfig.MESSAGE_RESPONSE, failMessage);
        this.mongoTemplate.save(leadStatus, leadDistributeStatusHistory);
        count++;
        continue;
      }
      String result = String.valueOf(response.getBody().getObject().get("httpCode"));
      String responseBody = String.valueOf(response.getBody());
      String messageResponse = String.valueOf(response.getBody().getObject().get("message"))+"{"+nameDistribute+"}";
      logger.info("message: {}", messageResponse);
      logger.info("http code response: {}", result);
      if (result.startsWith("2")) {
        countSuccess++;
        String resposeId = distributeApi.getResponseId();

        String responseIdGot;
        if (resposeId == null) {
          responseIdGot = null;
        } else {
          JSONObject responseBodyJsonObject = response.getBody().getObject();
          Set<String> keySet = responseBodyJsonObject.keySet();
          BasicDBObject responseDbOject = new BasicDBObject();
          for (String key : keySet) {
            responseDbOject.append(key, responseBodyJsonObject.get(key));
          }
          //todo: get responseId method
          String[] responseIdElement = getResponseIdMethod(resposeId);
          //todo: get responseId
          responseIdGot = getResponseId(responseDbOject, responseIdElement);
        }
        lead.put(MongoFieldsConfig.STATUS, TRANSFER_CRM_SUCCESS);
        this.mongoTemplate.save(lead, leadCollection);
        leadStatus.append(MongoFieldsConfig.RESPONSE_ID, responseIdGot);
        leadStatus.append(MongoFieldsConfig.STATUS, TRANSFER_CRM_SUCCESS);
      } else if (result.startsWith("3") || result.startsWith("4")) {
        logger.info("result {}", TRANSFER_CRM_FAIL);
        lead.put(MongoFieldsConfig.STATUS, TRANSFER_CRM_FAIL);
        this.mongoTemplate.save(lead, leadCollection);
        leadStatus.append(MongoFieldsConfig.STATUS, TRANSFER_CRM_FAIL);
      } else if (result.startsWith("5")) {
        logger.info("result {}", TRANSFER_CRM_ERROR);
        lead.put(MongoFieldsConfig.STATUS, TRANSFER_CRM_ERROR);
        this.mongoTemplate.save(lead, leadCollection);
        leadStatus.append(MongoFieldsConfig.STATUS, TRANSFER_CRM_ERROR);
      }
      leadStatus.put(MongoFieldsConfig.DISTRIBUTE_ID, distributeId);
      leadStatus.append(MongoFieldsConfig.RESPONSE_DATA, responseBody);
      leadStatus.append(MongoFieldsConfig.RESPONSE_STATUS, result);
      leadStatus.append(MongoFieldsConfig.MESSAGE_RESPONSE, messageResponse);
      this.mongoTemplate.save(leadStatus, leadDistributeStatusHistory);
      count++;
    }
    responsedto.setTotal(count);
    responsedto.setSuccess(countSuccess);
    Date finish = new Date();
    long _runTime = finish.getTime() - start.getTime();
    responsedto.setRunTime(_runTime);
    return responsedto;
  }

  //todo: get fail message function
  String getFailMessage(String input) {
    String[] elelemt = input.split("\"");
    return elelemt[5];
  }

  //todo: get responseId method
  public String[] getResponseIdMethod(String responseId) {
    return responseId.split("\\.");
  }

  public String getResponseId(BasicDBObject response, String[] elements) throws BaseException {
    BasicDBObject dbObject = new BasicDBObject(response);
    String result = "";
    for (String element : elements) {
      if (!dbObject.containsKey((Object) element)) {
        logger.info("element: {}", element);
        logger.info("response: {}", dbObject);
        return null;
      }
      result = String.valueOf(dbObject.get(element));
      logger.info(result);
      if (result.contains("\"")) {
        dbObject = BasicDBObject.parse(result);
      }
    }
    return result;
  }


  @Override
  public String updateStatusleadDistribite(String leadId, Integer value)
      throws ResourceNotFoundException {

    BasicDBObject basicDBObject = new BasicDBObject();
    if (!String.valueOf(leadId).isEmpty() && leadId != null) {
       basicDBObject = this.mongoTemplate.
          findById(leadId, BasicDBObject.class, leadCollection);
    }

    if (basicDBObject.isEmpty() && basicDBObject == null) {
      throw new ResourceNotFoundException("lead not found",
          ServiceInfo.getId() + ServiceMessageCode.LEAD_NOT_FOUND);
    }

    if (value >= 1 && value <= 5) {
      logger.info("status lead distribute");
      switch (value) {
        case 1:
          basicDBObject.put(MongoFieldsConfig.STATUS, this.CREATED_SUCCESS);
          break;
        case 2:
          basicDBObject.put(MongoFieldsConfig.STATUS, this.LEAD_ENOUGH_CONDITION);
          break;
        case 3:
          basicDBObject.put(MongoFieldsConfig.STATUS, this.TRANSFER_CRM_SUCCESS);
          break;
        case 4:
          basicDBObject.put(MongoFieldsConfig.STATUS, this.TRANSFER_CRM_FAIL);
          break;
        case 5:
          basicDBObject.put(MongoFieldsConfig.STATUS, this.TRANSFER_CRM_FAIL);
          break;
      }
    }
    this.mongoTemplate.save(basicDBObject, leadCollection);
    return leadId;
  }

  @Override
  public List<LeadDistributeHistoryResponseDto> getLeadDistributeHistory(String leadId)
      throws ParseException {
    Query query = new Query();
    List<LeadDistributeHistoryResponseDto> dtos = new ArrayList<>();
    if (leadId != null && !leadId.isEmpty()) {
      query.addCriteria(Criteria.where("lead_id").is(new ObjectId(leadId)));
    }
    Query query2 = new Query();
    if (leadId != null && !leadId.isEmpty()) {
      query2.addCriteria(Criteria.where("_id").is(new ObjectId(leadId)));
    }
    List<BasicDBObject> basicDBObjects1 = this.mongoTemplate.find(query2,
            BasicDBObject.class, this.leadCollection);
    if (!basicDBObjects1.isEmpty()){
      for (BasicDBObject dbObject : basicDBObjects1) {
        LeadDistributeHistoryResponseDto leadDistributeHistoryResponseDto = new
                LeadDistributeHistoryResponseDto();
        String detail = String.valueOf(dbObject.get(MongoFieldsConfig.MESSAGE_RESPONSE));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        String dateString = String.valueOf(dbObject.get("created_time"));
        Date d = sdf2.parse(dateString);
        String dateShow = sdf.format(d);
        logger.info("creareTime {}", dateString);
        leadDistributeHistoryResponseDto.setStatus("CREATED_SUCCESS");
        leadDistributeHistoryResponseDto.setCreateTime(dateShow);
        leadDistributeHistoryResponseDto.setDetails(detail);
        dtos.add(leadDistributeHistoryResponseDto);
//        }
      }

    }
    List<BasicDBObject> basicDBObjects = this.mongoTemplate.find(query,
        BasicDBObject.class, this.leadDistributeStatusHistory);
    if (basicDBObjects != null && !basicDBObjects.isEmpty()) {
      for (BasicDBObject dbObject : basicDBObjects) {
        LeadDistributeHistoryResponseDto leadDistributeHistoryResponseDto = new
            LeadDistributeHistoryResponseDto();
//        if (dbObject.get("status") != null && dbObject.get("created_time") != null) {
        String status = Convertor.convertToString(dbObject.get("status"));
        String detail = String.valueOf(dbObject.get(MongoFieldsConfig.MESSAGE_RESPONSE));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        String dateString = String.valueOf(dbObject.get("created_time"));
        Date d = sdf2.parse(dateString);
        String dateShow = sdf.format(d);
        logger.info("creareTime {}", dateString);
        leadDistributeHistoryResponseDto.setStatus(status);
        leadDistributeHistoryResponseDto.setCreateTime(dateShow);
        leadDistributeHistoryResponseDto.setDetails(detail);
        dtos.add(leadDistributeHistoryResponseDto);
//        }
      }
    }
    return dtos;
  }

  public void saveUpdateLead(BasicDBObject lead, String status) {
    Query query = new Query();
    query.addCriteria(Criteria.where(MongoFieldsConfig._ID).is(lead.get(MongoFieldsConfig._ID)));
    query.fields().include(MongoFieldsConfig._ID);
    Update update = new Update();
    update.set(MongoFieldsConfig.STATUS, status);
    this.mongoTemplate.updateFirst(query, update, leadDistributeStatusHistory);
  }

  Map<String, String> ckeckParamsFillter(Map<String, String> allParams) {
    Map<String, String> mapfiller = new HashMap<>();
    for (Map.Entry<String, String> filter : allParams.entrySet()) {
      Query queryexists = new Query();
      queryexists.addCriteria(Criteria.where(filter.getKey()).exists(true));
      List<BasicDBObject> totalData = this.mongoTemplate
          .find(queryexists, BasicDBObject.class, leadCollection);
      if (!totalData.isEmpty()) {
        mapfiller.put(filter.getKey(), filter.getValue());
      }
    }
    return mapfiller;
  }

  List<BasicDBObject> getListLead(List<Object> leadIds, boolean isBlackList,
      Map<String, String> allParams, Long fromDate, Long toDate,
      String search) {
    Query query = new Query();
    if (String.valueOf(leadIds) == null && String.valueOf(leadIds).isEmpty()) {
      query.addCriteria(Criteria.where("_id").all(leadIds));
    } else {
      if (!isBlackList) {
        query.addCriteria(Criteria.where("_id").in(leadIds));
      } else {
        List<TemplateLogRequest> temples = getTemplates(configTemplateRequestLogFileName);
        Map<String, String> mapfiller = this.ckeckParamsFillter(allParams);
        Map<String, String> mapsearch = new HashMap<>();
        List<String> statusLeads = new ArrayList<>();
        if (StringUtils.isNotBlank(search)) {
          search = Utils.escapeMetaCharacters(search);
          statusLeads = getLeadStatus(search);
          for (TemplateLogRequest templateLogRequest : temples) {
            if (TEMPLATE_TYPE_KEY.equals(templateLogRequest.getTypeValue())
                && !MongoFieldsConfig.STATUS.equals(templateLogRequest.getKey())) {
              mapsearch.put(templateLogRequest.getKey(), search);
            }
          }
        }
        query = this.createQuery(fromDate, toDate, mapsearch, statusLeads, mapfiller);
        query.addCriteria(Criteria.where("_id").nin(leadIds));
      }
    }
    List<BasicDBObject> leads = this.mongoTemplate
        .find(query, BasicDBObject.class, this.leadCollection);
    return leads;
  }

}
