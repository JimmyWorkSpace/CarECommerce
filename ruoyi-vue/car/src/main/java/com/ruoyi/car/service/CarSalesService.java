package com.ruoyi.car.service;

import java.util.List;
import com.ruoyi.car.domain.CarSalesEntity;

public interface CarSalesService {

	String getUidById(Long id);

	CarSalesEntity getByUid(String uid);

  CarSalesEntity getById(Long carSaleId);
  
  /**
   * 查询车辆销售列表
   * 
   * @param carSales 车辆销售信息
   * @return 车辆销售集合
   */
  List<CarSalesEntity> selectCarSalesList(CarSalesEntity carSales);
  
  /**
   * 根据ID列表批量查询车辆销售信息
   * 
   * @param ids 车辆销售ID列表
   * @return 车辆销售集合
   */
  List<CarSalesEntity> selectCarSalesByIds(List<Long> ids);
}
