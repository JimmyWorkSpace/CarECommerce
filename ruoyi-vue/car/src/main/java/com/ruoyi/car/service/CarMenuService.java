package com.ruoyi.car.service;

import java.util.List;
import com.ruoyi.car.domain.CarMenuEntity;

/**
 * 菜單維護Service接口
 * 
 * @author ruoyi
 * @date 2025-01-18
 */
public interface CarMenuService 
{
    /**
     * 查詢菜單維護
     * 
     * @param id 菜單維護主鍵
     * @return 菜單維護
     */
    public CarMenuEntity selectCarMenuById(Long id);

    /**
     * 查詢菜單維護列表
     * 
     * @param carMenu 菜單維護
     * @return 菜單維護集合
     */
    public List<CarMenuEntity> selectCarMenuList(CarMenuEntity carMenu);

    /**
     * 新增菜單維護
     * 
     * @param carMenu 菜單維護
     * @return 結果
     */
    public int insertCarMenu(CarMenuEntity carMenu);

    /**
     * 修改菜單維護
     * 
     * @param carMenu 菜單維護
     * @return 結果
     */
    public int updateCarMenu(CarMenuEntity carMenu);

    /**
     * 批量刪除菜單維護
     * 
     * @param ids 需要刪除的菜單維護主鍵集合
     * @return 結果
     */
    public int deleteCarMenuByIds(Long[] ids);

    /**
     * 刪除菜單維護信息
     * 
     * @param id 菜單維護主鍵
     * @return 結果
     */
    public int deleteCarMenuById(Long id);

    /**
     * 更新菜單顯示狀態
     * 
     * @param id 菜單ID
     * @param isShow 是否顯示
     * @return 結果
     */
    public int updateCarMenuShowStatus(Long id, Integer isShow);

    /**
     * 更新菜單排序
     * 
     * @param id 菜單ID
     * @param showOrder 顯示順序
     * @return 結果
     */
    public int updateCarMenuOrder(Long id, Integer showOrder);
}
