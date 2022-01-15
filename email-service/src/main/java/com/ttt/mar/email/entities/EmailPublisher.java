package com.ttt.mar.email.entities;

import com.ttt.rnd.lib.entities.BaseEntity;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "email_publisher")
public class EmailPublisher extends BaseEntity<Integer> implements Serializable {

  private static final long serialVersionUID = -7203743557034778103L;

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "name")
  private String name;

  @Column(name = "code", length = 100)
  private String code;

  @Column(name = "end_point", length = 2048)
  private String endPoint;

  @Column(name = "host")
  private String host;

  @Column(name = "port")
  private Integer port;

  @Column(name = "protocol")
  @Enumerated(EnumType.STRING)
  private EmailProtocol protocol;

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

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getEndPoint() {
    return endPoint;
  }

  public void setEndPoint(String endPoint) {
    this.endPoint = endPoint;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public Integer getPort() {
    return port;
  }

  public void setPort(Integer port) {
    this.port = port;
  }

  public EmailProtocol getProtocol() {
    return protocol;
  }

  public void setProtocol(EmailProtocol protocol) {
    this.protocol = protocol;
  }
}
