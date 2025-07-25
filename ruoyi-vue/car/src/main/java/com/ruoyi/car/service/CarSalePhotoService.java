package com.ruoyi.car.service;

import java.util.List;

public interface CarSalePhotoService {
  void deleteAndBatchInsert(Long carSaleId, List<String> images);
}
