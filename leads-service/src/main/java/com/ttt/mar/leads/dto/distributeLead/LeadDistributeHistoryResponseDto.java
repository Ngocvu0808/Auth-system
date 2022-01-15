package com.ttt.mar.leads.dto.distributeLead;

/**
 * @author Chien Chill
 * @create_date 26/10/2021
 */
public class LeadDistributeHistoryResponseDto {

  private String createTime;

  private String status;

  private String details;

  public String getCreateTime() {
    return createTime;
  }

  public void setCreateTime(String createTime) {
    this.createTime = createTime;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getDetails() {
    return details;
  }

  public void setDetails(String details) {
    this.details = details;
  }
}
