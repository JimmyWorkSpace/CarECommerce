package com.ruoyi.car.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.car.mapper.master.CarOrderDetailMapper;
import com.ruoyi.car.domain.CarOrderDetailEntity;
import com.ruoyi.car.service.ICarOrderDetailService;
import tk.mybatis.mapper.entity.Example;

/**
 * 訂單详情Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-01-01
 */
@Service
public class CarOrderDetailServiceImpl implements ICarOrderDetailService 
{
    @Autowired
    private CarOrderDetailMapper carOrderDetailMapper;

    /**
     * 查询訂單详情列表
     * 
     * @param carOrderDetail 訂單详情
     * @return 訂單详情
     */
    @Override
    public List<CarOrderDetailEntity> selectCarOrderDetailList(CarOrderDetailEntity carOrderDetail)
    {
        Example example = new Example(CarOrderDetailEntity.class);
        Example.Criteria criteria = example.createCriteria();
        
        // 添加查询条件
        if (carOrderDetail.getOrderId() != null) {
            criteria.andEqualTo("orderId", carOrderDetail.getOrderId());
        }
        if (carOrderDetail.getProductId() != null) {
            criteria.andEqualTo("productId", carOrderDetail.getProductId());
        }
        if (carOrderDetail.getProductName() != null && !carOrderDetail.getProductName().isEmpty()) {
            criteria.andLike("productName", "%" + carOrderDetail.getProductName() + "%");
        }
        
        // 排除已刪除的记录
        criteria.andEqualTo("delFlag", false);
        
        // 按建立時間倒序排列
        example.orderBy("createTime").desc();
        
        return carOrderDetailMapper.selectByExample(example);
    }
}
