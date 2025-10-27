package com.ruoyi.car.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.car.mapper.master.CarOrderInfoMapper;
import com.ruoyi.car.domain.CarOrderInfoEntity;
import com.ruoyi.car.service.ICarOrderInfoService;
import tk.mybatis.mapper.entity.Example;

/**
 * 訂單信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-01-01
 */
@Service
public class CarOrderInfoServiceImpl implements ICarOrderInfoService 
{
    @Autowired
    private CarOrderInfoMapper carOrderInfoMapper;

    /**
     * 查询訂單信息
     * 
     * @param id 訂單信息主键
     * @return 訂單信息
     */
    @Override
    public CarOrderInfoEntity selectCarOrderInfoById(Long id)
    {
        return carOrderInfoMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询訂單信息列表
     * 
     * @param carOrderInfo 訂單信息
     * @return 訂單信息
     */
    @Override
    public List<CarOrderInfoEntity> selectCarOrderInfoList(CarOrderInfoEntity carOrderInfo)
    {
        Example example = new Example(CarOrderInfoEntity.class);
        Example.Criteria criteria = example.createCriteria();
        
        // 添加查询条件
        if (carOrderInfo.getOrderNo() != null && !carOrderInfo.getOrderNo().isEmpty()) {
            criteria.andLike("orderNo", "%" + carOrderInfo.getOrderNo() + "%");
        }
        if (carOrderInfo.getUserId() != null) {
            criteria.andEqualTo("userId", carOrderInfo.getUserId());
        }
        if (carOrderInfo.getOrderStatus() != null) {
            criteria.andEqualTo("orderStatus", carOrderInfo.getOrderStatus());
        }
        if (carOrderInfo.getOrderType() != null) {
            criteria.andEqualTo("orderType", carOrderInfo.getOrderType());
        }
        if (carOrderInfo.getReceiverName() != null && !carOrderInfo.getReceiverName().isEmpty()) {
            criteria.andLike("receiverName", "%" + carOrderInfo.getReceiverName() + "%");
        }
        if (carOrderInfo.getReceiverMobile() != null && !carOrderInfo.getReceiverMobile().isEmpty()) {
            criteria.andLike("receiverMobile", "%" + carOrderInfo.getReceiverMobile() + "%");
        }
        
        // 排除已刪除的记录
        criteria.andEqualTo("delFlag", false);
        
        // 按建立時間倒序排列
        example.orderBy("createTime").desc();
        
        return carOrderInfoMapper.selectByExample(example);
    }

    /**
     * 新增訂單信息
     * 
     * @param carOrderInfo 訂單信息
     * @return 结果
     */
    @Override
    public int insertCarOrderInfo(CarOrderInfoEntity carOrderInfo)
    {
        carOrderInfo.setCreateTime(DateUtils.getNowDate());
        carOrderInfo.setDelFlag(false);
        return carOrderInfoMapper.insert(carOrderInfo);
    }

    /**
     * 修改訂單信息
     * 
     * @param carOrderInfo 訂單信息
     * @return 结果
     */
    @Override
    public int updateCarOrderInfo(CarOrderInfoEntity carOrderInfo)
    {
        return carOrderInfoMapper.updateByPrimaryKeySelective(carOrderInfo);
    }

    /**
     * 批量刪除訂單信息
     * 
     * @param ids 需要刪除的訂單信息主键
     * @return 结果
     */
    @Override
    public int deleteCarOrderInfoByIds(Long[] ids)
    {
        int count = 0;
        for (Long id : ids) {
            CarOrderInfoEntity entity = new CarOrderInfoEntity();
            entity.setId(id);
            entity.setDelFlag(true);
            count += carOrderInfoMapper.updateByPrimaryKeySelective(entity);
        }
        return count;
    }

    /**
     * 刪除訂單信息信息
     * 
     * @param id 訂單信息主键
     * @return 结果
     */
    @Override
    public int deleteCarOrderInfoById(Long id)
    {
        CarOrderInfoEntity entity = new CarOrderInfoEntity();
        entity.setId(id);
        entity.setDelFlag(true);
        return carOrderInfoMapper.updateByPrimaryKeySelective(entity);
    }
}
