package cc.carce.sale.service;

import cc.carce.sale.dto.CarFilterOptionsDto;
import cc.carce.sale.mapper.carcecloud.CarMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
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
    @Cacheable(value = "carFilterOptions", key = "'all'", unless = "#result == null")
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
            
            log.info("查询车辆筛选选项完成，变速系统: {}, 驱动方式: {}, 燃料系统: {}", 
                    transmissions.size(), drivetrains.size(), fuelSystems.size());
            
        } catch (Exception e) {
            log.error("查询车辆筛选选项失败", e);
            throw e;
        }
        
        return filterOptions;
    }
}
