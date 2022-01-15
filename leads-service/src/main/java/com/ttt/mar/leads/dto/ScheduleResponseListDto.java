package com.ttt.mar.leads.dto;

import com.ttt.mar.leads.entities.ScheduleStatus;
import java.util.Date;

/**
 * @author nguyen
 * @create_date 06/09/2021
 */
public class ScheduleResponseListDto {

  private int id;
  private String name;
  private String type;
  private Date createTime;
  private ScheduleStatus status;

  public ScheduleResponseListDto() {
  }

  public ScheduleResponseListDto(int id, String name, String type,
      Date createDate, ScheduleStatus status) {
    this.id = id;
    this.name = name;
    this.type = type;
    this.createTime = createDate;
    this.status = status;
  }

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

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public ScheduleStatus getStatus() {
    return status;
  }

  public void setStatus(ScheduleStatus status) {
    this.status = status;
  }
}
