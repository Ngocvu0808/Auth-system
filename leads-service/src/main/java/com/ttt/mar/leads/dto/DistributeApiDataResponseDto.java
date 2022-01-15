package com.ttt.mar.leads.dto;

import com.ttt.mar.leads.entities.DataType;

/**
 * @author kietdt
 * @created_date 20/04/2021
 */
public class DistributeApiDataResponseDto {

  private Integer id;
  private String field;
  private String value;
  private DataType dataType;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getField() {
    return field;
  }

  public void setField(String field) {
    this.field = field;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public DataType getDataType() {
    return dataType;
  }

  public void setDataType(DataType dataType) {
    this.dataType = dataType;
  }
}
