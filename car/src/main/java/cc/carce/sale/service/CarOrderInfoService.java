package cc.carce.sale.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cc.carce.sale.entity.CarOrderInfoEntity;
import cc.carce.sale.form.CreatePaymentForm;
import cc.carce.sale.entity.CarOrderDetailEntity;
import cc.carce.sale.mapper.manager.CarOrderInfoMapper;
import cc.carce.sale.mapper.manager.CarOrderDetailMapper;
import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Example;

/**
 * 订单主表服务
 */
@Service
@Slf4j
public class CarOrderInfoService {

    @Resource
    private CarOrderInfoMapper carOrderInfoMapper;
    
    @Resource
    private CarOrderDetailMapper carOrderDetailMapper;

    /**
     * 创建订单
     */
    @Transactional
    public CarOrderInfoEntity createOrder(Long userId, String orderNo, CreatePaymentForm form, List<CarOrderDetailEntity> orderDetails) {
        try {
            // 计算总价格
            Integer totalPrice = calculateTotalPrice(orderDetails);
            
            // 创建订单主表
            CarOrderInfoEntity orderInfo = new CarOrderInfoEntity();
            orderInfo.setOrderNo(orderNo);
            orderInfo.setUserId(userId);
            orderInfo.setTotalPrice(totalPrice);
            orderInfo.setDelFlag(false);
            orderInfo.setCreateTime(new Date());
            orderInfo.setShowOrder(0);
            orderInfo.setOrderStatus(CarOrderInfoEntity.OrderStatus.UNPAID.getCode());
            orderInfo.setOrderType(1); // 默认宅配到府
            orderInfo.setOrderBizType(1); // 默认普通商品订单
            orderInfo.setReceiverName(form.getReceiverName());
            orderInfo.setReceiverMobile(form.getReceiverMobile());
            orderInfo.setReceiverAddress(form.getReceiverAddress());
            orderInfo.setReceiverCity(form.getReceiverCity());
            orderInfo.setReceiverDistrict(form.getReceiverDistrict());
            orderInfo.setReceiverZipCode(form.getReceiverZipCode());
            
            // 插入订单主表
            carOrderInfoMapper.insert(orderInfo);
            
            // 插入订单详情
            for (CarOrderDetailEntity detail : orderDetails) {
                detail.setOrderId(orderInfo.getId());
                detail.setDelFlag(false);
                detail.setCreateTime(new Date());
                detail.setShowOrder(0);
                detail.setTotalPrice(detail.getProductPrice() * detail.getProductAmount());
                carOrderDetailMapper.insert(detail);
            }
            
            log.info("创建订单成功，订单号：{}，用户ID：{}", orderNo, userId);
            return orderInfo;
            
        } catch (Exception e) {
            log.error("创建订单失败，用户ID：{}", userId, e);
            throw new RuntimeException("创建订单失败：" + e.getMessage());
        }
    }

    /**
     * 建立卡券訂單（orderBizType=2，不需地址等收件資訊）
     */
    @Transactional
    public CarOrderInfoEntity createCardOrder(Long userId, String orderNo, List<CarOrderDetailEntity> orderDetails) {
        try {
            Integer totalPrice = calculateTotalPrice(orderDetails);
            CarOrderInfoEntity orderInfo = new CarOrderInfoEntity();
            orderInfo.setOrderNo(orderNo);
            orderInfo.setUserId(userId);
            orderInfo.setTotalPrice(totalPrice);
            orderInfo.setDelFlag(false);
            orderInfo.setCreateTime(new Date());
            orderInfo.setShowOrder(0);
            orderInfo.setOrderStatus(CarOrderInfoEntity.OrderStatus.UNPAID.getCode());
            orderInfo.setOrderType(1);
            orderInfo.setOrderBizType(2); // 卡券訂單
            // 地址等不需填寫
            carOrderInfoMapper.insert(orderInfo);
            for (CarOrderDetailEntity detail : orderDetails) {
                detail.setOrderId(orderInfo.getId());
                detail.setDelFlag(false);
                detail.setCreateTime(new Date());
                detail.setShowOrder(0);
                detail.setTotalPrice(detail.getProductPrice() * detail.getProductAmount());
                carOrderDetailMapper.insert(detail);
            }
            log.info("建立卡券訂單成功，訂單號：{}，用戶ID：{}", orderNo, userId);
            return orderInfo;
        } catch (Exception e) {
            log.error("建立卡券訂單失敗，用戶ID：{}", userId, e);
            throw new RuntimeException("建立卡券訂單失敗：" + e.getMessage());
        }
    }

