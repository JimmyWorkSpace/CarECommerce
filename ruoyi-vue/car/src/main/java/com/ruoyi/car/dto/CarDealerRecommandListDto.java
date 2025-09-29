package com.ruoyi.car.dto;

import com.ruoyi.car.domain.CarDealerEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CarDealerRecommandListDto extends CarDealerEntity {
     
 private Long recommendedValue;
}
