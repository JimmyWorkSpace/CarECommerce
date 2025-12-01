package com.ruoyi.car.service.impl;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.imageio.ImageIO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.car.mapper.master.CarProductMapper;
import com.ruoyi.car.mapper.master.CarProductImageMapper;
import com.ruoyi.car.mapper.master.CarProductAttrMapper;
import com.ruoyi.car.domain.CarProductEntity;
import com.ruoyi.car.domain.CarProductImageEntity;
import com.ruoyi.car.domain.CarProductAttrEntity;
import com.ruoyi.car.service.ICarProductService;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.service.FtpService;
import tk.mybatis.mapper.entity.Example;
import lombok.extern.slf4j.Slf4j;

/**
 * 商品Service業務層處理
 * 
 * @author ruoyi
 * @date 2024-01-01
 */
@Service
@Slf4j
public class CarProductServiceImpl implements ICarProductService 
{
    @Resource
    private CarProductMapper carProductMapper;

    @Resource
    private CarProductImageMapper carProductImageMapper;

    @Resource
    private CarProductAttrMapper carProductAttrMapper;

    @Resource
    private FtpService ftpService;

    @Value("${carce.prefix}")
    private String imagePrefix;

    /** 商品圖片上傳目錄 */
    private static final String PRODUCT_IMAGE_DIR = "/img/car_product";

    /** 縮略圖寬度 */
    private static final int THUMBNAIL_WIDTH = 180;

    /**
     * 查詢商品
     * 
     * @param id 商品主鍵
     * @return 商品
     */
    @Override
    public CarProductEntity selectCarProductById(Long id)
    {
        return carProductMapper.selectByPrimaryKey(id);
    }

    /**
     * 查詢商品列表
     * 
     * @param carProduct 商品
     * @return 商品
     */
    @Override
    public List<CarProductEntity> selectCarProductList(CarProductEntity carProduct)
    {
        Example example = new Example(CarProductEntity.class);
        Example.Criteria criteria = example.createCriteria();
        
        // 只查詢未刪除的記錄
        criteria.andNotEqualTo("delFlag", 1);
        
        if (StringUtils.isNotNull(carProduct.getProductTitle()) && !carProduct.getProductTitle().trim().isEmpty()) {
            criteria.andLike("productTitle", "%" + carProduct.getProductTitle() + "%");
        }
        if (StringUtils.isNotNull(carProduct.getCategoryId())) {
            criteria.andEqualTo("categoryId", carProduct.getCategoryId());
        }
        if (StringUtils.isNotNull(carProduct.getOnSale())) {
            criteria.andEqualTo("onSale", carProduct.getOnSale());
        }
        
        // 按ID倒序
        example.orderBy("id").desc();
        
        return carProductMapper.selectByExample(example);
    }

    /**
     * 新增商品
     * 
     * @param carProduct 商品
     * @return 結果
     */
    @Override
    @Transactional
    public int insertCarProduct(CarProductEntity carProduct)
    {
        if (carProduct.getDelFlag() == null) {
            carProduct.setDelFlag(0);
        }
        if (carProduct.getOnSale() == null) {
            carProduct.setOnSale(0);
        }
        if (carProduct.getAmount() == null) {
            carProduct.setAmount(0);
        }
        return carProductMapper.insertSelective(carProduct);
    }

    /**
     * 修改商品
     * 
     * @param carProduct 商品
     * @return 結果
     */
    @Override
    @Transactional
    public int updateCarProduct(CarProductEntity carProduct)
    {
        return carProductMapper.updateByPrimaryKeySelective(carProduct);
    }

    /**
     * 批量刪除商品
     * 
     * @param ids 需要刪除的商品主鍵集合
     * @return 結果
     */
    @Override
    @Transactional
    public int deleteCarProductByIds(Long[] ids)
    {
        int count = 0;
        for (Long id : ids)
        {
            // 邏輯刪除
            CarProductEntity product = new CarProductEntity();
            product.setId(id);
            product.setDelFlag(1);
            count += carProductMapper.updateByPrimaryKeySelective(product);
        }
        return count;
    }

    /**
     * 刪除商品信息
     * 
     * @param id 商品主鍵
     * @return 結果
     */
    @Override
    @Transactional
    public int deleteCarProductById(Long id)
    {
        // 邏輯刪除
        CarProductEntity product = new CarProductEntity();
        product.setId(id);
        product.setDelFlag(1);
        return carProductMapper.updateByPrimaryKeySelective(product);
    }

