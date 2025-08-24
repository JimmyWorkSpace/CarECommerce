package com.ruoyi.car.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarEquipmentDto {
  private String name;
  private List<String> tags;
}
