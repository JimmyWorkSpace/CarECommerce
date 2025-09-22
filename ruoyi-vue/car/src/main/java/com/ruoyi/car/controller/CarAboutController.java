package com.ruoyi.car.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.car.domain.CarRichContentEntity;
import com.ruoyi.car.service.ICarAboutService;

/**
 * 關於Controller
 * 
 * @author ruoyi
 * @date 2024-01-01
 */
@RestController
@RequestMapping("/car/about")
public class CarAboutController extends BaseController
{
    @Autowired
    private ICarAboutService carAboutService;

    /**
     * 獲取關於內容
     */
    @PreAuthorize("@ss.hasPermi('car:about:query')")
    @GetMapping
    public AjaxResult getAbout()
    {
        CarRichContentEntity about = carAboutService.getAbout();
        return AjaxResult.success(about);
    }

    /**
     * 修改關於內容
     */
    @PreAuthorize("@ss.hasPermi('car:about:edit')")
    @Log(title = "關於", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult editAbout(@RequestBody CarRichContentEntity carRichContent)
    {
        return toAjax(carAboutService.updateAbout(carRichContent));
    }
}
