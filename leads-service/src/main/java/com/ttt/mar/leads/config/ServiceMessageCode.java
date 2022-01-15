package com.ttt.mar.leads.config;

/**
 * @author bontk
 * @created_date 22/03/2021
 */
public interface ServiceMessageCode {

  public static final String SOURCE_NOT_FOUND = "0001";
  public static final String FIELD_ID_NOT_NULL = "0002";
  public static final String USER_NOT_FOUND = "0003";
  public static final String USER_DEACTIVE = "0004";
  public static final String VALIDATION_NOT_FOUND = "0005";
  public static final String ID_NOT_NULL = "0006";
  public static final String LEAD_SOURCE_NOT_FOUND = "0007";
  public static final String INVALID_STATUS_LEAD_SOURCE = "0008";
  public static final String NAME_LEAD_SOURCE_EXIST = "0009";
  public static final String UTM_SOURCE_LEAD_SOURCE_EXIST = "0010";
  public static final String DISTRIBUTE_NOT_FOUND = "0011";
  public static final String INVALID_STATUS_DISTRIBUTE = "0012";
  public static final String DISTRIBUTE_DATA_NOT_FOUND = "0013";
  public static final String DISTRIBUTE_HEADER_NOT_FOUND = "0014";
  public static final String DISTRIBUTE_FIELD_MAPPING_NOT_FOUND = "0015";
  public static final String NAME_DISTRIBUTE_EXIST = "0016";
  public static final String CODE_PROJECT_EXIST = "0017";
  public static final String START_END_DATE_INVALID = "0018";
  public static final String PROJECT_NOT_FOUND = "0019";
  public static final String PROJECT_SOURCE_EXIST_FOR_PROJECT = "0020";
  public static final String PROJECT_DISTRIBUTE_EXIST_FOR_PROJECT = "0021";
  public static final String PROJECT_SOURCE_NOT_FOUND = "0022";
  public static final String PROJECT_DISTRIBUTE_NOT_FOUND = "0023";
  public static final String ROLE_NOT_FOUND = "0024";
  public static final String INVALID_ROLE_TYPE = "0025";
  public static final String PROJECT_USER_EXIST = "0026";
  public static final String INVALID_STATUS_PROJECT = "0027";
  public static final String PROJECT_USER_NOT_FOUND = "0028";
  public static final String PROJECT_USER_PERMISSION_NOT_FOUND = "0029";
  public static final String PROJECT_STATUS_INVALID = "0030";
  public static final String PROJECT_STATUS_IS_DEACTIVE = "0031";
  public static final String PROJECT_TIME_OVER = "0032";

  public static final String CAMPAIGN_CODE_EXIST = "1000";
  public static final String CAMPAIGN_START_DATE_INVALID = "1001";
  public static final String CAMPAIGN_END_DATE_INVALID = "1002";
  public static final String CAMPAIGN_NOT_FOUND = "1003";
  public static final String PROJECT_FOR_CAMPAIGN_INVALID = "1004";
  public static final String CAMPAIGN_STATUS_INVALID = "1005";
  public static final String CAMPAIGN_FILTER_NOT_FOUND = "1006";
  public static final String CAMPAIGN_OFFER_NOT_FOUND = "1007";
  public static final String CAMPAIGN_FILTER_EXIST = "1009";

  public static final String CAMPAIGN_DISTRIBUTE_NOT_EXIST = "2000";
  public static final String DISTRIBUTE_FOR_CAMPAIGN_INVALID = "2001";
  public static final String CAMPAIGN_SOURCE_NOT_EXIST = "2002";
  public static final String SOURCE_FOR_CAMPAIGN_INVALID = "2003";
  public static final String CAMPAIGN_TIME_OVER = "2004";

  public static final String LEAD_ID_IVALID = "3001";
  public static final String LEAD_NOT_FOUND = "3002";
  public static final String UNABLE_TO_READ_OR_WRITE_FILE = "3003";
  public static final String FILE_FORMAT_ILLEGAL = "3004";
  public static final String COMPULSORY_FIELD_NOT_EXIST = "3005";
  public static final String LEAD_EXIST = "3006";
  public static final String INPUT_OVER_LIMIT = "3007";
  public static final String REQUIRE_FIELD_NOT_EXIST = "3008";
  public static final String RESPONSE_ID_NOT_MATCH = "3009";

  public static final String OFFER_NOT_FOUND = "4001";
  public static final String CREATE_OFFER_INVALID = "4001";
  public static final String FIELD_ID_CONDITION_ID_DUPLICATE = "4002";
  public static final String CONDITION_NOT_FOUND = "4003";
  public static final String OFFER_BY_CODE_EXIST = "4004";
  public static final String FIELD_CODE_OFFER_INFO_EXIST = "4005";
  public static final String CAMPAIGN_OFFER_EXIST = "4006";

  public static final String FILTER_NOT_FOUND = "5001";
  public static final String FILTER_BY_CODE_EXIST = "5002";

  public static final String VALUE_NOT_LEGAL = "6001";
  public static final String SCHEDULE_EXIST = "6002";
  public static final String SCHEDULE_NOT_FOUND = "6003";
  public static final String CAMPAIGN_ID_NOT_MATCH = "6004";
  public static final String SCHEDULE_STATUS_INVALID = "6005";
  public static final String SCHEDULE_STATUS_IS_DEACTIVE = "6006";
  public static final String SCHEDULE_STATUS_IS_ACTIVE = "6007";
  public static final String SCHEDULE_VALUE_NOT_FOUND = "6008";
  public static final String SCHEDULE_STATUS_NOT_CHANGE = "6009";
  public static final String NAME_ILLEGAL = "6010";
  public static final String BAD_START_TIME_INPUT = "6011";


}
