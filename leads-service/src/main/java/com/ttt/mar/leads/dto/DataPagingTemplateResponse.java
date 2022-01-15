package com.ttt.mar.leads.dto;

import com.ttt.rnd.common.dto.DataPagingResponse;
import java.util.List;

public class DataPagingTemplateResponse<T> extends DataPagingResponse {

  private List<T> template;

  public List<T> getTemplate() {
    return template;
  }

  public void setTemplate(List<T> template) {
    this.template = template;
  }
}
