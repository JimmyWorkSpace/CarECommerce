package com.ruoyi.car.service;

import com.ruoyi.car.domain.CarEntity;
import com.ruoyi.car.dto.CarBaseInfoDto;
import com.ruoyi.car.dto.CarEquipmentDto;
import com.ruoyi.car.dto.CarGuaranteeDto;
import java.util.List;

public interface CarService {
	CarEntity getById(Long saleId);

	CarBaseInfoDto getBaseInfoBySaleId(long saleId);
	
	CarBaseInfoDto getBaseInfoByUidId(String uid);

	List<CarEquipmentDto> getEquipmentBySaleId(Long saleId);

	List<CarGuaranteeDto> getGuaranteeBySaleId(Long saleId);

	List<CarEquipmentDto> getEquipmentByUidId(String uid);

	List<CarGuaranteeDto> getGuaranteeByUid(String uid);

	List<String> getImagesByUid(String uid);

	List<String> getVideoByUid(String uid);

}
