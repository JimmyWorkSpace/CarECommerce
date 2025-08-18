package com.ruoyi.car.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ruoyi.car.domain.CarAdvertisementEntity;
import com.ruoyi.car.mapper.master.CarAdvertisementMapper;
import com.ruoyi.car.service.CarAdvertisementService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CarAdvertisementServiceImpl implements CarAdvertisementService {

    @Resource
    private CarAdvertisementMapper carAdvertisementMapper;

    @Override
    public List<CarAdvertisementEntity> selectAllAdvertisements() {
        return carAdvertisementMapper.selectAllOrderByShowOrder();
    }

    @Override
    public CarAdvertisementEntity selectAdvertisementById(Long id) {
        return carAdvertisementMapper.selectByPrimaryKey(id);
    }

    @Override
    @Transactional
    public int insertAdvertisement(CarAdvertisementEntity carAdvertisement) {
        // 设置默认值
        if (carAdvertisement.getShowOrder() == null) {
            carAdvertisement.setShowOrder(0);
        }
        if (carAdvertisement.getDelFlag() == null) {
            carAdvertisement.setDelFlag(0);
        }
        if (carAdvertisement.getIsLink() == null) {
            carAdvertisement.setIsLink(0);
        }
        return carAdvertisementMapper.insert(carAdvertisement);
    }

    @Override
    @Transactional
    public int updateAdvertisement(CarAdvertisementEntity carAdvertisement) {
        return carAdvertisementMapper.updateByPrimaryKey(carAdvertisement);
    }

    @Override
    @Transactional
    public int deleteAdvertisement(Long id) {
        return carAdvertisementMapper.deleteByPrimaryKey(id);
    }

    @Override
    @Transactional
    public int updateAdvertisementOrder(List<CarAdvertisementEntity> advertisements) {
        return carAdvertisementMapper.batchUpdateShowOrder(advertisements);
    }
} 