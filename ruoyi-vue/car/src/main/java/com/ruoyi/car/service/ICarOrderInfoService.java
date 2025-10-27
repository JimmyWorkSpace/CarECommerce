package com.ruoyi.car.service;

import java.util.List;
import com.ruoyi.car.domain.CarOrderInfoEntity;

/**
 * 訂單信息Service接口
 * 
 * @author ruoyi
 * @date 2024-01-01
 */
public interface ICarOrderInfoService 
{
    /**
     * 查询訂單信息
     * 
     * @param id 訂單信息主键
     * @return 訂單信息
     */
    public CarOrderInfoEntity selectCarOrderInfoById(Long id);

    /**
     * 查询訂單信息列表
     * 
     * @param carOrderInfo 訂單信息
     * @return 訂單信息集合
     */
    public List<CarOrderInfoEntity> selectCarOrderInfoList(CarOrderInfoEntity carOrderInfo);

    /**
     * 新增訂單信息
     * 
     * @param carOrderInfo 訂單信息
     * @return 结果
     */
    public int insertCarOrderInfo(CarOrderInfoEntity carOrderInfo);

    /**
     * 修改訂單信息
     * 
     * @param carOrderInfo 訂單信息
     * @return 结果
     */
    public int updateCarOrderInfo(CarOrderInfoEntity carOrderInfo);

    /**
     * 批量刪除訂單信息
     * 
     * @param ids 需要刪除的訂單信息主键集合
     * @return 结果
     */
    public int deleteCarOrderInfoByIds(Long[] ids);

    /**
     * 刪除訂單信息信息
     * 
     * @param id 訂單信息主键
     * @return 结果
     */
    public int deleteCarOrderInfoById(Long id);
}
