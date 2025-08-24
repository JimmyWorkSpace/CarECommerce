package com.ruoyi.car.domain.dto;

import javax.persistence.Column;
import lombok.Data;

@Data
public class CarEquipment {

  @Column(name = "type")
  private Integer type;

  @Column(name = "item_name")
  private String itemName;
}
