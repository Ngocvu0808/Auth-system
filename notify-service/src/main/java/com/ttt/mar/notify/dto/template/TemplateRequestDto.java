package com.ttt.mar.notify.dto.template;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author kietdt
 * @created_date 05/05/2021
 */
public class TemplateRequestDto {

  @JsonIgnore
  private Integer id;

  private String name;

  private String code;

  private String description;

  private String to;

  @Size(max = 5000, message = "CC longer than 5000 characters")
  private String cc;

  @Size(max = 5000, message = "BCC longer than 5000 characters")
  private String bcc;

  @Size(max = 5000, message = "Subject longer than 5000 characters")
  private String subject;

  private String type;

  private List<TemplateAttachmentRequestDto> attachmentRequestDtos;

  private Integer headerId;

  private Integer footerId;

  private String content;

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

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
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

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public List<TemplateAttachmentRequestDto> getAttachmentRequestDtos() {
    return attachmentRequestDtos;
  }

  public void setAttachmentRequestDtos(
      List<TemplateAttachmentRequestDto> attachmentRequestDtos) {
    this.attachmentRequestDtos = attachmentRequestDtos;
  }

  public Integer getHeaderId() {
    return headerId;
  }

  public void setHeaderId(Integer headerId) {
    this.headerId = headerId;
  }

  public Integer getFooterId() {
    return footerId;
  }

  public void setFooterId(Integer footerId) {
    this.footerId = footerId;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public TemplateRequestDto() {
  }
}
