package cc.carce.sale.service;

import cc.carce.sale.dto.CarBrandSaleCountDto;
import cc.carce.sale.mapper.carcecloud.CarBrandMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

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
}
