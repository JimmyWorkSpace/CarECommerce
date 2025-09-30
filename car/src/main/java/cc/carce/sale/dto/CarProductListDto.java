package cc.carce.sale.dto;

import java.util.Date;
import lombok.Data;

@Data
public class CarProductListDto {
    
    private Long id;
    private String name;
    private String alias;
    private String model;
    private Long price;
    private Long marketPrice;
    private String brand;
    private String tag;
    private String source;
    private String memo;
    private Date cDt;
    private String image;
    private String category;
    private String description;
}
