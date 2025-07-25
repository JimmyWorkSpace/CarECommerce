package com.ruoyi.car.service;

import com.ruoyi.car.domain.CarSalesEntity;

public interface CarSalesService {

	String getUidById(Long id);

	CarSalesEntity getByUid(String uid);

  CarSalesEntity getById(Long carSaleId);
}
