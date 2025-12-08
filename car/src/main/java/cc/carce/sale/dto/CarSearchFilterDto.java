package cc.carce.sale.dto;

import cc.carce.sale.entity.CarBrandEntity;
import lombok.Data;
import java.util.List;

/**
 * 车辆搜索过滤条件DTO
 */
@Data
public class CarSearchFilterDto {
    
    /**
     * 品牌列表
     */
    private List<CarBrandEntity> brands;
    
    /**
     * 燃料系统选项
     */
    private List<String> fuelSystems;
    
    /**
     * 变速系统选项
     */
    private List<String> transmissions;
    
    /**
     * 驱动方式选项
     */
    private List<String> drivetrains;
    
    /**
     * 车色选项
     */
    private List<String> colors;
    
    /**
     * 车辆所在地选项
     */
    private List<String> locations;
    
    /**
     * 出厂年份最小值
     */
    private Integer minYear;
    
    /**
     * 出厂年份最大值
     */
    private Integer maxYear;
}

