package com.ttt.mar.leads.dto.distributeLead;

public class MapField<K, V> {

  private K source;
  private V target;

  public K getSource() {
    return source;
  }

  public void setSource(K source) {
    this.source = source;
  }

  public V getTarget() {
    return target;
  }

  public void setTarget(V target) {
    this.target = target;
  }
}
