package com.ruoyi.car.service;

import java.util.List;
import com.ruoyi.car.domain.CarQuestionAnswerEntity;

/**
 * 问答模块Service接口
 * 
 * @author ruoyi
 * @date 2024-01-01
 */
public interface ICarQuestionAnswerService 
{
    /**
     * 查询问答模块
     * 
     * @param id 问答模块主键
     * @return 问答模块
     */
    public CarQuestionAnswerEntity selectCarQuestionAnswerById(Long id);

    /**
     * 查询问答模块列表
     * 
     * @param carQuestionAnswer 问答模块
     * @return 问答模块集合
     */
    public List<CarQuestionAnswerEntity> selectCarQuestionAnswerList(CarQuestionAnswerEntity carQuestionAnswer);

    /**
     * 新增问答模块
     * 
     * @param carQuestionAnswer 问答模块
     * @return 结果
     */
    public int insertCarQuestionAnswer(CarQuestionAnswerEntity carQuestionAnswer);

    /**
     * 修改问答模块
     * 
     * @param carQuestionAnswer 问答模块
     * @return 结果
     */
    public int updateCarQuestionAnswer(CarQuestionAnswerEntity carQuestionAnswer);

    /**
     * 批量刪除问答模块
     * 
     * @param ids 需要刪除的问答模块主键集合
     * @return 结果
     */
    public int deleteCarQuestionAnswerByIds(Long[] ids);

    /**
     * 刪除问答模块信息
     * 
     * @param id 问答模块主键
     * @return 结果
     */
    public int deleteCarQuestionAnswerById(Long id);

    /**
     * 更新问答排序
     * 
     * @param id 问答ID
     * @param showOrder 顯示順序
     * @return 結果
     */
    public int updateCarQuestionAnswerOrder(Long id, Integer showOrder);
}