package com.ttt.mar.leads.dto.distributeLead;

import com.google.gson.annotations.SerializedName;
import com.ttt.mar.leads.entities.FieldType;

/**
 * @author nguyen
 * @create_date 13/10/2021
 */
public class EmailContentRequestDto {

  @SerializedName("body")
  private String body;
  @SerializedName("subject")
  private String subject;
  @SerializedName("type")
  private FieldType type;

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public FieldType getType() {
    return type;
  }

  public void setType(FieldType type) {
    this.type = type;
  }
}
