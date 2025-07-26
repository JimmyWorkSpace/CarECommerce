-- 轮播图表
CREATE TABLE `car_banner` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `imageUrl` varchar(500) DEFAULT NULL COMMENT '图片地址',
  `linkUrl` varchar(500) DEFAULT NULL COMMENT '跳转地址',
  `isLink` tinyint(1) DEFAULT '0' COMMENT '是否跳转(0:否,1:是)',
  `showOrder` int(11) DEFAULT '0' COMMENT '排序',
  `delFlag` tinyint(1) DEFAULT '0' COMMENT '删除标记(0:正常,1:删除)',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='轮播图表';

-- 插入示例数据
INSERT INTO `car_banner` (`imageUrl`, `linkUrl`, `isLink`, `showOrder`, `delFlag`, `createTime`) VALUES
('https://testcloud.carce.cc/img/car_sale/banner/sample1.jpg', 'https://example.com', 1, 1, 0, NOW()),
('https://testcloud.carce.cc/img/car_sale/banner/sample2.jpg', '', 0, 2, 0, NOW()),
('https://testcloud.carce.cc/img/car_sale/banner/sample3.jpg', 'https://example.com/page', 1, 3, 0, NOW()); 