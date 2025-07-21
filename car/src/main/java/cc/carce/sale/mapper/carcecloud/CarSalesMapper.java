package cc.carce.sale.mapper.carcecloud;

import cc.carce.sale.entity.CarSalesEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

public interface CarSalesMapper extends Mapper<CarSalesEntity> {

	@Select("select * from car_sales cs where cs.uid = #{uid} limit 1")
	CarSalesEntity getByUid(@Param("uid") String uid);

	@Select("select * from car_sales cs where cs.id = #{id} limit 1")
	CarSalesEntity getById(@Param("id") Long id);

	@Update("update car_sales set uid = #{uid} where id = #{id}")
	void updateUidById(@Param("uid") String uid, @Param("id") Long id);

}
