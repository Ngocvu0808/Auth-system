package com.ttt.mar.config.entities;

import com.ttt.rnd.lib.entities.BaseEntity;
import com.ttt.rnd.lib.entities.User;
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
@Table(name = "mar_field_config")
public class MarFieldConfig extends BaseEntity<Integer> implements Serializable {

  private static final long serialVersionUID = -1465666123381907662L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "`key`")
  private String key;

  @Column(columnDefinition = "TEXT")
  private String name;

  @Enumerated(EnumType.STRING)
  @Column(name = "type")
  private MarFieldConfigType type;

  @Column(name = "type_value")
  @Enumerated(EnumType.STRING)
  private MarFieldConfigTypeValue typeValue;

  @Column(name = "format_value")
  private String formatValue;

  @Column(columnDefinition = "TEXT")
  private String note;

  private String object;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public MarFieldConfigType getType() {
    return type;
  }

  public void setType(MarFieldConfigType type) {
    this.type = type;
  }

  public MarFieldConfigTypeValue getTypeValue() {
    return typeValue;
  }

  public void setTypeValue(MarFieldConfigTypeValue typeValue) {
    this.typeValue = typeValue;
  }

  public String getFormatValue() {
    return formatValue;
  }

  public void setFormatValue(String formatValue) {
    this.formatValue = formatValue;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  public String getObject() {
    return object;
  }

  public void setObject(String object) {
    this.object = object;
  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(Boolean deleted) {
    isDeleted = deleted;
  }

  public User getCreatorUser() {
    return creatorUser;
  }

  public void setCreatorUser(User creatorUser) {
    this.creatorUser = creatorUser;
  }

  public User getUpdaterUser() {
    return updaterUser;
  }

  public void setUpdaterUser(User updaterUser) {
    this.updaterUser = updaterUser;
  }
}
