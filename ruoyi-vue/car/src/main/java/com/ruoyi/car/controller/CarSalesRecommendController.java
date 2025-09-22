package com.ruoyi.car.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.car.domain.CarSalesEntity;
import com.ruoyi.car.dto.CarSalesDto;
import com.ruoyi.car.service.CarSalesService;
import com.ruoyi.car.service.CarService;
import com.ruoyi.car.service.ICarRecommandService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;

/**
 * 精选好车Controller
 * 
 * @author ruoyi
 * @date 2024-01-01
 */
@RestController
@RequestMapping("/car/salesRecommend")
public class CarSalesRecommendController extends BaseController
{
    @Autowired
    private CarSalesService carSalesService;
    
    @Autowired
    private CarService carService;
    
    @Autowired
    private ICarRecommandService carRecommandService;

    /**
     * 查询车辆销售列表（用于精选好车管理）
     */
    @PreAuthorize("@ss.hasPermi('car:salesRecommend:list')")
    @GetMapping("/list")
    public TableDataInfo list(CarSalesEntity carSales)
    {
        startPage();
        List<CarSalesDto> list = carSalesService.selectCarSalesList(carSales);
        
        
        return getDataTable(list);
    }
    
    /**
     * 设置车辆推荐状态
     */
    @PreAuthorize("@ss.hasPermi('car:salesRecommend:edit')")
    @Log(title = "精选好车", businessType = BusinessType.UPDATE)
    @PutMapping("/setRecommended")
    public AjaxResult setRecommended(@RequestBody CarSalesDto carSales)
    {
        Boolean isRecommended = carSales.getRecommendedValue() != null && carSales.getRecommendedValue() == 1;
        int result = carRecommandService.setRecommended(1, carSales.getId(), isRecommended);
        return toAjax(result);
    }
}
