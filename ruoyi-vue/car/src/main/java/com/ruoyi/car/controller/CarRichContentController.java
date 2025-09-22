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
import com.ruoyi.car.domain.CarRichContentEntity;
import com.ruoyi.car.service.ICarRichContentService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 富文本内容Controller
 * 
 * @author ruoyi
 * @date 2024-01-01
 */
@RestController
@RequestMapping("/car/richContent")
public class CarRichContentController extends BaseController
{
    @Autowired
    private ICarRichContentService carRichContentService;

    /**
     * 查询富文本内容列表
     */
    @PreAuthorize("@ss.hasPermi('car:richContent:list')")
    @GetMapping("/list")
    public TableDataInfo list(CarRichContentEntity carRichContent)
    {
        startPage();
        List<CarRichContentEntity> list = carRichContentService.selectCarRichContentList(carRichContent);
        return getDataTable(list);
    }

    /**
     * 导出富文本内容列表
     */
    @PreAuthorize("@ss.hasPermi('car:richContent:export')")
    @Log(title = "富文本内容", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CarRichContentEntity carRichContent)
    {
        List<CarRichContentEntity> list = carRichContentService.selectCarRichContentList(carRichContent);
        ExcelUtil<CarRichContentEntity> util = new ExcelUtil<CarRichContentEntity>(CarRichContentEntity.class);
        util.exportExcel(response, list, "富文本内容数据");
    }

    /**
     * 获取富文本内容详细信息
     */
    @PreAuthorize("@ss.hasPermi('car:richContent:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(carRichContentService.selectCarRichContentById(id));
    }

    /**
     * 新增富文本内容
     */
    @PreAuthorize("@ss.hasPermi('car:richContent:add')")
    @Log(title = "富文本内容", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CarRichContentEntity carRichContent)
    {
        return toAjax(carRichContentService.insertCarRichContent(carRichContent));
    }

    /**
     * 修改富文本内容
     */
    @PreAuthorize("@ss.hasPermi('car:richContent:edit')")
    @Log(title = "富文本内容", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CarRichContentEntity carRichContent)
    {
        return toAjax(carRichContentService.updateCarRichContent(carRichContent));
    }

    /**
     * 删除富文本内容
     */
    @PreAuthorize("@ss.hasPermi('car:richContent:remove')")
    @Log(title = "富文本内容", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(carRichContentService.deleteCarRichContentByIds(ids));
    }

    /**
     * 更新富文本内容排序
     */
    @PreAuthorize("@ss.hasPermi('car:richContent:edit')")
    @Log(title = "富文本内容排序", businessType = BusinessType.UPDATE)
    @PutMapping("/order")
    public AjaxResult updateOrder(@RequestBody List<CarRichContentEntity> carRichContentList)
    {
        return toAjax(carRichContentService.updateCarRichContentOrder(carRichContentList));
    }
}
