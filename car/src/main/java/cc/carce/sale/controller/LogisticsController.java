package cc.carce.sale.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cc.carce.sale.common.R;
import cc.carce.sale.dto.CarStoreInfoDto;
import cc.carce.sale.dto.LogisticsQueryResultDto;
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
    
    /**
     * 根据订单号查询物流信息
     */
    @GetMapping("/query")
    public R<LogisticsQueryResultDto> queryLogisticsByOrderNo(@RequestParam String orderNo) {
        try {
            log.info("根据订单号查询物流信息，订单号：{}", orderNo);
            
            // 验证参数
            if (orderNo == null || orderNo.trim().isEmpty()) {
                return R.fail("订单号不能为空", null);
            }
            
            // 调用服务层查询物流信息
            LogisticsQueryResultDto result = logisticsService.queryLogisticsByOrderNo(orderNo);
            
            if (result != null) {
                log.info("物流信息查询成功，订单号：{}，物流状态：{}", orderNo, result.getLogisticsStatus());
                return R.ok("物流信息查询成功", result);
            } else {
                log.warn("未找到物流信息，订单号：{}", orderNo);
                return R.fail("未找到物流信息", null);
            }
            
        } catch (Exception e) {
            log.error("查询物流信息异常，订单号：{}", orderNo, e);
            return R.fail("查询物流信息异常: " + e.getMessage(), null);
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
