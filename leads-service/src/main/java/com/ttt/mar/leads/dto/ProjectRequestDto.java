package com.ttt.mar.leads.dto;

import io.swagger.annotations.ApiModel;
import java.util.Date;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author kietdt
 * @created_date 20/04/2021
 */
@ApiModel(description = "Info create Project")
public class ProjectRequestDto {

  @NotBlank(message = "Name not blank")
  @Size(max = 255, message = "Name longer than 255 characters")
  private String name;
  @NotBlank(message = "Code not blank")
  @Size(max = 100, message = "Code longer than 100 characters")
  @Pattern(regexp = "^[A-Za-z0-9._-]+$", message = "Invalid code")
  private String code;
  @NotBlank(message = "partnerCode not blank")
  @Size(max = 100, message = "partnerCode longer than 100 characters")
  @Pattern(regexp = "^[A-Za-z0-9._-]+$", message = "Invalid partnerCode")
  private String partnerCode;
  @NotNull(message = "startDate not null")
  private Date startDate;
  @NotNull(message = "endDate not null")
  private Date endDate;
  private String note;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
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
