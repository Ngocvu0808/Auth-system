package com.ttt.mar.leads.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.Valid;
import java.util.List;

public class OfferUpdateRequestDto {

  @JsonIgnore
  private Integer id;

  private String name;

  @Valid
  private List<LeadConditionRequestDto> offerConditions;

  private List<OfferInfoRequestDto> offerInfos;

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

  public List<LeadConditionRequestDto> getOfferConditions() {
    return offerConditions;
  }

  public void setOfferConditions(
      List<LeadConditionRequestDto> offerConditions) {
    this.offerConditions = offerConditions;
  }

  public List<OfferInfoRequestDto> getOfferInfos() {
    return offerInfos;
  }

  public void setOfferInfos(List<OfferInfoRequestDto> offerInfos) {
    this.offerInfos = offerInfos;
  }
}
