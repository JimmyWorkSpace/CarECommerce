package com.ruoyi.car.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
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
        // 新增时自动计算showOrder：查找最大的showOrder，然后加100
        if (carMenu.getId() == null) {
            Integer maxShowOrder = carMenuMapper.selectMaxShowOrder();
            if (maxShowOrder == null) {
                maxShowOrder = 0;
            }
            carMenu.setShowOrder(maxShowOrder + 100);
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
        // 使用自定义的更新方法，确保 parentId 为 null 时也能正确更新
        return carMenuMapper.updateCarMenu(carMenu);
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
            // 先刪除子菜單
            CarMenuEntity query = new CarMenuEntity();
            query.setParentId(id);
            List<CarMenuEntity> children = carMenuMapper.selectCarMenuList(query);
            for (CarMenuEntity child : children) {
                carMenuMapper.deleteByPrimaryKey(child.getId());
            }
            // 再刪除父菜單
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
        // 先刪除子菜單
        CarMenuEntity query = new CarMenuEntity();
        query.setParentId(id);
        List<CarMenuEntity> children = carMenuMapper.selectCarMenuList(query);
        for (CarMenuEntity child : children) {
            carMenuMapper.deleteByPrimaryKey(child.getId());
        }
        // 再刪除父菜單
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

    /**
     * 查詢父菜單列表（parentId為null的菜單）
     * 
     * @return 父菜單集合
     */
    @Override
    public List<CarMenuEntity> selectParentMenuList()
    {
        return carMenuMapper.selectParentMenuList();
    }

    /**
     * 構建前端所需要樹結構
     * 
     * @param menus 菜單列表
     * @return 樹結構列表
     */
    @Override
    public List<CarMenuEntity> buildMenuTree(List<CarMenuEntity> menus)
    {
        List<CarMenuEntity> returnList = new ArrayList<CarMenuEntity>();
        List<Long> tempList = new ArrayList<Long>();
        for (CarMenuEntity menu : menus)
        {
            tempList.add(menu.getId());
        }
        for (Iterator<CarMenuEntity> iterator = menus.iterator(); iterator.hasNext();)
        {
            CarMenuEntity menu = (CarMenuEntity) iterator.next();
            // 如果是頂級節點, 遍歷該父節點的所有子節點
            if (menu.getParentId() == null || !tempList.contains(menu.getParentId()))
            {
                recursionFn(menus, menu);
                returnList.add(menu);
            }
        }
        if (returnList.isEmpty())
        {
            returnList = menus;
        }
        return returnList;
    }

    /**
     * 遞歸列表
     * 
     * @param list 菜單列表
     * @param t 菜單節點
     */
    private void recursionFn(List<CarMenuEntity> list, CarMenuEntity t)
    {
        // 得到子節點列表
        List<CarMenuEntity> childList = getChildList(list, t);
        t.setChildren(childList);
        for (CarMenuEntity tChild : childList)
        {
            if (hasChild(list, tChild))
            {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子節點列表
     */
    private List<CarMenuEntity> getChildList(List<CarMenuEntity> list, CarMenuEntity t)
    {
        List<CarMenuEntity> tlist = new ArrayList<CarMenuEntity>();
        Iterator<CarMenuEntity> it = list.iterator();
        while (it.hasNext())
        {
            CarMenuEntity n = (CarMenuEntity) it.next();
            if (n.getParentId() != null && n.getParentId().longValue() == t.getId().longValue())
            {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判斷是否有子節點
     */
    private boolean hasChild(List<CarMenuEntity> list, CarMenuEntity t)
    {
        return getChildList(list, t).size() > 0 ? true : false;
    }
}
