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
import com.ruoyi.car.domain.CarMenuEntity;
import com.ruoyi.car.service.CarMenuService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 菜單維護Controller
 * 
 * @author ruoyi
 * @date 2025-01-18
 */
@RestController
@RequestMapping("/car/menu")
public class CarMenuController extends BaseController
{
    @Autowired
    private CarMenuService carMenuService;

    /**
     * 查詢菜單維護列表
     */
    @PreAuthorize("@ss.hasPermi('car:menu:list')")
    @GetMapping("/list")
    public TableDataInfo list(CarMenuEntity carMenu)
    {
        startPage();
        List<CarMenuEntity> list = carMenuService.selectCarMenuList(carMenu);
        return getDataTable(list);
    }

    /**
     * 導出菜單維護列表
     */
    @PreAuthorize("@ss.hasPermi('car:menu:export')")
    @Log(title = "菜單維護", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CarMenuEntity carMenu)
    {
        List<CarMenuEntity> list = carMenuService.selectCarMenuList(carMenu);
        ExcelUtil<CarMenuEntity> util = new ExcelUtil<CarMenuEntity>(CarMenuEntity.class);
        util.exportExcel(response, list, "菜單維護數據");
    }

    /**
     * 獲取菜單維護詳細信息
     */
    @PreAuthorize("@ss.hasPermi('car:menu:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(carMenuService.selectCarMenuById(id));
    }

    /**
     * 新增菜單維護
     */
    @PreAuthorize("@ss.hasPermi('car:menu:add')")
    @Log(title = "菜單維護", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CarMenuEntity carMenu)
    {
        return toAjax(carMenuService.insertCarMenu(carMenu));
    }

    /**
     * 修改菜單維護
     */
    @PreAuthorize("@ss.hasPermi('car:menu:edit')")
    @Log(title = "菜單維護", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CarMenuEntity carMenu)
    {
        return toAjax(carMenuService.updateCarMenu(carMenu));
    }

    /**
     * 刪除菜單維護
     */
    @PreAuthorize("@ss.hasPermi('car:menu:remove')")
    @Log(title = "菜單維護", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(carMenuService.deleteCarMenuByIds(ids));
    }

    /**
     * 更新菜單顯示狀態
     */
    @PreAuthorize("@ss.hasPermi('car:menu:edit')")
    @Log(title = "菜單維護", businessType = BusinessType.UPDATE)
    @PutMapping("/updateShowStatus")
    public AjaxResult updateShowStatus(@RequestBody CarMenuEntity carMenu)
    {
        return toAjax(carMenuService.updateCarMenuShowStatus(carMenu.getId(), carMenu.getIsShow()));
    }

    /**
     * 更新菜單排序
     */
    @PreAuthorize("@ss.hasPermi('car:menu:edit')")
    @Log(title = "菜單維護", businessType = BusinessType.UPDATE)
    @PutMapping("/updateOrder")
    public AjaxResult updateOrder(@RequestBody CarMenuEntity carMenu)
    {
        return toAjax(carMenuService.updateCarMenuOrder(carMenu.getId(), carMenu.getShowOrder()));
    }

    /**
     * 批量更新菜單排序
     */
    @PreAuthorize("@ss.hasPermi('car:menu:edit')")
    @Log(title = "菜單維護", businessType = BusinessType.UPDATE)
    @PutMapping("/batchUpdateOrder")
    public AjaxResult batchUpdateOrder(@RequestBody List<CarMenuEntity> carMenuList)
    {
        int result = 0;
        for (CarMenuEntity carMenu : carMenuList) {
            result += carMenuService.updateCarMenuOrder(carMenu.getId(), carMenu.getShowOrder());
        }
        return toAjax(result);
    }
}
