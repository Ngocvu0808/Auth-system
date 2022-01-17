package com.ttt.mar.config.dto;

import com.ttt.mar.config.entities.MarFieldConfigType;
import com.ttt.mar.config.entities.MarFieldConfigTypeValue;
import java.util.Date;
import javax.validation.constraints.NotEmpty;

public class FieldConfigDto {

  private Integer id;

  @NotEmpty(message = "Name not null")
  private String name;

  @NotEmpty(message = "Key not null")
  private String key;
  private MarFieldConfigType type;
  private MarFieldConfigTypeValue typeValue;
  private String formatValue;
  private String note;
  private String object;
  private String creatorName;
  private Date createdTime;

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

  public String getFormatValue() {
    return formatValue;
  }

  public void setFormatValue(String formatValue) {
    this.formatValue = formatValue;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  public String getObject() {
    return object;
  }

  public void setObject(String object) {
    this.object = object;
  }

  public MarFieldConfigType getType() {
    return type;
  }

  public void setType(MarFieldConfigType type) {
    this.type = type;
  }

  public MarFieldConfigTypeValue getTypeValue() {
    return typeValue;
  }

  public void setTypeValue(MarFieldConfigTypeValue typeValue) {
    this.typeValue = typeValue;
  }

  public String getCreatorName() {
    return creatorName;
  }

  public void setCreatorName(String creatorName) {
    this.creatorName = creatorName;
  }

  public Date getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(Date createdTime) {
    this.createdTime = createdTime;
  }
}
