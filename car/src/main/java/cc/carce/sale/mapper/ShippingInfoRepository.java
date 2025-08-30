package cc.carce.sale.mapper;

import cc.carce.sale.entity.ShippingInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShippingInfoRepository extends JpaRepository<ShippingInfo, Long> {
    ShippingInfo findByOrderNo(String orderNo);
    ShippingInfo findByTrackingNo(String trackingNo);  
}
