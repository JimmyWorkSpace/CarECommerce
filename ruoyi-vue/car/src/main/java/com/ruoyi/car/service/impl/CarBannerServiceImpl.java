package com.ruoyi.car.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ruoyi.car.domain.CarBannerEntity;
import com.ruoyi.car.mapper.master.CarBannerMapper;
import com.ruoyi.car.service.CarBannerService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CarBannerServiceImpl implements CarBannerService {

    @Resource
    private CarBannerMapper carBannerMapper;

    @Override
    public List<CarBannerEntity> selectAllBanners() {
        List<CarBannerEntity> banners = carBannerMapper.selectAllOrderByShowOrder();
        // 确保所有记录的showOrder不为null
        for (CarBannerEntity banner : banners) {
            if (banner.getShowOrder() == null) {
                banner.setShowOrder(0);
            }
        }
        return banners;
    }

    @Override
    public CarBannerEntity selectBannerById(Long id) {
        return carBannerMapper.selectByPrimaryKey(id);
    }

    @Override
    @Transactional
    public int insertBanner(CarBannerEntity carBanner) {
        // 设置默认值
        if (carBanner.getDelFlag() == null) {
            carBanner.setDelFlag(0);
        }
        if (carBanner.getIsLink() == null) {
            carBanner.setIsLink(0);
        }
        // 新增时自动计算showOrder：查找最大的showOrder，然后加100
        if (carBanner.getId() == null) {
            Integer maxShowOrder = carBannerMapper.selectMaxShowOrder();
            if (maxShowOrder == null) {
                maxShowOrder = 0;
            }
            carBanner.setShowOrder(maxShowOrder + 100);
        }
        return carBannerMapper.insert(carBanner);
    }

    @Override
    @Transactional
    public int updateBanner(CarBannerEntity carBanner) {
        return carBannerMapper.updateByPrimaryKey(carBanner);
    }

    @Override
    @Transactional
    public int deleteBanner(Long id) {
        return carBannerMapper.deleteByPrimaryKey(id);
    }

    @Override
    @Transactional
    public int updateBannerOrder(List<CarBannerEntity> banners) {
        return carBannerMapper.batchUpdateShowOrder(banners);
    }
}
