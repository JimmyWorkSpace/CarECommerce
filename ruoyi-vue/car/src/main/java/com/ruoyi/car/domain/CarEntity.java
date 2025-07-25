package com.ruoyi.car.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "cars")
public class CarEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", columnDefinition = "INT COMMENT '主键ID'")
  private Long id;

  @Column(name = "id_garage", nullable = false, columnDefinition = "INT UNSIGNED COMMENT '店家ID'")
  private Long idGarage;

  @Column(name = "license_plate", nullable = false, columnDefinition = "VARCHAR(20) COMMENT '车牌'")
  private String licensePlate;

  @Column(name = "id_brand", nullable = false, columnDefinition = "INT UNSIGNED COMMENT '品牌ID'")
  private Long idBrand;

  @Column(name = "id_model", nullable = false, columnDefinition = "INT UNSIGNED COMMENT '型号ID'")
  private Long idModel;

  @Column(name = "custom_model", columnDefinition = "VARCHAR(50) COMMENT '自填型号'")
  private String customModel;

  @Column(name = "manufacture_year", columnDefinition = "YEAR COMMENT '出厂年份'")
  private Integer manufactureYear;

  @Column(name = "id_car_type", columnDefinition = "INT COMMENT '汽车种类ID'")
  private Long idCarType;

  @Column(name = "color", columnDefinition = "VARCHAR(20) COMMENT '车色'")
  private String color;

  @Column(name = "passenger_count", columnDefinition = "INT COMMENT '乘客数'")
  private Integer passengerCount;

  @Column(name = "door_count", columnDefinition = "INT COMMENT '车门数'")
  private Integer doorCount;

  @Column(name = "displacement", columnDefinition = "INT COMMENT '排气量'")
  private Integer displacement;

  @Column(name = "transmission", columnDefinition = "VARCHAR(20) COMMENT '变速系统'")
  private String transmission;

  @Column(name = "drivetrain", columnDefinition = "VARCHAR(20) COMMENT '驱动方式'")
  private String drivetrain;

  @Column(name = "fuel_system", columnDefinition = "VARCHAR(20) COMMENT '燃料系统'")
  private String fuelSystem;

  @Column(name = "vin", columnDefinition = "VARCHAR(50) COMMENT '车身号码'")
  private String vin;

  @Column(name = "engine_number", columnDefinition = "VARCHAR(50) COMMENT '引擎号码'")
  private String engineNumber;

  @Column(name = "manufacture_date", columnDefinition = "VARCHAR(20) COMMENT '出厂日期'")
  private String manufactureDate;

  @Column(name = "c_dt", columnDefinition = "TIMESTAMP COMMENT '创建时间'")
  private Date cDt;

  @Column(name = "u_dt", columnDefinition = "TIMESTAMP COMMENT '更新时间'")
  private Date uDt;

  @Column(name = "sell_at", columnDefinition = "TIMESTAMP COMMENT '卖出时间'")
  private Date sellAt;

  @Column(name = "model_year", columnDefinition = "VARCHAR(20) COMMENT '年式'")
  private String modelYear;

  @Column(name = "manufacture_month", columnDefinition = "VARCHAR(2) COMMENT '出厂月份'")
  private String manufactureMonth;

  @Column(name = "registration_date", columnDefinition = "DATE COMMENT '领牌日期'")
  private Date registrationDate;

  @Column(name = "vehicle_type", columnDefinition = "VARCHAR(20) COMMENT '车辆类型'")
  private String vehicleType;

  @Column(name = "is_procurement_transfer", nullable = false, columnDefinition = "TINYINT(1) COMMENT '是否转入采购'")
  private Boolean isProcurementTransfer;
}