    /**
     * 更新订单信息
     */
    @Transactional
    public boolean updateOrder(CarOrderInfoEntity orderInfo) {
        try {
            int result = carOrderInfoMapper.updateByPrimaryKeySelective(orderInfo);
            if (result > 0) {
                log.info("更新订单信息成功，订单ID：{}", orderInfo.getId());
                return true;
            } else {
                log.error("更新订单信息失败，订单ID：{}", orderInfo.getId());
                return false;
            }
        } catch (Exception e) {
            log.error("更新订单信息异常，订单ID：{}", orderInfo.getId(), e);
            throw new RuntimeException("更新订单信息失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID查询订单
     */
    public CarOrderInfoEntity getOrderById(Long id) {
        try {
            return carOrderInfoMapper.selectByPrimaryKey(id);
        } catch (Exception e) {
            log.error("查询订单失败，订单ID：{}", id, e);
            return null;
        }
    }

    /**
     * 根据订单号查询订单
     */
    public CarOrderInfoEntity getOrderByOrderNo(String orderNo) {
        try {
            Example example = new Example(CarOrderInfoEntity.class);
            example.createCriteria()
                .andEqualTo("orderNo", orderNo)
                .andEqualTo("delFlag", false);
            return carOrderInfoMapper.selectOneByExample(example);
        } catch (Exception e) {
            log.error("查询订单失败，订单号：{}", orderNo, e);
            return null;
        }
    }

    /**
     * 根据用户ID查询订单列表
     */
    public List<CarOrderInfoEntity> getOrdersByUserId(Long userId) {
        try {
            Example example = new Example(CarOrderInfoEntity.class);
            example.createCriteria()
                .andEqualTo("userId", userId)
                .andEqualTo("delFlag", false);
            example.orderBy("createTime").desc();
            
            return carOrderInfoMapper.selectByExample(example);
        } catch (Exception e) {
            log.error("查询用户订单列表失败，用户ID：{}", userId, e);
            return null;
        }
    }

    /**
     * 根据用户ID和订单状态查询订单列表
     */
    public List<CarOrderInfoEntity> getOrdersByUserIdAndStatus(Long userId, Integer orderStatus) {
        try {
            return carOrderInfoMapper.selectByUserIdAndStatus(userId, orderStatus);
        } catch (Exception e) {
            log.error("查询用户订单列表失败，用户ID：{}，订单状态：{}", userId, orderStatus, e);
            return null;
        }
    }

    /**
     * 更新订单状态
     */
    @Transactional
    public boolean updateOrderStatus(Long orderId, Integer orderStatus) {
        try {
            int result = carOrderInfoMapper.updateOrderStatus(orderId, orderStatus);
            if (result > 0) {
                log.info("更新订单状态成功，订单ID：{}，新状态：{}", orderId, orderStatus);
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("更新订单状态失败，订单ID：{}，订单状态：{}", orderId, orderStatus, e);
            return false;
        }
    }

    /**
     * 更新物流信息
     */
    @Transactional
    public boolean updateLogisticsInfo(Long orderId, String logicNumber, String receiverName, 
                                     String receiverMobile, String receiverAddress) {
        try {
            int result = carOrderInfoMapper.updateLogisticsInfo(orderId, logicNumber, receiverName, 
                                                              receiverMobile, receiverAddress, null, null, null);
            if (result > 0) {
                log.info("更新物流信息成功，订单ID：{}，物流单号：{}", orderId, logicNumber);
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("更新物流信息失败，订单ID：{}，物流单号：{}", orderId, logicNumber, e);
            return false;
        }
    }

    /**
     * 更新物流信息（包含完整收件人信息）
     */
    @Transactional
    public boolean updateLogisticsInfo(Long orderId, String logicNumber, String receiverName, 
                                     String receiverMobile, String receiverAddress, String receiverCity,
                                     String receiverDistrict, String receiverZipCode) {
        try {
            int result = carOrderInfoMapper.updateLogisticsInfo(orderId, logicNumber, receiverName, 
                                                              receiverMobile, receiverAddress, receiverCity,
                                                              receiverDistrict, receiverZipCode);
            if (result > 0) {
                log.info("更新物流信息成功，订单ID：{}，物流单号：{}", orderId, logicNumber);
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("更新物流信息失败，订单ID：{}，物流单号：{}", orderId, logicNumber, e);
            return false;
        }
    }

    /**
     * 取消订单
     */
    @Transactional
    public boolean cancelOrder(Long orderId) {
        try {
            return updateOrderStatus(orderId, CarOrderInfoEntity.OrderStatus.CANCELLED.getCode());
        } catch (Exception e) {
            log.error("取消订单失败，订单ID：{}", orderId, e);
            return false;
        }
    }

    /**
     * 删除订单（逻辑删除）
     */
    @Transactional
    public boolean deleteOrder(Long orderId) {
        try {
            int result = carOrderInfoMapper.deleteById(orderId);
            if (result > 0) {
                // 同时删除订单详情
                carOrderDetailMapper.deleteByOrderId(orderId);
                log.info("删除订单成功，订单ID：{}", orderId);
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("删除订单失败，订单ID：{}", orderId, e);
            return false;
        }
    }

    /**
     * 统计用户订单数量
     */
    public int countOrdersByUserId(Long userId) {
        try {
            return carOrderInfoMapper.countByUserId(userId);
        } catch (Exception e) {
            log.error("统计用户订单数量失败，用户ID：{}", userId, e);
            return 0;
        }
    }

    /**
     * 统计用户各状态订单数量
     */
    public int countOrdersByUserIdAndStatus(Long userId, Integer orderStatus) {
        try {
            return carOrderInfoMapper.countByUserIdAndStatus(userId, orderStatus);
        } catch (Exception e) {
            log.error("统计用户订单数量失败，用户ID：{}，订单状态：{}", userId, orderStatus, e);
            return 0;
        }
    }

    /**
     * 生成订单号
     */
    private String generateOrderNo() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        return "ORD" + timestamp + uuid;
    }

    /**
     * 计算总价格
     */
    private Integer calculateTotalPrice(List<CarOrderDetailEntity> orderDetails) {
        int total = 0;
        for (CarOrderDetailEntity detail : orderDetails) {
            int itemTotal = detail.getProductPrice() * detail.getProductAmount();
            total += itemTotal;
        }
        return total;
    }
}
