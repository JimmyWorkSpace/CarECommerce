package com.ruoyi.car.mapper.master;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ruoyi.car.domain.CarRecommandEntity;

import tk.mybatis.mapper.common.Mapper;

public interface CarRecommandMapper extends Mapper<CarRecommandEntity> {
    
    /**
     * 根据推薦類型和推薦ID查询推薦记录
     */
    CarRecommandEntity selectByRecommandTypeAndId(@Param("recommandType") Integer recommandType, @Param("recommandId") Long recommandId);
    
    /**
     * 根据推薦類型查询推薦列表
     */
    List<CarRecommandEntity> selectByRecommandType(@Param("recommandType") Integer recommandType);
    
    /**
     * 刪除推薦记录
     */
    int deleteByRecommandTypeAndId(@Param("recommandType") Integer recommandType, @Param("recommandId") Long recommandId);
}
