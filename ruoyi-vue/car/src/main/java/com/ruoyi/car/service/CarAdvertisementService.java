package com.ruoyi.car.service;

import com.ruoyi.car.domain.CarAdvertisementEntity;
import java.util.List;

public interface CarAdvertisementService {
    
    /**
     * 查询所有广告
     */
    List<CarAdvertisementEntity> selectAllAdvertisements();
    
    /**
     * 查询广告列表（支持条件查询）
     */
    List<CarAdvertisementEntity> selectAdvertisementList(CarAdvertisementEntity carAdvertisement);
    
    /**
     * 根据ID查询广告
     */
    CarAdvertisementEntity selectAdvertisementById(Long id);
    
    /**
     * 新增广告
     */
    int insertAdvertisement(CarAdvertisementEntity carAdvertisement);
    
    /**
     * 更新广告
     */
    int updateAdvertisement(CarAdvertisementEntity carAdvertisement);
    
    /**
     * 刪除广告
     */
    int deleteAdvertisement(Long id);
    
    /**
     * 批量更新排序
     */
    int updateAdvertisementOrder(List<CarAdvertisementEntity> advertisements);
} 