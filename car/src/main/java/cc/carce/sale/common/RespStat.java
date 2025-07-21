package cc.carce.sale.common;

import lombok.Data;

public enum RespStat {
  SUCCESS("1"),
  /**
   * 失败标记
   */
  FAIL("0");

  private String value;

  RespStat(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
