package com.ruoyi.car.service;

import java.util.List;
import com.ruoyi.car.domain.CarDealerEntity;
import com.ruoyi.car.dto.CarDealerInfoDto;

public interface CarDealerService {
	CarDealerInfoDto getInfoById(Long id);

	CarDealerInfoDto getInfoByUid(String uid);
	
	/**
	 * 查询经销商列表
	 * 
	 * @param carDealer 经销商信息
	 * @return 经销商集合
	 */
	List<CarDealerEntity> selectCarDealerList(CarDealerEntity carDealer);
}
