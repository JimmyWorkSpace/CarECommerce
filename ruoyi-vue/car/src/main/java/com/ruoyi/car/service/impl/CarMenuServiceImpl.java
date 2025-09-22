package com.ruoyi.car.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.car.mapper.master.CarMenuMapper;
import com.ruoyi.car.domain.CarMenuEntity;
import com.ruoyi.car.service.CarMenuService;

/**
 * 菜單維護Service業務層處理
 * 
 * @author ruoyi
 * @date 2025-01-18
 */
@Service
public class CarMenuServiceImpl implements CarMenuService 
{
    @Autowired
    private CarMenuMapper carMenuMapper;

    /**
     * 查詢菜單維護
     * 
     * @param id 菜單維護主鍵
     * @return 菜單維護
     */
    @Override
    public CarMenuEntity selectCarMenuById(Long id)
    {
        return carMenuMapper.selectByPrimaryKey(id);
    }

    /**
     * 查詢菜單維護列表
     * 
     * @param carMenu 菜單維護
     * @return 菜單維護
     */
    @Override
    public List<CarMenuEntity> selectCarMenuList(CarMenuEntity carMenu)
    {
        return carMenuMapper.selectCarMenuList(carMenu);
    }

    /**
     * 新增菜單維護
     * 
     * @param carMenu 菜單維護
     * @return 結果
     */
    @Override
    public int insertCarMenu(CarMenuEntity carMenu)
    {
        carMenu.setCreateTime(DateUtils.getNowDate());
        carMenu.setDelFlag(0);
        if (carMenu.getShowOrder() == null) {
            carMenu.setShowOrder(0);
        }
        if (carMenu.getIsShow() == null) {
            carMenu.setIsShow(1);
        }
        if (carMenu.getCanDel() == null) {
            carMenu.setCanDel(1);
        }
        return carMenuMapper.insert(carMenu);
    }

    /**
     * 修改菜單維護
     * 
     * @param carMenu 菜單維護
     * @return 結果
     */
    @Override
    public int updateCarMenu(CarMenuEntity carMenu)
    {
        return carMenuMapper.updateByPrimaryKeySelective(carMenu);
    }

    /**
     * 批量刪除菜單維護
     * 
     * @param ids 需要刪除的菜單維護主鍵
     * @return 結果
     */
    @Override
    public int deleteCarMenuByIds(Long[] ids)
    {
        int result = 0;
        for (Long id : ids) {
            result += carMenuMapper.deleteByPrimaryKey(id);
        }
        return result;
    }

    /**
     * 刪除菜單維護信息
     * 
     * @param id 菜單維護主鍵
     * @return 結果
     */
    @Override
    public int deleteCarMenuById(Long id)
    {
        return carMenuMapper.deleteByPrimaryKey(id);
    }

    /**
     * 更新菜單顯示狀態
     * 
     * @param id 菜單ID
     * @param isShow 是否顯示
     * @return 結果
     */
    @Override
    public int updateCarMenuShowStatus(Long id, Integer isShow)
    {
        return carMenuMapper.updateCarMenuShowStatus(id, isShow);
    }

    /**
     * 更新菜單排序
     * 
     * @param id 菜單ID
     * @param showOrder 顯示順序
     * @return 結果
     */
    @Override
    public int updateCarMenuOrder(Long id, Integer showOrder)
    {
        return carMenuMapper.updateCarMenuOrder(id, showOrder);
    }
}