    /**
     * 查詢商品圖片列表
     * 
     * @param productId 商品ID
     * @return 圖片列表
     */
    @Override
    public List<CarProductImageEntity> selectProductImageList(Long productId)
    {
        Example example = new Example(CarProductImageEntity.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("productId", productId);
        criteria.andNotEqualTo("delFlag", 1);
        example.orderBy("showOrder").asc().orderBy("id").asc();
        List<CarProductImageEntity> images = carProductImageMapper.selectByExample(example);
        // 設置完整圖片地址
        setFullImageUrl(images);
        return images;
    }

    /**
     * 上傳商品圖片（包含縮略圖生成）
     * 
     * @param files 圖片文件數組
     * @param productId 商品ID
     * @return 上傳結果
     */
    @Override
    @Transactional
    public List<CarProductImageEntity> uploadProductImages(MultipartFile[] files, Long productId)
    {
        List<CarProductImageEntity> imageList = new ArrayList<>();
        
        if (files == null || files.length == 0) {
            return imageList;
        }

        // 獲取當前最大顯示順序
        Example example = new Example(CarProductImageEntity.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("productId", productId);
        example.orderBy("showOrder").desc();
        List<CarProductImageEntity> existingImages = carProductImageMapper.selectByExample(example);
        int maxOrder = existingImages.isEmpty() ? 0 : (existingImages.get(0).getShowOrder() == null ? 0 : existingImages.get(0).getShowOrder());

        for (MultipartFile file : files) {
            try {
                if (file.isEmpty()) {
                    continue;
                }

                String originalFilename = file.getOriginalFilename();
                if (originalFilename == null) {
                    continue;
                }

                // 上傳原圖
                InputStream inputStream = file.getInputStream();
                String fileName = ftpService.uploadFile(inputStream, originalFilename, PRODUCT_IMAGE_DIR);
                String imageUrl = PRODUCT_IMAGE_DIR + "/" + fileName;

                // 生成並上傳縮略圖
                try {
                    byte[] thumbnailBytes = generateThumbnail(file);
                    if (thumbnailBytes != null) {
                        String thumbnailFileName = getThumbnailFileName(fileName);
                        ByteArrayInputStream thumbnailStream = new ByteArrayInputStream(thumbnailBytes);
                        ftpService.uploadFile(thumbnailStream, thumbnailFileName, PRODUCT_IMAGE_DIR);
                        log.info("縮略圖上傳成功: {}", thumbnailFileName);
                    }
                } catch (Exception e) {
                    log.error("生成縮略圖失敗: {}", originalFilename, e);
                    // 縮略圖生成失敗不影響主流程
                }

                // 保存圖片記錄
                CarProductImageEntity image = new CarProductImageEntity();
                image.setProductId(productId);
                image.setImageUrl(imageUrl);
                image.setShowOrder(++maxOrder);
                image.setDelFlag(0);
                carProductImageMapper.insertSelective(image);
                imageList.add(image);

            } catch (Exception e) {
                log.error("上傳圖片失敗: {}", file.getOriginalFilename(), e);
            }
        }

        // 設置完整圖片地址
        setFullImageUrl(imageList);
        return imageList;
    }

    /**
     * 生成縮略圖
     * 
     * @param file 原圖文件
     * @return 縮略圖字節數組
     */
    private byte[] generateThumbnail(MultipartFile file) throws Exception {
        BufferedImage originalImage = ImageIO.read(file.getInputStream());
        if (originalImage == null) {
            return null;
        }

        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();

        // 如果原圖寬度小於等於目標寬度，不需要縮放
        if (originalWidth <= THUMBNAIL_WIDTH) {
            return null;
        }

        // 計算縮略圖尺寸，保持寬高比
        double scale = (double) THUMBNAIL_WIDTH / originalWidth;
        int targetHeight = (int) (originalHeight * scale);

        // 創建縮略圖
        BufferedImage thumbnailImage = new BufferedImage(THUMBNAIL_WIDTH, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = thumbnailImage.createGraphics();

        // 設置渲染質量
        g2d.setRenderingHint(java.awt.RenderingHints.KEY_INTERPOLATION, java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, java.awt.RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

        // 繪製縮放後的圖片
        Image scaledImage = originalImage.getScaledInstance(THUMBNAIL_WIDTH, targetHeight, Image.SCALE_SMOOTH);
        g2d.drawImage(scaledImage, 0, 0, null);
        g2d.dispose();

        // 轉換為字節數組
        String formatName = getImageFormat(file.getOriginalFilename());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(thumbnailImage, formatName, baos);

        return baos.toByteArray();
    }

    /**
     * 獲取圖片格式
     */
    private String getImageFormat(String fileName) {
        if (fileName == null) {
            return "jpg";
        }
        String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
        switch (extension) {
            case "jpg":
            case "jpeg":
                return "jpg";
            case "png":
                return "png";
            case "gif":
                return "gif";
            case "bmp":
                return "bmp";
            default:
                return "jpg";
        }
    }

    /**
     * 獲取縮略圖文件名（原文件名.180.擴展名）
     */
    private String getThumbnailFileName(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return fileName + ".180";
        }
        int lastDot = fileName.lastIndexOf('.');
        String nameWithoutExt = fileName.substring(0, lastDot);
        String extension = fileName.substring(lastDot);
        return nameWithoutExt + ".180" + extension;
    }

    /**
     * 刪除商品圖片
     * 
     * @param imageId 圖片ID
     * @return 結果
     */
    @Override
    @Transactional
    public int deleteProductImage(Long imageId)
    {
        // 邏輯刪除
        CarProductImageEntity image = new CarProductImageEntity();
        image.setId(imageId);
        image.setDelFlag(1);
        return carProductImageMapper.updateByPrimaryKeySelective(image);
    }

    /**
     * 查詢商品屬性列表
     * 
     * @param productId 商品ID
     * @return 屬性列表
     */
    @Override
    public List<CarProductAttrEntity> selectProductAttrList(Long productId)
    {
        Example example = new Example(CarProductAttrEntity.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("productId", productId);
        criteria.andNotEqualTo("delFlag", 1);
        example.orderBy("showOrder").asc().orderBy("id").asc();
        return carProductAttrMapper.selectByExample(example);
    }

    /**
     * 保存商品屬性（從文本格式解析）
     * 
     * @param productId 商品ID
     * @param attrText 屬性文本（格式：屬性名:屬性值，多行分隔）
     * @return 結果
     */
    @Override
    @Transactional
    public int saveProductAttrs(Long productId, String attrText)
    {
        // 先刪除舊屬性
        deleteProductAttrs(productId);

        if (StringUtils.isEmpty(attrText)) {
            return 0;
        }

        // 解析屬性文本
        String[] lines = attrText.split("\n");
        int order = 0;
        int count = 0;

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) {
                continue;
            }

            // 按第一個英文冒號分隔
            int colonIndex = line.indexOf(':');
            if (colonIndex <= 0 || colonIndex >= line.length() - 1) {
                continue; // 格式不正確，跳過
            }

            String attrName = line.substring(0, colonIndex).trim();
            String attrValue = line.substring(colonIndex + 1).trim();

            if (attrName.isEmpty() || attrValue.isEmpty()) {
                continue;
            }

            // 保存屬性
            CarProductAttrEntity attr = new CarProductAttrEntity();
            attr.setProductId(productId);
            attr.setAttrName(attrName);
            attr.setAttrValue(attrValue);
            attr.setShowOrder(++order);
            attr.setDelFlag(0);
            carProductAttrMapper.insertSelective(attr);
            count++;
        }

        return count;
    }

    /**
     * 刪除商品所有屬性
     * 
     * @param productId 商品ID
     * @return 結果
     */
    @Override
    @Transactional
    public int deleteProductAttrs(Long productId)
    {
        // 邏輯刪除
        Example example = new Example(CarProductAttrEntity.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("productId", productId);
        criteria.andNotEqualTo("delFlag", 1);
        
        List<CarProductAttrEntity> attrs = carProductAttrMapper.selectByExample(example);
        int count = 0;
        for (CarProductAttrEntity attr : attrs) {
            attr.setDelFlag(1);
            count += carProductAttrMapper.updateByPrimaryKeySelective(attr);
        }
        return count;
    }

    /**
     * 設置完整圖片地址
     * 
     * @param images 圖片列表
     */
    private void setFullImageUrl(List<CarProductImageEntity> images) {
        if (images == null || images.isEmpty()) {
            return;
        }
        for (CarProductImageEntity image : images) {
            if (image != null && image.getImageUrl() != null) {
                String imageUrl = image.getImageUrl();
                // 如果imageUrl已經包含http或https，則直接使用
                if (imageUrl.startsWith("http://") || imageUrl.startsWith("https://")) {
                    image.setFullImageUrl(imageUrl);
                } else {
                    // 否則拼接前綴
                    // 確保imageUrl以/開頭
                    if (!imageUrl.startsWith("/")) {
                        imageUrl = "/" + imageUrl;
                    }
                    image.setFullImageUrl(imagePrefix + imageUrl);
                }
            }
        }
    }
}

