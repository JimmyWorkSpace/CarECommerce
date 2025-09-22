package com.ruoyi.car.service;

import java.util.List;
import com.ruoyi.car.domain.CarOrderInfoEntity;

/**
 * 订单信息Service接口
 * 
 * @author ruoyi
 * @date 2024-01-01
 */
public interface ICarOrderInfoService 
{
    /**
     * 查询订单信息
     * 
     * @param id 订单信息主键
     * @return 订单信息
     */
    public CarOrderInfoEntity selectCarOrderInfoById(Long id);

    /**
     * 查询订单信息列表
     * 
     * @param carOrderInfo 订单信息
     * @return 订单信息集合
     */
    public List<CarOrderInfoEntity> selectCarOrderInfoList(CarOrderInfoEntity carOrderInfo);

    /**
     * 新增订单信息
     * 
     * @param carOrderInfo 订单信息
     * @return 结果
     */
    public int insertCarOrderInfo(CarOrderInfoEntity carOrderInfo);

    /**
     * 修改订单信息
     * 
     * @param carOrderInfo 订单信息
     * @return 结果
     */
    public int updateCarOrderInfo(CarOrderInfoEntity carOrderInfo);

    /**
     * 批量删除订单信息
     * 
     * @param ids 需要删除的订单信息主键集合
     * @return 结果
     */
    public int deleteCarOrderInfoByIds(Long[] ids);

    /**
     * 删除订单信息信息
     * 
     * @param id 订单信息主键
     * @return 结果
     */
    public int deleteCarOrderInfoById(Long id);
}
