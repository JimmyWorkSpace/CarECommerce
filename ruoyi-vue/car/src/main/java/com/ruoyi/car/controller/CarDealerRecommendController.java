package com.ruoyi.car.controller;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.car.domain.CarDealerEntity;
import com.ruoyi.car.service.CarDealerService;
import com.ruoyi.car.service.ICarRecommandService;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 精选卖家Controller
 * 
 * @author ruoyi
 * @date 2024-01-01
 */
@RestController
@RequestMapping("/car/dealerRecommend")
public class CarDealerRecommendController extends BaseController
{
    @Autowired
    private CarDealerService carDealerService;
    
    @Autowired
    private ICarRecommandService carRecommandService;

    /**
     * 查询经销商列表（用于精选卖家管理）
     */
    @PreAuthorize("@ss.hasPermi('car:dealerRecommend:list')")
    @GetMapping("/list")
    public TableDataInfo list(CarDealerEntity carDealer)
    {
        startPage();
        List<CarDealerEntity> list = carDealerService.selectCarDealerList(carDealer);
        
        // 为每个经销商记录添加推荐状态
        for (CarDealerEntity dealer : list) {
            // 检查是否已推荐
            boolean isRecommended = carRecommandService.selectByRecommandTypeAndId(0, dealer.getId()) != null;
            dealer.setRecommendedValue(isRecommended ? 1L : 0L);
        }
        
        return getDataTable(list);
    }
    
    /**
     * 设置经销商推荐状态
     */
    @PreAuthorize("@ss.hasPermi('car:dealerRecommend:edit')")
    @Log(title = "精选卖家", businessType = BusinessType.UPDATE)
    @PutMapping("/setRecommended")
    public AjaxResult setRecommended(@RequestBody CarDealerEntity carDealer)
    {
        Boolean isRecommended = carDealer.getRecommendedValue() != null && carDealer.getRecommendedValue() == 1;
        int result = carRecommandService.setRecommended(0, carDealer.getId(), isRecommended);
        return toAjax(result);
    }
}
