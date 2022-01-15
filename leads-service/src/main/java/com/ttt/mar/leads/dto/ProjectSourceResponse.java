package com.ttt.mar.leads.dto;

import com.ttt.mar.leads.entities.LeadSourceStatus;

/**
 * @author kietdt
 * @created_date 20/04/2021
 */
public class ProjectSourceResponse {

  private Integer id;

  private String nameSource;

  private String utmSource;

  private String source;

  private LeadSourceStatus status;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getNameSource() {
    return nameSource;
  }

  public void setNameSource(String nameSource) {
    this.nameSource = nameSource;
  }

  public String getUtmSource() {
    return utmSource;
  }

  public void setUtmSource(String utmSource) {
    this.utmSource = utmSource;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public LeadSourceStatus getStatus() {
    return status;
  }

  public void setStatus(LeadSourceStatus status) {
    this.status = status;
  }
}
