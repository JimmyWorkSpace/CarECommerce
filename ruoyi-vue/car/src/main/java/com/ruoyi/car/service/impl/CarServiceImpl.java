package com.ruoyi.car.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ruoyi.car.domain.CarEntity;
import com.ruoyi.car.domain.CarSalesEntity;
import com.ruoyi.car.domain.dto.CarEquipment;
import com.ruoyi.car.domain.dto.CarGuarantee;
import com.ruoyi.car.dto.CarBaseInfoDto;
import com.ruoyi.car.dto.CarEquipmentDto;
import com.ruoyi.car.dto.CarGuaranteeDto;
import com.ruoyi.car.mapper.carcecloud.CarMapper;
import com.ruoyi.car.service.CarSalesService;
import com.ruoyi.car.service.CarService;
import com.ruoyi.car.service.ImageService;
import com.ruoyi.framework.service.FtpService;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
//@DataSource(DataSourceType.SLAVE)
public class CarServiceImpl implements CarService {
	@Resource
	private CarMapper carMapper;
	
	@Resource
	private CarSalesService carSalesService;

	@Resource
	private ImageService imageService;
	
	@Resource
	private FtpService ftpService;

	@Override
	public CarEntity getById(Long saleId) {
		return carMapper.selectByPrimaryKey(saleId);
	}

	/**
	 * 获取车辆基本信息
	 *
	 * @param saleId
	 * @return
	 */
	@Override
	public CarBaseInfoDto getBaseInfoBySaleId(long saleId) {
		CarBaseInfoDto result = carMapper.selectCarBaseInfoBySaleId(saleId);
		result.setSaleDescription(imageService.replaceImagePrefixInHtml(result.getSaleDescription()));
		return result;
	}

	/**
	 * 获取车辆配置信息
	 * @param saleId
	 * @return
	 */
	@Override
	public List<CarEquipmentDto> getEquipmentBySaleId(Long saleId) {
		List<CarEquipmentDto> result = new ArrayList<>();
		List<CarEquipment> list = carMapper.selectCarEquipmentBySaleId(saleId);
		if (CollUtil.isNotEmpty(list)) {
			// type = 1 安全配備 type = 2 影音設備 type = 3 車輛配備
			result.add(new CarEquipmentDto("車輛配備", list.stream().filter(tag -> Integer.valueOf(3).equals(tag.getType()))
					.map(CarEquipment::getItemName).distinct().collect(Collectors.toList())));
			result.add(new CarEquipmentDto("安全配備", list.stream().filter(tag -> Integer.valueOf(1).equals(tag.getType()))
					.map(CarEquipment::getItemName).distinct().collect(Collectors.toList())));
			result.add(new CarEquipmentDto("影音設備", list.stream().filter(tag -> Integer.valueOf(2).equals(tag.getType()))
					.map(CarEquipment::getItemName).distinct().collect(Collectors.toList())));
		}
		return result;
	}

	/**
	 * 获取车辆保证信息
	 * @param saleId
	 * @return
	 */
	@Override
	public List<CarGuaranteeDto> getGuaranteeBySaleId(Long saleId) {
		List<CarGuarantee> list = carMapper.selectCarGuaranteeBySaleId(saleId);

		return list.stream().map(l -> {
			CarGuaranteeDto dto = new CarGuaranteeDto();
			dto.setItemName(l.getItemName());
			dto.setDescription(l.getDescription());
			return dto;
		}).collect(Collectors.toList());
	}

	/**
	 * 根据uid获取车辆基本信息
	 * @param uid
	 * @return
	 */
	@Override
	public CarBaseInfoDto getBaseInfoByUidId(String uid) {
		CarBaseInfoDto result = carMapper.selectCarBaseInfoByUid(uid);
//		将图片地址替换为图片服务器地址，加上前缀
		result.setSaleDescription(imageService.replaceImagePrefixInHtml(result.getSaleDescription()));
		return result;
	}

	@Override
	public List<CarEquipmentDto> getEquipmentByUidId(String uid) {
		List<CarEquipmentDto> result = new ArrayList<>();
		List<CarEquipment> list = carMapper.selectCarEquipmentByUid(uid);
		if (CollUtil.isNotEmpty(list)) {
			result.add(new CarEquipmentDto("車輛配備", list.stream().filter(tag -> Integer.valueOf(3).equals(tag.getType()))
					.map(CarEquipment::getItemName).distinct().collect(Collectors.toList())));
			result.add(new CarEquipmentDto("安全配備", list.stream().filter(tag -> Integer.valueOf(1).equals(tag.getType()))
					.map(CarEquipment::getItemName).distinct().collect(Collectors.toList())));
			result.add(new CarEquipmentDto("影音設備", list.stream().filter(tag -> Integer.valueOf(2).equals(tag.getType()))
					.map(CarEquipment::getItemName).distinct().collect(Collectors.toList())));
		}
		return result;
	}

	@Override
	public List<CarGuaranteeDto> getGuaranteeByUid(String uid) {
		List<CarGuarantee> list = carMapper.selectCarGuaranteeByUid(uid);

		return list.stream().map(l -> {
			CarGuaranteeDto dto = new CarGuaranteeDto();
			dto.setItemName(l.getItemName());
			dto.setDescription(l.getDescription());
			return dto;
		}).collect(Collectors.toList());
	}

	@Override
	public List<String> getImagesByUid(String uid) {
		CarSalesEntity cs = carSalesService.getByUid(uid);
		try {
			if(cs != null) {
				String dir = "/img/car_sale/" + cs.getId() + "/img";
				List<String> images = ftpService.listFiles(dir);
				return images.stream().map(img -> imageService.replaceImagePrefix(dir + "/" + img)).collect(Collectors.toList());
			}
		} catch (Exception e) {
			log.error("从ftp获取图片信息失败", e);
		}
		return new ArrayList<>();
	}

	@Override
	public List<String> getVideoByUid(String uid) {
		CarSalesEntity cs = carSalesService.getByUid(uid);
		try {
			if(cs != null) {
				String dir = "/img/car_sale/" + cs.getId() + "/video";
				List<String> images = ftpService.listFiles(dir);
				for(String img : images) {
					if(!img.endsWith(".txt")) {
//						非txt文件，替换视频地址，当做视频返回
						return CollUtil.newArrayList(imageService.replaceImagePrefix(dir + "/" + img));
					}else {
//						如果是txt文件，取文件名作为youtube的id，拼接youtube的播放地址
						return CollUtil.newArrayList("https://www.youtube.com/watch?v=" + img.substring(0, img.lastIndexOf(".")));
					}
				}
			}
		} catch (Exception e) {
			log.error("从ftp获取图片信息失败", e);
		}
		return new ArrayList<>();
	}

}
