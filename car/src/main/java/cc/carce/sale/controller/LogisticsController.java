package cc.carce.sale.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cc.carce.sale.common.R;
import cc.carce.sale.dto.CarStoreInfoDto;
import cc.carce.sale.entity.CarStoreInfoEntity;
import cc.carce.sale.service.CarStoreInfoService;
import cc.carce.sale.service.LogisticsService;
import lombok.extern.slf4j.Slf4j;

/**
 * 物流控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/logistics")
public class LogisticsController {
    
    @Resource
    private LogisticsService logisticsService;
    
    @Resource
    private CarStoreInfoService carStoreInfoService;
    /**
     * 获取超商门店列表
     */
    @GetMapping("/stores")
    public R<List<CarStoreInfoDto>> getStoreList(String city, String district) {
        try {
            log.info("获取超商门店列表，县市：{}，区镇：{}", city, district);
            List<CarStoreInfoEntity> storeList = carStoreInfoService.getStoreList(city + district);
            return R.ok("获取超商门店列表成功", storeList.stream().map(CarStoreInfoDto::new).collect(Collectors.toList()));
        } catch (Exception e) {
            log.error("获取超商门店列表异常", e);
            return R.fail("获取超商门店列表异常: " + e.getMessage(), null);
        }
    }
    
    // /**
    //  * 根据门店ID获取门店详情
    //  */
    // @GetMapping("/stores/{storeId}")
    // public R<Map<String, Object>> getStoreById(@PathVariable String storeId) {
    //     try {
    //         log.info("获取门店详情，门店ID: {}", storeId);
    //         return logisticsService.getStoreById(storeId);
    //     } catch (Exception e) {
    //         log.error("获取门店详情异常，门店ID: {}", storeId, e);
    //         return R.fail("获取门店详情异常: " + e.getMessage(), null);
    //     }
    // }
}
