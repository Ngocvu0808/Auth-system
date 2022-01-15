package com.ttt.mar.email.dto.emailconfig;

import com.ttt.mar.email.dto.emailpublisher.EmailPublisherPublicResponseDto;

public class EmailConfigDetailResponseDto {

  private Integer id;

  private String brandName;

  private String sender;

  private String accountCode;

  private String username;

  private String password;

  private String token;

  private EmailPublisherPublicResponseDto emailPublisherPublicResponseDto;

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

  public String getSender() {
    return sender;
  }

  public void setSender(String sender) {
    this.sender = sender;
  }

  public String getAccountCode() {
    return accountCode;
  }

  public void setAccountCode(String accountCode) {
    this.accountCode = accountCode;
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

  public EmailPublisherPublicResponseDto getEmailPublisherPublicResponseDto() {
    return emailPublisherPublicResponseDto;
  }

  public void setEmailPublisherPublicResponseDto(
      EmailPublisherPublicResponseDto emailPublisherPublicResponseDto) {
    this.emailPublisherPublicResponseDto = emailPublisherPublicResponseDto;
  }
}
