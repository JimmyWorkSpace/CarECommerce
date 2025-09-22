package cc.carce.sale.service;

import cc.carce.sale.entity.CarMenuEntity;
import java.util.List;

public interface CarMenuService {
    
    /**
     * 获取所有显示的菜单项，按显示顺序排序
     * @return 菜单列表
     */
    List<CarMenuEntity> getVisibleMenus();
}
