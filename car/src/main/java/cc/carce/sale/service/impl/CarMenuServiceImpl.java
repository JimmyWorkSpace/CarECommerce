package cc.carce.sale.service.impl;

import cc.carce.sale.entity.CarMenuEntity;
import cc.carce.sale.mapper.manager.CarMenuMapper;
import cc.carce.sale.service.CarMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class CarMenuServiceImpl implements CarMenuService {

    @Autowired
    private CarMenuMapper carMenuMapper;

    @Override
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
