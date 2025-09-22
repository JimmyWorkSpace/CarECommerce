package com.ruoyi.car.controller;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.car.domain.CarOrderDetailEntity;
import com.ruoyi.car.service.ICarOrderDetailService;

/**
 * 订单详情Controller
 * 
 * @author ruoyi
 * @date 2024-01-01
 */
@RestController
@RequestMapping("/car/orderDetail")
public class CarOrderDetailController extends BaseController
{
    @Autowired
    private ICarOrderDetailService carOrderDetailService;

    /**
     * 查询订单详情列表
     */
    @PreAuthorize("@ss.hasPermi('car:orderDetail:list')")
    @GetMapping("/list")
    public TableDataInfo list(CarOrderDetailEntity carOrderDetail)
    {
        startPage();
        List<CarOrderDetailEntity> list = carOrderDetailService.selectCarOrderDetailList(carOrderDetail);
        return getDataTable(list);
    }
}
