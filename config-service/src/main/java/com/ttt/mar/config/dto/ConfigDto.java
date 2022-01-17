package com.ttt.mar.config.dto;

import java.util.Date;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class ConfigDto {

  private Integer id;
  @NotBlank(message = "Key not null")
  @Size(max = 255, message = "Key longer than 255 characters")
  private String key;
  @NotBlank(message = "Value not null")
  @Size(max = 255, message = "Value longer than 255 characters")
  private String value;
  @Size(max = 4000, message = "Value longer than 4000 characters")
  private String note;
  private Date createdTime;
  private String creatorName;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
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
