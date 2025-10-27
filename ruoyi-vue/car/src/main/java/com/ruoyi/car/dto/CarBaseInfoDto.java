package com.ruoyi.car.dto;

import java.math.BigDecimal;
import javax.persistence.Column;
import lombok.Data;

@Data
public class CarBaseInfoDto {

  @Column(name = "id")
  private Long id;

  @Column(name = "custom_model")
  private String customModel; // 型號

  @Column(name = "manufacture_year")
  private Integer manufactureYear; // 出厂年

  @Column(name = "displacement")
  private String displacement; // 排气量

  @Column(name = "fuel_system")
  private String fuelSystem; // 燃料系统

  @Column(name = "transmission")
  private String transmission; // 变速系统

  @Column(name = "color")
  private String color; // 颜色

  @Column(name = "door_count")
  private Integer doorCount; // 车门数

  @Column(name = "passenger_count")
  private Integer passengerCount; // 乘坐人数

  @Column(name = "id_brand")
  private Long idBrand;

  // 以下字段来自关联表
  @Column(name = "brand")
  private String brand; // 品牌

  @Column(name = "sale_price")
  private BigDecimal salePrice; // 售价

  @Column(name = "sale_title")
  private String saleTitle; // 標題

  @Column(name = "sale_description")
  private String saleDescription; // 详情

  @Column(name = "mileage")
  private BigDecimal mileage; // 公里数

  @Column(name = "location_name")
  private String locationName; // 地点

  @Column(name = "garage_id")
  public String garageId;
}
