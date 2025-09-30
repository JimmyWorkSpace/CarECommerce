package cc.carce.sale.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cc.carce.sale.dto.CarProductListDto;
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
    public List<CarProductListDto> getPublicProducts() {
        try {
            Example example = new Example(CarProductsEntity.class);
            example.createCriteria()
                .andEqualTo("isPublic", true)
                .andNotEqualTo("price", 0);
            example.orderBy("cDt").desc();
            
            List<CarProductsEntity> products = carProductsMapper.selectByExample(example);
            List<CarProductListDto> result = new ArrayList<>();
            
            for (CarProductsEntity product : products) {
                CarProductListDto dto = new CarProductListDto();
                dto.setId(product.getId());
                dto.setName(product.getName());
                dto.setAlias(product.getAlias());
                dto.setModel(product.getModel());
                dto.setMarketPrice(product.getMarketPrice());
                dto.setBrand(product.getBrand());
                dto.setTag(product.getTag());
                dto.setSource(product.getSource());
                dto.setMemo(product.getMemo());
                dto.setCDt(product.getCDt());
                
                // 根据商品ID获取图片
                String imageUrl = getProductImageUrl(product.getId());
                dto.setImage(imageUrl);
                
                // 设置商品分类（根据tag字段）
                String category = mapTagToCategory(product.getTag());
                dto.setCategory(category);
                
                // 设置商品描述
                String description = buildProductDescription(product);
                dto.setDescription(description);
                
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
    public CarProductsEntity getProductById(Long productId) {
        try {
            return carProductsMapper.selectByPrimaryKey(productId);
        } catch (Exception e) {
            log.error("查询商品失败，商品ID：{}", productId, e);
            return null;
        }
    }
    
    /**
     * 根据分类获取商品
     */
    public List<CarProductListDto> getProductsByCategory(String category) {
        List<CarProductListDto> allProducts = getPublicProducts();
        
        if ("all".equals(category)) {
            return allProducts;
        }
        
        return allProducts.stream()
            .filter(product -> category.equals(product.getCategory()))
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
