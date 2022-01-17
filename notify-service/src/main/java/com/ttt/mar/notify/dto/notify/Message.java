package com.ttt.mar.notify.dto.notify;

import java.util.List;

public class Message {

  private String message;
  private List<String> invalidEmails;

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public List<String> getInvalidEmails() {
    return invalidEmails;
  }

  public void setInvalidEmails(List<String> invalidEmails) {
    this.invalidEmails = invalidEmails;
  }
}
