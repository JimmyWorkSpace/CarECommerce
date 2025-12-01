package com.ruoyi.car.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.car.mapper.master.CarProductCategoryMapper;
import com.ruoyi.car.domain.CarProductCategoryEntity;
import com.ruoyi.car.service.ICarProductCategoryService;
import com.ruoyi.common.core.domain.TreeSelect;
import com.ruoyi.common.utils.StringUtils;
import tk.mybatis.mapper.entity.Example;
import java.util.stream.Collectors;

/**
 * 商品目錄分類Service業務層處理
 * 
 * @author ruoyi
 * @date 2024-01-01
 */
@Service
public class CarProductCategoryServiceImpl implements ICarProductCategoryService 
{
    @Resource
    private CarProductCategoryMapper carProductCategoryMapper;

    /**
     * 查詢商品目錄分類
     * 
     * @param id 商品目錄分類主鍵
     * @return 商品目錄分類
     */
    @Override
    public CarProductCategoryEntity selectCarProductCategoryById(Long id)
    {
        return carProductCategoryMapper.selectByPrimaryKey(id);
    }

    /**
     * 查詢商品目錄分類列表
     * 
     * @param carProductCategory 商品目錄分類
     * @return 商品目錄分類
     */
    @Override
    public List<CarProductCategoryEntity> selectCarProductCategoryList(CarProductCategoryEntity carProductCategory)
    {
        Example example = new Example(CarProductCategoryEntity.class);
        Example.Criteria criteria = example.createCriteria();
        
        // 只查詢未刪除的記錄
        criteria.andNotEqualTo("delFlag", 1);
        
        if (StringUtils.isNotNull(carProductCategory.getCategoryName()) && !carProductCategory.getCategoryName().trim().isEmpty()) {
            criteria.andLike("categoryName", "%" + carProductCategory.getCategoryName() + "%");
        }
        if (StringUtils.isNotNull(carProductCategory.getParentId())) {
            criteria.andEqualTo("parentId", carProductCategory.getParentId());
        }
        if (StringUtils.isNotNull(carProductCategory.getShowOrder())) {
            criteria.andEqualTo("showOrder", carProductCategory.getShowOrder());
        }
        
        // 按顯示順序和ID排序
        example.orderBy("showOrder").asc().orderBy("id").asc();
        
        List<CarProductCategoryEntity> list = carProductCategoryMapper.selectByExample(example);
        
        // 設置父分類名稱
        for (CarProductCategoryEntity category : list) {
            if (StringUtils.isNotNull(category.getParentId()) && category.getParentId() != 0) {
                CarProductCategoryEntity parent = carProductCategoryMapper.selectByPrimaryKey(category.getParentId());
                if (StringUtils.isNotNull(parent)) {
                    category.setParentName(parent.getCategoryName());
                }
            } else {
                category.setParentName("主類目");
            }
        }
        
        return list;
    }

    /**
     * 構建前端所需要樹結構
     * 
     * @param categories 分類列表
     * @return 樹結構列表
     */
    @Override
    public List<CarProductCategoryEntity> buildCategoryTree(List<CarProductCategoryEntity> categories)
    {
        List<CarProductCategoryEntity> returnList = new ArrayList<CarProductCategoryEntity>();
        List<Long> tempList = new ArrayList<Long>();
        for (CarProductCategoryEntity category : categories)
        {
            tempList.add(category.getId());
        }
        for (CarProductCategoryEntity category : categories)
        {
            // 如果是頂級節點, 遍歷該父節點的所有子節點
            if (category.getParentId() == null || category.getParentId() == 0 || !tempList.contains(category.getParentId()))
            {
                recursionFn(categories, category);
                returnList.add(category);
            }
        }
        if (returnList.isEmpty())
        {
            returnList = categories;
        }
        return returnList;
    }

    /**
     * 構建前端所需要下拉樹結構
     * 
     * @param categories 分類列表
     * @return 下拉樹結構列表
     */
    @Override
    public List<TreeSelect> buildCategoryTreeSelect(List<CarProductCategoryEntity> categories)
    {
        List<CarProductCategoryEntity> categoryTrees = buildCategoryTree(categories);
        return categoryTrees.stream().map(category -> {
            TreeSelect treeSelect = new TreeSelect();
            treeSelect.setId(category.getId());
            treeSelect.setLabel(category.getCategoryName());
            if (category.getChildren() != null && !category.getChildren().isEmpty()) {
                treeSelect.setChildren(buildCategoryTreeSelect(category.getChildren()));
            }
            return treeSelect;
        }).collect(Collectors.toList());
    }

