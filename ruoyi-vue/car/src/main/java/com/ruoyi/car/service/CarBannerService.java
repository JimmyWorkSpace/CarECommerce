package com.ruoyi.car.service;

import com.ruoyi.car.domain.CarBannerEntity;
import java.util.List;

public interface CarBannerService {
    
    /**
     * 查詢輪播圖列表（可選按輪播圖類型篩選）
     * @param query 查詢條件，可為 null 或僅設 bannerType 篩選
     */
    List<CarBannerEntity> selectAllBanners(CarBannerEntity query);
    
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
