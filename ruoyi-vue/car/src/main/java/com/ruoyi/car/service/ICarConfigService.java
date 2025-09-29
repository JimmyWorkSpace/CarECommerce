package com.ruoyi.car.service;

import java.util.List;
import com.ruoyi.car.domain.CarConfigEntity;
import tk.mybatis.mapper.entity.Example;

/**
 * 网站配置Service接口
 * 
 * @author ruoyi
 * @date 2024-01-01
 */
public interface ICarConfigService 
{
    /**
     * 查询网站配置
     * 
     * @param id 网站配置主键
     * @return 网站配置
     */
    public CarConfigEntity selectCarConfigById(Long id);

    /**
     * 查询网站配置列表
     * 
     * @param carConfig 网站配置
     * @return 网站配置集合
     */
    public List<CarConfigEntity> selectCarConfigList(CarConfigEntity carConfig);

    /**
     * 修改网站配置（只修改value字段）
     * 
     * @param carConfig 网站配置
     * @return 结果
     */
    public int updateCarConfig(CarConfigEntity carConfig);
}
