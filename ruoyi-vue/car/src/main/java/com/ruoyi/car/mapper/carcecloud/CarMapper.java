package com.ruoyi.car.mapper.carcecloud;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ruoyi.car.domain.CarEntity;
import com.ruoyi.car.domain.dto.CarEquipment;
import com.ruoyi.car.domain.dto.CarGuarantee;
import com.ruoyi.car.dto.CarBaseInfoDto;

import tk.mybatis.mapper.common.Mapper;

public interface CarMapper extends Mapper<CarEntity> {

	CarBaseInfoDto selectCarBaseInfoBySaleId(@Param("saleId") Long saleId);

	List<CarEquipment> selectCarEquipmentBySaleId(@Param("saleId") Long saleId);

	List<CarGuarantee> selectCarGuaranteeBySaleId(@Param("saleId") Long saleId);

	CarBaseInfoDto selectCarBaseInfoByUid(@Param("uid") String uid);

	List<CarEquipment> selectCarEquipmentByUid(@Param("uid") String uid);

	List<CarGuarantee> selectCarGuaranteeByUid(String uid);
}
