package com.ruoyi.car.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.car.mapper.master.CarRichContentMapper;
import com.ruoyi.car.domain.CarRichContentEntity;
import com.ruoyi.car.service.ICarRichContentService;
import tk.mybatis.mapper.entity.Example;

/**
 * 富文本内容Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-01-01
 */
@Service
public class CarRichContentServiceImpl implements ICarRichContentService 
{
    @Autowired
    private CarRichContentMapper carRichContentMapper;

    /**
     * 查询富文本内容
     * 
     * @param id 富文本内容主键
     * @return 富文本内容
     */
    @Override
    public CarRichContentEntity selectCarRichContentById(Long id)
    {
        Example example = new Example(CarRichContentEntity.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", id);
        criteria.andCondition("(delFlag = 0 or delFlag is null)");
        return carRichContentMapper.selectOneByExample(example);
    }

    /**
     * 查询富文本内容列表
     * 
     * @param carRichContent 富文本内容
     * @return 富文本内容
     */
    @Override
    public List<CarRichContentEntity> selectCarRichContentList(CarRichContentEntity carRichContent)
    {
        Example example = new Example(CarRichContentEntity.class);
        Example.Criteria criteria = example.createCriteria();
        
        // 未删除条件
        criteria.andCondition("(delFlag = 0 or delFlag is null)");
        
        // 标题模糊查询
        if (carRichContent.getTitle() != null && !carRichContent.getTitle().trim().isEmpty()) {
            criteria.andLike("title", "%" + carRichContent.getTitle() + "%");
        }
        
        // 内容类型查询
        if (carRichContent.getContentType() != null) {
            criteria.andEqualTo("contentType", carRichContent.getContentType());
        }
        
        // 排序
        example.orderBy("showOrder").asc().orderBy("createTime").desc();
        
        return carRichContentMapper.selectByExample(example);
    }

    /**
     * 新增富文本内容
     * 
     * @param carRichContent 富文本内容
     * @return 结果
     */
    @Override
    public int insertCarRichContent(CarRichContentEntity carRichContent)
    {
        carRichContent.setCreateTime(LocalDateTime.now());
        carRichContent.setDelFlag(false);
        return carRichContentMapper.insertSelective(carRichContent);
    }

    /**
     * 修改富文本内容
     * 
     * @param carRichContent 富文本内容
     * @return 结果
     */
    @Override
    public int updateCarRichContent(CarRichContentEntity carRichContent)
    {
        return carRichContentMapper.updateByPrimaryKeySelective(carRichContent);
    }

    /**
     * 批量删除富文本内容
     * 
     * @param ids 需要删除的富文本内容主键
     * @return 结果
     */
    @Override
    public int deleteCarRichContentByIds(Long[] ids)
    {
        int result = 0;
        for (Long id : ids) {
            CarRichContentEntity entity = new CarRichContentEntity();
            entity.setId(id);
            entity.setDelFlag(true);
            result += carRichContentMapper.updateByPrimaryKeySelective(entity);
        }
        return result;
    }

    /**
     * 删除富文本内容信息
     * 
     * @param id 富文本内容主键
     * @return 结果
     */
    @Override
    public int deleteCarRichContentById(Long id)
    {
        CarRichContentEntity entity = new CarRichContentEntity();
        entity.setId(id);
        entity.setDelFlag(true);
        return carRichContentMapper.updateByPrimaryKeySelective(entity);
    }

    /**
     * 批量更新富文本内容排序
     * 
     * @param carRichContentList 富文本内容列表
     * @return 结果
     */
    @Override
    public int updateCarRichContentOrder(List<CarRichContentEntity> carRichContentList)
    {
        int result = 0;
        for (CarRichContentEntity entity : carRichContentList) {
            CarRichContentEntity updateEntity = new CarRichContentEntity();
            updateEntity.setId(entity.getId());
            updateEntity.setShowOrder(entity.getShowOrder());
            result += carRichContentMapper.updateByPrimaryKeySelective(updateEntity);
        }
        return result;
    }
}
