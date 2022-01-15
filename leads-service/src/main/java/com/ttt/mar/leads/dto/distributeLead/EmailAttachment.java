package com.ttt.mar.leads.dto.distributeLead;

import com.google.gson.annotations.SerializedName;

/**
 * @author nguyen
 * @create_date 13/10/2021
 */
public class EmailAttachment {

  @SerializedName("content")
  private Byte[] content;
  @SerializedName("mine_type")
  private String mineType;
  @SerializedName("name")
  private String name;

  public Byte[] getContent() {
    return content;
  }

  public void setContent(Byte[] content) {
    this.content = content;
  }

  public String getMineType() {
    return mineType;
  }

  public void setMineType(String mineType) {
    this.mineType = mineType;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
