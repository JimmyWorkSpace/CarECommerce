package com.ruoyi.car.mapper.master;

import com.ruoyi.car.domain.CarCardDetailEntity;
import com.ruoyi.car.domain.vo.CarCardDetailVo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * 票券明細 Mapper
 *
 * @author ruoyi
 */
public interface CarCardDetailMapper extends Mapper<CarCardDetailEntity> {

    /**
     * 依ID查詢明細詳情（含所屬用戶用戶名、手機號）
     */
    CarCardDetailVo selectDetailById(@Param("id") Long id);

    /**
     * 依訂單ID查詢明細列表
     */
    List<CarCardDetailEntity> selectByOrderId(@Param("orderId") Long orderId);

    /**
     * 依券碼模糊查詢列表
     */
    List<CarCardDetailEntity> selectListByTicketCodeLike(@Param("ticketCode") String ticketCode);

    /**
     * 依券碼模糊查詢列表（含所屬用戶用戶名、手機號）
     */
    List<CarCardDetailVo> selectListByTicketCodeLikeVo(@Param("ticketCode") String ticketCode);

    /**
     * 依票券唯一碼更新核銷狀態及核銷人ID
     */
    int updateRedeemedByTicketCode(@Param("ticketCode") String ticketCode, @Param("redeemed") Integer redeemed, @Param("redeemedTime") java.util.Date redeemedTime, @Param("redeemedUserId") Long redeemedUserId);
}
