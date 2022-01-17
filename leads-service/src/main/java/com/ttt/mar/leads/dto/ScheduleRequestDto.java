package com.ttt.mar.leads.dto;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ScheduleRequestDto {

  @ApiModelProperty(value = "kieu phan phoi", required = true, example = "ALWAYS/CALENDER")
  private String type;

  @ApiModelProperty(value = "Ten lich pp", required = true, example = "Ten lich pp")
  @NotBlank(message = "Name not Blank")
  @Size(max = 255, message = "Name longer than 255 characters")
  private String name;

  private boolean isLimit;

  private int dayLimit;

  @ApiModelProperty(value = "CampaignId", required = true)
  @NotNull(message = "CampaignId not Blank")
  private Integer campaignId;

  private String value;

  @NotNull(message = "Start time not blank")
  private long _start;

  public long get_start() {
    return _start;
  }

  public void set_start(long _start) {
    this._start = _start;
  }

  public ScheduleRequestDto() {
  }

  public ScheduleRequestDto(String type, String name, boolean isLimit, int dayLimit,
      Integer campaignId, String value) {
    this.type = type;
    this.name = name;
    this.isLimit = isLimit;
    this.dayLimit = dayLimit;
    this.campaignId = campaignId;
    this.value = value;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public Integer getCampaignId() {
    return campaignId;
  }

  public void setCampaignId(Integer campaignId) {
    this.campaignId = campaignId;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean getIsLimit() {
    return isLimit;
  }

  public void setIsLimit(boolean limit) {
    isLimit = limit;
  }

  public int getDayLimit() {
    return dayLimit;
  }

  public void setDayLimit(int dayLimit) {
    this.dayLimit = dayLimit;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}
