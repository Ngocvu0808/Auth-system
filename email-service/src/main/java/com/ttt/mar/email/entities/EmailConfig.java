package com.ttt.mar.email.entities;

import com.ttt.rnd.lib.entities.BaseEntity;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Table(name = "email_config")
@Entity
public class EmailConfig extends BaseEntity<Integer> implements Serializable {

  private static final long serialVersionUID = 6037199396322803494L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @Column(name = "sender")
  private String sender;

  @Column(name = "brand_name")
  private String brandName;

  @Column(name = "account_code", length = 150)
  private String accountCode;

  @Column(name = "username")
  private String username;

  @Column(name = "password")
  private String password;

  @Column(name = "token", length = 2048)
  private String token;

  @Column(name = "publisher_id",insertable = false, updatable = false)
  private Integer publisherId;

  @ManyToOne
  @JoinColumn(name = "publisher_id")
  private EmailPublisher emailPublisher;


  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getSender() {
    return sender;
  }

  public void setSender(String sender) {
    this.sender = sender;
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

  public Integer getPublisherId() {
    return publisherId;
  }

  public void setPublisherId(Integer publisherId) {
    this.publisherId = publisherId;
  }

  public EmailPublisher getEmailPublisher() {
    return emailPublisher;
  }

  public void setEmailPublisher(EmailPublisher emailPublisher) {
    this.emailPublisher = emailPublisher;
  }
}
