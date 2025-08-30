package cc.carce.sale.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

@Data
@Table(name = "car_sales")
public class CarSalesEntity {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    @Column(name = "id_garage")
    private Long idGarage;

    @Column(name = "car_id")
    private Long carId;

    @Column(name = "uid")
    private String uid;

    @Column(name = "list_number")
    private String listNumber;

    @Column(name = "salesperson")
    private String salesperson;

    @Column(name = "publisher")
    private String publisher;

    @Column(name = "sale_price")
    private Integer salePrice;

    @Column(name = "special_price")
    private Integer specialPrice;

    @Column(name = "base_price")
    private Integer basePrice;

    @Column(name = "adjust_price")
    private Integer adjustPrice;

    @Column(name = "authority_price")
    private Integer authorityPrice;

    @Column(name = "deal_price")
    private Integer dealPrice;

    @Column(name = "mileage")
    private Integer mileage;

    @Column(name = "car_location_id")
    private String carLocationId;

    @Column(name = "status")
    private String status;

    @Column(name = "sign_date")
    private Date signDate;

    @Column(name = "delivery_date")
    private Date deliveryDate;

    @Column(name = "repairman_bonus")
    private Integer repairmanBonus;

    @Column(name = "contract_note")
    private String contractNote;

    @Column(name = "sale_title")
    private String saleTitle;

    @Column(name = "sale_description")
    private String saleDescription;

    @Column(name = "c_dt")
    private Date cDt;

    @Column(name = "u_dt")
    private Date uDt;

    @Column(name = "buyer_id")
    private Long buyerId;

    @Column(name = "customer_source_id")
    private Long customerSourceId;

    @Column(name = "viewing_date")
    private Date viewingDate;

    @Column(name = "business_result_id")
    private Long businessResultId;

    @Column(name = "car_show_location")
    private String carShowLocation;

    @Column(name = "car_location_city")
    private String carLocationCity;

    @Column(name = "transaction_date")
    private Date transactionDate;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "is_visible")
    private Integer isVisible;

    @Column(name = "is_ignore_from_tracking")
    private Integer isIgnoreFromTracking;

    @Column(name = "is_warning_active")
    private Integer isWarningActive;

    @Column(name = "first_warning_issued_at")
    private Date firstWarningIssuedAt;

    @Column(name = "second_warning_issued_at")
    private Date secondWarningIssuedAt;
    
    @Column(name = "recommended_value")
    private Long recommendedValue;
}