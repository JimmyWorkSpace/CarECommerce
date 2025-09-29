package cc.carce.sale.service;

import cc.carce.sale.entity.CarConfigEntity;
import cc.carce.sale.entity.dto.CarConfigContent;
import cc.carce.sale.mapper.manager.CarConfigMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.List;

/**
 * 网站配置服务
 * 
 * @author ruoyi
 * @date 2024-01-01
 */
@Service
@Slf4j
public class CarConfigService {

    @Resource
    private CarConfigMapper carConfigMapper;

    /**
     * 获取所有配置项
     * 
     * @return 配置项列表
     */
    public List<CarConfigEntity> getAllConfigs() {
        CarConfigEntity query = new CarConfigEntity();
        query.setDelFlag(0); // 只查询未删除的配置
        return carConfigMapper.select(query);
    }

    /**
     * 根据代码获取配置值
     * 
     * @param code 配置代码
     * @return 配置值
     */
    public String getConfigValue(String code) {
        CarConfigEntity query = new CarConfigEntity();
        query.setCode(code);
        query.setDelFlag(0);
        CarConfigEntity config = carConfigMapper.selectOne(query);
        return config != null ? config.getValue() : null;
    }

    /**
     * 获取网站配置内容，映射到CarConfigContent对象
     * 使用缓存提高性能
     * 
     * @return 配置内容对象
     */
    @Cacheable(value = "carConfig", key = "'content'", unless = "#result == null")
    public CarConfigContent getConfigContent() {
        log.info("查询网站配置内容，未命中缓存");
        
        try {
            List<CarConfigEntity> configs = getAllConfigs();
            CarConfigContent content = new CarConfigContent();
            
            // 使用反射将配置项映射到CarConfigContent对象
            for (CarConfigEntity config : configs) {
                String code = config.getCode();
                String value = config.getValue();
                
                try {
                    // 根据code获取对应的字段
                    Field field = CarConfigContent.class.getDeclaredField(code);
                    field.setAccessible(true);
                    field.set(content, value);
                } catch (NoSuchFieldException e) {
                    log.warn("配置代码 {} 在CarConfigContent中没有对应的字段", code);
                } catch (IllegalAccessException e) {
                    log.error("设置配置字段 {} 失败", code, e);
                }
            }
            
            log.info("网站配置内容查询完成，配置项数量: {}", configs.size());
            return content;
            
        } catch (Exception e) {
            log.error("查询网站配置内容失败", e);
            throw e;
        }
    }
}
