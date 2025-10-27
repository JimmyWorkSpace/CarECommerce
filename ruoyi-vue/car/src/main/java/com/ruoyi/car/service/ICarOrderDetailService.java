package com.ruoyi.car.service;

import java.util.List;
import com.ruoyi.car.domain.CarOrderDetailEntity;

/**
 * 訂單详情Service接口
 * 
 * @author ruoyi
 * @date 2024-01-01
 */
public interface ICarOrderDetailService 
{
    /**
     * 查询訂單详情列表
     * 
     * @param carOrderDetail 訂單详情
     * @return 訂單详情集合
     */
    public List<CarOrderDetailEntity> selectCarOrderDetailList(CarOrderDetailEntity carOrderDetail);
}
