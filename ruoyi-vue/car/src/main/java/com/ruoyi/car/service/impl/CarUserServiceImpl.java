package com.ruoyi.car.service.impl;

import java.util.List;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.car.mapper.master.CarUserMapper;
import com.ruoyi.car.domain.CarUserEntity;
import com.ruoyi.car.service.ICarUserService;
import tk.mybatis.mapper.entity.Example;

/**
 * 用户管理Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-01-01
 */
@Service
public class CarUserServiceImpl implements ICarUserService 
{
    @Autowired
    private CarUserMapper carUserMapper;

    /**
     * 查询用户管理
     * 
     * @param id 用户管理主键
     * @return 用户管理
     */
    @Override
    public CarUserEntity selectCarUserById(Long id)
    {
        return carUserMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询用户管理列表
     * 
     * @param carUser 用户管理
     * @return 用户管理
     */
    @Override
    public List<CarUserEntity> selectCarUserList(CarUserEntity carUser)
    {
        Example example = new Example(CarUserEntity.class);
        Example.Criteria criteria = example.createCriteria();
        
        if (carUser.getPhoneNumber() != null && !carUser.getPhoneNumber().isEmpty()) {
            criteria.andLike("phoneNumber", "%" + carUser.getPhoneNumber() + "%");
        }
        if (carUser.getNickName() != null && !carUser.getNickName().isEmpty()) {
            criteria.andLike("nickName", "%" + carUser.getNickName() + "%");
        }
        if (carUser.getDelFlag() != null) {
            criteria.andEqualTo("delFlag", carUser.getDelFlag());
        }
        
        example.orderBy("createTime").desc();
        return carUserMapper.selectByExample(example);
    }

    /**
     * 新增用户管理
     * 
     * @param carUser 用户管理
     * @return 结果
     */
    @Override
    public int insertCarUser(CarUserEntity carUser)
    {
        carUser.setCreateTime(new Date());
        carUser.setDelFlag(false);
        return carUserMapper.insert(carUser);
    }

    /**
     * 修改用户管理
     * 
     * @param carUser 用户管理
     * @return 结果
     */
    @Override
    public int updateCarUser(CarUserEntity carUser)
    {
        return carUserMapper.updateByPrimaryKey(carUser);
    }

    /**
     * 批量删除用户管理
     * 
     * @param ids 需要删除的用户管理主键
     * @return 结果
     */
    @Override
    public int deleteCarUserByIds(Long[] ids)
    {
        Example example = new Example(CarUserEntity.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", java.util.Arrays.asList(ids));
        
        // 逻辑删除，设置delFlag为true
        CarUserEntity updateEntity = new CarUserEntity();
        updateEntity.setDelFlag(true);
        
        return carUserMapper.updateByExampleSelective(updateEntity, example);
    }

    /**
     * 删除用户管理信息
     * 
     * @param id 用户管理主键
     * @return 结果
     */
    @Override
    public int deleteCarUserById(Long id)
    {
        // 逻辑删除，设置delFlag为true
        CarUserEntity updateEntity = new CarUserEntity();
        updateEntity.setId(id);
        updateEntity.setDelFlag(true);
        
        return carUserMapper.updateByPrimaryKeySelective(updateEntity);
    }
}
