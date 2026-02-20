package cc.carce.sale.service;

import cc.carce.sale.dto.CardDetailWithValidityDto;
import cc.carce.sale.dto.MyTicketItemDto;
import cc.carce.sale.entity.CarCardDetailEntity;
import cc.carce.sale.entity.CarCardEntity;
import cc.carce.sale.entity.CarOrderDetailEntity;
import cc.carce.sale.entity.CarOrderInfoEntity;
import cc.carce.sale.mapper.manager.CarCardDetailMapper;
import cc.carce.sale.util.TicketCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 票券明細服務：支付成功後為卡券訂單產生明細與唯一碼
 */
@Slf4j
@Service
public class CarCardDetailService {

    private static final int MAX_CODE_RETRY = 10;

    @Resource
    private CarCardDetailMapper carCardDetailMapper;

    @Resource
    private CarOrderDetailService carOrderDetailService;

    @Resource
    private CarOrderInfoService carOrderInfoService;

    @Resource
    private CarCardService carCardService;

    /**
     * 為卡券訂單產生票券明細（每張票一個唯一碼）
     */
    @Transactional
    public void createDetailsForCardOrder(Long orderId) {
        List<CarOrderDetailEntity> details = carOrderDetailService.getOrderDetailsByOrderId(orderId);
        if (details == null) return;
        Date now = new Date();
        for (CarOrderDetailEntity d : details) {
            if (d.getCardId() == null) continue;
            String cardName = d.getProductName() != null ? d.getProductName() : "";
            int amount = d.getProductAmount() != null ? d.getProductAmount() : 0;
            for (int i = 0; i < amount; i++) {
                String code = generateUniqueTicketCode();
                CarCardDetailEntity detail = new CarCardDetailEntity();
                detail.setOrderId(orderId);
                detail.setCardId(d.getCardId());
                detail.setCardName(cardName);
                detail.setTicketCode(code);
                detail.setRedeemed(0);
                detail.setCreateTime(now);
                carCardDetailMapper.insertSelective(detail);
            }
        }
        log.info("已為卡券訂單產生票券明細，orderId: {}", orderId);
    }

    /**
     * 產生一組尚未使用過的票券唯一碼
     */
    private String generateUniqueTicketCode() {
        for (int i = 0; i < MAX_CODE_RETRY; i++) {
            String code = TicketCodeUtil.generate();
            if (carCardDetailMapper.countByTicketCode(code) == 0) {
                return code;
            }
        }
        throw new IllegalStateException("產生票券唯一碼重試次數過多");
    }

    public List<CarCardDetailEntity> listByOrderId(Long orderId) {
        return carCardDetailMapper.selectByOrderId(orderId);
    }

    /**
     * 該訂單是否已產生過票券明細（用於避免重複產生）
     */
    public boolean hasDetailsForOrder(Long orderId) {
        if (orderId == null) return false;
        List<CarCardDetailEntity> list = carCardDetailMapper.selectByOrderId(orderId);
        return list != null && !list.isEmpty();
    }

    /**
     * 依訂單ID查詢票券明細，並依 cardId 關聯 car_card 計算過期時間
     */
    public List<CardDetailWithValidityDto> listByOrderIdWithValidity(Long orderId) {
        if (orderId == null) return new ArrayList<>();
        List<CarCardDetailEntity> list = carCardDetailMapper.selectByOrderId(orderId);
        if (list == null || list.isEmpty()) return new ArrayList<>();
        CarOrderInfoEntity order = carOrderInfoService.getOrderById(orderId);
        Date orderCreateTime = order != null ? order.getCreateTime() : new Date();
        List<CardDetailWithValidityDto> result = new ArrayList<>();
        for (CarCardDetailEntity d : list) {
            CardDetailWithValidityDto vo = new CardDetailWithValidityDto();
            vo.setId(d.getId());
            vo.setOrderId(d.getOrderId());
            vo.setCardId(d.getCardId());
            vo.setCardName(d.getCardName());
            vo.setTicketCode(d.getTicketCode());
            vo.setRedeemed(d.getRedeemed());
            vo.setRedeemedTime(d.getRedeemedTime());
            vo.setCreateTime(d.getCreateTime());
            if (d.getCardId() != null) {
                CarCardEntity card = carCardService.getById(d.getCardId());
                if (card != null) {
                    Date endTime = computeValidityEndTime(card, orderCreateTime);
                    vo.setValidityEndTime(endTime);
                }
            }
            result.add(vo);
        }
        return result;
    }

    /**
     * 依票券方案與訂單建立時間計算過期時間
     */
    private Date computeValidityEndTime(CarCardEntity card, Date orderCreateTime) {
        if (card.getValidityType() == null) return null;
        if (card.getValidityType() == 1) {
            if (card.getValidityEndDate() == null) return null;
            Calendar cal = Calendar.getInstance();
            cal.setTime(card.getValidityEndDate());
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            cal.set(Calendar.MILLISECOND, 999);
            return cal.getTime();
        }
        if (card.getValidityType() == 2 && card.getValidityDays() != null && card.getValidityDays() > 0) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(orderCreateTime != null ? orderCreateTime : new Date());
            cal.add(Calendar.DAY_OF_MONTH, card.getValidityDays());
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            cal.set(Calendar.MILLISECOND, 999);
            return cal.getTime();
        }
        return null;
    }

    /**
     * 依用戶ID查詢其已購買之票券列表（僅已支付之卡券訂單），含狀態：未核銷／已核銷／已過期
     */
    public List<MyTicketItemDto> listMyTicketsByUserId(Long userId) {
        if (userId == null) return new ArrayList<>();
        List<CarOrderInfoEntity> orders = carOrderInfoService.getOrdersByUserId(userId);
        if (orders == null) return new ArrayList<>();
        Date now = new Date();
        List<MyTicketItemDto> result = new ArrayList<>();
        for (CarOrderInfoEntity order : orders) {
            if (order.getOrderBizType() == null || order.getOrderBizType() != 2) continue;
            if (order.getOrderStatus() == null || order.getOrderStatus() != 2) continue;
            List<CardDetailWithValidityDto> list = listByOrderIdWithValidity(order.getId());
            for (CardDetailWithValidityDto d : list) {
                MyTicketItemDto item = new MyTicketItemDto();
                item.setId(d.getId());
                item.setOrderId(d.getOrderId());
                item.setCardName(d.getCardName());
                item.setTicketCode(d.getTicketCode());
                item.setCreateTime(d.getCreateTime());
                item.setRedeemedTime(d.getRedeemedTime());
                item.setValidityEndTime(d.getValidityEndTime());
                String status = computeTicketStatus(d.getRedeemed(), d.getValidityEndTime(), now);
                item.setStatus(status);
                result.add(item);
            }
        }
        result.sort((a, b) -> {
            Date t1 = a.getCreateTime();
            Date t2 = b.getCreateTime();
            if (t1 == null && t2 == null) return 0;
            if (t1 == null) return 1;
            if (t2 == null) return -1;
            return t2.compareTo(t1);
        });
        return result;
    }

    private String computeTicketStatus(Integer redeemed, Date validityEndTime, Date now) {
        if (redeemed != null && redeemed == 1) return "已核銷";
        if (validityEndTime != null && validityEndTime.before(now)) return "已過期";
        return "未核銷";
    }
}
