package com.ruoyi.car.controller;

import com.ruoyi.car.domain.CarBannerEntity;
import com.ruoyi.car.service.CarBannerService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.service.FtpService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.List;

/**
 * 輪播圖維護Controller
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/car/banner")
public class CarBannerController extends BaseController {
    
    @Resource
    private CarBannerService carBannerService;
    
    @Resource
    private FtpService ftpService;
    
    @Value("${carce.prefix}")
    private String imagePrefix;

    /**
     * 查詢輪播圖列表
     */
    @PreAuthorize("@ss.hasPermi('car:banner:list')")
    @GetMapping("/list")
    public TableDataInfo list(CarBannerEntity carBanner) {
        startPage();
        List<CarBannerEntity> list = carBannerService.selectAllBanners();
        return getDataTable(list);
    }

    /**
     * 匯出輪播圖列表
     */
    @PreAuthorize("@ss.hasPermi('car:banner:export')")
    @Log(title = "輪播圖", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CarBannerEntity carBanner) {
        List<CarBannerEntity> list = carBannerService.selectAllBanners();
        ExcelUtil<CarBannerEntity> util = new ExcelUtil<CarBannerEntity>(CarBannerEntity.class);
        util.exportExcel(response, list, "輪播圖資料");
    }

    /**
     * 獲取輪播圖詳細資訊
     */
    @PreAuthorize("@ss.hasPermi('car:banner:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(carBannerService.selectBannerById(id));
    }

    /**
     * 新增輪播圖
     */
    @PreAuthorize("@ss.hasPermi('car:banner:add')")
    @Log(title = "輪播圖", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CarBannerEntity carBanner) {
        return toAjax(carBannerService.insertBanner(carBanner));
    }

    /**
     * 修改輪播圖
     */
    @PreAuthorize("@ss.hasPermi('car:banner:edit')")
    @Log(title = "輪播圖", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CarBannerEntity carBanner) {
        return toAjax(carBannerService.updateBanner(carBanner));
    }

    /**
     * 刪除輪播圖
     */
    @PreAuthorize("@ss.hasPermi('car:banner:remove')")
    @Log(title = "輪播圖", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        int result = 0;
        for (Long id : ids) {
            result += carBannerService.deleteBanner(id);
        }
        return toAjax(result);
    }
    
    /**
     * 上傳圖片
     */
    @PreAuthorize("@ss.hasPermi('car:banner:upload')")
    @PostMapping("/upload")
    public AjaxResult uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return error("請選擇要上傳的檔案");
            }
            
            // 檢查檔案類型
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || !isImageFile(originalFilename)) {
                return error("只能上傳圖片檔案");
            }
            
            // 上傳到FTP伺服器
            InputStream inputStream = file.getInputStream();
            String fileName = ftpService.uploadFile(inputStream, originalFilename, "/img/car_sale/banner");
            
            // 返回完整的圖片URL
            String imageUrl = imagePrefix + "/img/car_sale/banner/" + fileName;
            
            return AjaxResult.success("上傳成功", imageUrl);
            
        } catch (Exception e) {
            logger.error("圖片上傳失敗", e);
            return error("圖片上傳失敗：" + e.getMessage());
        }
    }
    
    /**
     * 更新輪播圖排序
     */
    @PreAuthorize("@ss.hasPermi('car:banner:edit')")
    @Log(title = "輪播圖排序", businessType = BusinessType.UPDATE)
    @PutMapping("/order")
    public AjaxResult updateOrder(@RequestBody List<CarBannerEntity> banners) {
        return toAjax(carBannerService.updateBannerOrder(banners));
    }
    
    /**
     * 檢查是否為圖片檔案
     */
    private boolean isImageFile(String fileName) {
        String[] allowedExtensions = {".jpg", ".jpeg", ".png", ".gif", ".bmp", ".webp"};
        String lowerFileName = fileName.toLowerCase();
        for (String ext : allowedExtensions) {
            if (lowerFileName.endsWith(ext)) {
                return true;
            }
        }
        return false;
    }
} 