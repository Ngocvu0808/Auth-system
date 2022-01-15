package com.ttt.mar.sms.entities;

import com.ttt.rnd.lib.entities.BaseEntity;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "sms_config")
public class SmsConfig extends BaseEntity<Integer> implements Serializable {

  private static final long serialVersionUID = -2454677310920809672L;

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "brand_name")
  private String brandName;

  @Column(name = "account_code", length = 150)
  private String accountCode;

  @Column(name = "phone_number")
  private String phoneNumber;

  @Column(name = "publisher_id", insertable = false, updatable = false)
  private Integer publisherId;

  @ManyToOne
  @JoinColumn(name = "publisher_id")
  private SmsPublisher smsPublisher;

  @Column(name = "username")
  private String username;

  @Column(name = "password")
  private String password;

  @Column(name = "token", length = 150)
  private String token;

  @OneToMany(mappedBy = "smsConfig")
  private List<SmsHistory> smsHistories;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getBrandName() {
    return brandName;
  }

  public void setBrandName(String brandName) {
    this.brandName = brandName;
  }

  public String getAccountCode() {
    return accountCode;
  }

  public void setAccountCode(String accountCode) {
    this.accountCode = accountCode;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public Integer getPublisherId() {
    return publisherId;
  }

  public void setPublisherId(Integer publisherId) {
    this.publisherId = publisherId;
  }

  public SmsPublisher getSmsPublisher() {
    return smsPublisher;
  }

  public void setSmsPublisher(SmsPublisher smsPublisher) {
    this.smsPublisher = smsPublisher;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public List<SmsHistory> getSmsHistories() {
    return smsHistories;
  }

  public void setSmsHistories(List<SmsHistory> smsHistories) {
    this.smsHistories = smsHistories;
  }
}
