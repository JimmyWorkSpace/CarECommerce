package com.ruoyi.car.service;

import java.util.List;
import com.ruoyi.car.domain.CarProductEntity;
import com.ruoyi.car.domain.CarProductImageEntity;
import com.ruoyi.car.domain.CarProductAttrEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * 商品Service接口
 * 
 * @author ruoyi
 * @date 2024-01-01
 */
public interface ICarProductService 
{
    /**
     * 查詢商品
     * 
     * @param id 商品主鍵
     * @return 商品
     */
    public CarProductEntity selectCarProductById(Long id);

    /**
     * 查詢商品列表
     * 
     * @param carProduct 商品
     * @return 商品集合
     */
    public List<CarProductEntity> selectCarProductList(CarProductEntity carProduct);

    /**
     * 新增商品
     * 
     * @param carProduct 商品
     * @return 結果
     */
    public int insertCarProduct(CarProductEntity carProduct);

    /**
     * 修改商品
     * 
     * @param carProduct 商品
     * @return 結果
     */
    public int updateCarProduct(CarProductEntity carProduct);

    /**
     * 批量刪除商品
     * 
     * @param ids 需要刪除的商品主鍵集合
     * @return 結果
     */
    public int deleteCarProductByIds(Long[] ids);

    /**
     * 刪除商品信息
     * 
     * @param id 商品主鍵
     * @return 結果
     */
    public int deleteCarProductById(Long id);

    /**
     * 查詢商品圖片列表
     * 
     * @param productId 商品ID
     * @return 圖片列表
     */
    public List<CarProductImageEntity> selectProductImageList(Long productId);

    /**
     * 上傳商品圖片（包含縮略圖生成）
     * 
     * @param files 圖片文件數組
     * @param productId 商品ID
     * @return 上傳結果
     */
    public List<CarProductImageEntity> uploadProductImages(MultipartFile[] files, Long productId);

    /**
     * 刪除商品圖片
     * 
     * @param imageId 圖片ID
     * @return 結果
     */
    public int deleteProductImage(Long imageId);

    /**
     * 查詢商品屬性列表
     * 
     * @param productId 商品ID
     * @return 屬性列表
     */
    public List<CarProductAttrEntity> selectProductAttrList(Long productId);

    /**
     * 保存商品屬性（從文本格式解析）
     * 
     * @param productId 商品ID
     * @param attrText 屬性文本（格式：屬性名:屬性值，多行分隔）
     * @return 結果
     */
    public int saveProductAttrs(Long productId, String attrText);

    /**
     * 刪除商品所有屬性
     * 
     * @param productId 商品ID
     * @return 結果
     */
    public int deleteProductAttrs(Long productId);
}

