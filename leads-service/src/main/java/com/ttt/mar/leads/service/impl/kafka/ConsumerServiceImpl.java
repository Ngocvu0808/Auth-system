package com.ttt.mar.leads.service.impl.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.mongodb.BasicDBObject;
import com.ttt.mar.leads.dto.distributeLead.ApiAuthDto;
import com.ttt.mar.leads.dto.distributeLead.CallApiRequestLog;
import com.ttt.mar.leads.dto.distributeLead.DistributeApiDataDto;
import com.ttt.mar.leads.dto.distributeLead.DistributeApiHeaderDto;
import com.ttt.mar.leads.dto.distributeLead.DistributeFieldMappingDto;
import com.ttt.mar.leads.dto.distributeLead.KeyMapResponseDto;
import com.ttt.mar.leads.dto.distributeLead.LeadDistributeApiRequestDto;
import com.ttt.mar.leads.dto.distributeLead.ConfirmInfoRequest;
import com.ttt.mar.leads.dto.distributeLead.LeadDistributeCallRequestDto;
import com.ttt.mar.leads.entities.ApiMethod;
import com.ttt.mar.leads.entities.ApiSecureMethod;
import com.ttt.mar.leads.entities.DistributeApi;
import com.ttt.mar.leads.entities.DistributeApiData;
import com.ttt.mar.leads.entities.DistributeApiHeader;
import com.ttt.mar.leads.entities.DistributeFieldMapping;
import com.ttt.mar.leads.repositories.DistributeApiDataRepository;
import com.ttt.mar.leads.repositories.DistributeApiHeaderRepository;
import com.ttt.mar.leads.repositories.DistributeApiRepository;
import com.ttt.mar.leads.repositories.DistributeFieldMappingRepository;
import com.ttt.mar.leads.service.iface.kafka.consumer.ConsumerService;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.utils.JsonUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import kong.unirest.Headers;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import kong.unirest.UnirestParsingException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class ConsumerServiceImpl implements ConsumerService<LeadDistributeApiRequestDto> {

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
      .getLogger(ConsumerServiceImpl.class);

  private final DistributeApiHeaderRepository distributeApiHeaderRepository;

  private final DistributeApiDataRepository distributeApiDataRepository;

  private final DistributeFieldMappingRepository distributeFieldMappingRepository;

  private final DistributeApiRepository distributeApiRepository;

  @Value("${leadgen-service.kafka.default-topic}")
  public String topic;

  @Value("${spring.kafka.consumer.group-id}")
  public String group_id;


  public ConsumerServiceImpl(
      DistributeApiHeaderRepository distributeApiHeaderRepository,
      DistributeApiDataRepository distributeApiDataRepository,
      DistributeFieldMappingRepository distributeFieldMappingRepository,
      DistributeApiRepository distributeApiRepository) {
    this.distributeApiHeaderRepository = distributeApiHeaderRepository;
    this.distributeApiDataRepository = distributeApiDataRepository;
    this.distributeFieldMappingRepository = distributeFieldMappingRepository;
    this.distributeApiRepository = distributeApiRepository;
  }

  /*
   *todo: get lead enqueued and send
   *  datasend is a DBObject
   **/

  private final String EXCEPTION_KEY = "exception";

  public HttpResponse<JsonNode> consume(@Payload LeadDistributeApiRequestDto dto,
      @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) throws OperationNotImplementException {

    logger.info("Method consume(), payload {}, topic {}", dto, topic);
    //todo: set body to send
    Integer distributeId = dto.getDistributeId();
    BasicDBObject lead = dto.getLead();
    DistributeApi distributeApi = distributeApiRepository.findByIdAndIsDeletedFalse(distributeId);
    List<DistributeApiHeader> headers = distributeApiHeaderRepository.
        findByDistributeIdAndIsDeletedFalse(distributeId);
    List<DistributeApiData> datas = distributeApiDataRepository.
        findByDistributeIdAndIsDeletedFalse(distributeId);
    List<DistributeFieldMapping> fieldMappings = distributeFieldMappingRepository.
        findByDistributeIdAndIsDeletedFalse(distributeId);
//    BasicDBObject data = setRequest(datas, lead);

    BasicDBObject request = getContent(fieldMappings, lead);
    if (request.containsKey((Object) EXCEPTION_KEY)) {
      return genHttpResponse(900, "Require field is not exist", false);
    }
    BasicDBObject dataExist = addDataExist(datas);
    BasicDBObject content = createContent(request, dataExist);
    logger.info("content {}", content.entrySet());
    //todo: send request
    LeadDistributeCallRequestDto apiDto = getDto(distributeApi, headers, datas, fieldMappings);
//  BasicDBObject request = setFieldMapping(fieldMappings, lead);
    HttpResponse<JsonNode> response = callApiTransferConfirmInfo(apiDto, content);
    logger.info("Response: {}", JsonUtils.toJson(response.getBody()));
    return response;
  }

//  public BasicDBObject getRequestBody(BasicDBObject lead, List<DistributeApiData> datas) {
//    BasicDBObject objectTest = new BasicDBObject();
//    for (DistributeApiData data : datas) {
//      objectTest.append(data.getField(), data.getValue());
//    }
  //    String body = getBody(lead);
////    BasicDBObject content = new BasicDBObject();
//    objectTest.append(NAME_VARIABLE, lead.get("CustomerName"));
//    objectTest.append(CAMPAIGN_CODE_VARIABLE, lead.get("CampaignCode"));
//    objectTest.append(PHONE_NUMBER_1, lead.get("PhoneNumber1"));
//    objectTest.append(VariableConfig.KEY_CAMPAIGN_CODE, )
//    return objectTest;
//  }

  public BasicDBObject createContent(BasicDBObject request, BasicDBObject dataExist) {
    BasicDBObject result = new BasicDBObject(dataExist);
    Set<String> keys = request.keySet();
    for (String key : keys) {
      result.append(key, request.get(key));
    }
    return result;
  }

  //todo: add data exist
  public BasicDBObject addDataExist(List<DistributeApiData> datas) {
    BasicDBObject result = new BasicDBObject();
    for (DistributeApiData data : datas) {
      result.append(data.getField(), data.getValue());
    }
    return result;
  }

  public BasicDBObject getContent(List<DistributeFieldMapping> fieldMappings, BasicDBObject lead)
      throws OperationNotImplementException {
    BasicDBObject objectTest = new BasicDBObject();
    List<String> list = new ArrayList<String>();
    for (DistributeFieldMapping fieldMapping : fieldMappings) {
      if (fieldMapping.getRequire()) {
        logger.info("{}", lead.get(fieldMapping.getFieldSource()));
        if (String.valueOf(lead.get(fieldMapping.getFieldSource())).equals("") ||
            String.valueOf(lead.get(fieldMapping.getFieldSource())) == null ||
            String.valueOf(lead.get(fieldMapping.getFieldSource())).equals("null")) {
          BasicDBObject exception = new BasicDBObject();
          exception.append(EXCEPTION_KEY, "exception");
          return exception;
        }
      }
      if(String.valueOf(fieldMapping.getFieldTarget()).equals("receiver") &&
         !String.valueOf(lead.get(fieldMapping.getFieldSource())).equals("")&&
         !String.valueOf(lead.get(fieldMapping.getFieldSource())).equals("null")){
               list.add(String.valueOf(lead.get(fieldMapping.getFieldSource())));

      }
      if(!String.valueOf(fieldMapping.getFieldTarget()).equals("receiver")) {
        objectTest.append("$".concat(fieldMapping.getFieldTarget()).concat("$"),
                lead.get(fieldMapping.getFieldSource()));
      }
    }
    String email= list.stream().collect(Collectors.joining(String.valueOf(",")));
    objectTest.append("$".concat("receiver").concat("$"),email);
    return objectTest;
  }

  //todo: get value
  public String getValue(List<DistributeFieldMapping> fieldMappings, BasicDBObject lead,
      String key) {

    KeyMapResponseDto get = new KeyMapResponseDto();
    get.setKey(key);
    get.setValue(String.valueOf(lead.get(key)));
    KeyMapResponseDto objectMapped = mapField(fieldMappings, get);
    return String.valueOf(objectMapped.getValue());
  }

  public HttpResponse<JsonNode> callApiTransferConfirmInfo(LeadDistributeCallRequestDto dto,
      BasicDBObject object) {
    try {
      logger.info("callApiTransferConfirmInfo()");
      HttpResponse<JsonNode> response = null;
      ApiSecureMethod secureMethod = dto.getSecureMethod();

      logger.info("header {}", convertListHeadToMap(dto.getHeaderDtos()));
      logger.info("body {}", JsonUtils.toJson(object));
      ApiMethod method = dto.getMethod();
      Map<String, Object> mapObject = new HashMap<>();
      Set<String> keys = object.keySet();
      for (String key : keys) {
        mapObject.put(key, object.get(key));
      }
      Set<String> keyObject = object.keySet();
      Map<String, Object> mapObjects = new HashMap<>();
      for (String key : keyObject){
        mapObjects.put(key, object.get(key));
        if(key.equals("$receiver$")){
          mapObjects.put("receiver", object.get("$receiver$"));
        }
      }
      if (method == ApiMethod.POST) {
        if (secureMethod != null && secureMethod.equals(ApiSecureMethod.BASIC)) {
          logger.info("body data send to Email service: {}", JsonUtils.toJson(mapObjects));
          response = Unirest.post(dto.getUrl())
              .basicAuth(dto.getUsername(), dto.getPassword())
              .header("Content-Type", "application/json")
              .headers(convertListHeadToMap(dto.getHeaderDtos()))
              .body(JsonUtils.toJson(mapObjects))
              .asJson();
        } else {
          response = Unirest.post(dto.getUrl())
              .header("Content-Type", "application/json")
              .headers(convertListHeadToMap(dto.getHeaderDtos()))
              .body(JsonUtils.toJson(mapObjects))
              .asJson();
        }
      }
      if (method == ApiMethod.GET) {
        if (secureMethod != null && secureMethod.equals(ApiSecureMethod.BASIC)) {
          response = Unirest.get(dto.getUrl())
              .basicAuth(dto.getUsername(), dto.getPassword())
              .header("Content-Type", "application/json")
              .headers(convertListHeadToMap(dto.getHeaderDtos()))
              .queryString(mapObject)
              .asJson();
        } else {
          response = Unirest.get(dto.getUrl())
              .header("Content-Type", "application/json")
              .headers(convertListHeadToMap(dto.getHeaderDtos()))
              .queryString(mapObject)
              .asJson();
        }
      }
      logger.info("response call api VP: {}", response.getBody());
      return response;
      //todo: if distribute-picked is invalid,
      // reject exception and return response with status = 400
    } catch (UnirestException | NullPointerException e) {
      logger.error("Distribute selected is invalid!");
      HttpResponse response = genHttpResponse(900, "Distribute is invalid", false);
      return response;
    }
  }

  public HttpResponse<JsonNode> genHttpResponse(int httpCode, String message, boolean status) {
    HttpResponse<JsonNode> response = new HttpResponse<>() {

      @Override
      public int getStatus() {
        return httpCode;
      }

      @Override
      public String getStatusText() {
        return null;
      }

      @Override
      public Headers getHeaders() {
        return null;
      }

      @Override
      public JsonNode getBody() {
        return getResponseObject(httpCode, message, status);
      }

      @Override
      public Optional<UnirestParsingException> getParsingError() {
        return Optional.empty();
      }

      @Override
      public Object mapBody(Function function) {
        return null;
      }

      @Override
      public HttpResponse ifSuccess(Consumer consumer) {
        return null;
      }

      @Override
      public HttpResponse ifFailure(Consumer consumer) {
        return null;
      }

      @Override
      public boolean isSuccess() {
        return false;
      }
    };
    return response;
  }

  JsonNode getResponseObject(int httpCode, String message, boolean status) {
    Map map = new HashMap();
    map.put("httpCode", httpCode);
    map.put("message", message);
    map.put("status", status);

    JSONObject obj = new JSONObject(map);
//    JSONArray array =new JSONArray();
    return new JsonNode("[" + obj.toString() + "]");
  }

//  public String convertWithStream(Map map) {
//    String mapAsString = String.valueOf(map.keySet().stream()
//        .map(key -> key + "=" + map.get(key))
//        .collect(Collectors.joining(", ", "{", "}")));
//    return mapAsString;
//  }


  //todo: getDto
  public LeadDistributeCallRequestDto getDto(DistributeApi distributeApi,
      List<DistributeApiHeader> headers, List<DistributeApiData> datas,
      List<DistributeFieldMapping> fieldMappings) {
    LeadDistributeCallRequestDto result = new LeadDistributeCallRequestDto();
    List<DistributeApiDataDto> dataDtos = new ArrayList<>();
    for (DistributeApiData data : datas) {
      DistributeApiDataDto dataDto = new DistributeApiDataDto();
      dataDto.setField(data.getField());
      dataDto.setDataType(data.getDataType());
      dataDto.setValue(data.getValue());
      dataDtos.add(dataDto);
    }
    List<DistributeApiHeaderDto> headerDtos = new ArrayList<>();
    for (DistributeApiHeader header : headers) {
      DistributeApiHeaderDto headerDto = new DistributeApiHeaderDto();
      headerDto.setKey(header.getKey());
      headerDto.setValue(header.getValue());
      headerDtos.add(headerDto);
    }

    List<DistributeFieldMappingDto> fieldMappingDtos = new ArrayList<>();
    for (DistributeFieldMapping fieldMapping : fieldMappings) {
      DistributeFieldMappingDto fieldMappingDto = new DistributeFieldMappingDto();
      fieldMappingDto.setFieldSource(fieldMapping.getFieldSource());
      fieldMappingDto.setFieldTarget(fieldMapping.getFieldTarget());
      fieldMappingDto.setRequire(fieldMapping.getRequire());
      fieldMappingDto.setValidationCode(fieldMapping.getValidationCode());
      fieldMappingDtos.add(fieldMappingDto);
    }

    ApiAuthDto apiAuth = new ApiAuthDto(distributeApi.getUsername(),
        distributeApi.getPassword(), distributeApi.getSecureMethod());
    result.setId(distributeApi.getId());
    result.setName(distributeApi.getName());
    result.setDataDtos(dataDtos);
    result.setMethod(distributeApi.getMethod());
    result.setHeaderDtos(headerDtos);
    result.setFieldMappingDtos(fieldMappingDtos);
    result.setSecureMethod(distributeApi.getSecureMethod());
    result.setApiAuth(apiAuth);
    result.setDescription(distributeApi.getDescription());
    result.setUrl(distributeApi.getUrl());
    result.setStatus(distributeApi.getStatus());
    return result;
  }

  //todo: set bobydata to send function
  public BasicDBObject setRequest(List<DistributeApiData> datas, BasicDBObject lead) {
    BasicDBObject result = new BasicDBObject();
    for (DistributeApiData data : datas) {
      result.append(data.getField(), data.getValue());
    }
    Set sets = lead.keySet();
    for (Object set : sets) {
      String key = String.valueOf(set);
      result.append(key, lead.get(key));
    }
    return result;
  }


  //todo: convert header to map
  public Map<String, String> convertListHeadToMap(List<DistributeApiHeaderDto> headers) {
    Map<String, String> mapHeaders = new HashMap<>();
    if (headers == null || headers.isEmpty()) {
      return mapHeaders;
    }
    for (DistributeApiHeaderDto apiHeaders : headers) {
      mapHeaders.put(apiHeaders.getKey(), apiHeaders.getValue());
    }
    return mapHeaders;
  }

  //todo: function update field
  public BasicDBObject setFieldMapping(List<DistributeFieldMapping> fieldMappings,
      BasicDBObject body) {
    BasicDBObject result = new BasicDBObject(body);
    Map<String, String> pairFields = new HashMap<>();
    for (DistributeFieldMapping fieldMapping : fieldMappings) {
      pairFields.put(fieldMapping.getFieldSource(), fieldMapping.getFieldTarget());
    }
    for (Map.Entry<String, String> pairField : pairFields.entrySet()) {
      if (result.containsKey((Object) pairField.getKey())) {
        result.append(pairField.getValue(), result.get(pairField.getKey()));
        result.remove(pairField.getKey());
      }
    }
    return result;
  }

  public KeyMapResponseDto mapField(List<DistributeFieldMapping> fieldMappings,
      KeyMapResponseDto mapField) {
    KeyMapResponseDto result = new KeyMapResponseDto();
    Map<String, String> pairFields = new HashMap<>();
    for (DistributeFieldMapping fieldMapping : fieldMappings) {
      pairFields.put(fieldMapping.getFieldSource(), fieldMapping.getFieldTarget());
    }
    for (Map.Entry<String, String> pairField : pairFields.entrySet()) {
      if (pairField.getKey().equals(mapField.getKey())) {
        result.setKey(pairField.getValue());
        result.setValue(mapField.getValue());
      } else {
        result.setKey(mapField.getKey());
        result.setValue(mapField.getValue());
      }
    }
    return result;
  }

  //todo: send request function


  /*
  public void listenWithHeaders(
  @Payload String message,
  @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
      System.out.println(
        "Received Message: " + message"
        + "from partition: " + partition);
   */

  public CallApiRequestLog callApiFromEndpoint(ConfirmInfoRequest request,
      LeadDistributeCallRequestDto dto) {
    logger.info("callApiFromEndpoint ()");
    Date requestTime = new Date();
    ApiMethod apiMethod = dto.getMethod();
    CallApiRequestLog callApiRequestLog = new CallApiRequestLog();
    HttpResponse<JsonNode> response;
    if (apiMethod != null) {
      List<DistributeApiHeaderDto> headerDtos = dto.getHeaderDtos();

      ApiSecureMethod secureMethod = dto.getSecureMethod();
      ApiAuthDto authDto = dto.getApiAuth();
      List<DistributeApiDataDto> dataDtos = dto.getDataDtos();
      logger.info("Call Api Method Post");
      if (secureMethod != null && secureMethod.equals(ApiSecureMethod.BASIC)) {
        response = Unirest.post(dto.getUrl())
            .basicAuth(authDto.getUserName(), authDto.getPassWord())
            .header("Content-Type", "application/json")
            .headers(convertListHeadToMap(headerDtos))
//            .header("token", token)
            .body(createJSonObject(request, dataDtos))
            .asJson();
        setCallApiRequestLog(callApiRequestLog, requestTime, new Date(),
            dto.getUrl(), JsonUtils.toJson(request), JsonUtils.toJson(response));
      }
    }
    return callApiRequestLog;
  }

  public void setCallApiRequestLog(CallApiRequestLog callApiRequestLog,
      Date requestTime, Date responseTime, String url, String request, String response) {
    if (callApiRequestLog == null) {
      callApiRequestLog = new CallApiRequestLog();
    }
    callApiRequestLog.setUrl(url);
    callApiRequestLog.setRequestTime(requestTime);
    callApiRequestLog.setResponseTime(responseTime);
    callApiRequestLog.setRequest(request);
    callApiRequestLog.setResponse(response);
  }

  public JSONObject createJSonObject(ConfirmInfoRequest request,
      List<DistributeApiDataDto> dataResponseDtos) {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("receiver", request.getReceiver());
    jsonObject.put("accountCode", request.getAccountCode());
    jsonObject.put("brandName", request.getBrandName());
    jsonObject.put("cc", request.getCc());
    jsonObject.put("body", request.getContent().getBody());
    jsonObject.put("subject", request.getContent().getSubject());
    jsonObject.put("emailAttachmentList", request.getEmailAttachmentList());

    for (DistributeApiDataDto dataResponseDto : dataResponseDtos) {
      jsonObject.put(dataResponseDto.getField(), dataResponseDto.getValue());
    }
    logger.info("createJSonObject: " + jsonObject.toString());
    return jsonObject;
  }


}