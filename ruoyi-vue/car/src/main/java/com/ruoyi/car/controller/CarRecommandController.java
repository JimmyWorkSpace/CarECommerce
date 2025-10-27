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
import com.ruoyi.car.domain.CarRecommandEntity;
import com.ruoyi.car.service.ICarRecommandService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 推薦管理Controller
 * 
 * @author ruoyi
 * @date 2024-01-01
 */
@RestController
@RequestMapping("/car/recommand")
public class CarRecommandController extends BaseController
{
    @Autowired
    private ICarRecommandService carRecommandService;

    /**
     * 查询推薦管理列表
     */
    @PreAuthorize("@ss.hasPermi('car:recommand:list')")
    @GetMapping("/list")
    public TableDataInfo list(CarRecommandEntity carRecommand)
    {
        startPage();
        List<CarRecommandEntity> list = carRecommandService.selectCarRecommandList(carRecommand);
        return getDataTable(list);
    }

    /**
     * 导出推薦管理列表
     */
    @PreAuthorize("@ss.hasPermi('car:recommand:export')")
    @Log(title = "推薦管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CarRecommandEntity carRecommand)
    {
        List<CarRecommandEntity> list = carRecommandService.selectCarRecommandList(carRecommand);
        ExcelUtil<CarRecommandEntity> util = new ExcelUtil<CarRecommandEntity>(CarRecommandEntity.class);
        util.exportExcel(response, list, "推薦管理数据");
    }

    /**
     * 获取推薦管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('car:recommand:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(carRecommandService.selectCarRecommandById(id));
    }

    /**
     * 新增推薦管理
     */
    @PreAuthorize("@ss.hasPermi('car:recommand:add')")
    @Log(title = "推薦管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CarRecommandEntity carRecommand)
    {
        return toAjax(carRecommandService.insertCarRecommand(carRecommand));
    }

    /**
     * 修改推薦管理
     */
    @PreAuthorize("@ss.hasPermi('car:recommand:edit')")
    @Log(title = "推薦管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CarRecommandEntity carRecommand)
    {
        return toAjax(carRecommandService.updateCarRecommand(carRecommand));
    }

    /**
     * 刪除推薦管理
     */
    @PreAuthorize("@ss.hasPermi('car:recommand:remove')")
    @Log(title = "推薦管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(carRecommandService.deleteCarRecommandByIds(ids));
    }
    
    /**
     * 设置推薦狀態
     */
    @PreAuthorize("@ss.hasPermi('car:recommand:edit')")
    @Log(title = "推薦管理", businessType = BusinessType.UPDATE)
    @PutMapping("/setRecommended")
    public AjaxResult setRecommended(@RequestBody CarRecommandEntity carRecommand)
    {
        Boolean isRecommended = carRecommand.getDelFlag() == null || carRecommand.getDelFlag() == 0;
        return toAjax(carRecommandService.setRecommended(carRecommand.getRecommandType(), carRecommand.getRecommandId(), isRecommended));
    }
}
