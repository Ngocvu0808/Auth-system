package com.ttt.mar.email.dto.emailconfig;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class EmailConfigCreateRequestDto {

  private String accountCode;

  @Size(max = 255, message = "Brand Name longer than 255 characters")
  private String brandName;

  @NotBlank(message = "From not null")
  @Size(max = 255, message = "From longer than 255 characters")
  private String sender;

  @NotNull(message = "publisherId not null")
  private Integer publisherId;

  private String token;

  private String username;

  private String password;

  public String getAccountCode() {
    return accountCode;
  }

  public void setAccountCode(String accountCode) {
    this.accountCode = accountCode;
  }

  public String getBrandName() {
    return brandName;
  }

  public void setBrandName(String brandName) {
    this.brandName = brandName;
  }

  public String getSender() {
    return sender;
  }

  public void setSender(String sender) {
    this.sender = sender;
  }

  public Integer getPublisherId() {
    return publisherId;
  }

  public void setPublisherId(Integer publisherId) {
    this.publisherId = publisherId;
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
}
