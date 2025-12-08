package cc.carce.sale.mapper.carcecloud;

import java.util.List;
import tk.mybatis.mapper.common.Mapper;
import cc.carce.sale.dto.CarBrandSaleCountDto;
import cc.carce.sale.entity.CarBrandEntity;

public interface CarBrandMapper extends Mapper<CarBrandEntity> {
    
    /**
     * 查询品牌在售数量统计
     * @return 品牌在售数量列表
     */
    List<CarBrandSaleCountDto> selectBrandSaleCount();
    
    /**
     * 查询已上架车辆的品牌列表
     * @return 品牌列表
     */
    List<CarBrandEntity> selectBrandsFromPublishedCars();
}
