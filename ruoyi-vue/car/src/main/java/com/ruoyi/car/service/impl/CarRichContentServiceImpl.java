package com.ruoyi.car.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.car.mapper.master.CarRichContentMapper;
import com.ruoyi.car.domain.CarRichContentEntity;
import com.ruoyi.car.service.ICarRichContentService;

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
        return carRichContentMapper.selectById(id);
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
        return carRichContentMapper.selectCarRichContentList(carRichContent);
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
        return carRichContentMapper.insert(carRichContent);
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
        return carRichContentMapper.updateById(carRichContent);
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
            result += carRichContentMapper.deleteById(id);
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
        return carRichContentMapper.deleteById(id);
    }
}
