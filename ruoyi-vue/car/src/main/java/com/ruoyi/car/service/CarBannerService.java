package com.ruoyi.car.service;

import com.ruoyi.car.domain.CarBannerEntity;
import java.util.List;

public interface CarBannerService {
    
    /**
     * 查询所有轮播图
     */
    List<CarBannerEntity> selectAllBanners();
    
    /**
     * 根据ID查询轮播图
     */
    CarBannerEntity selectBannerById(Long id);
    
    /**
     * 新增轮播图
     */
    int insertBanner(CarBannerEntity carBanner);
    
    /**
     * 更新轮播图
     */
    int updateBanner(CarBannerEntity carBanner);
    
    /**
     * 刪除轮播图
     */
    int deleteBanner(Long id);
    
    /**
     * 批量更新排序
     */
    int updateBannerOrder(List<CarBannerEntity> banners);
}
