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
     * 限制最多显示两层（只显示一级分类，不显示二级分类的子分类）
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
            // 只显示一级分类，不显示二级分类的子分类（限制最多两层）
            // 如果是一级分类，显示其直接子分类；如果是二级分类，不显示子分类
            if (category.getChildren() != null && !category.getChildren().isEmpty()) {
                // 一级分类显示子分类，但子分类不再显示子分类
                treeSelect.setChildren(category.getChildren().stream().map(child -> {
                    TreeSelect childTreeSelect = new TreeSelect();
                    childTreeSelect.setId(child.getId());
                    childTreeSelect.setLabel(child.getCategoryName());
                    // 二级分类不显示子分类
                    return childTreeSelect;
                }).collect(Collectors.toList()));
            }
            return treeSelect;
        }).collect(Collectors.toList());
    }
    
    /**
     * 構建前端所需要下拉樹結構（只返回一级分类，用于选择上级分类）
     * 
     * @param categories 分類列表
     * @return 下拉樹結構列表（只包含一级分类）
     */
    @Override
    public List<TreeSelect> buildFirstLevelCategoryTreeSelect(List<CarProductCategoryEntity> categories)
    {
        // 只返回一级分类（parentId为0或null的分类）
        return categories.stream()
            .filter(category -> category.getParentId() == null || category.getParentId() == 0)
            .map(category -> {
                TreeSelect treeSelect = new TreeSelect();
                treeSelect.setId(category.getId());
                treeSelect.setLabel(category.getCategoryName());
                // 不包含children，只显示一级分类
                return treeSelect;
            })
            .collect(Collectors.toList());
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
        // 层级限制：最多维护两层
        if (carProductCategory.getParentId() != null && carProductCategory.getParentId() != 0) {
            CarProductCategoryEntity parent = carProductCategoryMapper.selectByPrimaryKey(carProductCategory.getParentId());
            if (parent != null && parent.getParentId() != null && parent.getParentId() != 0) {
                throw new RuntimeException("最多只能维护两层分类，无法继续添加子分类");
            }
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
        // 层级限制：最多维护两层
        if (carProductCategory.getParentId() != null && carProductCategory.getParentId() != 0) {
            CarProductCategoryEntity parent = carProductCategoryMapper.selectByPrimaryKey(carProductCategory.getParentId());
            if (parent != null && parent.getParentId() != null && parent.getParentId() != 0) {
                throw new RuntimeException("最多只能维护两层分类，无法继续添加子分类");
            }
            // 防止将分类设置为自己的子分类
            if (carProductCategory.getId() != null && carProductCategory.getId().equals(carProductCategory.getParentId())) {
                throw new RuntimeException("不能将分类设置为自己的子分类");
            }
            // 防止循环引用：检查父分类是否是当前分类的子分类
            if (isDescendant(carProductCategory.getId(), carProductCategory.getParentId())) {
                throw new RuntimeException("不能将分类设置为自己的子分类");
            }
        }
        return carProductCategoryMapper.updateByPrimaryKeySelective(carProductCategory);
    }
    
    /**
     * 检查targetId是否是sourceId的后代节点
     * 
     * @param sourceId 源节点ID
     * @param targetId 目标节点ID
     * @return 是否是后代节点
     */
    private boolean isDescendant(Long sourceId, Long targetId) {
        if (sourceId == null || targetId == null) {
            return false;
        }
        CarProductCategoryEntity target = carProductCategoryMapper.selectByPrimaryKey(targetId);
        if (target == null || target.getParentId() == null || target.getParentId() == 0) {
            return false;
        }
        if (target.getParentId().equals(sourceId)) {
            return true;
        }
        return isDescendant(sourceId, target.getParentId());
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

