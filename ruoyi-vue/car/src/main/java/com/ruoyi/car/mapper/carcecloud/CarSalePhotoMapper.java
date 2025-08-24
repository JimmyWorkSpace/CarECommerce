package com.ruoyi.car.mapper.carcecloud;

import com.ruoyi.car.domain.CarEntity;
import com.ruoyi.car.domain.CarSalePhotoEntity;
import com.ruoyi.car.domain.dto.CarEquipment;
import com.ruoyi.car.domain.dto.CarGuarantee;
import com.ruoyi.car.dto.CarBaseInfoDto;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface CarSalePhotoMapper extends Mapper<CarSalePhotoEntity> {

  @Delete("delete from car_sale_photos where car_sales_id = #{carSaleId}")
  void deleteByCarSalesId(@Param("carSaleId") Long carSaleId);
}
