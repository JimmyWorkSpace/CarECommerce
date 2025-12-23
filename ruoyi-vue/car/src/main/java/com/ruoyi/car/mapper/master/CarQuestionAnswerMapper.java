package com.ruoyi.car.mapper.master;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ruoyi.car.domain.CarQuestionAnswerEntity;

import tk.mybatis.mapper.common.Mapper;

/**
 * 问答Mapper
 */
public interface CarQuestionAnswerMapper extends Mapper<CarQuestionAnswerEntity> {

    /**
     * 插入问答
     */
    int insert(CarQuestionAnswerEntity entity);

    /**
     * 根据ID查询问答
     */
    CarQuestionAnswerEntity selectById(@Param("id") Long id);

    /**
     * 根据ID更新问答
     */
    int updateById(CarQuestionAnswerEntity entity);

    /**
     * 根据ID刪除问答（逻辑刪除）
     */
    int deleteById(@Param("id") Long id);

    /**
     * 根据頻道ID查询问答列表
     */
    List<CarQuestionAnswerEntity> selectByChannelId(@Param("channelId") Long channelId);

    /**
     * 查询所有问答
     */
    List<CarQuestionAnswerEntity> selectAll();

    /**
     * 根据頻道ID查询问答列表（按排序）
     */
    List<CarQuestionAnswerEntity> selectByChannelIdOrderByShowOrder(@Param("channelId") Long channelId);

    /**
     * 查詢最大的顯示順序
     * 
     * @return 最大的顯示順序，如果沒有記錄則返回0
     */
    Integer selectMaxShowOrder();
}