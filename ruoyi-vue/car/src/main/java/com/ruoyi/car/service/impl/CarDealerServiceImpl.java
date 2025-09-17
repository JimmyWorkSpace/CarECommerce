package com.ruoyi.car.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ruoyi.car.domain.CarDealerEntity;
import com.ruoyi.car.domain.CarDealerPhotoEntity;
import com.ruoyi.car.domain.CarSalesEntity;
import com.ruoyi.car.dto.CarDealerInfoDto;
import com.ruoyi.car.mapper.carcecloud.CarDealerMapper;
import com.ruoyi.car.mapper.carcecloud.CarDealerPhotoMapper;
import com.ruoyi.car.mapper.carcecloud.CarSalesMapper;
import com.ruoyi.car.service.CarDealerService;
import com.ruoyi.car.service.CarSalesService;
import com.ruoyi.car.service.ImageService;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Example;

@Service
@Slf4j
//@DataSource(DataSourceType.SLAVE)
public class CarDealerServiceImpl implements CarDealerService {

	@Resource
	private CarDealerMapper carDealerMapper;

	@Resource
	private CarDealerPhotoMapper carDealerPhotoMapper;
	
	@Resource
	private CarSalesService carSalesService;
	
	@Resource
	private CarSalesMapper carSalesMapper;

	@Resource
	private ImageService imageService;

	@Override
	public CarDealerInfoDto getInfoById(Long id) {
		CarDealerInfoDto result = new CarDealerInfoDto();
		Example example = new Example(CarDealerEntity.class);
		example.createCriteria().andEqualTo("idGarage", id);
		List<CarDealerEntity> carDealerList = carDealerMapper.selectByExample(example);
		if (CollUtil.isNotEmpty(carDealerList)) {
			CarDealerEntity cd = carDealerList.get(0);
			BeanUtil.copyProperties(cd, result);
			result.setDescription(imageService.replaceImagePrefixInHtml(result.getDescription()));
			example = new Example(CarDealerPhotoEntity.class);
			example.createCriteria().andEqualTo("idGarage", id).andEqualTo("dealerId", cd.getId());
			example.orderBy("photoOrder");
			List<CarDealerPhotoEntity> photoList = carDealerPhotoMapper.selectByExample(example);
			if (CollUtil.isNotEmpty(photoList)) {
				result.setPhotos(photoList.stream().map(p -> imageService.replaceImagePrefix(p.getPhotoUrl()))
						.collect(Collectors.toList()));
			}
		}
		return result;
	}

	/**
	 * 根据uid获取车商信息
	 * @param uid
	 * @return
	 */
	@Override
	public CarDealerInfoDto getInfoByUid(String uid) {
//		Example example = new Example(CarSalesEntity.class);
//		example.createCriteria().andEqualTo("uid", uid);
//		CarSalesEntity cs = carSalesMapper.selectOneByExample(example);
		CarSalesEntity cs = carSalesMapper.getByUid(uid);
		if(cs == null) {
			return new CarDealerInfoDto();
		}
		
		CarDealerInfoDto result = new CarDealerInfoDto();
//		example = new Example(CarDealerEntity.class);
//		example.createCriteria().andEqualTo("idGarage", cs.getIdGarage());
		List<CarDealerEntity> carDealerList = carDealerMapper.selectByIdGarage(cs.getIdGarage());
		if (CollUtil.isNotEmpty(carDealerList)) {
			CarDealerEntity cd = carDealerList.get(0);
			BeanUtil.copyProperties(cd, result);
			result.setDescription(imageService.replaceImagePrefixInHtml(result.getDescription()));
//			example = new Example(CarDealerPhotoEntity.class);
//			example.createCriteria().andEqualTo("idGarage", cs.getIdGarage()).andEqualTo("dealerId", cd.getId());
//			example.orderBy("photoOrder");
			List<CarDealerPhotoEntity> photoList = carDealerPhotoMapper.selectByIdGarageAndDealerId(cs.getIdGarage(), cd.getId());
			if (CollUtil.isNotEmpty(photoList)) {
				result.setPhotos(photoList.stream().map(p -> imageService.replaceImagePrefix(p.getPhotoUrl()))
						.collect(Collectors.toList()));
			}
		}
		return result;
	}
	
	/**
	 * 查询经销商列表
	 * 
	 * @param carDealer 经销商信息
	 * @return 经销商集合
	 */
	@Override
	public List<CarDealerEntity> selectCarDealerList(CarDealerEntity carDealer) {
		Example example = new Example(CarDealerEntity.class);
		Example.Criteria criteria = example.createCriteria();
		
		if (carDealer.getDealerName() != null && !carDealer.getDealerName().trim().isEmpty()) {
			criteria.andLike("dealerName", "%" + carDealer.getDealerName() + "%");
		}
		if (carDealer.getContactPerson() != null && !carDealer.getContactPerson().trim().isEmpty()) {
			criteria.andLike("contactPerson", "%" + carDealer.getContactPerson() + "%");
		}
		if (carDealer.getCompanyPhone() != null && !carDealer.getCompanyPhone().trim().isEmpty()) {
			criteria.andLike("companyPhone", "%" + carDealer.getCompanyPhone() + "%");
		}
		
		example.orderBy("cDt").desc();
		return carDealerMapper.selectByExample(example);
	}
}
