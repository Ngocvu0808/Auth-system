package com.ttt.mar.notify.dto.notify.email;

import javax.validation.constraints.NotNull;

public class EmailAttachment {

  private byte[] content;

  @NotNull(message = "Name not null")
  private String name;

  private String mimeType;

  public byte[] getContent() {
    return content;
  }

  public void setContent(byte[] content) {
    this.content = content;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getMimeType() {
    return mimeType;
  }

  public void setMimeType(String mimeType) {
    this.mimeType = mimeType;
  }
}
