package com.ttt.mar.email.dto.emailhistory;

import java.util.List;

public class DataPagingEmailHistoryResponse<T> {
  private List<T> list;
  // Tổng số bản ghi
  private long num;
  // Tổng số trang
  private long totalPage;

  private long currentPage;

  private List<Template> templates;

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

  public List<Template> getTemplates() {
    return templates;
  }

  public void setTemplates(List<Template> templates) {
    this.templates = templates;
  }
}
