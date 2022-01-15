package com.ttt.mar.auth.entities.application;

import com.ttt.mar.auth.entities.enums.ClientApiKeyStatus;
import com.ttt.rnd.lib.entities.BaseEntity;
import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * @author bontk
 * @created_date 17/09/2020
 */
@Table(name = "auth_client_api_key")
@Entity
@Data
public class ClientApiKey extends BaseEntity<Integer> implements Serializable {

  private static final long serialVersionUID = -3247116790931476746L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "api_key")
  private String apiKey;

  @Column(name = "client_id", insertable = false, updatable = false)
  private Integer clientId;

  @ManyToOne(cascade = CascadeType.MERGE)
  @OnDelete(action = OnDeleteAction.NO_ACTION)
  @JoinColumn(name = "client_id", referencedColumnName = "id")
  private Client client;

  @Enumerated(EnumType.STRING)
  private ClientApiKeyStatus status;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getApiKey() {
    return apiKey;
  }

  public void setApiKey(String apiKey) {
    this.apiKey = apiKey;
  }

  public Integer getClientId() {
    return clientId;
  }

  public void setClientId(Integer clientId) {
    this.clientId = clientId;
  }

  public Client getClient() {
    return client;
  }

  public void setClient(Client client) {
    this.client = client;
  }

  public ClientApiKeyStatus getStatus() {
    return status;
  }

  public void setStatus(ClientApiKeyStatus status) {
    this.status = status;
  }
}
