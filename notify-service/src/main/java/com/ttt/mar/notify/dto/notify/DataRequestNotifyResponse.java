package com.ttt.mar.notify.dto.notify;

import com.ttt.rnd.lib.dto.logrequest.TemplateLogRequest;
import java.util.List;

public class DataRequestNotifyResponse<T> {

  private List<T> list;
  private long num;
  private long totalPage;
  private long currentPage;
  private List<TemplateLogRequest> template;

  public List<T> getList() {
    return list;
  }

  public void setList(List<T> list) {
    this.list = list;
  }

  public long getNum() {
    return num;
  }

  public void setNum(long num) {
    this.num = num;
  }

  public long getTotalPage() {
    return totalPage;
  }

  public void setTotalPage(long totalPage) {
    this.totalPage = totalPage;
  }

  public long getCurrentPage() {
    return currentPage;
  }

  public void setCurrentPage(long currentPage) {
    this.currentPage = currentPage;
  }

  public List<TemplateLogRequest> getTemplate() {
    return template;
  }

  public void setTemplate(List<TemplateLogRequest> template) {
    this.template = template;
  }
}
