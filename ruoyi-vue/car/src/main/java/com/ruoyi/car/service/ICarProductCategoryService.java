package com.ruoyi.car.service;

import java.util.List;
import com.ruoyi.car.domain.CarProductCategoryEntity;
import com.ruoyi.common.core.domain.TreeSelect;

/**
 * 商品目錄分類Service接口
 * 
 * @author ruoyi
 * @date 2024-01-01
 */
public interface ICarProductCategoryService 
{
    /**
     * 查詢商品目錄分類
     * 
     * @param id 商品目錄分類主鍵
     * @return 商品目錄分類
     */
    public CarProductCategoryEntity selectCarProductCategoryById(Long id);

    /**
     * 查詢商品目錄分類列表
     * 
     * @param carProductCategory 商品目錄分類
     * @return 商品目錄分類集合
     */
    public List<CarProductCategoryEntity> selectCarProductCategoryList(CarProductCategoryEntity carProductCategory);

    /**
     * 構建前端所需要樹結構
     * 
     * @param categories 分類列表
     * @return 樹結構列表
     */
    public List<CarProductCategoryEntity> buildCategoryTree(List<CarProductCategoryEntity> categories);

    /**
     * 構建前端所需要下拉樹結構
     * 
     * @param categories 分類列表
     * @return 下拉樹結構列表
     */
    public List<TreeSelect> buildCategoryTreeSelect(List<CarProductCategoryEntity> categories);

    /**
     * 新增商品目錄分類
     * 
     * @param carProductCategory 商品目錄分類
     * @return 結果
     */
    public int insertCarProductCategory(CarProductCategoryEntity carProductCategory);

    /**
     * 修改商品目錄分類
     * 
     * @param carProductCategory 商品目錄分類
     * @return 結果
     */
    public int updateCarProductCategory(CarProductCategoryEntity carProductCategory);

    /**
     * 批量刪除商品目錄分類
     * 
     * @param ids 需要刪除的商品目錄分類主鍵集合
     * @return 結果
     */
    public int deleteCarProductCategoryByIds(Long[] ids);

    /**
     * 刪除商品目錄分類信息
     * 
     * @param id 商品目錄分類主鍵
     * @return 結果
     */
    public int deleteCarProductCategoryById(Long id);

    /**
     * 是否存在子節點
     * 
     * @param id 分類ID
     * @return 結果
     */
    public boolean hasChildByCategoryId(Long id);

    /**
     * 查詢是否存在商品
     * 
     * @param id 分類ID
     * @return 結果
     */
    public boolean checkCategoryExistProduct(Long id);
}

