package com.ruoyi.car.service;

import com.ruoyi.car.domain.CarCardDetailEntity;
import com.ruoyi.car.domain.vo.CarCardDetailVo;

import java.util.List;

/**
 * 票券明細 Service 介面（後台查詢與核銷）
 *
 * @author ruoyi
 */
public interface CarCardDetailService {

    /**
     * 依ID查詢票券明細詳情（含所屬用戶用戶名、手機號）
     */
    CarCardDetailVo getDetailById(Long id);

    /**
     * 依訂單ID查詢票券明細列表
     */
    List<CarCardDetailEntity> selectByOrderId(Long orderId);

    /**
     * 券碼列表，支援券碼模糊搜尋（含所屬用戶用戶名、手機號）
     */
    List<CarCardDetailVo> selectListByTicketCodeLike(String ticketCode);

    /**
     * 依票券唯一碼核銷，記錄核銷時間與當前登入用戶ID
     *
     * @return 更新筆數
     */
    int redeemByTicketCode(String ticketCode);
}
