package cc.carce.sale.mapper.manager;

import cc.carce.sale.entity.CarQuestionAnswerEntity;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

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
     * 根据ID删除问答（逻辑删除）
     */
    int deleteById(@Param("id") Long id);

    /**
     * 根据频道ID查询问答列表
     */
    List<CarQuestionAnswerEntity> selectByChannelId(@Param("channelId") Long channelId);

    /**
     * 查询所有问答
     */
    List<CarQuestionAnswerEntity> selectAll();

    /**
     * 根据频道ID查询问答列表（按排序）
     */
    List<CarQuestionAnswerEntity> selectByChannelIdOrderByShowOrder(@Param("channelId") Long channelId);
    
    /**
     * 根据菜单ID查询问答列表（按排序）
     */
    List<CarQuestionAnswerEntity> selectByMenuIdOrderByShowOrder(@Param("menuId") Long menuId);
}