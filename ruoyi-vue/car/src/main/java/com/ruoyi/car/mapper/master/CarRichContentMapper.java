package com.ruoyi.car.mapper.master;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ruoyi.car.domain.CarRichContentEntity;

import tk.mybatis.mapper.common.Mapper;

/**
 * 富文本内容Mapper
 */
public interface CarRichContentMapper extends Mapper<CarRichContentEntity> {
    
    /**
     * 插入富文本内容
     */
    int insert(CarRichContentEntity entity);
    
    /**
     * 根据ID查询富文本内容
     */
    CarRichContentEntity selectById(@Param("id") Long id);
    
    /**
     * 更新富文本内容
     */
    int updateById(CarRichContentEntity entity);
    
    /**
     * 根据ID删除富文本内容（逻辑删除）
     */
    int deleteById(@Param("id") Long id);
    
    /**
     * 根据内容类型查询富文本内容列表
     */
    List<CarRichContentEntity> selectByContentType(@Param("contentType") Integer contentType);
    
    /**
     * 查询所有富文本内容列表（未删除）
     */
    List<CarRichContentEntity> selectAll();
    
    /**
     * 根据内容类型和排序查询富文本内容列表
     */
    List<CarRichContentEntity> selectByContentTypeOrderByShowOrder(@Param("contentType") Integer contentType);
    
    /**
     * 根据条件查询富文本内容列表
     */
    List<CarRichContentEntity> selectCarRichContentList(CarRichContentEntity carRichContent);
}
