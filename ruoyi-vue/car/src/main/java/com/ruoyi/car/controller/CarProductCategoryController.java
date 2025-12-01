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
import com.ruoyi.car.domain.CarProductCategoryEntity;
import com.ruoyi.car.service.ICarProductCategoryService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.domain.TreeSelect;

/**
 * 商品目錄分類Controller
 * 
 * @author ruoyi
 * @date 2024-01-01
 */
@RestController
@RequestMapping("/car/productCategory")
public class CarProductCategoryController extends BaseController
{
    @Autowired
    private ICarProductCategoryService carProductCategoryService;

    /**
     * 查詢商品目錄分類列表
     */
    @PreAuthorize("@ss.hasPermi('car:productCategory:list')")
    @GetMapping("/list")
    public TableDataInfo list(CarProductCategoryEntity carProductCategory)
    {
        startPage();
        List<CarProductCategoryEntity> list = carProductCategoryService.selectCarProductCategoryList(carProductCategory);
        return getDataTable(list);
    }

    /**
     * 導出商品目錄分類列表
     */
    @PreAuthorize("@ss.hasPermi('car:productCategory:export')")
    @Log(title = "商品目錄分類", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CarProductCategoryEntity carProductCategory)
    {
        List<CarProductCategoryEntity> list = carProductCategoryService.selectCarProductCategoryList(carProductCategory);
        ExcelUtil<CarProductCategoryEntity> util = new ExcelUtil<CarProductCategoryEntity>(CarProductCategoryEntity.class);
        util.exportExcel(response, list, "商品目錄分類數據");
    }

    /**
     * 獲取商品目錄分類詳細信息
     */
    @PreAuthorize("@ss.hasPermi('car:productCategory:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(carProductCategoryService.selectCarProductCategoryById(id));
    }

    /**
     * 獲取商品目錄分類下拉樹列表
     */
    @GetMapping("/treeselect")
    public AjaxResult treeselect(CarProductCategoryEntity carProductCategory)
    {
        List<CarProductCategoryEntity> categories = carProductCategoryService.selectCarProductCategoryList(carProductCategory);
        List<TreeSelect> categoryTrees = carProductCategoryService.buildCategoryTreeSelect(categories);
        return AjaxResult.success(categoryTrees);
    }

    /**
     * 加載對應角色商品目錄分類列表樹
     */
    @GetMapping(value = "/roleCategoryTreeselect/{roleId}")
    public AjaxResult roleCategoryTreeselect(@PathVariable("roleId") Long roleId)
    {
        List<CarProductCategoryEntity> categories = carProductCategoryService.selectCarProductCategoryList(new CarProductCategoryEntity());
        AjaxResult ajax = AjaxResult.success();
        ajax.put("checkedKeys", new Long[0]);
        ajax.put("categories", carProductCategoryService.buildCategoryTreeSelect(categories));
        return ajax;
    }

    /**
     * 新增商品目錄分類
     */
    @PreAuthorize("@ss.hasPermi('car:productCategory:add')")
    @Log(title = "商品目錄分類", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CarProductCategoryEntity carProductCategory)
    {
        return toAjax(carProductCategoryService.insertCarProductCategory(carProductCategory));
    }

    /**
     * 修改商品目錄分類
     */
    @PreAuthorize("@ss.hasPermi('car:productCategory:edit')")
    @Log(title = "商品目錄分類", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CarProductCategoryEntity carProductCategory)
    {
        return toAjax(carProductCategoryService.updateCarProductCategory(carProductCategory));
    }

    /**
     * 刪除商品目錄分類
     */
    @PreAuthorize("@ss.hasPermi('car:productCategory:remove')")
    @Log(title = "商品目錄分類", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(carProductCategoryService.deleteCarProductCategoryByIds(ids));
    }
}

