package cc.carce.sale.mapper.carcecloud;

import cc.carce.sale.entity.CarSalePhotoEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface CarSalePhotoMapper extends Mapper<CarSalePhotoEntity> {

  @Delete("delete from car_sale_photos where car_sales_id = #{carSaleId}")
  void deleteByCarSalesId(@Param("carSaleId") Long carSaleId);
}
