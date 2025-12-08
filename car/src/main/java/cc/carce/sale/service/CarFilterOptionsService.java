package cc.carce.sale.service;

import cc.carce.sale.dto.CarFilterOptionsDto;
import cc.carce.sale.mapper.carcecloud.CarMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class CarFilterOptionsService {

    @Resource
    private CarMapper carMapper;

    /**
     * 获取所有筛选选项，带缓存
     * 缓存时间：1小时
     * @return 筛选选项DTO
     */
    public CarFilterOptionsDto getCarFilterOptions() {
        log.info("查询车辆筛选选项，未命中缓存");
        
        CarFilterOptionsDto filterOptions = new CarFilterOptionsDto();
        
        try {
            // 查询变速系统选项
            List<String> transmissions = carMapper.selectDistinctTransmissions();
            filterOptions.setTransmissions(transmissions);
            
            // 查询驱动方式选项
            List<String> drivetrains = carMapper.selectDistinctDrivetrains();
            filterOptions.setDrivetrains(drivetrains);
            
            // 查询燃料系统选项
            List<String> fuelSystems = carMapper.selectDistinctFuelSystems();
            filterOptions.setFuelSystems(fuelSystems);
            
            // 查询车色选项
            List<String> colors = carMapper.selectDistinctColors();
            filterOptions.setColors(colors);
            
            // 查询车辆所在地选项
            List<String> locations = carMapper.selectDistinctLocationCities();
            filterOptions.setLocations(locations);
            
            // 查询出厂年份范围
            java.util.Map<String, Object> yearRange = carMapper.selectManufactureYearRange();
            if (yearRange != null) {
                Object minYearObj = yearRange.get("minYear");
                Object maxYearObj = yearRange.get("maxYear");
                if (minYearObj != null) {
                    try {
                        // 处理可能是日期格式或数字的情况
                        String minYearStr = minYearObj.toString();
                        if (minYearStr.contains("-") || minYearStr.contains("/")) {
                            // 如果是日期格式，提取年份
                            filterOptions.setMinYear(Integer.valueOf(minYearStr.substring(0, 4)));
                        } else {
                            filterOptions.setMinYear(Integer.valueOf(minYearStr));
                        }
                    } catch (NumberFormatException e) {
                        log.warn("解析最小年份失败: {}", minYearObj, e);
                    }
                }
                if (maxYearObj != null) {
                    try {
                        // 处理可能是日期格式或数字的情况
                        String maxYearStr = maxYearObj.toString();
                        if (maxYearStr.contains("-") || maxYearStr.contains("/")) {
                            // 如果是日期格式，提取年份
                            filterOptions.setMaxYear(Integer.valueOf(maxYearStr.substring(0, 4)));
                        } else {
                            filterOptions.setMaxYear(Integer.valueOf(maxYearStr));
                        }
                    } catch (NumberFormatException e) {
                        log.warn("解析最大年份失败: {}", maxYearObj, e);
                    }
                }
            }
            // 如果没有数据，设置默认值
            if (filterOptions.getMinYear() == null) {
                filterOptions.setMinYear(1990);
            }
            if (filterOptions.getMaxYear() == null) {
                filterOptions.setMaxYear(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR));
            }
            
            log.info("查询车辆筛选选项完成，变速系统: {}, 驱动方式: {}, 燃料系统: {}, 车色: {}, 所在地: {}, 年份范围: {}-{}", 
                    transmissions.size(), drivetrains.size(), fuelSystems.size(), colors.size(), locations.size(),
                    filterOptions.getMinYear(), filterOptions.getMaxYear());
            
        } catch (Exception e) {
            log.error("查询车辆筛选选项失败", e);
            throw e;
        }
        
        return filterOptions;
    }
    
    /**
     * 根据品牌获取型号列表
     * @param brand 品牌名称
     * @return 型号列表
     */
    public List<String> getModelsByBrand(String brand) {
        log.info("根据品牌查询型号列表，品牌: {}", brand);
        if (brand == null || brand.trim().isEmpty()) {
            return new java.util.ArrayList<>();
        }
        try {
            return carMapper.selectModelsByBrand(brand);
        } catch (Exception e) {
            log.error("根据品牌查询型号列表失败，品牌: {}", brand, e);
            return new java.util.ArrayList<>();
        }
    }
}
