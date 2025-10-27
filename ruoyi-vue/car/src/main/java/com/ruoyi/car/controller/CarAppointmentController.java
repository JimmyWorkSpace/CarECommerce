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
import com.ruoyi.car.domain.CarAppointmentEntity;
import com.ruoyi.car.dto.CarAppointmentDto;
import com.ruoyi.car.service.CarAppointmentService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 预约看车Controller
 * 
 * @author ruoyi
 * @date 2025-01-16
 */
@RestController
@RequestMapping("/car/appointment")
public class CarAppointmentController extends BaseController
{
    @Autowired
    private CarAppointmentService carAppointmentService;

    /**
     * 查询预约看车列表
     */
    @PreAuthorize("@ss.hasPermi('car:appointment:list')")
    @GetMapping("/list")
    public TableDataInfo list(CarAppointmentEntity carAppointment)
    {
        startPage();
        List<CarAppointmentDto> list = carAppointmentService.selectCarAppointmentListWithSaleTitle(carAppointment);
        return getDataTable(list);
    }

    /**
     * 导出预约看车列表
     */
    @PreAuthorize("@ss.hasPermi('car:appointment:export')")
    @Log(title = "预约看车", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CarAppointmentEntity carAppointment)
    {
        List<CarAppointmentEntity> list = carAppointmentService.selectCarAppointmentList(carAppointment);
        ExcelUtil<CarAppointmentEntity> util = new ExcelUtil<CarAppointmentEntity>(CarAppointmentEntity.class);
        util.exportExcel(response, list, "预约看车数据");
    }

    /**
     * 获取预约看车详细信息
     */
    @PreAuthorize("@ss.hasPermi('car:appointment:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(carAppointmentService.selectCarAppointmentById(id));
    }

    /**
     * 新增预约看车
     */
    @PreAuthorize("@ss.hasPermi('car:appointment:add')")
    @Log(title = "预约看车", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CarAppointmentEntity carAppointment)
    {
        return toAjax(carAppointmentService.insertCarAppointment(carAppointment));
    }

    /**
     * 修改预约看车
     */
    @PreAuthorize("@ss.hasPermi('car:appointment:edit')")
    @Log(title = "预约看车", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CarAppointmentEntity carAppointment)
    {
        return toAjax(carAppointmentService.updateCarAppointment(carAppointment));
    }

    /**
     * 刪除预约看车
     */
    @PreAuthorize("@ss.hasPermi('car:appointment:remove')")
    @Log(title = "预约看车", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(carAppointmentService.deleteCarAppointmentByIds(ids));
    }
}
