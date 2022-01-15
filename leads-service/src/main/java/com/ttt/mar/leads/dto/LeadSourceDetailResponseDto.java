package com.ttt.mar.leads.dto;

import com.ttt.mar.leads.entities.LeadSourceStatus;
import io.swagger.annotations.ApiModel;
import java.util.Date;
import java.util.List;

/**
 * @author kietdt
 * @created_date 20/04/2021
 */
@ApiModel(description = "Thong tin chi tiet leads")
public class LeadSourceDetailResponseDto {

  private Integer id;
  private String source;
  private String name;
  private String utmSource;
  private List<LeadSourceFieldValidationResponseDto> leadSourceFieldValidations;
  private Date createdTime;
  private String creatorName;
  private Date modifiedTime;
  private String updaterName;
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

  public List<LeadSourceFieldValidationResponseDto> getLeadSourceFieldValidations() {
    return leadSourceFieldValidations;
  }

  public void setLeadSourceFieldValidations(
      List<LeadSourceFieldValidationResponseDto> leadSourceFieldValidations) {
    this.leadSourceFieldValidations = leadSourceFieldValidations;
  }

  public Date getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(Date createdTime) {
    this.createdTime = createdTime;
  }

  public String getCreatorName() {
    return creatorName;
  }

  public void setCreatorName(String creatorName) {
    this.creatorName = creatorName;
  }

  public Date getModifiedTime() {
    return modifiedTime;
  }

  public void setModifiedTime(Date modifiedTime) {
    this.modifiedTime = modifiedTime;
  }

  public String getUpdaterName() {
    return updaterName;
  }

  public void setUpdaterName(String updaterName) {
    this.updaterName = updaterName;
  }

  public LeadSourceStatus getStatus() {
    return status;
  }

  public void setStatus(LeadSourceStatus status) {
    this.status = status;
  }
}
