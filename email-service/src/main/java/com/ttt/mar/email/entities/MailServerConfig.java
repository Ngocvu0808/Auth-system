package com.ttt.mar.email.entities;

import com.ttt.rnd.lib.entities.BaseEntity;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * @author bontk
 * @created_date 17/07/2020
 */

@Data
@Entity
@Table(name = "mail_server_config")
public class MailServerConfig extends BaseEntity<Integer> implements Serializable {

  private static final long serialVersionUID = 6893237410073930902L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String code;

  @Column(name = "end_point")
  private String url;

  private String username;

  private String password;

  private String protocol;

  private Integer port;

  @Enumerated(EnumType.STRING)
  private MailServerStatus status;

  @Column(name = "rate_limit")
  private Long rateLimit;

  @Column(name = "rate_type")
  @Enumerated(EnumType.STRING)
  private MailRateType rateType;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
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

  public String getProtocol() {
    return protocol;
  }

  public void setProtocol(String protocol) {
    this.protocol = protocol;
  }

  public Integer getPort() {
    return port;
  }

  public void setPort(Integer port) {
    this.port = port;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public MailServerStatus getStatus() {
    return status;
  }

  public void setStatus(MailServerStatus status) {
    this.status = status;
  }

  public Long getRateLimit() {
    return rateLimit;
  }

  public void setRateLimit(Long rateLimit) {
    this.rateLimit = rateLimit;
  }

  public MailRateType getRateType() {
    return rateType;
  }

  public void setRateType(MailRateType rateType) {
    this.rateType = rateType;
  }

}
