-- 支付订单表
CREATE TABLE `payment_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `merchant_trade_no` varchar(50) NOT NULL COMMENT '商户订单号',
  `ecpay_trade_no` varchar(50) DEFAULT NULL COMMENT '绿界支付交易号',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `total_amount` decimal(10,2) NOT NULL COMMENT '订单金额',
  `item_name` varchar(255) DEFAULT NULL COMMENT '商品名称',
  `trade_desc` text COMMENT '订单描述',
  `payment_type` varchar(50) DEFAULT NULL COMMENT '支付方式',
  `payment_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '支付状态：0-待支付，1-支付成功，2-支付失败，3-已取消',
  `payment_time` datetime DEFAULT NULL COMMENT '支付时间',
  `ecpay_status` varchar(50) DEFAULT NULL COMMENT '绿界支付状态',
  `ecpay_status_desc` varchar(255) DEFAULT NULL COMMENT '绿界支付状态描述',
  `error_message` text COMMENT '错误信息',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` bit(1) NOT NULL DEFAULT b'0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_merchant_trade_no` (`merchant_trade_no`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_payment_status` (`payment_status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='支付订单表';

-- 插入测试数据（可选）
INSERT INTO `payment_order` (`merchant_trade_no`, `user_id`, `total_amount`, `item_name`, `trade_desc`, `payment_status`, `create_time`, `update_time`, `del_flag`) 
VALUES 
('202412011200001', 1, 150000.00, '奔驰C200L', '购买奔驰C200L轿车', 0, NOW(), NOW(), 0),
('202412011200002', 1, 250000.00, '宝马3系', '购买宝马3系轿车', 0, NOW(), NOW(), 0);
