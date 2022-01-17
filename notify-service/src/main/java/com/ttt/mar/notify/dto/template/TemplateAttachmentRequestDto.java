package com.ttt.mar.notify.dto.template;


/**
 * @author kietdt
 * @created_date 06/05/2021
 */
public class TemplateAttachmentRequestDto {

  private Integer id;

  private String name;

  private String url;

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

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }
}
