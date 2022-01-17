package com.ttt.mar.notify.config;

public interface ServiceMessageCode {

  String NOTIFY_HISTORY_NOT_FOUND = "0001";

  String OPERATION_NOT_IMPLEMENT = "0002";

  String USER_NOT_FOUND = "0003";

  String USER_DEACTIVE = "0004";

  String RESOURCE_NOT_FOUND = "0005";

  public static final String VARIABLES_CODE_EXIST = "1001";
  public static final String VARIABLES_NOT_FOUND = "1002";

  public static final String TEMPLATE_SEMANTIC_NOT_FOUND = "2001";

  public static final String ADD_TEMPLATE_ATTACHMENT_INVALID = "3001";
  public static final String TEMPLATE_ATTACHMENT_NOT_FOUND = "3002";
  public static final String TEMPLATE_ATTACHMENT_LIMIT_INVALID = "3003";
  public static final String TEMPLATE_CODE_EXIST = "3004";

  public static final String TEMPLATE_TYPE_NOT_FOUND = "4001";
  public static final String TEMPLATE_TYPE_NOT_BLANK = "4002";

  public static final String TEMPLATE_NOT_FOUND = "5001";
  public static final String EMAIL_INVALID = "5002";
  public static final String PHONE_INVALID = "5003";
  public static final String TO_INVALID = "5004";
}
