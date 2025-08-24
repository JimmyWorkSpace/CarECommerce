package com.ruoyi.car.mapper.carcecloud;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.ruoyi.car.domain.CarDealerPhotoEntity;

import tk.mybatis.mapper.common.Mapper;
public interface CarDealerPhotoMapper extends Mapper<CarDealerPhotoEntity> {

	@Select("select * from car_dealer_photos where id_garage = #{idGarage} and dealer_id = #{dealerId}")
	List<CarDealerPhotoEntity> selectByIdGarageAndDealerId(@Param("idGarage") Long idGarage,@Param("dealerId")  Long dealerId);
}
