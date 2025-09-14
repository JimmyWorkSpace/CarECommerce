package cc.carce.sale.dto;

import lombok.Data;

import java.util.List;

@Data
public class CarFilterOptionsDto {
    
    /**
     * 变速系统选项
     */
    private List<String> transmissions;
    
    /**
     * 驱动方式选项
     */
    private List<String> drivetrains;
    
    /**
     * 燃料系统选项
     */
    private List<String> fuelSystems;
}
