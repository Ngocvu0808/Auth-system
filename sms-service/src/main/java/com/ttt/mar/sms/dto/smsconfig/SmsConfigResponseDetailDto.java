package com.ttt.mar.sms.dto.smsconfig;

import com.ttt.mar.sms.dto.smpublisher.SmsPublisherCustomResponseDto;

public class SmsConfigResponseDetailDto {

  private Integer id;

  private String brandName;

  private String phoneNumber;

  private String accountCode;

  private SmsPublisherCustomResponseDto publisher;

  private String token;

  private String username;

  private String password;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getBrandName() {
    return brandName;
  }

  public void setBrandName(String brandName) {
    this.brandName = brandName;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public SmsPublisherCustomResponseDto getPublisher() {
    return publisher;
  }

  public void setPublisher(SmsPublisherCustomResponseDto publisher) {
    this.publisher = publisher;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getAccountCode() {
    return accountCode;
  }

  public void setAccountCode(String accountCode) {
    this.accountCode = accountCode;
  }
}
