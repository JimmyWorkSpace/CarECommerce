package cc.carce.sale.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cc.carce.sale.entity.CarOrderDetailEntity;
import cc.carce.sale.mapper.manager.CarOrderDetailMapper;
import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Example;

/**
 * 订单详情表服务
 */
@Service
@Slf4j
public class CarOrderDetailService {

    @Resource
    private CarOrderDetailMapper carOrderDetailMapper;

    /**
     * 创建订单详情
     */
    @Transactional
    public CarOrderDetailEntity createOrderDetail(Long orderId, Long productId, String productName, 
                                                Integer productAmount, BigDecimal productPrice) {
        try {
            CarOrderDetailEntity orderDetail = new CarOrderDetailEntity();
            orderDetail.setOrderId(orderId);
            orderDetail.setProductId(productId);
            orderDetail.setProductName(productName);
            orderDetail.setProductAmount(productAmount);
            orderDetail.setProductPrice(productPrice);
            orderDetail.setTotalPrice(productPrice.multiply(new BigDecimal(productAmount)));
            orderDetail.setDelFlag(false);
            orderDetail.setCreateTime(new Date());
            orderDetail.setShowOrder(0);
            
            carOrderDetailMapper.insert(orderDetail);
            
            log.info("创建订单详情成功，订单ID：{}，产品ID：{}", orderId, productId);
            return orderDetail;
            
        } catch (Exception e) {
            log.error("创建订单详情失败，订单ID：{}，产品ID：{}", orderId, productId, e);
            throw new RuntimeException("创建订单详情失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID查询订单详情
     */
    public CarOrderDetailEntity getOrderDetailById(Long id) {
        try {
            return carOrderDetailMapper.selectById(id);
        } catch (Exception e) {
            log.error("查询订单详情失败，详情ID：{}", id, e);
            return null;
        }
    }

    /**
     * 根据订单ID查询订单详情列表
     */
    public List<CarOrderDetailEntity> getOrderDetailsByOrderId(Long orderId) {
        try {
            Example example = new Example(CarOrderDetailEntity.class);
            example.createCriteria()
                .andEqualTo("orderId", orderId)
                .andEqualTo("delFlag", false);
            example.orderBy("createTime").asc();
            
            return carOrderDetailMapper.selectByExample(example);
        } catch (Exception e) {
            log.error("查询订单详情列表失败，订单ID：{}", orderId, e);
            return null;
        }
    }

    /**
     * 根据产品ID查询订单详情列表
     */
    public List<CarOrderDetailEntity> getOrderDetailsByProductId(Long productId) {
        try {
            Example example = new Example(CarOrderDetailEntity.class);
            example.createCriteria()
                .andEqualTo("productId", productId)
                .andEqualTo("delFlag", false);
            example.orderBy("createTime").desc();
            
            return carOrderDetailMapper.selectByExample(example);
        } catch (Exception e) {
            log.error("查询产品订单详情列表失败，产品ID：{}", productId, e);
            return null;
        }
    }

    /**
     * 根据订单ID和产品ID查询订单详情
     */
    public CarOrderDetailEntity getOrderDetailByOrderIdAndProductId(Long orderId, Long productId) {
        try {
            return carOrderDetailMapper.selectByOrderIdAndProductId(orderId, productId);
        } catch (Exception e) {
            log.error("查询订单详情失败，订单ID：{}，产品ID：{}", orderId, productId, e);
            return null;
        }
    }

    /**
     * 更新订单详情
     */
    @Transactional
    public boolean updateOrderDetail(CarOrderDetailEntity orderDetail) {
        try {
            int result = carOrderDetailMapper.updateById(orderDetail);
            if (result > 0) {
                log.info("更新订单详情成功，详情ID：{}", orderDetail.getId());
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("更新订单详情失败，详情ID：{}", orderDetail.getId(), e);
            return false;
        }
    }

    /**
     * 更新产品数量
     */
    @Transactional
    public boolean updateProductAmount(Long detailId, Integer productAmount) {
        try {
            int result = carOrderDetailMapper.updateProductAmount(detailId, productAmount);
            if (result > 0) {
                // 重新计算总价
                CarOrderDetailEntity detail = carOrderDetailMapper.selectById(detailId);
                if (detail != null) {
                    BigDecimal newTotalPrice = detail.getProductPrice().multiply(new BigDecimal(productAmount));
                    detail.setTotalPrice(newTotalPrice);
                    carOrderDetailMapper.updateById(detail);
                }
                
                log.info("更新产品数量成功，详情ID：{}，新数量：{}", detailId, productAmount);
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("更新产品数量失败，详情ID：{}，产品数量：{}", detailId, productAmount, e);
            return false;
        }
    }

    /**
     * 更新产品价格
     */
    @Transactional
    public boolean updateProductPrice(Long detailId, BigDecimal productPrice) {
        try {
            int result = carOrderDetailMapper.updateProductPrice(detailId, productPrice);
            if (result > 0) {
                // 重新计算总价
                CarOrderDetailEntity detail = carOrderDetailMapper.selectById(detailId);
                if (detail != null) {
                    BigDecimal newTotalPrice = productPrice.multiply(new BigDecimal(detail.getProductAmount()));
                    detail.setTotalPrice(newTotalPrice);
                    carOrderDetailMapper.updateById(detail);
                }
                
                log.info("更新产品价格成功，详情ID：{}，新价格：{}", detailId, productPrice);
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("更新产品价格失败，详情ID：{}，产品价格：{}", detailId, productPrice, e);
            return false;
        }
    }

    /**
     * 删除订单详情（逻辑删除）
     */
    @Transactional
    public boolean deleteOrderDetail(Long detailId) {
        try {
            int result = carOrderDetailMapper.deleteById(detailId);
            if (result > 0) {
                log.info("删除订单详情成功，详情ID：{}", detailId);
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("删除订单详情失败，详情ID：{}", detailId, e);
            return false;
        }
    }

    /**
     * 根据订单ID删除所有订单详情（逻辑删除）
     */
    @Transactional
    public boolean deleteOrderDetailsByOrderId(Long orderId) {
        try {
            int result = carOrderDetailMapper.deleteByOrderId(orderId);
            if (result > 0) {
                log.info("删除订单详情成功，订单ID：{}", orderId);
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("删除订单详情失败，订单ID：{}", orderId, e);
            return false;
        }
    }

    /**
     * 统计订单详情数量
     */
    public int countOrderDetailsByOrderId(Long orderId) {
        try {
            return carOrderDetailMapper.countByOrderId(orderId);
        } catch (Exception e) {
            log.error("统计订单详情数量失败，订单ID：{}", orderId, e);
            return 0;
        }
    }

    /**
     * 统计产品在订单中的总数量
     */
    public int sumProductAmountByProductId(Long productId) {
        try {
            return carOrderDetailMapper.sumProductAmountByProductId(productId);
        } catch (Exception e) {
            log.error("统计产品订单数量失败，产品ID：{}", productId, e);
            return 0;
        }
    }

    /**
     * 计算订单详情总金额
     */
    public BigDecimal calculateOrderDetailsTotal(Long orderId) {
        try {
            List<CarOrderDetailEntity> details = getOrderDetailsByOrderId(orderId);
            BigDecimal total = BigDecimal.ZERO;
            
            for (CarOrderDetailEntity detail : details) {
                if (detail.getTotalPrice() != null) {
                    total = total.add(detail.getTotalPrice());
                }
            }
            
            return total;
        } catch (Exception e) {
            log.error("计算订单详情总金额失败，订单ID：{}", orderId, e);
            return BigDecimal.ZERO;
        }
    }
}
