package cc.carce.sale.form;

import cc.carce.sale.common.BasePageForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class CarSalesSearchForm extends BasePageForm {
    
    private List<String> brand;     // 品牌列表
    private String model;           // 型号
    private Integer year;           // 年份
    private Integer priceMin;       // 最低价格
    private Integer priceMax;       // 最高价格
    private String keyword;         // 关键词搜索
    private String sortBy;          // 排序方式：newest, price-low, price-high, year-new
    
    // 新增筛选字段
    private List<String> transmission;     // 变速系统列表
    private List<String> drivetrain;       // 驱动方式列表
    private List<String> fuelSystem;       // 燃料系统列表
}
