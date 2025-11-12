package com.ruoyi.car.service;

import java.util.List;

import com.ruoyi.car.domain.CarSalesEntity;
import com.ruoyi.car.dto.CarSalesDto;

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
  List<CarSalesDto> selectCarSalesList(CarSalesEntity carSales);
  
  /**
   * 根据ID列表批量查询车辆销售信息
   * 
   * @param ids 车辆销售ID列表
   * @return 车辆销售集合
   */
  List<CarSalesEntity> selectCarSalesByIds(List<Long> ids);
  
  /**
   * 更新车辆销售的后台审核结果
   * 
   * @param id 车辆销售ID
   * @param isAdminCheck 后台审核结果 0 待审核 1 通过 2 拒绝
   * @return 更新结果
   */
  int updateAdminCheck(Long id, Integer isAdminCheck);
}
