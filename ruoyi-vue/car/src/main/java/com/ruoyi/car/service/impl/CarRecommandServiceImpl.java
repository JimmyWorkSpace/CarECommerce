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
 * 推荐管理Service业务层处理
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
     * 查询推荐记录
     * 
     * @param id 推荐记录主键
     * @return 推荐记录
     */
    @Override
    public CarRecommandEntity selectCarRecommandById(Long id)
    {
        return carRecommandMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询推荐记录列表
     * 
     * @param carRecommand 推荐记录
     * @return 推荐记录
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
     * 新增推荐记录
     * 
     * @param carRecommand 推荐记录
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
     * 修改推荐记录
     * 
     * @param carRecommand 推荐记录
     * @return 结果
     */
    @Override
    public int updateCarRecommand(CarRecommandEntity carRecommand)
    {
        return carRecommandMapper.updateByPrimaryKey(carRecommand);
    }

    /**
     * 批量删除推荐记录
     * 
     * @param ids 需要删除的推荐记录主键
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
     * 删除推荐记录信息
     * 
     * @param id 推荐记录主键
     * @return 结果
     */
    @Override
    public int deleteCarRecommandById(Long id)
    {
        return carRecommandMapper.deleteByPrimaryKey(id);
    }
    
    /**
     * 根据推荐类型和推荐ID查询推荐记录
     * 
     * @param recommandType 推荐类型
     * @param recommandId 推荐ID
     * @return 推荐记录
     */
    @Override
    public CarRecommandEntity selectByRecommandTypeAndId(Integer recommandType, Long recommandId)
    {
        return carRecommandMapper.selectByRecommandTypeAndId(recommandType, recommandId);
    }
    
    /**
     * 根据推荐类型查询推荐列表
     * 
     * @param recommandType 推荐类型
     * @return 推荐记录集合
     */
    @Override
    public List<CarRecommandEntity> selectByRecommandType(Integer recommandType)
    {
        return carRecommandMapper.selectByRecommandType(recommandType);
    }
    
    /**
     * 删除推荐记录
     * 
     * @param recommandType 推荐类型
     * @param recommandId 推荐ID
     * @return 结果
     */
    @Override
    public int deleteByRecommandTypeAndId(Integer recommandType, Long recommandId)
    {
        return carRecommandMapper.deleteByRecommandTypeAndId(recommandType, recommandId);
    }
    
    /**
     * 设置推荐状态
     * 
     * @param recommandType 推荐类型
     * @param recommandId 推荐ID
     * @param isRecommended 是否推荐
     * @return 结果
     */
    @Override
    public int setRecommended(Integer recommandType, Long recommandId, Boolean isRecommended)
    {
        if (isRecommended) {
            // 添加推荐
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
            // 取消推荐
            return deleteByRecommandTypeAndId(recommandType, recommandId);
        }
    }
}
