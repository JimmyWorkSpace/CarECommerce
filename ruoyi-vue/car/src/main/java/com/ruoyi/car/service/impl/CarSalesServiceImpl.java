package com.ruoyi.car.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ruoyi.car.domain.CarSalesEntity;
import com.ruoyi.car.mapper.carcecloud.CarSalesMapper;
import com.ruoyi.car.service.CarSalesService;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Example;

@Service
@Slf4j
//@DataSource(DataSourceType.SLAVE)
public class CarSalesServiceImpl implements CarSalesService {

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
		Example example = new Example(CarSalesEntity.class);
		example.createCriteria().andEqualTo("uid", uid);
		return carSalesMapper.selectOneByExample(example);
	}

	@Override
	public CarSalesEntity getById(Long carSaleId) {
		Example example = new Example(CarSalesEntity.class);
		example.createCriteria().andEqualTo("id", carSaleId);
		return carSalesMapper.selectOneByExample(example);
	}
	
	/**
	 * 查询车辆销售列表
	 * 
	 * @param carSales 车辆销售信息
	 * @return 车辆销售集合
	 */
	@Override
	public List<CarSalesEntity> selectCarSalesList(CarSalesEntity carSales) {
		Example example = new Example(CarSalesEntity.class);
		Example.Criteria criteria = example.createCriteria();
		
		if (carSales.getSaleTitle() != null && !carSales.getSaleTitle().trim().isEmpty()) {
			criteria.andLike("saleTitle", "%" + carSales.getSaleTitle() + "%");
		}
		if (carSales.getSalesperson() != null && !carSales.getSalesperson().trim().isEmpty()) {
			criteria.andLike("salesperson", "%" + carSales.getSalesperson() + "%");
		}
		if (carSales.getStatus() != null && !carSales.getStatus().trim().isEmpty()) {
			criteria.andEqualTo("status", carSales.getStatus());
		}
		if (carSales.getPublisher() != null && !carSales.getPublisher().trim().isEmpty()) {
			criteria.andLike("publisher", "%" + carSales.getPublisher() + "%");
		}
		
		example.orderBy("createDate").desc();
		return carSalesMapper.selectByExample(example);
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
		
		Example example = new Example(CarSalesEntity.class);
		example.createCriteria().andIn("id", ids);
		return carSalesMapper.selectByExample(example);
	}
}
