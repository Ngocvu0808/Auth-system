package com.ttt.mar.leads.dto;

import java.util.List;

public class OfferResponseDto {

  private int id;

  private String name;

  private String code;

  private List<OfferConditionResponseDto> leadConditions;

  private List<OfferInfoResponseDto> offerInfos;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public List<OfferInfoResponseDto> getOfferInfos() {
    return offerInfos;
  }

  public void setOfferInfos(List<OfferInfoResponseDto> offerInfos) {
    this.offerInfos = offerInfos;
  }

  public List<OfferConditionResponseDto> getLeadConditions() {
    return leadConditions;
  }

  public void setLeadConditions(
      List<OfferConditionResponseDto> leadConditions) {
    this.leadConditions = leadConditions;
  }
}
