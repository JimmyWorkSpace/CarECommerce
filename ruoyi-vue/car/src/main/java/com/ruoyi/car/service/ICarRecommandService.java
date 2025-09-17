package com.ruoyi.car.service;

import java.util.List;
import com.ruoyi.car.domain.CarRecommandEntity;

/**
 * 推荐管理Service接口
 * 
 * @author ruoyi
 * @date 2024-01-01
 */
public interface ICarRecommandService 
{
    /**
     * 查询推荐记录
     * 
     * @param id 推荐记录主键
     * @return 推荐记录
     */
    public CarRecommandEntity selectCarRecommandById(Long id);

    /**
     * 查询推荐记录列表
     * 
     * @param carRecommand 推荐记录
     * @return 推荐记录集合
     */
    public List<CarRecommandEntity> selectCarRecommandList(CarRecommandEntity carRecommand);

    /**
     * 新增推荐记录
     * 
     * @param carRecommand 推荐记录
     * @return 结果
     */
    public int insertCarRecommand(CarRecommandEntity carRecommand);

    /**
     * 修改推荐记录
     * 
     * @param carRecommand 推荐记录
     * @return 结果
     */
    public int updateCarRecommand(CarRecommandEntity carRecommand);

    /**
     * 批量删除推荐记录
     * 
     * @param ids 需要删除的推荐记录主键集合
     * @return 结果
     */
    public int deleteCarRecommandByIds(Long[] ids);

    /**
     * 删除推荐记录信息
     * 
     * @param id 推荐记录主键
     * @return 结果
     */
    public int deleteCarRecommandById(Long id);
    
    /**
     * 根据推荐类型和推荐ID查询推荐记录
     * 
     * @param recommandType 推荐类型
     * @param recommandId 推荐ID
     * @return 推荐记录
     */
    public CarRecommandEntity selectByRecommandTypeAndId(Integer recommandType, Long recommandId);
    
    /**
     * 根据推荐类型查询推荐列表
     * 
     * @param recommandType 推荐类型
     * @return 推荐记录集合
     */
    public List<CarRecommandEntity> selectByRecommandType(Integer recommandType);
    
    /**
     * 删除推荐记录
     * 
     * @param recommandType 推荐类型
     * @param recommandId 推荐ID
     * @return 结果
     */
    public int deleteByRecommandTypeAndId(Integer recommandType, Long recommandId);
    
    /**
     * 设置推荐状态
     * 
     * @param recommandType 推荐类型
     * @param recommandId 推荐ID
     * @param isRecommended 是否推荐
     * @return 结果
     */
    public int setRecommended(Integer recommandType, Long recommandId, Boolean isRecommended);
}
