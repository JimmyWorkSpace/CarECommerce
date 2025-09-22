package com.ruoyi.car.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ruoyi.car.domain.CarSalesEntity;
import com.ruoyi.car.dto.CarSalesDto;
import com.ruoyi.car.mapper.carcecloud.CarSalesMapper;
import com.ruoyi.car.service.CarSalesService;
import com.ruoyi.framework.service.BaseServiceImpl;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
//@DataSource(DataSourceType.SLAVE)
public class CarSalesServiceImpl extends BaseServiceImpl implements CarSalesService {

	@Resource
	private CarSalesMapper carSalesMapper;

	/**
	 * 根据id获取uid,  如果uid为空则生成一个
	 * @param id
	 * @return
	 */
	@Override
	public String getUidById(Long id) {
		CarSalesEntity cs = carSalesMapper.getById(id);
		if (cs != null) {
			if (StrUtil.isBlank(cs.getUid())) {
				cs.setUid(generateShortCode());
				carSalesMapper.updateUidById(cs.getUid(), cs.getId());
			}
			return cs.getUid();
		}
		return null;
	}

	/**
	 * 生成短码
	 * @return
	 */
	private String generateShortCode() {
		String shortId = "";
		String letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		do {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < 4; i++) {
				int index = (int) (Math.random() * letters.length());
				sb.append(letters.charAt(index));
			}
			shortId = sb.toString();
		}
		while (getByUid(shortId) != null);
		return shortId;
	}

	@Override
	public CarSalesEntity getByUid(String uid) {
		return carSalesMapper.getByUid(uid);
	}

	@Override
	public CarSalesEntity getById(Long carSaleId) {
		return carSalesMapper.getById(carSaleId);
	}
	
	/**
	 * 查询车辆销售列表
	 * 
	 * @param carSales 车辆销售信息
	 * @return 车辆销售集合
	 */
	@Override
	public List<CarSalesDto> selectCarSalesList(CarSalesEntity carSales) {
		return carSalesMapper.selectCarSalesList(carSales, mgrDbName);
	}
	
	/**
	 * 根据ID列表批量查询车辆销售信息
	 * 
	 * @param ids 车辆销售ID列表
	 * @return 车辆销售集合
	 */
	@Override
	public List<CarSalesEntity> selectCarSalesByIds(List<Long> ids) {
		if (ids == null || ids.isEmpty()) {
			return new ArrayList<>();
		}
		
		return carSalesMapper.selectCarSalesByIds(ids);
	}
}
