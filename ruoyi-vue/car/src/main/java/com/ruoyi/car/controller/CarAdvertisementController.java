package com.ruoyi.car.controller;

import com.ruoyi.car.domain.CarAdvertisementEntity;
import com.ruoyi.car.service.CarAdvertisementService;
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
 * 广告位维护Controller
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/car/advertisement")
public class CarAdvertisementController extends BaseController {
    
    @Resource
    private CarAdvertisementService carAdvertisementService;
    
    @Resource
    private FtpService ftpService;
    
    @Value("${carce.prefix}")
    private String imagePrefix;

    /**
     * 查询广告列表
     */
    @PreAuthorize("@ss.hasPermi('car:advertisement:list')")
    @GetMapping("/list")
    public TableDataInfo list(CarAdvertisementEntity carAdvertisement) {
        startPage();
        List<CarAdvertisementEntity> list = carAdvertisementService.selectAdvertisementList(carAdvertisement);
        return getDataTable(list);
    }

    /**
     * 导出广告列表
     */
    @PreAuthorize("@ss.hasPermi('car:advertisement:export')")
    @Log(title = "广告位", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CarAdvertisementEntity carAdvertisement) {
        List<CarAdvertisementEntity> list = carAdvertisementService.selectAdvertisementList(carAdvertisement);
        ExcelUtil<CarAdvertisementEntity> util = new ExcelUtil<CarAdvertisementEntity>(CarAdvertisementEntity.class);
        util.exportExcel(response, list, "广告位数据");
    }

    /**
     * 获取广告详细信息
     */
    @PreAuthorize("@ss.hasPermi('car:advertisement:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(carAdvertisementService.selectAdvertisementById(id));
    }

    /**
     * 新增广告
     */
    @PreAuthorize("@ss.hasPermi('car:advertisement:add')")
    @Log(title = "广告位", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CarAdvertisementEntity carAdvertisement) {
        return toAjax(carAdvertisementService.insertAdvertisement(carAdvertisement));
    }

    /**
     * 修改广告
     */
    @PreAuthorize("@ss.hasPermi('car:advertisement:edit')")
    @Log(title = "广告位", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CarAdvertisementEntity carAdvertisement) {
        return toAjax(carAdvertisementService.updateAdvertisement(carAdvertisement));
    }

    /**
     * 刪除广告
     */
    @PreAuthorize("@ss.hasPermi('car:advertisement:remove')")
    @Log(title = "广告位", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        int result = 0;
        for (Long id : ids) {
            result += carAdvertisementService.deleteAdvertisement(id);
        }
        return toAjax(result);
    }
    
    /**
     * 上传圖片
     */
    @PreAuthorize("@ss.hasPermi('car:advertisement:upload')")
    @PostMapping("/upload")
    public AjaxResult uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return error("请选择要上传的文件");
            }
            
            // 检查文件類型
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || !isImageFile(originalFilename)) {
                return error("只能上传圖片文件");
            }
            
            // 上传到FTP服务器
            InputStream inputStream = file.getInputStream();
            String fileName = ftpService.uploadFile(inputStream, originalFilename, "/img/car_sale/advertisement");
            
            // 返回完整的圖片URL
            String imageUrl = imagePrefix + "/img/car_sale/advertisement/" + fileName;
            
            return AjaxResult.success("上传成功", imageUrl);
            
        } catch (Exception e) {
            logger.error("圖片上传失败", e);
            return error("圖片上传失败：" + e.getMessage());
        }
    }
    
    /**
     * 更新广告排序
     */
    @PreAuthorize("@ss.hasPermi('car:advertisement:edit')")
    @Log(title = "广告位排序", businessType = BusinessType.UPDATE)
    @PutMapping("/order")
    public AjaxResult updateOrder(@RequestBody List<CarAdvertisementEntity> advertisements) {
        return toAjax(carAdvertisementService.updateAdvertisementOrder(advertisements));
    }
    
    /**
     * 检查是否为圖片文件
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