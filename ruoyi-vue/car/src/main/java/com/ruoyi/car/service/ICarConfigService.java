package com.ruoyi.car.service;

import java.util.List;
import com.ruoyi.car.domain.CarConfigEntity;

/**
 * 網站配置Service接口
 * 
 * @author ruoyi
 * @date 2024-01-01
 */
public interface ICarConfigService 
{
    /**
     * 查詢網站配置
     * 
     * @param id 網站配置主鍵
     * @return 網站配置
     */
    public CarConfigEntity selectCarConfigById(Long id);

    /**
     * 查詢網站配置列表
     * 
     * @param carConfig 網站配置
     * @return 網站配置集合
     */
    public List<CarConfigEntity> selectCarConfigList(CarConfigEntity carConfig);

    /**
     * 修改網站配置（只修改value字段）
     * 
     * @param carConfig 網站配置
     * @return 結果
     */
    public int updateCarConfig(CarConfigEntity carConfig);
}
