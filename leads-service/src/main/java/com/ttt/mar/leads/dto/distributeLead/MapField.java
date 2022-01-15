package com.ttt.mar.leads.dto.distributeLead;

/**
 * @author nguyen
 * @create_date 13/10/2021
 */
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
