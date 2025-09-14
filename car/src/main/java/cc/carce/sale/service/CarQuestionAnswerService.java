package cc.carce.sale.service;

import cc.carce.sale.entity.CarQuestionAnswerEntity;
import cc.carce.sale.mapper.manager.CarQuestionAnswerMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 问答服务类
 */
@Slf4j
@Service
public class CarQuestionAnswerService {

    @Autowired
    private CarQuestionAnswerMapper carQuestionAnswerMapper;

    /**
     * 创建问答
     */
    public boolean createQuestionAnswer(CarQuestionAnswerEntity entity) {
        try {
            entity.setCreateTime(LocalDateTime.now());
            entity.setDelFlag(false);
            int result = carQuestionAnswerMapper.insert(entity);
            return result > 0;
        } catch (Exception e) {
            log.error("创建问答失败", e);
            return false;
        }
    }

    /**
     * 根据ID获取问答
     */
    public CarQuestionAnswerEntity getQuestionAnswerById(Long id) {
        try {
            return carQuestionAnswerMapper.selectById(id);
        } catch (Exception e) {
            log.error("获取问答失败，ID: {}", id, e);
            return null;
        }
    }

    /**
     * 更新问答
     */
    public boolean updateQuestionAnswer(CarQuestionAnswerEntity entity) {
        try {
            int result = carQuestionAnswerMapper.updateById(entity);
            return result > 0;
        } catch (Exception e) {
            log.error("更新问答失败", e);
            return false;
        }
    }

    /**
     * 删除问答（逻辑删除）
     */
    public boolean deleteQuestionAnswer(Long id) {
        try {
            int result = carQuestionAnswerMapper.deleteById(id);
            return result > 0;
        } catch (Exception e) {
            log.error("删除问答失败，ID: {}", id, e);
            return false;
        }
    }

    /**
     * 根据频道ID获取问答列表
     */
    public List<CarQuestionAnswerEntity> getQuestionAnswersByChannelId(Long channelId) {
        try {
            return carQuestionAnswerMapper.selectByChannelId(channelId);
        } catch (Exception e) {
            log.error("根据频道ID获取问答失败，频道ID: {}", channelId, e);
            return null;
        }
    }

    /**
     * 获取所有问答
     */
    public List<CarQuestionAnswerEntity> getAllQuestionAnswers() {
        try {
            return carQuestionAnswerMapper.selectAll();
        } catch (Exception e) {
            log.error("获取所有问答失败", e);
            return null;
        }
    }

    /**
     * 根据频道ID获取问答列表（按排序）
     */
    public List<CarQuestionAnswerEntity> getQuestionAnswersByChannelIdOrderByShowOrder(Long channelId) {
        try {
            return carQuestionAnswerMapper.selectByChannelIdOrderByShowOrder(channelId);
        } catch (Exception e) {
            log.error("根据频道ID获取问答失败，频道ID: {}", channelId, e);
            return null;
        }
    }
}