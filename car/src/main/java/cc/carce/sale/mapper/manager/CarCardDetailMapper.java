package cc.carce.sale.mapper.manager;

import cc.carce.sale.entity.CarCardDetailEntity;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * 票券明細 Mapper
 */
public interface CarCardDetailMapper extends Mapper<CarCardDetailEntity> {

    /**
     * 依票券唯一碼查詢數量（用於唯一性檢查）
     */
    int countByTicketCode(@Param("ticketCode") String ticketCode);

    /**
     * 依訂單ID查詢明細列表
     */
    List<CarCardDetailEntity> selectByOrderId(@Param("orderId") Long orderId);
}
