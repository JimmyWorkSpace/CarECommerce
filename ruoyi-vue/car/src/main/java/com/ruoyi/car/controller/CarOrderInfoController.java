package com.ruoyi.car.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.car.domain.CarOrderInfoEntity;
import com.ruoyi.car.service.ICarOrderInfoService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 订单信息Controller
 * 
 * @author ruoyi
 * @date 2024-01-01
 */
@RestController
@RequestMapping("/car/orderInfo")
public class CarOrderInfoController extends BaseController
{
    @Autowired
    private ICarOrderInfoService carOrderInfoService;

    /**
     * 查询订单信息列表
     */
    @PreAuthorize("@ss.hasPermi('car:orderInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(CarOrderInfoEntity carOrderInfo)
    {
        startPage();
        List<CarOrderInfoEntity> list = carOrderInfoService.selectCarOrderInfoList(carOrderInfo);
        return getDataTable(list);
    }

    /**
     * 导出订单信息列表
     */
    @PreAuthorize("@ss.hasPermi('car:orderInfo:export')")
    @Log(title = "订单信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CarOrderInfoEntity carOrderInfo)
    {
        List<CarOrderInfoEntity> list = carOrderInfoService.selectCarOrderInfoList(carOrderInfo);
        ExcelUtil<CarOrderInfoEntity> util = new ExcelUtil<CarOrderInfoEntity>(CarOrderInfoEntity.class);
        util.exportExcel(response, list, "订单信息数据");
    }

    /**
     * 获取订单信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('car:orderInfo:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(carOrderInfoService.selectCarOrderInfoById(id));
    }

    /**
     * 新增订单信息
     */
    @PreAuthorize("@ss.hasPermi('car:orderInfo:add')")
    @Log(title = "订单信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CarOrderInfoEntity carOrderInfo)
    {
        return toAjax(carOrderInfoService.insertCarOrderInfo(carOrderInfo));
    }

    /**
     * 修改订单信息
     */
    @PreAuthorize("@ss.hasPermi('car:orderInfo:edit')")
    @Log(title = "订单信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CarOrderInfoEntity carOrderInfo)
    {
        return toAjax(carOrderInfoService.updateCarOrderInfo(carOrderInfo));
    }

    /**
     * 删除订单信息
     */
    @PreAuthorize("@ss.hasPermi('car:orderInfo:remove')")
    @Log(title = "订单信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(carOrderInfoService.deleteCarOrderInfoByIds(ids));
    }
}
