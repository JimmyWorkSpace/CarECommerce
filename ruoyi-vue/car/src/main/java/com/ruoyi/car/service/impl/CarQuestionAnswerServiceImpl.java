package com.ruoyi.car.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.car.mapper.master.CarQuestionAnswerMapper;
import com.ruoyi.car.domain.CarQuestionAnswerEntity;
import com.ruoyi.car.service.ICarQuestionAnswerService;
import tk.mybatis.mapper.entity.Example;

/**
 * 问答模块Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-01-01
 */
@Service
public class CarQuestionAnswerServiceImpl implements ICarQuestionAnswerService 
{
    @Autowired
    private CarQuestionAnswerMapper carQuestionAnswerMapper;

    /**
     * 查询问答模块
     * 
     * @param id 问答模块主键
     * @return 问答模块
     */
    @Override
    public CarQuestionAnswerEntity selectCarQuestionAnswerById(Long id)
    {
        // 使用tkMyBatis现成的方法
        CarQuestionAnswerEntity entity = new CarQuestionAnswerEntity();
        entity.setId(id);
        entity.setDelFlag(false);
        return carQuestionAnswerMapper.selectOne(entity);
    }

    /**
     * 查询问答模块列表
     * 
     * @param carQuestionAnswer 问答模块
     * @return 问答模块
     */
    @Override
    public List<CarQuestionAnswerEntity> selectCarQuestionAnswerList(CarQuestionAnswerEntity carQuestionAnswer)
    {
        Example example = new Example(CarQuestionAnswerEntity.class);
        Example.Criteria criteria = example.createCriteria();
        
        // 只查询未删除的数据
        criteria.andEqualTo("delFlag", false);
        
        // 根据频道ID查询
        if (carQuestionAnswer.getChannelId() != null) {
            criteria.andEqualTo("channelId", carQuestionAnswer.getChannelId());
        }
        
        // 根据问题模糊查询
        if (carQuestionAnswer.getQuestion() != null && !carQuestionAnswer.getQuestion().trim().isEmpty()) {
            criteria.andLike("question", "%" + carQuestionAnswer.getQuestion() + "%");
        }
        
        // 按排序字段和创建时间排序
        example.orderBy("showOrder").asc().orderBy("createTime").desc();
        
        return carQuestionAnswerMapper.selectByExample(example);
    }

    /**
     * 新增问答模块
     * 
     * @param carQuestionAnswer 问答模块
     * @return 结果
     */
    @Override
    public int insertCarQuestionAnswer(CarQuestionAnswerEntity carQuestionAnswer)
    {
        carQuestionAnswer.setCreateTime(LocalDateTime.now());
        carQuestionAnswer.setDelFlag(false);
        // 使用tkMyBatis现成的方法
        return carQuestionAnswerMapper.insertSelective(carQuestionAnswer);
    }

    /**
     * 修改问答模块
     * 
     * @param carQuestionAnswer 问答模块
     * @return 结果
     */
    @Override
    public int updateCarQuestionAnswer(CarQuestionAnswerEntity carQuestionAnswer)
    {
        // 使用tkMyBatis现成的方法
        return carQuestionAnswerMapper.updateByPrimaryKeySelective(carQuestionAnswer);
    }

    /**
     * 批量删除问答模块（逻辑删除）
     * 
     * @param ids 需要删除的问答模块主键
     * @return 结果
     */
    @Override
    public int deleteCarQuestionAnswerByIds(Long[] ids)
    {
        int count = 0;
        for (Long id : ids) {
            CarQuestionAnswerEntity entity = new CarQuestionAnswerEntity();
            entity.setId(id);
            entity.setDelFlag(true);
            count += carQuestionAnswerMapper.updateByPrimaryKeySelective(entity);
        }
        return count;
    }

    /**
     * 删除问答模块信息（逻辑删除）
     * 
     * @param id 问答模块主键
     * @return 结果
     */
    @Override
    public int deleteCarQuestionAnswerById(Long id)
    {
        CarQuestionAnswerEntity entity = new CarQuestionAnswerEntity();
        entity.setId(id);
        entity.setDelFlag(true);
        return carQuestionAnswerMapper.updateByPrimaryKeySelective(entity);
    }
}