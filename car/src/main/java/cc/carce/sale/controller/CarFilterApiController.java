package cc.carce.sale.controller;

import cc.carce.sale.common.R;
import cc.carce.sale.dto.CarBrandSaleCountDto;
import cc.carce.sale.dto.CarFilterOptionsDto;
import cc.carce.sale.entity.CarBrandEntity;
import cc.carce.sale.service.CarBrandService;
import cc.carce.sale.service.CarFilterOptionsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "汽车筛选API", description = "汽车筛选相关接口")
@RestController
@RequestMapping("api/car-filter")
@Slf4j
@CrossOrigin
public class CarFilterApiController {

    @Resource
    private CarBrandService carBrandService;
    
    @Resource
    private CarFilterOptionsService carFilterOptionsService;

    @ApiOperation(value = "获取品牌在售数量统计", notes = "获取所有品牌及其在售车辆数量，按在售数量倒序排列")
    @GetMapping("/brand-sale-count")
    public R<List<CarBrandSaleCountDto>> getBrandSaleCount() {
        try {
            List<CarBrandSaleCountDto> brandSaleCountList = carBrandService.getBrandSaleCount();
            return R.ok("查询成功", brandSaleCountList);
        } catch (Exception e) {
            log.error("获取品牌在售数量统计失败", e);
            return R.fail("查询失败", null);
        }
    }
    
    @ApiOperation(value = "获取所有品牌列表", notes = "获取所有品牌信息，按品牌名称排序")
    @GetMapping("/brands")
    public R<List<CarBrandEntity>> getAllBrands() {
        try {
            List<CarBrandEntity> brands = carBrandService.getAllBrands();
            return R.ok("查询成功", brands);
        } catch (Exception e) {
            log.error("获取所有品牌列表失败", e);
            return R.fail("查询失败", null);
        }
    }
    
    @ApiOperation(value = "获取车辆筛选选项", notes = "获取所有车辆筛选条件选项，包括乘客数、车门数、排气量、变速系统、驱动方式、燃料系统")
    @GetMapping("/options")
    public R<CarFilterOptionsDto> getCarFilterOptions() {
        try {
            CarFilterOptionsDto filterOptions = carFilterOptionsService.getCarFilterOptions();
            return R.ok("查询成功", filterOptions);
        } catch (Exception e) {
            log.error("获取车辆筛选选项失败", e);
            return R.fail("查询失败", null);
        }
    }
    
    @ApiOperation(value = "根据品牌获取型号列表", notes = "根据品牌ID获取该品牌下的所有型号列表")
    @GetMapping("/models")
    public R<List<String>> getModelsByBrand(
            @org.springframework.web.bind.annotation.RequestParam(required = false) String brand,
            @org.springframework.web.bind.annotation.RequestParam(required = false) Long brandId) {
        try {
            List<String> models;
            if (brandId != null) {
                // 优先使用品牌ID
                models = carFilterOptionsService.getModelsByBrandId(brandId);
            } else if (brand != null && !brand.trim().isEmpty()) {
                // 兼容旧的品牌名称方式
                models = carFilterOptionsService.getModelsByBrand(brand);
            } else {
                return R.fail("品牌ID或品牌名称不能为空", null);
            }
            return R.ok("查询成功", models);
        } catch (Exception e) {
            log.error("根据品牌获取型号列表失败，品牌ID: {}, 品牌名称: {}", brandId, brand, e);
            return R.fail("查询失败", null);
        }
    }
}
