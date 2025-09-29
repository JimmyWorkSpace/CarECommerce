package com.ruoyi.car.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.car.domain.CarConfigEntity;
import com.ruoyi.car.service.ICarConfigService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 网站配置Controller
 * 
 * @author ruoyi
 * @date 2024-01-01
 */
@RestController
@RequestMapping("/car/config")
public class CarConfigController extends BaseController
{
    @Autowired
    private ICarConfigService carConfigService;

    /**
     * 查询网站配置列表
     */
    @PreAuthorize("@ss.hasPermi('car:config:list')")
    @GetMapping("/list")
    public TableDataInfo list(CarConfigEntity carConfig)
    {
        startPage();
        List<CarConfigEntity> list = carConfigService.selectCarConfigList(carConfig);
        return getDataTable(list);
    }

    /**
     * 导出网站配置列表
     */
    @PreAuthorize("@ss.hasPermi('car:config:export')")
    @Log(title = "网站配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CarConfigEntity carConfig)
    {
        List<CarConfigEntity> list = carConfigService.selectCarConfigList(carConfig);
        ExcelUtil<CarConfigEntity> util = new ExcelUtil<CarConfigEntity>(CarConfigEntity.class);
        util.exportExcel(response, list, "网站配置数据");
    }

    /**
     * 获取网站配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('car:config:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(carConfigService.selectCarConfigById(id));
    }

    /**
     * 修改网站配置
     */
    @PreAuthorize("@ss.hasPermi('car:config:edit')")
    @Log(title = "网站配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CarConfigEntity carConfig)
    {
        return toAjax(carConfigService.updateCarConfig(carConfig));
    }
}
