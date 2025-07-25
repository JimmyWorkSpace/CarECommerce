package com.ruoyi.car.domain.dto;

import javax.persistence.Column;
import lombok.Data;

@Data
public class CarGuarantee {

  @Column(name = "item_name")
  private String itemName;

  @Column(name = "description")
  private String description;
}
