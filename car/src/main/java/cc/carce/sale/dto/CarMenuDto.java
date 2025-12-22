package cc.carce.sale.dto;

import cc.carce.sale.entity.CarMenuEntity;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单DTO，包含子菜单列表
 */
@Data
public class CarMenuDto {
    
    /**
     * 菜单ID
     */
    private Long id;
    
    /**
     * 父菜单ID
     */
    private Long parentId;
    
    /**
     * 菜单标题
     */
    private String title;
    
    /**
     * 显示顺序
     */
    private Integer showOrder;
    
    /**
     * 链接URL
     */
    private String linkUrl;
    
    /**
     * 链接类型：0-普通链接，1-富文本
     */
    private Integer linkType;
    
    /**
     * 子菜单列表
     */
    private List<CarMenuDto> children;
    
    /**
     * 从实体类转换为DTO
     */
    public static CarMenuDto fromEntity(CarMenuEntity entity) {
        CarMenuDto dto = new CarMenuDto();
        dto.setId(entity.getId());
        dto.setParentId(entity.getParentId());
        dto.setTitle(entity.getTitle());
        dto.setShowOrder(entity.getShowOrder());
        dto.setLinkUrl(entity.getLinkUrl());
        dto.setLinkType(entity.getLinkType());
        dto.setChildren(new ArrayList<>());
        return dto;
    }
    
    /**
     * 是否有子菜单
     */
    public boolean hasChildren() {
        return children != null && !children.isEmpty();
    }
}

