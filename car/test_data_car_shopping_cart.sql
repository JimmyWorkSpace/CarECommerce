-- 购物车测试数据
-- 注意：需要先有用户数据和车辆数据

-- 插入购物车测试数据
INSERT INTO car_shopping_cart (userId, productId, productAmount, productPrice, productName, delFlag, createTime, showOrder) VALUES
(1, 1, 2, 150000.00, '丰田凯美瑞 2.0L 豪华版', 0, NOW(), 1),
(1, 2, 1, 200000.00, '本田雅阁 2.4L 尊贵版', 0, NOW(), 2),
(1, 3, 3, 120000.00, '日产天籁 2.0L 舒适版', 0, NOW(), 3),
(2, 4, 1, 180000.00, '大众帕萨特 1.8T 豪华版', 0, NOW(), 1),
(2, 5, 2, 160000.00, '别克君威 2.0T 精英版', 0, NOW(), 2);

-- 如果需要创建购物车表，可以使用以下SQL：
/*
CREATE TABLE IF NOT EXISTS car_shopping_cart (
    id BIGINT(20) AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    userId BIGINT(20) NOT NULL COMMENT '用户ID',
    productId BIGINT(20) NOT NULL COMMENT '产品ID',
    productAmount INT(11) NOT NULL COMMENT '产品数量',
    productPrice DECIMAL(10,2) COMMENT '加购时的产品价格',
    productName VARCHAR(255) COMMENT '产品名称',
    delFlag BIT(1) DEFAULT b'0' COMMENT '删除标记 1 是 0 否',
    createTime DATETIME COMMENT '创建时间',
    showOrder INT(11) COMMENT '显示顺序',
    INDEX idx_user_id (userId),
    INDEX idx_product_id (productId),
    INDEX idx_del_flag (delFlag)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购物车表';
*/
