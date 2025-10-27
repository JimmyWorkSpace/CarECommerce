package com.ruoyi.car.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.car.mapper.master.CarRecommandMapper;
import com.ruoyi.car.domain.CarRecommandEntity;
import com.ruoyi.car.service.ICarRecommandService;
import tk.mybatis.mapper.entity.Example;

/**
 * 推薦管理Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-01-01
 */
@Service
public class CarRecommandServiceImpl implements ICarRecommandService 
{
    @Autowired
    private CarRecommandMapper carRecommandMapper;

    /**
     * 查询推薦记录
     * 
     * @param id 推薦记录主键
     * @return 推薦记录
     */
    @Override
    public CarRecommandEntity selectCarRecommandById(Long id)
    {
        return carRecommandMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询推薦记录列表
     * 
     * @param carRecommand 推薦记录
     * @return 推薦记录
     */
    @Override
    public List<CarRecommandEntity> selectCarRecommandList(CarRecommandEntity carRecommand)
    {
        Example example = new Example(CarRecommandEntity.class);
        Example.Criteria criteria = example.createCriteria();
        
        if (carRecommand.getRecommandType() != null) {
            criteria.andEqualTo("recommandType", carRecommand.getRecommandType());
        }
        if (carRecommand.getRecommandId() != null) {
            criteria.andEqualTo("recommandId", carRecommand.getRecommandId());
        }
        if (carRecommand.getDelFlag() != null) {
            criteria.andEqualTo("delFlag", carRecommand.getDelFlag());
        } else {
            criteria.andCondition("(delFlag = 0 or delFlag is null)");
        }
        
        example.orderBy("showOrder").asc().orderBy("createTime").desc();
        return carRecommandMapper.selectByExample(example);
    }

    /**
     * 新增推薦记录
     * 
     * @param carRecommand 推薦记录
     * @return 结果
     */
    @Override
    public int insertCarRecommand(CarRecommandEntity carRecommand)
    {
        carRecommand.setCreateTime(new Date());
        if (carRecommand.getDelFlag() == null) {
            carRecommand.setDelFlag(0);
        }
        if (carRecommand.getShowOrder() == null) {
            carRecommand.setShowOrder(0);
        }
        return carRecommandMapper.insert(carRecommand);
    }

    /**
     * 修改推薦记录
     * 
     * @param carRecommand 推薦记录
     * @return 结果
     */
    @Override
    public int updateCarRecommand(CarRecommandEntity carRecommand)
    {
        return carRecommandMapper.updateByPrimaryKey(carRecommand);
    }

    /**
     * 批量刪除推薦记录
     * 
     * @param ids 需要刪除的推薦记录主键
     * @return 结果
     */
    @Override
    public int deleteCarRecommandByIds(Long[] ids)
    {
        Example example = new Example(CarRecommandEntity.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", java.util.Arrays.asList(ids));
        return carRecommandMapper.deleteByExample(example);
    }

    /**
     * 刪除推薦记录信息
     * 
     * @param id 推薦记录主键
     * @return 结果
     */
    @Override
    public int deleteCarRecommandById(Long id)
    {
        return carRecommandMapper.deleteByPrimaryKey(id);
    }
    
    /**
     * 根据推薦類型和推薦ID查询推薦记录
     * 
     * @param recommandType 推薦類型
     * @param recommandId 推薦ID
     * @return 推薦记录
     */
    @Override
    public CarRecommandEntity selectByRecommandTypeAndId(Integer recommandType, Long recommandId)
    {
        return carRecommandMapper.selectByRecommandTypeAndId(recommandType, recommandId);
    }
    
    /**
     * 根据推薦類型查询推薦列表
     * 
     * @param recommandType 推薦類型
     * @return 推薦记录集合
     */
    @Override
    public List<CarRecommandEntity> selectByRecommandType(Integer recommandType)
    {
        return carRecommandMapper.selectByRecommandType(recommandType);
    }
    
    /**
     * 刪除推薦记录
     * 
     * @param recommandType 推薦類型
     * @param recommandId 推薦ID
     * @return 结果
     */
    @Override
    public int deleteByRecommandTypeAndId(Integer recommandType, Long recommandId)
    {
        return carRecommandMapper.deleteByRecommandTypeAndId(recommandType, recommandId);
    }
    
    /**
     * 设置推薦狀態
     * 
     * @param recommandType 推薦類型
     * @param recommandId 推薦ID
     * @param isRecommended 是否推薦
     * @return 结果
     */
    @Override
    public int setRecommended(Integer recommandType, Long recommandId, Boolean isRecommended)
    {
        if (isRecommended) {
            // 添加推薦
            CarRecommandEntity existing = selectByRecommandTypeAndId(recommandType, recommandId);
            if (existing == null) {
                CarRecommandEntity carRecommand = new CarRecommandEntity();
                carRecommand.setRecommandType(recommandType);
                carRecommand.setRecommandId(recommandId);
                carRecommand.setShowOrder(0);
                carRecommand.setDelFlag(0);
                carRecommand.setCreateTime(new Date());
                return insertCarRecommand(carRecommand);
            }
            return 1; // 已存在
        } else {
            // 取消推薦
            return deleteByRecommandTypeAndId(recommandType, recommandId);
        }
    }
}
