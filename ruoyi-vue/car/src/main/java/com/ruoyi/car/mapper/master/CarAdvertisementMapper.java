package com.ruoyi.car.mapper.master;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ruoyi.car.domain.CarAdvertisementEntity;

import tk.mybatis.mapper.common.Mapper;

public interface CarAdvertisementMapper extends Mapper<CarAdvertisementEntity> {
	/**
     * 查询所有广告（按排序字段排序）
     */
    List<CarAdvertisementEntity> selectAllOrderByShowOrder();
    
    /**
     * 批量更新排序
     */
    int batchUpdateShowOrder(@Param("advertisements") List<CarAdvertisementEntity> advertisements);
} 