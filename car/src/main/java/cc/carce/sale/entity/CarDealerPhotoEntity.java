package cc.carce.sale.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "car_dealer_photos")
public class CarDealerPhotoEntity {
  @Column(name = "id")
  private Long id;

  @Column(name = "id_garage")
  private Long idGarage;

  @Column(name = "dealer_id")
  private Long dealerId;

  @Column(name = "photo_url")
  private String photoUrl;

  @Column(name = "is_cover")
  private Boolean isCover;

  @Column(name = "photo_order")
  private Integer photoOrder;

  @Column(name = "c_dt")
  private Date cDt;
}