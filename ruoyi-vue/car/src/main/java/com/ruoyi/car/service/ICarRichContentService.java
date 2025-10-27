package com.ruoyi.car.service;

import java.util.List;
import com.ruoyi.car.domain.CarRichContentEntity;

/**
 * 富文本内容Service接口
 * 
 * @author ruoyi
 * @date 2024-01-01
 */
public interface ICarRichContentService 
{
    /**
     * 查询富文本内容
     * 
     * @param id 富文本内容主键
     * @return 富文本内容
     */
    public CarRichContentEntity selectCarRichContentById(Long id);

    /**
     * 查询富文本内容列表
     * 
     * @param carRichContent 富文本内容
     * @return 富文本内容集合
     */
    public List<CarRichContentEntity> selectCarRichContentList(CarRichContentEntity carRichContent);

    /**
     * 新增富文本内容
     * 
     * @param carRichContent 富文本内容
     * @return 结果
     */
    public int insertCarRichContent(CarRichContentEntity carRichContent);

    /**
     * 修改富文本内容
     * 
     * @param carRichContent 富文本内容
     * @return 结果
     */
    public int updateCarRichContent(CarRichContentEntity carRichContent);

    /**
     * 批量刪除富文本内容
     * 
     * @param ids 需要刪除的富文本内容主键集合
     * @return 结果
     */
    public int deleteCarRichContentByIds(Long[] ids);

    /**
     * 刪除富文本内容信息
     * 
     * @param id 富文本内容主键
     * @return 结果
     */
    public int deleteCarRichContentById(Long id);

    /**
     * 批量更新富文本内容排序
     * 
     * @param carRichContentList 富文本内容列表
     * @return 结果
     */
    public int updateCarRichContentOrder(List<CarRichContentEntity> carRichContentList);
}
