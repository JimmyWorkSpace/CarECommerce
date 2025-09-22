package com.ruoyi.car.mapper.master;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ruoyi.car.domain.CarRecommandEntity;

import tk.mybatis.mapper.common.Mapper;

public interface CarRecommandMapper extends Mapper<CarRecommandEntity> {
    
    /**
     * 根据推荐类型和推荐ID查询推荐记录
     */
    CarRecommandEntity selectByRecommandTypeAndId(@Param("recommandType") Integer recommandType, @Param("recommandId") Long recommandId);
    
    /**
     * 根据推荐类型查询推荐列表
     */
    List<CarRecommandEntity> selectByRecommandType(@Param("recommandType") Integer recommandType);
    
    /**
     * 删除推荐记录
     */
    int deleteByRecommandTypeAndId(@Param("recommandType") Integer recommandType, @Param("recommandId") Long recommandId);
}
