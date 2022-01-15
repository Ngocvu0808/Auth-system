package com.ttt.mar.leads.dto.distributeLead;


import com.ttt.mar.leads.entities.ApiSecureMethod;

/**
 * @author nguyen
 * @create_date 13/10/2021
 */
public class ApiAuthDto {

  private String userName;
  private String passWord;


  private ApiSecureMethod apiSecureMethod;

  public String getUserName() {
    return userName;
  }

  public ApiAuthDto(String userName, String passWord,
      ApiSecureMethod apiSecureMethod) {
    this.userName = userName;
    this.passWord = passWord;
    this.apiSecureMethod = apiSecureMethod;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPassWord() {
    return passWord;
  }

  public void setPassWord(String passWord) {
    this.passWord = passWord;
  }

  public ApiSecureMethod getApiSecureMethod() {
    return apiSecureMethod;
  }

  public void setApiSecureMethod(ApiSecureMethod apiSecureMethod) {
    this.apiSecureMethod = apiSecureMethod;
  }
}
