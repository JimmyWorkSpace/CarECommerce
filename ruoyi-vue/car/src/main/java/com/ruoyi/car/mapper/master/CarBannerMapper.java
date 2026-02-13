package com.ruoyi.car.mapper.master;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ruoyi.car.domain.CarBannerEntity;

import tk.mybatis.mapper.common.Mapper;

public interface CarBannerMapper extends Mapper<CarBannerEntity> {
	/**
     * 查詢輪播圖列表（按排序欄位排序，可選按輪播圖類型篩選）
     * @param bannerType 輪播圖類型，可為 null 表示不過濾
     */
    List<CarBannerEntity> selectAllOrderByShowOrder(@Param("bannerType") Integer bannerType);
    
    /**
     * 批量更新排序
     */
    int batchUpdateShowOrder(@Param("banners") List<CarBannerEntity> banners);

    /**
     * 查詢最大的顯示順序
     * 
     * @return 最大的顯示順序，如果沒有記錄則返回0
     */
    Integer selectMaxShowOrder();
} 