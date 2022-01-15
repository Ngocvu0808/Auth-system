package com.ttt.mar.leads.dto;

import com.ttt.mar.leads.entities.FieldType;

public class OfferInfoRequestDto {

//    private Integer id;

  private Integer fieldId;

  private String value;

  public OfferInfoRequestDto() {
  }

//    public Integer getId() {
//        return id;
//    }
//
//    public void setId(Integer id) {
//        this.id = id;
//    }

  public Integer getFieldId() {
    return fieldId;
  }

  public void setFieldId(Integer fieldId) {
    this.fieldId = fieldId;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}
