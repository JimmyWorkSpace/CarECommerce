package cc.carce.sale.service;

import cc.carce.sale.entity.CarRichContentEntity;
import cc.carce.sale.mapper.manager.CarRichContentMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 富文本内容服务
 */
@Slf4j
@Service
public class CarRichContentService {
    
    @Autowired
    private CarRichContentMapper carRichContentMapper;
    
    /**
     * 创建富文本内容
     */
    public boolean createRichContent(CarRichContentEntity entity) {
        try {
            entity.setDelFlag(false);
            entity.setCreateTime(LocalDateTime.now());
            
            int result = carRichContentMapper.insert(entity);
            log.info("创建富文本内容成功，ID: {}, 标题: {}, 类型: {}", 
                    entity.getId(), entity.getTitle(), entity.getContentType());
            return result > 0;
        } catch (Exception e) {
            log.error("创建富文本内容失败", e);
            return false;
        }
    }
    
    /**
     * 根据ID查询富文本内容
     */
    public CarRichContentEntity getRichContentById(Long id) {
        try {
            return carRichContentMapper.selectById(id);
        } catch (Exception e) {
            log.error("查询富文本内容失败，ID: {}", id, e);
            return null;
        }
    }
    
    /**
     * 更新富文本内容
     */
    public boolean updateRichContent(CarRichContentEntity entity) {
        try {
            int result = carRichContentMapper.updateById(entity);
            log.info("更新富文本内容成功，ID: {}", entity.getId());
            return result > 0;
        } catch (Exception e) {
            log.error("更新富文本内容失败，ID: {}", entity.getId(), e);
            return false;
        }
    }
    
    /**
     * 删除富文本内容（逻辑删除）
     */
    public boolean deleteRichContent(Long id) {
        try {
            int result = carRichContentMapper.deleteById(id);
            log.info("删除富文本内容成功，ID: {}", id);
            return result > 0;
        } catch (Exception e) {
            log.error("删除富文本内容失败，ID: {}", id, e);
            return false;
        }
    }
    
    /**
     * 根据内容类型查询富文本内容列表
     */
    public List<CarRichContentEntity> getRichContentByType(Integer contentType) {
        try {
            return carRichContentMapper.selectByContentType(contentType);
        } catch (Exception e) {
            log.error("根据类型查询富文本内容失败，类型: {}", contentType, e);
            return null;
        }
    }
    
    /**
     * 查询所有富文本内容列表
     */
    public List<CarRichContentEntity> getAllRichContent() {
        try {
            return carRichContentMapper.selectAll();
        } catch (Exception e) {
            log.error("查询所有富文本内容失败", e);
            return null;
        }
    }
    
    /**
     * 根据内容类型和排序查询富文本内容列表
     */
    public List<CarRichContentEntity> getRichContentByTypeOrderByShowOrder(Integer contentType) {
        try {
            return carRichContentMapper.selectByContentTypeOrderByShowOrder(contentType);
        } catch (Exception e) {
            log.error("根据类型和排序查询富文本内容失败，类型: {}", contentType, e);
            return null;
        }
    }
    
    /**
     * 获取关于页面的富文本内容
     */
    public List<CarRichContentEntity> getAboutContent() {
        return getRichContentByTypeOrderByShowOrder(CarRichContentEntity.ContentType.ABOUT.getCode());
    }
    
    /**
     * 获取关于页面的第一项富文本内容
     */
    public CarRichContentEntity getFirstAboutContent() {
        try {
            List<CarRichContentEntity> contentList = getRichContentByTypeOrderByShowOrder(CarRichContentEntity.ContentType.ABOUT.getCode());
            if (contentList != null && !contentList.isEmpty()) {
                return contentList.get(0);
            }
            return null;
        } catch (Exception e) {
            log.error("获取关于页面第一项内容失败", e);
            return null;
        }
    }
    
    /**
     * 获取频道页面的富文本内容
     */
    public List<CarRichContentEntity> getChannelContent() {
        return getRichContentByTypeOrderByShowOrder(CarRichContentEntity.ContentType.CHANNEL.getCode());
    }
    
    /**
     * 获取频道页面的第一项富文本内容
     */
    public CarRichContentEntity getFirstChannelContent() {
        try {
            List<CarRichContentEntity> contentList = getRichContentByTypeOrderByShowOrder(CarRichContentEntity.ContentType.CHANNEL.getCode());
            if (contentList != null && !contentList.isEmpty()) {
                return contentList.get(0);
            }
            return null;
        } catch (Exception e) {
            log.error("获取频道页面第一项内容失败", e);
            return null;
        }
    }
}
