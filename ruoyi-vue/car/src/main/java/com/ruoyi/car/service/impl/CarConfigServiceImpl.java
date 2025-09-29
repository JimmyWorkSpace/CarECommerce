package com.ruoyi.car.service.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.car.mapper.master.CarConfigMapper;
import com.ruoyi.car.domain.CarConfigEntity;
import com.ruoyi.car.service.ICarConfigService;
import tk.mybatis.mapper.entity.Example;

/**
 * 網站配置Service業務層處理
 * 
 * @author ruoyi
 * @date 2024-01-01
 */
@Service
public class CarConfigServiceImpl implements ICarConfigService 
{
    @Resource
    private CarConfigMapper carConfigMapper;

    /**
     * 查詢網站配置
     * 
     * @param id 網站配置主鍵
     * @return 網站配置
     */
    @Override
    public CarConfigEntity selectCarConfigById(Long id)
    {
        return carConfigMapper.selectByPrimaryKey(id);
    }

    /**
     * 查詢網站配置列表
     * 
     * @param carConfig 網站配置
     * @return 網站配置
     */
    @Override
    public List<CarConfigEntity> selectCarConfigList(CarConfigEntity carConfig)
    {
        Example example = new Example(CarConfigEntity.class);
        Example.Criteria criteria = example.createCriteria();
        
        if (carConfig.getCode() != null && !carConfig.getCode().trim().isEmpty()) {
            criteria.andEqualTo("code", carConfig.getCode());
        }
        if (carConfig.getValue() != null && !carConfig.getValue().trim().isEmpty()) {
            criteria.andEqualTo("value", carConfig.getValue());
        }
        if (carConfig.getName() != null && !carConfig.getName().trim().isEmpty()) {
            criteria.andLike("name", "%" + carConfig.getName() + "%");
        }
        if (carConfig.getShowOrder() != null) {
            criteria.andEqualTo("showOrder", carConfig.getShowOrder());
        }
        if (carConfig.getDelFlag() != null) {
            criteria.andEqualTo("delFlag", carConfig.getDelFlag());
        }
        
        // 按顯示順序和ID排序
        example.orderBy("showOrder").asc().orderBy("id").asc();
        
        return carConfigMapper.selectByExample(example);
    }

    /**
     * 修改網站配置（只修改value字段）
     * 
     * @param carConfig 網站配置
     * @return 結果
     */
    @Override
    @Transactional
    public int updateCarConfig(CarConfigEntity carConfig)
    {
        // 只更新value字段
        CarConfigEntity updateEntity = new CarConfigEntity();
        updateEntity.setId(carConfig.getId());
        updateEntity.setValue(carConfig.getValue());
        
        return carConfigMapper.updateByPrimaryKeySelective(updateEntity);
    }
}
