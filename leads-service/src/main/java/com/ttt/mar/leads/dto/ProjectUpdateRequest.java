package com.ttt.mar.leads.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class ProjectUpdateRequest {

  @JsonIgnore
  private Integer id;

  @NotBlank(message = "Name not blank")
  @Size(max = 255, message = "Name longer than 255 characters")
  private String name;

  @NotBlank(message = "partnerCode not blank")
  @Size(max = 100, message = "partnerCode longer than 100 characters")
  @Pattern(regexp = "^[A-Za-z0-9._-]+$", message = "Invalid partnerCode")
  private String partnerCode;

  @NotNull(message = "startDate not null")
  private Date startDate;

  @NotNull(message = "endDate not null")
  private Date endDate;

  private String note;

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

  public String getPartnerCode() {
    return partnerCode;
  }

  public void setPartnerCode(String partnerCode) {
    this.partnerCode = partnerCode;
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

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }
}
