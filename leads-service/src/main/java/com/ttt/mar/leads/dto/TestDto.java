package com.ttt.mar.leads.dto;

import com.mongodb.BasicDBObject;

import java.util.List;

public class TestDto {

  private List<BasicDBObject> listData;

  private Integer count;

  public List<BasicDBObject> getListData() {
    return listData;
  }

  public Integer getCount() {
    return count;
  }

  public void setListData(List<BasicDBObject> listData) {
    this.listData = listData;
  }

  public void setCount(Integer count) {
    this.count = count;
  }
}