    /**
     * 新增商品目錄分類
     * 
     * @param carProductCategory 商品目錄分類
     * @return 結果
     */
    @Override
    @Transactional
    public int insertCarProductCategory(CarProductCategoryEntity carProductCategory)
    {
        if (carProductCategory.getParentId() == null) {
            carProductCategory.setParentId(0L);
        }
        if (carProductCategory.getDelFlag() == null) {
            carProductCategory.setDelFlag(0);
        }
        if (carProductCategory.getShowOrder() == null) {
            carProductCategory.setShowOrder(0);
        }
        return carProductCategoryMapper.insertSelective(carProductCategory);
    }

    /**
     * 修改商品目錄分類
     * 
     * @param carProductCategory 商品目錄分類
     * @return 結果
     */
    @Override
    @Transactional
    public int updateCarProductCategory(CarProductCategoryEntity carProductCategory)
    {
        return carProductCategoryMapper.updateByPrimaryKeySelective(carProductCategory);
    }

    /**
     * 批量刪除商品目錄分類
     * 
     * @param ids 需要刪除的商品目錄分類主鍵集合
     * @return 結果
     */
    @Override
    @Transactional
    public int deleteCarProductCategoryByIds(Long[] ids)
    {
        int count = 0;
        for (Long id : ids)
        {
            // 檢查是否存在子節點
            if (hasChildByCategoryId(id))
            {
                throw new RuntimeException("存在子分類，不允許刪除");
            }
            // 檢查是否存在商品
            if (checkCategoryExistProduct(id))
            {
                throw new RuntimeException("分類下存在商品，不允許刪除");
            }
            // 邏輯刪除
            CarProductCategoryEntity category = new CarProductCategoryEntity();
            category.setId(id);
            category.setDelFlag(1);
            count += carProductCategoryMapper.updateByPrimaryKeySelective(category);
        }
        return count;
    }

    /**
     * 刪除商品目錄分類信息
     * 
     * @param id 商品目錄分類主鍵
     * @return 結果
     */
    @Override
    @Transactional
    public int deleteCarProductCategoryById(Long id)
    {
        // 檢查是否存在子節點
        if (hasChildByCategoryId(id))
        {
            throw new RuntimeException("存在子分類，不允許刪除");
        }
        // 檢查是否存在商品
        if (checkCategoryExistProduct(id))
        {
            throw new RuntimeException("分類下存在商品，不允許刪除");
        }
        // 邏輯刪除
        CarProductCategoryEntity category = new CarProductCategoryEntity();
        category.setId(id);
        category.setDelFlag(1);
        return carProductCategoryMapper.updateByPrimaryKeySelective(category);
    }

    /**
     * 是否存在子節點
     * 
     * @param id 分類ID
     * @return 結果
     */
    @Override
    public boolean hasChildByCategoryId(Long id)
    {
        Example example = new Example(CarProductCategoryEntity.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("parentId", id);
        criteria.andNotEqualTo("delFlag", 1);
        int result = carProductCategoryMapper.selectCountByExample(example);
        return result > 0;
    }

    /**
     * 查詢是否存在商品
     * 
     * @param id 分類ID
     * @return 結果
     */
    @Override
    public boolean checkCategoryExistProduct(Long id)
    {
        // TODO: 如果car_product表有categoryId字段，可以在這裡檢查
        // 目前先返回false，表示不存在商品
        return false;
    }

    /**
     * 遞歸列表
     */
    private void recursionFn(List<CarProductCategoryEntity> list, CarProductCategoryEntity t)
    {
        // 得到子節點列表
        List<CarProductCategoryEntity> childList = getChildList(list, t);
        t.setChildren(childList);
        for (CarProductCategoryEntity tChild : childList)
        {
            if (hasChild(list, tChild))
            {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子節點列表
     */
    private List<CarProductCategoryEntity> getChildList(List<CarProductCategoryEntity> list, CarProductCategoryEntity t)
    {
        List<CarProductCategoryEntity> tlist = new ArrayList<CarProductCategoryEntity>();
        Iterator<CarProductCategoryEntity> it = list.iterator();
        while (it.hasNext())
        {
            CarProductCategoryEntity n = it.next();
            if (StringUtils.isNotNull(n.getParentId()) && n.getParentId().longValue() == t.getId().longValue())
            {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判斷是否有子節點
     */
    private boolean hasChild(List<CarProductCategoryEntity> list, CarProductCategoryEntity t)
    {
        return getChildList(list, t).size() > 0;
    }
}

