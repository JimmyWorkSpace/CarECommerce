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
     * 查询广告列表（支持条件查询）
     */
    List<CarAdvertisementEntity> selectAdvertisementList(CarAdvertisementEntity carAdvertisement);
    
    /**
     * 批量更新排序
     */
    int batchUpdateShowOrder(@Param("advertisements") List<CarAdvertisementEntity> advertisements);
    
    /**
     * 查詢最大的顯示順序
     * 
     * @return 最大的顯示順序，如果沒有記錄則返回0
     */
    Integer selectMaxShowOrder();
} 