package com.ruoyi.car.service;

import java.util.List;
import com.ruoyi.car.domain.CarOrderDetailEntity;

/**
 * 订单详情Service接口
 * 
 * @author ruoyi
 * @date 2024-01-01
 */
public interface ICarOrderDetailService 
{
    /**
     * 查询订单详情列表
     * 
     * @param carOrderDetail 订单详情
     * @return 订单详情集合
     */
    public List<CarOrderDetailEntity> selectCarOrderDetailList(CarOrderDetailEntity carOrderDetail);
}
