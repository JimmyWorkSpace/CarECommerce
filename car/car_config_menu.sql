-- 创建网站配置表
CREATE TABLE `car_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '配置代码',
  `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '配置值',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '配置名称',
  `showOrder` int(11) DEFAULT 0 COMMENT '显示顺序',
  `delFlag` int(11) DEFAULT 0 COMMENT '删除标记 1 是 0 否',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_code` (`code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='网站配置表' ROW_FORMAT=Compact;

-- 插入网站配置数据
INSERT INTO `car_config` (`code`, `value`, `name`, `showOrder`, `delFlag`, `createTime`) VALUES
('kefu', '400-123-4567', '客服热线', 1, 0, NOW()),
('youxiang', 'service@carce.cc', '邮箱地址', 2, 0, NOW()),
('dizhi', '台北市信義區信義路五段7號', '公司地址', 3, 0, NOW()),
('fwsj1', '週一至週五：9:00-18:00', '服务时间1', 4, 0, NOW()),
('fwsj2', '週六至週日：10:00-17:00', '服务时间2', 5, 0, NOW()),
('fwsj3', '節假日：10:00-16:00', '服务时间3', 6, 0, NOW());
