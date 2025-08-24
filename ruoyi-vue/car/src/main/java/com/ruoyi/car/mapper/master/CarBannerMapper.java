package com.ruoyi.car.mapper.master;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ruoyi.car.domain.CarBannerEntity;

import tk.mybatis.mapper.common.Mapper;

public interface CarBannerMapper extends Mapper<CarBannerEntity> {
	/**
     * 查询所有轮播图（按排序字段排序）
     */
    List<CarBannerEntity> selectAllOrderByShowOrder();
    
    /**
     * 批量更新排序
     */
    int batchUpdateShowOrder(@Param("banners") List<CarBannerEntity> banners);
} 