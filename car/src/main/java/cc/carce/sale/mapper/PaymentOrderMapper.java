package cc.carce.sale.mapper;

import cc.carce.sale.entity.PaymentOrderEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 支付订单Mapper
 */
@Mapper
public interface PaymentOrderMapper {
    
    /**
     * 插入支付订单
     */
    int insert(PaymentOrderEntity paymentOrder);
    
    /**
     * 根据ID查询支付订单
     */
    PaymentOrderEntity selectById(@Param("id") Long id);
    
    /**
     * 根据商户订单号查询支付订单
     */
    PaymentOrderEntity selectByMerchantTradeNo(@Param("merchantTradeNo") String merchantTradeNo);
    
    /**
     * 根据绿界支付交易号查询支付订单
     */
    PaymentOrderEntity selectByEcpayTradeNo(@Param("ecpayTradeNo") String ecpayTradeNo);
    
    /**
     * 根据用户ID查询支付订单列表
     */
    List<PaymentOrderEntity> selectByUserId(@Param("userId") Long userId);
    
    /**
     * 更新支付订单
     */
    int updateById(PaymentOrderEntity paymentOrder);
    
    /**
     * 更新支付状态
     */
    int updatePaymentStatus(@Param("id") Long id, 
                           @Param("paymentStatus") Integer paymentStatus,
                           @Param("ecpayStatus") String ecpayStatus,
                           @Param("ecpayStatusDesc") String ecpayStatusDesc,
                           @Param("paymentTime") java.util.Date paymentTime);
    
    /**
     * 根据支付状态查询订单列表
     */
    List<PaymentOrderEntity> selectByPaymentStatus(@Param("paymentStatus") Integer paymentStatus);
    
    /**
     * 删除支付订单（逻辑删除）
     */
    int deleteById(@Param("id") Long id);
}
