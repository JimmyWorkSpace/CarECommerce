package com.ruoyi.framework.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.formula.functions.T;

@Data
public class R<T> {
  private String code;

  private String msg;

  private T data;

  public R() {
    super();
  }

  public R(T data) {
    super();
    this.data = data;
    this.msg = "success";
    this.code = RespStat.SUCCESS.getValue();
  }

  public R(T data, String msg) {
    super();
    this.data = data;
    this.msg = msg;
    this.code = RespStat.SUCCESS.getValue();
  }

  public R(Throwable e) {
    super();
    this.msg = e.getMessage();
    this.code = RespStat.FAIL.getValue();
  }
}
