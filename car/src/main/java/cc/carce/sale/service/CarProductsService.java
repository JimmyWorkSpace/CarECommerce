package cc.carce.sale.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cc.carce.sale.entity.CarProductsEntity;
import cc.carce.sale.mapper.carcecloud.CarProductsMapper;
import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Example;

@Service
@Slf4j
public class CarProductsService {

    @Resource
    private CarProductsMapper carProductsMapper;
    
    @Value("${carce.prefix}")
    private String imagePrefix;

    /**
     * 获取已发布的商品列表
     */
    public List<Map<String, Object>> getPublicProducts() {
        try {
            Example example = new Example(CarProductsEntity.class);
            example.createCriteria()
                .andEqualTo("isPublic", true)
                .andNotEqualTo("price", 0);
            example.orderBy("cDt").desc();
            
            List<CarProductsEntity> products = carProductsMapper.selectByExample(example);
            List<Map<String, Object>> result = new ArrayList<>();
            
            for (CarProductsEntity product : products) {
                Map<String, Object> productMap = new HashMap<>();
                productMap.put("id", product.getId());
                productMap.put("name", product.getName());
                productMap.put("alias", product.getAlias());
                productMap.put("model", product.getModel());
                productMap.put("price", product.getPrice());
                productMap.put("marketPrice", product.getMarketPrice());
                productMap.put("brand", product.getBrand());
                productMap.put("tag", product.getTag());
                productMap.put("source", product.getSource());
                productMap.put("memo", product.getMemo());
                productMap.put("cDt", product.getCDt());
                
                // 根据商品ID获取图片
                String imageUrl = getProductImageUrl(product.getId());
                productMap.put("image", imageUrl);
                
                // 设置商品分类（根据tag字段）
                String category = mapTagToCategory(product.getTag());
                productMap.put("category", category);
                
                // 设置商品描述
                String description = buildProductDescription(product);
                productMap.put("description", description);
                
                result.add(productMap);
            }
            
            return result;
        } catch (Exception e) {
            log.error("获取商品列表失败", e);
            return new ArrayList<>();
        }
    }
    
    /**
     * 根据分类获取商品
     */
    public List<Map<String, Object>> getProductsByCategory(String category) {
        List<Map<String, Object>> allProducts = getPublicProducts();
        
        if ("all".equals(category)) {
            return allProducts;
        }
        
        return allProducts.stream()
            .filter(product -> category.equals(product.get("category")))
            .collect(java.util.stream.Collectors.toList());
    }
    
    /**
     * 根据商品ID获取图片URL
     */
    private String getProductImageUrl(Long productId) {
        try {
            // 这里可以通过SQL查询获取商品对应的图片
            // 暂时使用默认图片，实际应该查询products_pimages和pimages表
            String defaultImage = "/img/product/" + productId + "_90x90.jpg";
            return imagePrefix + defaultImage;
        } catch (Exception e) {
            log.error("获取商品图片失败，商品ID: {}", productId, e);
            // 返回默认图片
            return imagePrefix + "/img/product/default_90x90.jpg";
        }
    }
    
    /**
     * 将商品标签映射到前端分类
     */
    private String mapTagToCategory(String tag) {
        if (tag == null) return "other";
        
        switch (tag.toLowerCase()) {
            case "發動機":
            case "引擎":
                return "engine";
            case "制動":
            case "煞車":
                return "brake";
            case "懸挂":
            case "懸架":
                return "suspension";
            case "電氣":
            case "電器":
                return "electrical";
            case "外觀":
            case "外飾":
                return "exterior";
            case "工具":
                return "tools";
            case "設備":
                return "equipment";
            default:
                return "other";
        }
    }
    
    /**
     * 构建商品描述
     */
    private String buildProductDescription(CarProductsEntity product) {
        StringBuilder description = new StringBuilder();
        
        if (product.getBrand() != null && !product.getBrand().isEmpty()) {
            description.append(product.getBrand()).append(" ");
        }
        
        if (product.getModel() != null && !product.getModel().isEmpty()) {
            description.append(product.getModel()).append(" ");
        }
        
        if (product.getAlias() != null && !product.getAlias().isEmpty()) {
            description.append(product.getAlias()).append(" ");
        }
        
        description.append("高品質汽車配件，提升您的駕駛體驗");
        
        return description.toString();
    }
}
