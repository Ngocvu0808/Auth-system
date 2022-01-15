package com.ttt.mar.leads.dto;

/**
 * @author nguyen
 * @create_date 23/09/2021
 */
public class LeadImportResponseDto {

  private int total;

  private int success;

  private int fail;

  public int getTotal() {
    return total;
  }

  public void setTotal(int total) {
    this.total = total;
  }

  public int getSuccess() {
    return success;
  }

  public void setSuccess(int success) {
    this.success = success;
  }

  public int getFail() {
    return fail;
  }

  public void setFail(int fail) {
    this.fail = fail;
  }
}
