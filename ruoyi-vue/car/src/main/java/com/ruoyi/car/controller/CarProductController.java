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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.car.domain.CarProductEntity;
import com.ruoyi.car.domain.CarProductImageEntity;
import com.ruoyi.car.domain.CarProductAttrEntity;
import com.ruoyi.car.service.ICarProductService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 商品Controller
 * 
 * @author ruoyi
 * @date 2024-01-01
 */
@RestController
@RequestMapping("/car/product")
public class CarProductController extends BaseController
{
    @Autowired
    private ICarProductService carProductService;

    /**
     * 查詢商品列表
     */
    @PreAuthorize("@ss.hasPermi('car:product:list')")
    @GetMapping("/list")
    public TableDataInfo list(CarProductEntity carProduct)
    {
        startPage();
        List<CarProductEntity> list = carProductService.selectCarProductList(carProduct);
        return getDataTable(list);
    }

    /**
     * 導出商品列表
     */
    @PreAuthorize("@ss.hasPermi('car:product:export')")
    @Log(title = "商品", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CarProductEntity carProduct)
    {
        List<CarProductEntity> list = carProductService.selectCarProductList(carProduct);
        ExcelUtil<CarProductEntity> util = new ExcelUtil<CarProductEntity>(CarProductEntity.class);
        util.exportExcel(response, list, "商品數據");
    }

    /**
     * 獲取商品詳細信息
     */
    @PreAuthorize("@ss.hasPermi('car:product:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        CarProductEntity product = carProductService.selectCarProductById(id);
        if (product != null) {
            // 查詢圖片列表
            List<CarProductImageEntity> images = carProductService.selectProductImageList(id);
            product.setImages(images);
            
            // 查詢屬性列表
            List<CarProductAttrEntity> attrs = carProductService.selectProductAttrList(id);
            product.setAttrs(attrs);
        }
        return AjaxResult.success(product);
    }

    /**
     * 新增商品
     */
    @PreAuthorize("@ss.hasPermi('car:product:add')")
    @Log(title = "商品", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CarProductEntity carProduct)
    {
        return toAjax(carProductService.insertCarProduct(carProduct));
    }

    /**
     * 修改商品
     */
    @PreAuthorize("@ss.hasPermi('car:product:edit')")
    @Log(title = "商品", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CarProductEntity carProduct)
    {
        return toAjax(carProductService.updateCarProduct(carProduct));
    }

    /**
     * 刪除商品
     */
    @PreAuthorize("@ss.hasPermi('car:product:remove')")
    @Log(title = "商品", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(carProductService.deleteCarProductByIds(ids));
    }

    /**
     * 上傳商品圖片
     */
    @PreAuthorize("@ss.hasPermi('car:product:edit')")
    @Log(title = "商品圖片上傳", businessType = BusinessType.OTHER)
    @PostMapping("/uploadImages")
    public AjaxResult uploadImages(
            @RequestParam("file") MultipartFile file,
            @RequestParam("productId") Long productId)
    {
        try {
            // 將單個文件轉換為數組，以兼容Service層的接口
            MultipartFile[] files = new MultipartFile[]{file};
            List<CarProductImageEntity> images = carProductService.uploadProductImages(files, productId);
            return AjaxResult.success("圖片上傳成功").put("images", images);
        } catch (Exception e) {
            return error("圖片上傳失敗：" + e.getMessage());
        }
    }

    /**
     * 刪除商品圖片
     */
    @PreAuthorize("@ss.hasPermi('car:product:edit')")
    @Log(title = "商品圖片刪除", businessType = BusinessType.DELETE)
    @DeleteMapping("/image/{imageId}")
    public AjaxResult deleteImage(@PathVariable("imageId") Long imageId)
    {
        return toAjax(carProductService.deleteProductImage(imageId));
    }

    /**
     * 保存商品屬性
     */
    @PreAuthorize("@ss.hasPermi('car:product:edit')")
    @Log(title = "商品屬性保存", businessType = BusinessType.UPDATE)
    @PostMapping("/saveAttrs")
    public AjaxResult saveAttrs(
            @RequestParam("productId") Long productId,
            @RequestParam("attrText") String attrText)
    {
        int count = carProductService.saveProductAttrs(productId, attrText);
        return AjaxResult.success("屬性保存成功，共保存 " + count + " 條屬性");
    }

    /**
     * 更新商品推薦狀態
     */
    @PreAuthorize("@ss.hasPermi('car:product:edit')")
    @Log(title = "商品推薦狀態", businessType = BusinessType.UPDATE)
    @PutMapping("/updateRecommended/{id}")
    public AjaxResult updateRecommended(@PathVariable("id") Long id, @RequestParam("isRecommended") Integer isRecommended)
    {
        CarProductEntity product = new CarProductEntity();
        product.setId(id);
        product.setIsRecommended(isRecommended);
        return toAjax(carProductService.updateCarProduct(product));
    }
}

