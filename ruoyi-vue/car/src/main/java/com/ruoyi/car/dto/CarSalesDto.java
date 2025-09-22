package com.ruoyi.car.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

@Data
public class CarSalesDto {
    private Long id;

    private Long idGarage;

    private Long carId;

    private String uid;

    private String listNumber;

    private String salesperson;

    private String publisher;

    private Integer salePrice;

    private Integer specialPrice;

    private Integer basePrice;

    private Integer adjustPrice;

    private Integer authorityPrice;

    private Integer dealPrice;

    private Integer mileage;

    private String carLocationId;

    private String status;

    private Date signDate;

    private Date deliveryDate;

    private Integer repairmanBonus;

    private String contractNote;

    private String saleTitle;
    private String saleTitleShow;

    private String saleDescription;

    private Date cDt;

    private Date uDt;

    private Long buyerId;

    private Long customerSourceId;

    private Date viewingDate;

    private Long businessResultId;

    private String carShowLocation;

    private String carLocationCity;

    private Date transactionDate;

    private Date createDate;

    private Integer isVisible;

    private Integer isIgnoreFromTracking;

    private Integer isWarningActive;

    private Date firstWarningIssuedAt;

    private Date secondWarningIssuedAt;
    
    private Long recommendedValue;
}
