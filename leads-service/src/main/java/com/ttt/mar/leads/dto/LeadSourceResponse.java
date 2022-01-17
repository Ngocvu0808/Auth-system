package com.ttt.mar.leads.dto;

import com.ttt.mar.leads.entities.LeadSourceStatus;
import java.util.Date;

public class LeadSourceResponse {

  private Integer id;
  private String source;
  private String name;
  private String utmSource;
  private Date createdTime;
  private UserResponseDto userResponseDto;
  private LeadSourceStatus status;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUtmSource() {
    return utmSource;
  }

  public void setUtmSource(String utmSource) {
    this.utmSource = utmSource;
  }

  public Date getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(Date createdTime) {
    this.createdTime = createdTime;
  }

  public UserResponseDto getUserResponseDto() {
    return userResponseDto;
  }

  public void setUserResponseDto(UserResponseDto userResponseDto) {
    this.userResponseDto = userResponseDto;
  }

  public LeadSourceStatus getStatus() {
    return status;
  }

  public void setStatus(LeadSourceStatus status) {
    this.status = status;
  }
}
