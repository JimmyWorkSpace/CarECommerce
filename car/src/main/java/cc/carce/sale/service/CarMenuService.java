package cc.carce.sale.service;

import cc.carce.sale.entity.CarMenuEntity;
import cc.carce.sale.mapper.manager.CarMenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class CarMenuService {

    @Autowired
    private CarMenuMapper carMenuMapper;
    
    /**
     * 获取所有显示的菜单项，按显示顺序排序
     * @return 菜单列表
     */
    public List<CarMenuEntity> getVisibleMenus() {
        Example example = new Example(CarMenuEntity.class);
        Example.Criteria criteria = example.createCriteria();
        
        // 查询条件：显示状态为1，删除标志为0
        criteria.andEqualTo("isShow", 1);
        criteria.andEqualTo("delFlag", 0);
        
        // 按显示顺序排序
        example.orderBy("showOrder").asc();
        
        return carMenuMapper.selectByExample(example);
    }
}
