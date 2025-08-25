package com.ruoyi.framework.dto;

import lombok.Data;

public enum RespStat {
  SUCCESS("1"),
  /**
 * 失敗標記
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
