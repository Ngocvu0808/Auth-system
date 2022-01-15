package com.ttt.mar.leads.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Chien Chill
 * @create_date 06/09/2021
 */
public class ScheduleResponseDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private Integer id;

  private String name;

  private String type;

  private boolean isLimit;

  private int dayLimit;

  private Date startTime;

  private String value;

  private String status;

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

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public boolean getIsLimit() {
    return isLimit;
  }

  public void setIsLimit(boolean isLimit) {
    this.isLimit = isLimit;
  }

  public int getDayLimit() {
    return dayLimit;
  }

  public void setDayLimit(int dayLimit) {
    this.dayLimit = dayLimit;
  }

  public Date getStartTime() {
    return startTime;
  }

  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
