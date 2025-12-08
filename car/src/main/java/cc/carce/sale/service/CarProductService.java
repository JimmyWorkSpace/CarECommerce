package cc.carce.sale.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cc.carce.sale.dto.CarProductListDto;
import cc.carce.sale.entity.CarProductEntity;
import cc.carce.sale.entity.CarProductImageEntity;
import cc.carce.sale.entity.CarProductCategoryEntity;
import cc.carce.sale.entity.CarProductAttrEntity;
import cc.carce.sale.mapper.manager.CarProductMapper;
import cc.carce.sale.mapper.manager.CarProductImageMapper;
import cc.carce.sale.mapper.manager.CarProductCategoryMapper;
import cc.carce.sale.mapper.manager.CarProductAttrMapper;
import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Example;

/**
 * 商品服务类
 */
@Service
@Slf4j
public class CarProductService {

    @Resource
    private CarProductMapper carProductMapper;
    
    @Resource
    private CarProductImageMapper carProductImageMapper;
    
    @Resource
    private CarProductCategoryMapper carProductCategoryMapper;
    
    @Resource
    private CarProductAttrMapper carProductAttrMapper;
    
    @Value("${carce.prefix:}")
    private String imagePrefix;
    
    /**
     * 获取已上架的商品列表
     */
    public List<CarProductListDto> getPublicProducts() {
        try {
            Example example = new Example(CarProductEntity.class);
            example.createCriteria()
                .andEqualTo("onSale", 1)
                .andEqualTo("delFlag", 0);
            example.orderBy("id").desc();
            
            List<CarProductEntity> products = carProductMapper.selectByExample(example);
            List<CarProductListDto> result = new ArrayList<>();
            
            for (CarProductEntity product : products) {
                CarProductListDto dto = convertToDto(product);
                result.add(dto);
            }
            
            return result;
        } catch (Exception e) {
            log.error("获取商品列表失败", e);
            return new ArrayList<>();
        }
    }
    
    /**
     * 根据ID查询商品
     */
    public CarProductEntity getProductById(Long productId) {
        try {
            return carProductMapper.selectByPrimaryKey(productId);
        } catch (Exception e) {
            log.error("查询商品失败，商品ID：{}", productId, e);
            return null;
        }
    }
    
    /**
     * 根据分类ID获取商品列表
     * 如果是一级分类，则返回该一级分类及其所有二级分类下的商品
     */
    public List<CarProductListDto> getProductsByCategoryId(Long categoryId) {
        try {
            Example example = new Example(CarProductEntity.class);
            Example.Criteria criteria = example.createCriteria()
                .andEqualTo("onSale", 1)
                .andEqualTo("delFlag", 0);
            
            if (categoryId != null && categoryId > 0) {
                // 检查是否是一级分类（parentId为null或0）
                CarProductCategoryEntity category = getCategoryById(categoryId);
                if (category != null && (category.getParentId() == null || category.getParentId() == 0)) {
                    // 是一级分类，需要查询该一级分类及其所有二级分类下的商品
                    // 先获取所有二级分类ID
                    List<CarProductCategoryEntity> subCategories = getCategoriesByParentId(categoryId);
                    List<Long> categoryIds = new ArrayList<>();
                    categoryIds.add(categoryId); // 包含一级分类本身
                    for (CarProductCategoryEntity subCategory : subCategories) {
                        categoryIds.add(subCategory.getId());
                    }
                    // 使用IN查询
                    criteria.andIn("categoryId", categoryIds);
                } else {
                    // 是二级分类，直接查询
                    criteria.andEqualTo("categoryId", categoryId);
                }
            }
            
            example.orderBy("id").desc();
            
            List<CarProductEntity> products = carProductMapper.selectByExample(example);
            List<CarProductListDto> result = new ArrayList<>();
            
            for (CarProductEntity product : products) {
                CarProductListDto dto = convertToDto(product);
                result.add(dto);
            }
            
            return result;
        } catch (Exception e) {
            log.error("根据分类获取商品失败，分类ID：{}", categoryId, e);
            return new ArrayList<>();
        }
    }
    
    /**
     * 根据商品ID获取商品图片列表
     */
    public List<CarProductImageEntity> getProductImages(Long productId) {
        try {
            Example example = new Example(CarProductImageEntity.class);
            example.createCriteria()
                .andEqualTo("productId", productId)
                .andEqualTo("delFlag", 0);
            example.orderBy("showOrder").asc();
            
            return carProductImageMapper.selectByExample(example);
        } catch (Exception e) {
            log.error("获取商品图片失败，商品ID：{}", productId, e);
            return new ArrayList<>();
        }
    }
    
    /**
     * 根据商品ID获取商品属性列表
     */
    public List<CarProductAttrEntity> getProductAttrs(Long productId) {
        try {
            Example example = new Example(CarProductAttrEntity.class);
            example.createCriteria()
                .andEqualTo("productId", productId)
                .andEqualTo("delFlag", 0);
            example.orderBy("showOrder").asc();
            
            return carProductAttrMapper.selectByExample(example);
        } catch (Exception e) {
            log.error("获取商品属性失败，商品ID：{}", productId, e);
            return new ArrayList<>();
        }
    }
    
