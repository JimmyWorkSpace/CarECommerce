package com.ruoyi.car.service;

import java.util.List;
import com.ruoyi.car.domain.CarUserEntity;

/**
 * 用户管理Service接口
 * 
 * @author ruoyi
 * @date 2024-01-01
 */
public interface ICarUserService 
{
    /**
     * 查询用户管理
     * 
     * @param id 用户管理主键
     * @return 用户管理
     */
    public CarUserEntity selectCarUserById(Long id);

    /**
     * 查询用户管理列表
     * 
     * @param carUser 用户管理
     * @return 用户管理集合
     */
    public List<CarUserEntity> selectCarUserList(CarUserEntity carUser);

    /**
     * 新增用户管理
     * 
     * @param carUser 用户管理
     * @return 结果
     */
    public int insertCarUser(CarUserEntity carUser);

    /**
     * 修改用户管理
     * 
     * @param carUser 用户管理
     * @return 结果
     */
    public int updateCarUser(CarUserEntity carUser);

    /**
     * 批量删除用户管理
     * 
     * @param ids 需要删除的用户管理主键集合
     * @return 结果
     */
    public int deleteCarUserByIds(Long[] ids);

    /**
     * 删除用户管理信息
     * 
     * @param id 用户管理主键
     * @return 结果
     */
    public int deleteCarUserById(Long id);
}
