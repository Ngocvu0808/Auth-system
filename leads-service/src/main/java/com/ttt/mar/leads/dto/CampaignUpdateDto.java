package com.ttt.mar.leads.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ApiModel(description = "Thong tin chinh sua chien dich")
public class CampaignUpdateDto {

  @ApiModelProperty(value = "Ma chien dich", example = "1")
  @JsonIgnore
  private Integer id;

  @ApiModelProperty(value = "Ten chien dich", required = true, example = "Ten chien dich")
  @NotBlank(message = "Name not Blank")
  @Size(max = 255, message = "Name longer than 255 characters")
  private String name;

  @ApiModelProperty(value = "Ma nguon cap lead", required = true, example = "1")
  @NotNull(message = "Lead source not Blank")
  private Integer leadSourceId;

  @ApiModelProperty(value = "Ma kenh phan phoi", required = true)
  @NotNull(message = "Distribute not Blank")
  private Integer distributeId;

  @ApiModelProperty(value = "Ngay bat dau", required = true)
  @NotNull(message = "startDate not null")
  private Date startDate;

  @ApiModelProperty(value = "Ngay ket thuc", required = false)
  private Date endDate;

  @ApiModelProperty(value = "Ghi chu", required = false)
  @Size(max = 1000, message = "description longer than 1000 characters")
  private String description;

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

  public Integer getLeadSourceId() {
    return leadSourceId;
  }

  public void setLeadSourceId(Integer leadSourceId) {
    this.leadSourceId = leadSourceId;
  }

  public Integer getDistributeId() {
    return distributeId;
  }

  public void setDistributeId(Integer distributeId) {
    this.distributeId = distributeId;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
