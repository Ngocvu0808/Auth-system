package com.ttt.mar.sms.dto.smsconfig;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class SmsConfigDtoRequest {


  private Integer id;

  @Size(max = 255, message = "brandName longer than 255 characters")
  private String brandName;

  @NotNull(message = "code not null")
  @Pattern(regexp = "^[A-Za-z0-9_.+-@%]+$", message = "code has spec character")
  @Size(max = 150, message = "accountCode longer than 150 characters")
  private String accountCode;

  @NotNull(message = "phoneNumber not null")
  @Size(max = 11, message = "phoneNumber longer than 11 characters")
  private String phoneNumber;

  @Size(max = 255, message = "username longer than 255 characters")
  private String username;

  @Size(max = 255, message = "password longer than 255 characters")
  private String password;

  @Size(max = 100, message = "token longer than 100 characters")
  private String token;

  private Integer publisherId;

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

  public String getAccountCode() {
    return accountCode;
  }

  public void setAccountCode(String accountCode) {
    this.accountCode = accountCode;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
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

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public Integer getPublisherId() {
    return publisherId;
  }

  public void setPublisherId(Integer publisherId) {
    this.publisherId = publisherId;
  }
}
