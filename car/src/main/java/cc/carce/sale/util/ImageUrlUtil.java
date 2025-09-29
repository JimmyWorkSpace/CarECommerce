package cc.carce.sale.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 图片URL处理工具类
 * 用于统一处理图片URL的前缀和缩略图转换
 */
@Component
public class ImageUrlUtil {
    
    @Value("${carce.prefix:}")
    private String imagePrefix;
    
    /**
     * 为图片URL添加前缀
     * @param imageUrl 原始图片URL
     * @return 带前缀的完整URL
     */
    public String addPrefix(String imageUrl) {
        if (imageUrl == null || imageUrl.trim().isEmpty()) {
            return "";
        }
        
        // 如果URL已经包含协议，直接返回
        if (imageUrl.startsWith("http://") || imageUrl.startsWith("https://")) {
            return imageUrl;
        }
        
        // 确保前缀以/结尾，URL不以/开头
        String prefix = imagePrefix;
        if (!prefix.endsWith("/")) {
            prefix += "/";
        }
        if (imageUrl.startsWith("/")) {
            imageUrl = imageUrl.substring(1);
        }
        
        return prefix + imageUrl;
    }
    
    /**
     * 获取缩略图URL（90x90）
     * @param originalUrl 原始图片URL
     * @return 缩略图URL
     */
    public String getThumbnailUrl(String originalUrl) {
        if (originalUrl == null || originalUrl.trim().isEmpty()) {
            return "";
        }
        
        // 检查URL是否已经包含_90x90后缀
        if (originalUrl.contains("_90x90.")) {
            return originalUrl;
        }
        
        // 获取文件扩展名
        int lastDotIndex = originalUrl.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return originalUrl;
        }
        
        // 在扩展名前插入_90x90
        String baseUrl = originalUrl.substring(0, lastDotIndex);
        String extension = originalUrl.substring(lastDotIndex);
        
        return baseUrl + "_90x90" + extension;
    }
    
    /**
     * 为图片URL添加前缀并转换为缩略图
     * @param imageUrl 原始图片URL
     * @return 带前缀的缩略图URL
     */
    public String getThumbnailUrlWithPrefix(String imageUrl) {
        String urlWithPrefix = addPrefix(imageUrl);
        return getThumbnailUrl(urlWithPrefix);
    }
    
    /**
     * 为图片URL添加前缀（不转换为缩略图）
     * @param imageUrl 原始图片URL
     * @return 带前缀的完整URL
     */
    public String getFullUrlWithPrefix(String imageUrl) {
        return addPrefix(imageUrl);
    }
}
