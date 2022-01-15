package com.ttt.mar.leads.dto;

import com.ttt.mar.leads.entities.CampaignStatus;
import java.util.Date;

public class CampaignResponse {

  private Integer id;

  private String code;

  private String name;

  private CampaignStatus status;

  private String projectCode;

  private String leadSourceName;

  private String distributeName;

  private UserResponseDto createdUser;

  private Date createdTime;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public CampaignStatus getStatus() {
    return status;
  }

  public void setStatus(CampaignStatus status) {
    this.status = status;
  }

  public String getProjectCode() {
    return projectCode;
  }

  public void setProjectCode(String projectCode) {
    this.projectCode = projectCode;
  }

  public String getLeadSourceName() {
    return leadSourceName;
  }

  public void setLeadSourceName(String leadSourceName) {
    this.leadSourceName = leadSourceName;
  }

  public String getDistributeName() {
    return distributeName;
  }

  public void setDistributeName(String distributeName) {
    this.distributeName = distributeName;
  }

  public UserResponseDto getCreatedUser() {
    return createdUser;
  }

  public void setCreatedUser(UserResponseDto createdUser) {
    this.createdUser = createdUser;
  }

  public Date getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(Date createdTime) {
    this.createdTime = createdTime;
  }
}
