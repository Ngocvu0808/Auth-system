package com.ttt.mar.auth.entities.application;

import com.ttt.mar.auth.entities.enums.TokenStatus;
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
 * @created_date 03/06/2020
 */
@Data
@Entity
@Table(name = "auth_access_token")
public class AccessToken extends BaseEntity<Integer> implements Serializable {

  private static final long serialVersionUID = 2239312902856105441L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String token;

  @Column(name = "refresh_token_id", insertable = false, updatable = false)
  private Long refreshTokenId;

  @ManyToOne(cascade = CascadeType.MERGE)
  @OnDelete(action = OnDeleteAction.NO_ACTION)
  @JoinColumn(name = "refresh_token_id", referencedColumnName = "id")
  private RefreshToken refreshToken;

  @Column(name = "expire_time")
  private Long expireTime;

  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  private TokenStatus status;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public Long getRefreshTokenId() {
    return refreshTokenId;
  }

  public void setRefreshTokenId(Long refreshTokenId) {
    this.refreshTokenId = refreshTokenId;
  }

  public RefreshToken getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(RefreshToken refreshToken) {
    this.refreshToken = refreshToken;
  }

  public Long getExpireTime() {
    return expireTime;
  }

  public void setExpireTime(Long expireTime) {
    this.expireTime = expireTime;
  }

  public TokenStatus getStatus() {
    return status;
  }

  public void setStatus(TokenStatus status) {
    this.status = status;
  }
}
