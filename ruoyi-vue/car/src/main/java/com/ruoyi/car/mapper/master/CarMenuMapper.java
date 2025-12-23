package com.ruoyi.car.mapper.master;

import com.ruoyi.car.domain.CarMenuEntity;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface CarMenuMapper extends Mapper<CarMenuEntity>{

    /**
     * 查詢菜單列表
     * 
     * @param carMenu 菜單信息
     * @return 菜單集合
     */
    List<CarMenuEntity> selectCarMenuList(CarMenuEntity carMenu);

    /**
     * 更新菜單顯示狀態
     * 
     * @param id 菜單ID
     * @param isShow 是否顯示
     * @return 結果
     */
    int updateCarMenuShowStatus(@Param("id") Long id, @Param("isShow") Integer isShow);

    /**
     * 更新菜單排序
     * 
     * @param id 菜單ID
     * @param showOrder 顯示順序
     * @return 結果
     */
    int updateCarMenuOrder(@Param("id") Long id, @Param("showOrder") Integer showOrder);

    /**
     * 查詢父菜單列表（parentId為null的菜單）
     * 
     * @return 父菜單集合
     */
    List<CarMenuEntity> selectParentMenuList();

    /**
     * 更新菜單維護（支持 parentId 為 null 的情況）
     * 
     * @param carMenu 菜單信息
     * @return 結果
     */
    int updateCarMenu(CarMenuEntity carMenu);

    /**
     * 查詢最大的顯示順序
     * 
     * @return 最大的顯示順序，如果沒有記錄則返回0
     */
    Integer selectMaxShowOrder();
}
