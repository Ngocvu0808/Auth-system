package com.ttt.mar.leads.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ttt.mar.leads.entities.ProjectStatus;
import java.util.Date;

/**
 * @author bontk
 * @created_date 19/04/2021
 */
public class ProjectResponseDto {

  private Integer id;

  private String name;

  private String code;

  @JsonProperty(value = "partner_code")
  private String partnerCode;

  @JsonProperty(value = "start_date")
  private Date startDate;

  @JsonProperty(value = "end_date")
  private Date endDate;

  private ProjectStatus status;


  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getPartnerCode() {
    return partnerCode;
  }

  public void setPartnerCode(String partnerCode) {
    this.partnerCode = partnerCode;
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

  public ProjectStatus getStatus() {
    return status;
  }

  public void setStatus(ProjectStatus status) {
    this.status = status;
  }
}
