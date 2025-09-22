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
import com.ruoyi.car.domain.CarReportEntity;
import com.ruoyi.car.service.CarReportService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 車輛檢舉Controller
 * 
 * @author ruoyi
 * @date 2025-01-16
 */
@RestController
@RequestMapping("/car/report")
public class CarReportController extends BaseController
{
    @Autowired
    private CarReportService carReportService;

    /**
     * 查询車輛檢舉列表
     */
    @PreAuthorize("@ss.hasPermi('car:report:list')")
    @GetMapping("/list")
    public TableDataInfo list(CarReportEntity carReport)
    {
        startPage();
        List<com.ruoyi.car.dto.CarReportDto> list = carReportService.selectCarReportList(carReport);
        return getDataTable(list);
    }

    /**
     * 导出車輛檢舉列表
     */
    @PreAuthorize("@ss.hasPermi('car:report:export')")
    @Log(title = "車輛檢舉", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CarReportEntity carReport)
    {
        List<com.ruoyi.car.dto.CarReportDto> list = carReportService.selectCarReportList(carReport);
        ExcelUtil<com.ruoyi.car.dto.CarReportDto> util = new ExcelUtil<com.ruoyi.car.dto.CarReportDto>(com.ruoyi.car.dto.CarReportDto.class);
        util.exportExcel(response, list, "車輛檢舉数据");
    }

    /**
     * 获取車輛檢舉详细信息
     */
    @PreAuthorize("@ss.hasPermi('car:report:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(carReportService.selectCarReportById(id));
    }

    /**
     * 新增車輛檢舉
     */
    @PreAuthorize("@ss.hasPermi('car:report:add')")
    @Log(title = "車輛檢舉", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CarReportEntity carReport)
    {
        return toAjax(carReportService.insertCarReport(carReport));
    }

    /**
     * 修改車輛檢舉
     */
    @PreAuthorize("@ss.hasPermi('car:report:edit')")
    @Log(title = "車輛檢舉", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CarReportEntity carReport)
    {
        return toAjax(carReportService.updateCarReport(carReport));
    }

    /**
     * 删除車輛檢舉
     */
    @PreAuthorize("@ss.hasPermi('car:report:remove')")
    @Log(title = "車輛檢舉", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(carReportService.deleteCarReportByIds(ids));
    }
}
