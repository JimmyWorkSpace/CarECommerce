package com.ruoyi.car.controller;

import com.ruoyi.car.domain.CarCardEntity;
import com.ruoyi.car.service.CarCardService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 票券方案維護 Controller
 * 停用之票券方案不可再被購買；已售出票券不因方案停用而失效。
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/car/card")
public class CarCardController extends BaseController {

    @Resource
    private CarCardService carCardService;

    /**
     * 查詢票券方案列表
     */
    @PreAuthorize("@ss.hasPermi('car:card:list')")
    @GetMapping("/list")
    public TableDataInfo list(CarCardEntity carCard) {
        startPage();
        List<CarCardEntity> list = carCardService.selectCardList(carCard);
        return getDataTable(list);
    }

    /**
     * 導出票券方案列表
     */
    @PreAuthorize("@ss.hasPermi('car:card:export')")
    @Log(title = "票券方案", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CarCardEntity carCard) {
        List<CarCardEntity> list = carCardService.selectCardList(carCard);
        ExcelUtil<CarCardEntity> util = new ExcelUtil<>(CarCardEntity.class);
        util.exportExcel(response, list, "票券方案資料");
    }

    /**
     * 取得票券方案詳細資訊
     */
    @PreAuthorize("@ss.hasPermi('car:card:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(carCardService.selectCardById(id));
    }

    /**
     * 新增票券方案
     */
    @PreAuthorize("@ss.hasPermi('car:card:add')")
    @Log(title = "票券方案", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CarCardEntity carCard) {
        return toAjax(carCardService.insertCard(carCard));
    }

    /**
     * 修改票券方案
     */
    @PreAuthorize("@ss.hasPermi('car:card:edit')")
    @Log(title = "票券方案", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CarCardEntity carCard) {
        return toAjax(carCardService.updateCard(carCard));
    }

    /**
     * 刪除票券方案
     */
    @PreAuthorize("@ss.hasPermi('car:card:remove')")
    @Log(title = "票券方案", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(carCardService.deleteCardByIds(ids));
    }
}
