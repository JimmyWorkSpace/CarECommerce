package com.ruoyi.car.service;

import java.util.List;
import com.ruoyi.car.domain.CarRecommandEntity;

/**
 * 推薦管理Service接口
 * 
 * @author ruoyi
 * @date 2024-01-01
 */
public interface ICarRecommandService 
{
    /**
     * 查询推薦记录
     * 
     * @param id 推薦记录主键
     * @return 推薦记录
     */
    public CarRecommandEntity selectCarRecommandById(Long id);

    /**
     * 查询推薦记录列表
     * 
     * @param carRecommand 推薦记录
     * @return 推薦记录集合
     */
    public List<CarRecommandEntity> selectCarRecommandList(CarRecommandEntity carRecommand);

    /**
     * 新增推薦记录
     * 
     * @param carRecommand 推薦记录
     * @return 结果
     */
    public int insertCarRecommand(CarRecommandEntity carRecommand);

    /**
     * 修改推薦记录
     * 
     * @param carRecommand 推薦记录
     * @return 结果
     */
    public int updateCarRecommand(CarRecommandEntity carRecommand);

    /**
     * 批量刪除推薦记录
     * 
     * @param ids 需要刪除的推薦记录主键集合
     * @return 结果
     */
    public int deleteCarRecommandByIds(Long[] ids);

    /**
     * 刪除推薦记录信息
     * 
     * @param id 推薦记录主键
     * @return 结果
     */
    public int deleteCarRecommandById(Long id);
    
    /**
     * 根据推薦類型和推薦ID查询推薦记录
     * 
     * @param recommandType 推薦類型
     * @param recommandId 推薦ID
     * @return 推薦记录
     */
    public CarRecommandEntity selectByRecommandTypeAndId(Integer recommandType, Long recommandId);
    
    /**
     * 根据推薦類型查询推薦列表
     * 
     * @param recommandType 推薦類型
     * @return 推薦记录集合
     */
    public List<CarRecommandEntity> selectByRecommandType(Integer recommandType);
    
    /**
     * 刪除推薦记录
     * 
     * @param recommandType 推薦類型
     * @param recommandId 推薦ID
     * @return 结果
     */
    public int deleteByRecommandTypeAndId(Integer recommandType, Long recommandId);
    
    /**
     * 设置推薦狀態
     * 
     * @param recommandType 推薦類型
     * @param recommandId 推薦ID
     * @param isRecommended 是否推薦
     * @return 结果
     */
    public int setRecommended(Integer recommandType, Long recommandId, Boolean isRecommended);
}
