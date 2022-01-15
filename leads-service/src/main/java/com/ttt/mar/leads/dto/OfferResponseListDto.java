package com.ttt.mar.leads.dto;

import java.io.Serializable;
import java.util.Date;

public class OfferResponseListDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private int id;

  private String name;

  private String code;

  private Date createdTime;

  public int getId() {
    return id;
  }

  public void setId(int id) {
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

  public void setCode(String offerCode) {
    this.code = offerCode;
  }

  public Date getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(Date createdTime) {
    this.createdTime = createdTime;
  }

}
