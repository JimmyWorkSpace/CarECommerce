package cc.carce.sale.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarListDto {
    
    private Long id;                    // car_sales.id
    private String uid;                 // car_sales.uid
    private Long carId;                 // car_sales.car_id
    private String brand;               // 品牌
    private String model;               // 型号
    private Integer manufactureYear;    // 出厂年份
    private Integer salePrice;          // 售价
    private String saleTitle;           // 标题
    private String saleDescription;     // 描述
    private Integer mileage;            // 里程数
    private String transmission;        // 变速箱
    private String fuelSystem;          // 燃料系统
    private String color;               // 颜色
    private String locationName;        // 地点
    private String coverImage;          // 封面图片URL
    private String status;              // 状态
    private Long garageId;              // 店家ID
}
