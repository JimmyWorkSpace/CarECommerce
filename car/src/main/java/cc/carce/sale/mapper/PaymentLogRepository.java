package cc.carce.sale.mapper;

import cc.carce.sale.entity.PaymentLog;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PaymentLogRepository extends JpaRepository<PaymentLog, Long> {
}
