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
}
