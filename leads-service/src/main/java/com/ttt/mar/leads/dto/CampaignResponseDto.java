package com.ttt.mar.leads.dto;

import com.ttt.mar.leads.entities.CampaignStatus;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author NamBN
 * @created_date 08/06/2021
 */
public class CampaignResponseDto {

  private Integer id;

  private String code;

  private String name;

  private ProjectResponse project;

  private LeadSourceResponseDto leadSource;

  private DistributeResponseDto distribute;

  private UserResponseDto createdUser;

  private Date createdTime;

  private CampaignStatus status;

  private Date startDate;

  private Date endDate;

  private String description;

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

  public ProjectResponse getProject() {
    return project;
  }

  public void setProject(ProjectResponse project) {
    this.project = project;
  }

  public LeadSourceResponseDto getLeadSource() {
    return leadSource;
  }

  public void setLeadSource(LeadSourceResponseDto leadSource) {
    this.leadSource = leadSource;
  }

  public DistributeResponseDto getDistribute() {
    return distribute;
  }

  public void setDistribute(DistributeResponseDto distribute) {
    this.distribute = distribute;
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

  public CampaignStatus getStatus() {
    return status;
  }

  public void setStatus(CampaignStatus status) {
    this.status = status;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
