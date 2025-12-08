package cc.carce.sale.service;

import cc.carce.sale.dto.CarBrandSaleCountDto;
import cc.carce.sale.entity.CarBrandEntity;
import cc.carce.sale.mapper.carcecloud.CarBrandMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class CarBrandService {

    @Resource
    private CarBrandMapper carBrandMapper;

    /**
     * 获取品牌在售数量统计，带缓存
     * 缓存时间：1小时
     * @return 品牌在售数量列表
     */
    @Cacheable(value = "brandSaleCount", key = "'all'", unless = "#result == null || #result.isEmpty()")
    public List<CarBrandSaleCountDto> getBrandSaleCount() {
        log.info("查询品牌在售数量统计，未命中缓存");
        return carBrandMapper.selectBrandSaleCount();
    }
    
    /**
     * 获取所有品牌列表，带缓存
     * 缓存时间：1小时
     * @return 所有品牌列表，按品牌名称排序
     */
    @Cacheable(value = "allBrands", key = "'all'", unless = "#result == null || #result.isEmpty()")
    public List<CarBrandEntity> getAllBrands() {
        log.info("查询所有品牌列表，未命中缓存");
        Example example = new Example(CarBrandEntity.class);
        example.orderBy("brand").asc();
        return carBrandMapper.selectByExample(example);
    }
    
    /**
     * 获取已上架车辆的品牌列表，带缓存
     * 只返回在已上架车辆中使用的品牌
     * 缓存时间：1小时
     * @return 品牌列表，按品牌名称排序
     */
    @Cacheable(value = "publishedBrands", key = "'all'", unless = "#result == null || #result.isEmpty()")
    public List<CarBrandEntity> getBrandsFromPublishedCars() {
        log.info("查询已上架车辆的品牌列表，未命中缓存");
        return carBrandMapper.selectBrandsFromPublishedCars();
    }
}
