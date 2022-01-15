package com.ttt.mar.leads.dto.distributeLead;

import com.mongodb.BasicDBObject;

/**
 * @author nguyen
 * @create_date 13/10/2021
 */
public class LeadDistributeApiRequestDto {

  private BasicDBObject lead;
  private Integer distributeId;

  public BasicDBObject getLead() {
    return lead;
  }

  public void setLead(BasicDBObject lead) {
    this.lead = lead;
  }

  public Integer getDistributeId() {
    return distributeId;
  }

  public void setDistributeId(Integer distributeId) {
    this.distributeId = distributeId;
  }
}
