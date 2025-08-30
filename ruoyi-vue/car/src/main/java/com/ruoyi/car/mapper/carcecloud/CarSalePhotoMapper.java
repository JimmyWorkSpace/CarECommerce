package com.ruoyi.car.mapper.carcecloud;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.ruoyi.car.domain.CarSalePhotoEntity;

import tk.mybatis.mapper.common.Mapper;

public interface CarSalePhotoMapper extends Mapper<CarSalePhotoEntity> {

  @Delete("delete from car_sale_photos where car_sales_id = #{carSaleId}")
  void deleteByCarSalesId(@Param("carSaleId") Long carSaleId);

  @Select("select * from car_sale_photos t where t.is_thumb_do = 0")
  List<CarSalePhotoEntity> getNotThumbedImage();
}
