package com.ttt.mar.leads.dto;

import com.mongodb.BasicDBObject;
import java.util.List;

/**
 * @author nguyen
 * @create_date 29/09/2021
 */
public class LeadFilterDuplicateDto {

  private List<BasicDBObject> lead;
  private List<BasicDBObject> duplicate;

  public List<BasicDBObject> getLead() {
    return lead;
  }

  public void setLead(List<BasicDBObject> lead) {
    this.lead = lead;
  }

  public List<BasicDBObject> getDuplicate() {
    return duplicate;
  }

  public void setDuplicate(List<BasicDBObject> duplicate) {
    this.duplicate = duplicate;
  }
}
