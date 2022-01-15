package com.ttt.mar.leads.dto.distributeLead;

import com.ttt.mar.leads.entities.DataType;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * @author nguyen
 * @create_date 13/10/2021
 */
public class DistributeApiDataDto {

  private String field;

  private String value;

  @Enumerated(EnumType.STRING)
  private DataType dataType;

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
