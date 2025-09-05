package cc.carce.sale.form;

import cc.carce.sale.common.BasePageForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CarSalesSearchForm extends BasePageForm {
    
    private String brand;           // 品牌
    private String model;           // 型号
    private Integer year;           // 年份
    private Integer priceMin;       // 最低价格
    private Integer priceMax;       // 最高价格
    private String fuelType;        // 燃料类型
    private String transmission;    // 变速箱
    private Integer mileage;        // 里程数
    private String bodyType;        // 车身类型
    private String keyword;         // 关键词搜索
    private String sortBy;          // 排序方式：newest, price-low, price-high, year-new
}