    /**
     * 获取所有分类列表
     */
    public List<CarProductCategoryEntity> getAllCategories() {
        try {
            Example example = new Example(CarProductCategoryEntity.class);
            example.createCriteria()
                .andEqualTo("delFlag", 0);
            example.orderBy("showOrder").asc();
            
            return carProductCategoryMapper.selectByExample(example);
        } catch (Exception e) {
            log.error("获取分类列表失败", e);
            return new ArrayList<>();
        }
    }
    
    /**
     * 根据ID查询分类
     */
    public CarProductCategoryEntity getCategoryById(Long categoryId) {
        try {
            return carProductCategoryMapper.selectByPrimaryKey(categoryId);
        } catch (Exception e) {
            log.error("查询分类失败，分类ID：{}", categoryId, e);
            return null;
        }
    }
    
    /**
     * 根据父分类ID获取子分类列表
     */
    public List<CarProductCategoryEntity> getCategoriesByParentId(Long parentId) {
        try {
            Example example = new Example(CarProductCategoryEntity.class);
            Example.Criteria criteria = example.createCriteria()
                .andEqualTo("delFlag", 0);
            
            if (parentId == null || parentId == 0) {
                criteria.andIsNull("parentId");
            } else {
                criteria.andEqualTo("parentId", parentId);
            }
            
            example.orderBy("showOrder").asc();
            
            return carProductCategoryMapper.selectByExample(example);
        } catch (Exception e) {
            log.error("根据父分类获取子分类失败，父分类ID：{}", parentId, e);
            return new ArrayList<>();
        }
    }
    
    /**
     * 根据标签搜索商品
     * 标签在productTags字段中，使用英文逗号分隔
     */
    public List<CarProductListDto> getProductsByTag(String tag) {
        try {
            if (tag == null || tag.trim().isEmpty()) {
                return new ArrayList<>();
            }
            
            Example example = new Example(CarProductEntity.class);
            Example.Criteria criteria = example.createCriteria()
                .andEqualTo("onSale", 1)
                .andEqualTo("delFlag", 0);
            
            // 使用LIKE查询，匹配包含该标签的商品
            // 标签可能是：tag, tag, 或 ,tag, 或 tag, 或 ,tag
            String searchPattern = "%" + tag.trim() + "%";
            criteria.andLike("productTags", searchPattern);
            
            example.orderBy("id").desc();
            
            List<CarProductEntity> products = carProductMapper.selectByExample(example);
            List<CarProductListDto> result = new ArrayList<>();
            
            // 过滤出真正包含该标签的商品（精确匹配）
            String tagToMatch = tag.trim();
            for (CarProductEntity product : products) {
                if (product.getProductTags() != null) {
                    String[] tags = product.getProductTags().split(",");
                    boolean found = false;
                    for (String t : tags) {
                        if (t.trim().equals(tagToMatch)) {
                            found = true;
                            break;
                        }
                    }
                    if (found) {
                        CarProductListDto dto = convertToDto(product);
                        result.add(dto);
                    }
                }
            }
            
            return result;
        } catch (Exception e) {
            log.error("根据标签搜索商品失败，标签：{}", tag, e);
            return new ArrayList<>();
        }
    }
    
    /**
     * 将实体转换为DTO
     */
    private CarProductListDto convertToDto(CarProductEntity product) {
        CarProductListDto dto = new CarProductListDto();
        dto.setId(product.getId());
        dto.setName(product.getProductTitle());
        dto.setAlias(product.getProductDespShort());
        dto.setModel(null); // 新表没有model字段
        dto.setPrice(product.getSalePrice() != null ? product.getSalePrice().longValue() : 0L);
        dto.setMarketPrice(product.getSupplyPrice() != null ? product.getSupplyPrice().longValue() : 0L);
        dto.setPromotionalPrice(product.getPromotionalPrice() != null ? product.getPromotionalPrice().longValue() : null);
        dto.setBrand(null); // 新表没有brand字段
        dto.setTag(product.getProductTags());
        dto.setSource(null); // 新表没有source字段
        dto.setMemo(product.getProductDesp());
        dto.setCDt(null); // 新表没有创建时间字段
        
        // 获取商品第一张图片
        List<CarProductImageEntity> images = getProductImages(product.getId());
        if (images != null && !images.isEmpty()) {
            String imageUrl = images.get(0).getImageUrl();
            // 如果imageUrl已经是完整URL（以http://或https://开头），则不需要加前缀
            // 否则加上前缀
            if (imageUrl != null && (imageUrl.startsWith("http://") || imageUrl.startsWith("https://"))) {
                dto.setImage(imageUrl);
            } else {
                // 如果imageUrl以/开头，直接拼接前缀，否则加上/分隔符
                if (imageUrl != null && imageUrl.startsWith("/")) {
                    dto.setImage(imagePrefix + imageUrl);
                } else if (imageUrl != null) {
                    dto.setImage(imagePrefix + "/" + imageUrl);
                } else {
                    dto.setImage(imagePrefix + "/img/product/default_90x90.jpg");
                }
            }
        } else {
            dto.setImage(imagePrefix + "/img/product/default_90x90.jpg");
        }
        
        // 设置分类
        if (product.getCategoryId() != null) {
            dto.setCategoryId(product.getCategoryId());
            CarProductCategoryEntity category = getCategoryById(product.getCategoryId());
            if (category != null) {
                dto.setCategory(category.getCategoryName());
            }
        }
        
        // 设置商品标签
        dto.setProductTags(product.getProductTags());
        
        // 设置描述
        dto.setDescription(product.getProductDespShort());
        
        return dto;
    }
}

