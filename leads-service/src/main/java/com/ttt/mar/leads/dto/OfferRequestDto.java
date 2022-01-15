package com.ttt.mar.leads.dto;

import java.util.List;
import javax.validation.constraints.NotNull;

public class OfferRequestDto {

  @NotNull(message = "name is not null")
  private String name;

  @NotNull(message = "code is not null")
  private String code;

  private List<LeadConditionRequestDto> leadConditions;

  private List<OfferInfoRequestDto> offerInfos;

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

  public List<LeadConditionRequestDto> getLeadConditions() {
    return leadConditions;
  }

  public void setLeadConditions(List<LeadConditionRequestDto> leadConditions) {
    this.leadConditions = leadConditions;
  }

  public List<OfferInfoRequestDto> getOfferInfos() {
    return offerInfos;
  }

  public void setOfferInfos(List<OfferInfoRequestDto> offerInfos) {
    this.offerInfos = offerInfos;
  }
}
