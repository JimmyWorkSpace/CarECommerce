package cc.carce.sale.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import cn.hutool.core.collection.CollUtil;
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
    public String getSaleTitleJoin(){
        return CollUtil.join(CollUtil.newArrayList(brand, model, manufactureYear), " ");
    }
    private String saleDescription;     // 描述
    private Integer mileage;            // 里程数
    private String transmission;        // 变速箱
    private String fuelSystem;          // 燃料系统
    private String color;               // 颜色
    private String displacement;        // 排量
    private Integer doorCount;          // 车门数
    private Integer passengerCount;     // 乘坐人数
    private String locationName;        // 地点
    private String coverImage;          // 封面图片URL
    private String status;              // 状态
    private Long garageId;              // 店家ID
    @Builder.Default
    private String dealerName = "";              // 经销商名称
}
