package cc.carce.sale.service;

import cc.carce.sale.dto.CarMenuDto;
import cc.carce.sale.entity.CarMenuEntity;
import cc.carce.sale.mapper.manager.CarMenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    
    /**
     * 获取所有显示的菜单项（带子菜单结构），按显示顺序排序
     * @return 菜单DTO列表（只包含主菜单，子菜单在children中）
     */
    public List<CarMenuDto> getVisibleMenusWithChildren() {
        // 获取所有显示的菜单
        List<CarMenuEntity> allMenus = getVisibleMenus();
        
        // 转换为DTO
        List<CarMenuDto> allMenuDtos = allMenus.stream()
                .map(CarMenuDto::fromEntity)
                .collect(Collectors.toList());
        
        // 构建父子关系
        List<CarMenuDto> parentMenus = new ArrayList<>();
        for (CarMenuDto menu : allMenuDtos) {
            if (menu.getParentId() == null) {
                // 主菜单
                parentMenus.add(menu);
            } else {
                // 子菜单，找到对应的父菜单并添加到children中
                for (CarMenuDto parent : allMenuDtos) {
                    if (parent.getId().equals(menu.getParentId())) {
                        parent.getChildren().add(menu);
                        break;
                    }
                }
            }
        }
        
        // 对子菜单也进行排序
        for (CarMenuDto parent : parentMenus) {
            if (parent.hasChildren()) {
                parent.getChildren().sort((a, b) -> {
                    Integer orderA = a.getShowOrder() != null ? a.getShowOrder() : 0;
                    Integer orderB = b.getShowOrder() != null ? b.getShowOrder() : 0;
                    return orderA.compareTo(orderB);
                });
            }
        }
        
        return parentMenus;
    }
    
    /**
     * 根据ID获取菜单项
     * @param id 菜单ID
     * @return 菜单实体
     */
    public CarMenuEntity getMenuById(Long id) {
        return carMenuMapper.selectByPrimaryKey(id);
    }
    
    /**
     * 根据linkUrl获取菜单项
     * @param linkUrl 链接URL
     * @return 菜单实体，如果不存在返回null
     */
    public CarMenuEntity getMenuByLinkUrl(String linkUrl) {
        Example example = new Example(CarMenuEntity.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("linkUrl", linkUrl);
        criteria.andEqualTo("isShow", 1);
        criteria.andEqualTo("delFlag", 0);
        List<CarMenuEntity> menus = carMenuMapper.selectByExample(example);
        return menus != null && !menus.isEmpty() ? menus.get(0) : null;
    }
}
