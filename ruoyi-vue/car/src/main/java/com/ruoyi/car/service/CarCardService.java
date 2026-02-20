package com.ruoyi.car.service;

import com.ruoyi.car.domain.CarCardEntity;

import java.util.List;

/**
 * 票券方案 Service 介面
 *
 * @author ruoyi
 */
public interface CarCardService {

    /**
     * 查詢票券方案列表（支援名稱、狀態篩選）
     *
     * @param query 查詢條件，可為 null
     * @return 票券方案列表
     */
    List<CarCardEntity> selectCardList(CarCardEntity query);

    /**
     * 根據 ID 查詢票券方案
     *
     * @param id 主鍵
     * @return 票券方案
     */
    CarCardEntity selectCardById(Long id);

    /**
     * 新增票券方案
     *
     * @param carCard 票券方案
     * @return 影響行數
     */
    int insertCard(CarCardEntity carCard);

    /**
     * 修改票券方案
     *
     * @param carCard 票券方案
     * @return 影響行數
     */
    int updateCard(CarCardEntity carCard);

    /**
     * 刪除票券方案
     *
     * @param id 主鍵
     * @return 影響行數
     */
    int deleteCardById(Long id);

    /**
     * 批量刪除票券方案
     *
     * @param ids 主鍵陣列
     * @return 影響行數
     */
    int deleteCardByIds(Long[] ids);
}
