package com.ttt.mar.leads.dto;

/**
 * @author nguyen
 * @create_date 07/10/2021
 */
public class LeadDistributeResponseDto {

  private int total;
  private int success;
  private long runTime;

  public long getRunTime() {
    return runTime;
  }

  public void setRunTime(long runTime) {
    this.runTime = runTime;
  }

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
}
