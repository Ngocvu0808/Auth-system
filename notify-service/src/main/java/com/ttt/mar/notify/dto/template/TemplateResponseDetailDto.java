package com.ttt.mar.notify.dto.template;

import java.util.Date;
import java.util.List;

/**
 * @author kietdt
 * @created_date 06/05/2021
 */
public class TemplateResponseDetailDto {

  private Integer id;

  private String name;

  private String code;

  private TemplateSemanticResponse header;

  private TemplateSemanticResponse footer;

  private String to;

  private String cc;

  private String bcc;

  private String subject;

  private String content;

  private String description;

  private List<TemplateAttachmentResponse> attachmentResponses;

  private String userName;

  private Date createdTime;

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

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public TemplateSemanticResponse getHeader() {
    return header;
  }

  public void setHeader(TemplateSemanticResponse header) {
    this.header = header;
  }

  public TemplateSemanticResponse getFooter() {
    return footer;
  }

  public void setFooter(TemplateSemanticResponse footer) {
    this.footer = footer;
  }

  public String getTo() {
    return to;
  }

  public void setTo(String to) {
    this.to = to;
  }

  public String getCc() {
    return cc;
  }

  public void setCc(String cc) {
    this.cc = cc;
  }

  public String getBcc() {
    return bcc;
  }

  public void setBcc(String bcc) {
    this.bcc = bcc;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<TemplateAttachmentResponse> getAttachmentResponses() {
    return attachmentResponses;
  }

  public void setAttachmentResponses(
      List<TemplateAttachmentResponse> attachmentResponses) {
    this.attachmentResponses = attachmentResponses;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public Date getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(Date createdTime) {
    this.createdTime = createdTime;
  }

}
