package cc.carce.sale.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cc.carce.sale.common.R;
import cc.carce.sale.dto.CarProductListDto;
import cc.carce.sale.entity.CarProductEntity;
import cc.carce.sale.entity.CarProductImageEntity;
import cc.carce.sale.entity.CarProductAttrEntity;
import cc.carce.sale.entity.CarProductCategoryEntity;
import cc.carce.sale.service.CarProductService;
import lombok.extern.slf4j.Slf4j;

/**
 * 商品API控制器
 */
@RestController
@RequestMapping("api/products")
@Slf4j
public class CarProductApiController extends BaseController {

    @Resource
    private CarProductService carProductService;

    /**
     * 获取所有已上架的商品列表
     */
    @GetMapping("/list")
    public R<List<CarProductListDto>> getProductsList() {
        try {
            List<CarProductListDto> products = carProductService.getPublicProducts();
            return R.ok("获取商品列表成功", products);
        } catch (Exception e) {
            log.error("获取商品列表失败", e);
            return R.fail("获取商品列表失败：" + e.getMessage(), null);
        }
    }

    /**
     * 根据分类ID获取商品列表
     */
    @GetMapping("/category")
    public R<List<CarProductListDto>> getProductsByCategory(@RequestParam(required = false) Long categoryId) {
        try {
            List<CarProductListDto> products = carProductService.getProductsByCategoryId(categoryId);
            return R.ok("获取分类商品成功", products);
        } catch (Exception e) {
            log.error("获取分类商品失败", e);
            return R.fail("获取分类商品失败：" + e.getMessage(), null);
        }
    }

    /**
     * 根据标签搜索商品列表
     */
    @GetMapping("/tag")
    public R<List<CarProductListDto>> getProductsByTag(@RequestParam(required = false) String tag) {
        try {
            List<CarProductListDto> products = carProductService.getProductsByTag(tag);
            return R.ok("获取标签商品成功", products);
        } catch (Exception e) {
            log.error("获取标签商品失败，标签：{}", tag, e);
            return R.fail("获取标签商品失败：" + e.getMessage(), null);
        }
    }

    /**
     * 根据ID获取商品详情
     */
    @GetMapping("/{id}")
    public R<CarProductEntity> getProductById(@PathVariable Long id) {
        try {
            CarProductEntity product = carProductService.getProductById(id);
            if (product != null) {
                return R.ok("获取商品详情成功", product);
            } else {
                return R.fail("商品不存在", null);
            }
        } catch (Exception e) {
            log.error("获取商品详情失败，商品ID：{}", id, e);
            return R.fail("获取商品详情失败：" + e.getMessage(), null);
        }
    }

    /**
     * 获取商品图片列表
     */
    @GetMapping("/{id}/images")
    public R<List<CarProductImageEntity>> getProductImages(@PathVariable Long id) {
        try {
            List<CarProductImageEntity> images = carProductService.getProductImages(id);
            return R.ok("获取商品图片成功", images);
        } catch (Exception e) {
            log.error("获取商品图片失败，商品ID：{}", id, e);
            return R.fail("获取商品图片失败：" + e.getMessage(), null);
        }
    }

    /**
     * 获取商品属性列表
     */
    @GetMapping("/{id}/attrs")
    public R<List<CarProductAttrEntity>> getProductAttrs(@PathVariable Long id) {
        try {
            List<CarProductAttrEntity> attrs = carProductService.getProductAttrs(id);
            return R.ok("获取商品属性成功", attrs);
        } catch (Exception e) {
            log.error("获取商品属性失败，商品ID：{}", id, e);
            return R.fail("获取商品属性失败：" + e.getMessage(), null);
        }
    }

    /**
     * 获取所有分类列表
     */
    @GetMapping("/categories")
    public R<List<CarProductCategoryEntity>> getAllCategories() {
        try {
            List<CarProductCategoryEntity> categories = carProductService.getAllCategories();
            return R.ok("获取分类列表成功", categories);
        } catch (Exception e) {
            log.error("获取分类列表失败", e);
            return R.fail("获取分类列表失败：" + e.getMessage(), null);
        }
    }

    /**
     * 根据父分类ID获取子分类列表
     */
    @GetMapping("/categories/parent")
    public R<List<CarProductCategoryEntity>> getCategoriesByParentId(@RequestParam(required = false) Long parentId) {
        try {
            List<CarProductCategoryEntity> categories = carProductService.getCategoriesByParentId(parentId);
            return R.ok("获取子分类列表成功", categories);
        } catch (Exception e) {
            log.error("获取子分类列表失败，父分类ID：{}", parentId, e);
            return R.fail("获取子分类列表失败：" + e.getMessage(), null);
        }
    }

    /**
     * 获取分类树结构（一级分类及其二级分类）
     */
    @GetMapping("/categories/tree")
    public R<List<CategoryTreeDto>> getCategoryTree() {
        try {
            // 获取所有一级分类
            List<CarProductCategoryEntity> firstLevelCategories = carProductService.getCategoriesByParentId(null);
            List<CategoryTreeDto> tree = new ArrayList<>();
            
            for (CarProductCategoryEntity firstLevel : firstLevelCategories) {
                CategoryTreeDto treeDto = new CategoryTreeDto();
                treeDto.setId(firstLevel.getId());
                treeDto.setName(firstLevel.getCategoryName());
                treeDto.setShowOrder(firstLevel.getShowOrder());
                
                // 获取该一级分类下的所有二级分类
                List<CarProductCategoryEntity> secondLevelCategories = carProductService.getCategoriesByParentId(firstLevel.getId());
                List<CategoryTreeDto> children = new ArrayList<>();
                for (CarProductCategoryEntity secondLevel : secondLevelCategories) {
                    CategoryTreeDto childDto = new CategoryTreeDto();
                    childDto.setId(secondLevel.getId());
                    childDto.setName(secondLevel.getCategoryName());
                    childDto.setShowOrder(secondLevel.getShowOrder());
                    children.add(childDto);
                }
                treeDto.setChildren(children);
                tree.add(treeDto);
            }
            
            return R.ok("获取分类树成功", tree);
        } catch (Exception e) {
            log.error("获取分类树失败", e);
            return R.fail("获取分类树失败：" + e.getMessage(), null);
        }
    }
    
    /**
     * 分类树DTO
     */
    public static class CategoryTreeDto {
        private Long id;
        private String name;
        private Integer showOrder;
        private List<CategoryTreeDto> children;
        
        public Long getId() {
            return id;
        }
        
        public void setId(Long id) {
            this.id = id;
        }
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public Integer getShowOrder() {
            return showOrder;
        }
        
        public void setShowOrder(Integer showOrder) {
            this.showOrder = showOrder;
        }
        
        public List<CategoryTreeDto> getChildren() {
            return children;
        }
        
        public void setChildren(List<CategoryTreeDto> children) {
            this.children = children;
        }
    }
}


