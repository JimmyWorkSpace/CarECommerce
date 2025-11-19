package com.ruoyi.car.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.framework.service.FtpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 通用文件上传Controller
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/car/upload")
public class FileUploadController extends BaseController {
    
    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);
    
    @Resource
    private FtpService ftpService;
    
    @Value("${carce.prefix}")
    private String imagePrefix;
    
    /**
     * 默认上传目录
     */
    private static final String DEFAULT_UPLOAD_DIR = "/img/car_sale/common";
    
    /**
     * 默认允许的图片扩展名
     */
    private static final String[] DEFAULT_IMAGE_EXTENSIONS = {".jpg", ".jpeg", ".png", ".gif", ".bmp", ".webp"};
    
    /**
     * 默认允许的文件扩展名（包括图片和文档）
     */
    private static final String[] DEFAULT_FILE_EXTENSIONS = {
        ".jpg", ".jpeg", ".png", ".gif", ".bmp", ".webp",
        ".pdf", ".doc", ".docx", ".xls", ".xlsx", ".ppt", ".pptx",
        ".txt", ".zip", ".rar", ".7z"
    };
    
    /**
     * 单文件上传（通用）
     * 
     * @param file 文件
     * @param dir 上传目录（可选，默认为 /img/car_sale/common）
     * @param type 文件类型限制（可选，image-仅图片，file-所有允许的文件，不传则不限制）
     * @return 上传结果
     */
    @PreAuthorize("@ss.hasPermi('car:upload:upload')")
    @Log(title = "文件上传", businessType = BusinessType.OTHER)
    @PostMapping("/file")
    public AjaxResult uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "dir", required = false) String dir,
            @RequestParam(value = "type", required = false) String type) {
        try {
            if (file.isEmpty()) {
                return error("请选择要上传的文件");
            }
            
            // 确定上传目录
            String uploadDir = (dir != null && !dir.trim().isEmpty()) ? dir : DEFAULT_UPLOAD_DIR;
            
            // 文件类型验证
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null) {
                return error("文件名不能为空");
            }
            
            // 根据type参数进行文件类型验证
            if ("image".equalsIgnoreCase(type)) {
                if (!isAllowedFile(originalFilename, DEFAULT_IMAGE_EXTENSIONS)) {
                    return error("只能上传图片文件（jpg, jpeg, png, gif, bmp, webp）");
                }
            } else if ("file".equalsIgnoreCase(type)) {
                if (!isAllowedFile(originalFilename, DEFAULT_FILE_EXTENSIONS)) {
                    return error("不支持的文件类型");
                }
            }
            // type为空或不匹配时不限制文件类型
            
            // 上传到FTP服务器
            InputStream inputStream = file.getInputStream();
            String fileName = ftpService.uploadFile(inputStream, originalFilename, uploadDir);
            
            // 返回完整的文件URL
            String fileUrl = imagePrefix + uploadDir + "/" + fileName;
            
            AjaxResult result = AjaxResult.success("上传成功");
            result.put("url", fileUrl);
            result.put("fileName", fileName);
            result.put("originalFilename", originalFilename);
            result.put("dir", uploadDir);
            
            return result;
            
        } catch (Exception e) {
            logger.error("文件上传失败", e);
            return error("文件上传失败：" + e.getMessage());
        }
    }
    
    /**
     * 多文件上传（通用）
     * 
     * @param files 文件列表
     * @param dir 上传目录（可选，默认为 /img/car_sale/common）
     * @param type 文件类型限制（可选，image-仅图片，file-所有允许的文件，不传则不限制）
     * @return 上传结果
     */
    @PreAuthorize("@ss.hasPermi('car:upload:upload')")
    @Log(title = "批量文件上传", businessType = BusinessType.OTHER)
    @PostMapping("/files")
    public AjaxResult uploadFiles(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam(value = "dir", required = false) String dir,
            @RequestParam(value = "type", required = false) String type) {
        try {
            if (files == null || files.length == 0) {
                return error("请选择要上传的文件");
            }
            
            // 确定上传目录
            String uploadDir = (dir != null && !dir.trim().isEmpty()) ? dir : DEFAULT_UPLOAD_DIR;
            
            List<String> urls = new ArrayList<>();
            List<String> fileNames = new ArrayList<>();
            List<String> originalFilenames = new ArrayList<>();
            List<String> errors = new ArrayList<>();
            
            for (MultipartFile file : files) {
                try {
                    if (file.isEmpty()) {
                        errors.add(file.getOriginalFilename() + ": 文件为空");
                        continue;
                    }
                    
                    String originalFilename = file.getOriginalFilename();
                    if (originalFilename == null) {
                        errors.add("未知文件名: 文件名不能为空");
                        continue;
                    }
                    
                    // 文件类型验证
                    if ("image".equalsIgnoreCase(type)) {
                        if (!isAllowedFile(originalFilename, DEFAULT_IMAGE_EXTENSIONS)) {
                            errors.add(originalFilename + ": 只能上传图片文件");
                            continue;
                        }
                    } else if ("file".equalsIgnoreCase(type)) {
                        if (!isAllowedFile(originalFilename, DEFAULT_FILE_EXTENSIONS)) {
                            errors.add(originalFilename + ": 不支持的文件类型");
                            continue;
                        }
                    }
                    
                    // 上传到FTP服务器
                    InputStream inputStream = file.getInputStream();
                    String fileName = ftpService.uploadFile(inputStream, originalFilename, uploadDir);
                    
                    // 构建完整的文件URL
                    String fileUrl = imagePrefix + uploadDir + "/" + fileName;
                    
                    urls.add(fileUrl);
                    fileNames.add(fileName);
                    originalFilenames.add(originalFilename);
                    
                } catch (Exception e) {
                    logger.error("文件上传失败: {}", file.getOriginalFilename(), e);
                    errors.add(file.getOriginalFilename() + ": " + e.getMessage());
                }
            }
            
            AjaxResult result = AjaxResult.success();
            result.put("urls", urls);
            result.put("fileNames", fileNames);
            result.put("originalFilenames", originalFilenames);
            result.put("successCount", urls.size());
            result.put("totalCount", files.length);
            result.put("dir", uploadDir);
            
            if (!errors.isEmpty()) {
                result.put("errors", errors);
                result.put("message", "部分文件上传失败，成功: " + urls.size() + "，失败: " + errors.size());
            } else {
                result.put("message", "所有文件上传成功，共 " + urls.size() + " 个文件");
            }
            
            return result;
            
        } catch (Exception e) {
            logger.error("批量文件上传失败", e);
            return error("批量文件上传失败：" + e.getMessage());
        }
    }
    
    /**
     * 图片上传（快捷方法，仅支持图片）
     * 
     * @param file 图片文件
     * @param dir 上传目录（可选，默认为 /img/car_sale/common）
     * @return 上传结果
     */
    @PreAuthorize("@ss.hasPermi('car:upload:upload')")
    @Log(title = "图片上传", businessType = BusinessType.OTHER)
    @PostMapping("/image")
    public AjaxResult uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "dir", required = false) String dir) {
        return uploadFile(file, dir, "image");
    }
    
    /**
     * 检查文件扩展名是否在允许的列表中
     * 
     * @param fileName 文件名
     * @param allowedExtensions 允许的扩展名数组
     * @return 是否允许
     */
    private boolean isAllowedFile(String fileName, String[] allowedExtensions) {
        if (fileName == null || allowedExtensions == null) {
            return false;
        }
        
        String lowerFileName = fileName.toLowerCase();
        for (String ext : allowedExtensions) {
            if (lowerFileName.endsWith(ext.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}

