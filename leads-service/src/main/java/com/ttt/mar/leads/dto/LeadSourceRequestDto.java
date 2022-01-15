package com.ttt.mar.leads.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author bontk
 * @created_date 22/03/2021
 */
public class LeadSourceRequestDto {

  @JsonIgnore
  private Integer id;
  @NotBlank(message = "sourceId not blank")
  @Pattern(regexp = "^[A-Za-z0-9._-]+$", message = "Invalid source")
  private String source;
  @NotBlank(message = "name not blank")
  @Size(max = 255)
  private String name;
  @NotBlank(message = "utmSource not blank")
  @Pattern(regexp = "^[A-Za-z0-9._-]+$", message = "Invalid utmSource")
  @Size(max = 255)
  private String utmSource;
  private List<LeadSourceFieldValidationRequestDto> leadSourceFieldValidationRequestDtos;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUtmSource() {
    return utmSource;
  }

  public void setUtmSource(String utmSource) {
    this.utmSource = utmSource;
  }

  public List<LeadSourceFieldValidationRequestDto> getLeadSourceFieldValidationRequestDtos() {
    return leadSourceFieldValidationRequestDtos;
  }

  public void setLeadSourceFieldValidationRequestDtos(
      List<LeadSourceFieldValidationRequestDto> leadSourceFieldValidationRequestDtos) {
    this.leadSourceFieldValidationRequestDtos = leadSourceFieldValidationRequestDtos;
  }
}
