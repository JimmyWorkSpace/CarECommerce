package com.ruoyi.car.service;

import com.ruoyi.car.dto.CarDealerInfoDto;

public interface CarDealerService {
	CarDealerInfoDto getInfoById(Long id);

	CarDealerInfoDto getInfoByUid(String uid);
}
