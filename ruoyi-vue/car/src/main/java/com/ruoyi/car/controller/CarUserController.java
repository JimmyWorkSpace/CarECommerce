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
import com.ruoyi.car.domain.CarUserEntity;
import com.ruoyi.car.service.ICarUserService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 用户管理Controller
 * 
 * @author ruoyi
 * @date 2024-01-01
 */
@RestController
@RequestMapping("/car/user")
public class CarUserController extends BaseController
{
    @Autowired
    private ICarUserService carUserService;

    /**
     * 查询用户管理列表
     */
    @PreAuthorize("@ss.hasPermi('car:user:list')")
    @GetMapping("/list")
    public TableDataInfo list(CarUserEntity carUser)
    {
        startPage();
        List<CarUserEntity> list = carUserService.selectCarUserList(carUser);
        return getDataTable(list);
    }

    /**
     * 导出用户管理列表
     */
    @PreAuthorize("@ss.hasPermi('car:user:export')")
    @Log(title = "用户管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CarUserEntity carUser)
    {
        List<CarUserEntity> list = carUserService.selectCarUserList(carUser);
        ExcelUtil<CarUserEntity> util = new ExcelUtil<CarUserEntity>(CarUserEntity.class);
        util.exportExcel(response, list, "用户管理数据");
    }

    /**
     * 获取用户管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('car:user:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(carUserService.selectCarUserById(id));
    }

    /**
     * 新增用户管理
     */
    @PreAuthorize("@ss.hasPermi('car:user:add')")
    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CarUserEntity carUser)
    {
        return toAjax(carUserService.insertCarUser(carUser));
    }

    /**
     * 修改用户管理
     */
    @PreAuthorize("@ss.hasPermi('car:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CarUserEntity carUser)
    {
        return toAjax(carUserService.updateCarUser(carUser));
    }

    /**
     * 刪除用户管理
     */
    @PreAuthorize("@ss.hasPermi('car:user:remove')")
    @Log(title = "用户管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(carUserService.deleteCarUserByIds(ids));
    }
}
