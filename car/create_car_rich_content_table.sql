-- 创建富文本内容表
CREATE TABLE `car_rich_content` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '内容',
  `contentType` int(11) NULL DEFAULT NULL COMMENT '类型 1 关于 2 频道',
  `showOrder` int(11) NULL DEFAULT NULL COMMENT '排序',
  `delFlag` bit(1) NULL DEFAULT b'0' COMMENT '删除标记 1 是 0 否',
  `createTime` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '富文本内容' ROW_FORMAT = Compact;

-- 插入测试数据
INSERT INTO `car_rich_content` (`title`, `content`, `contentType`, `showOrder`, `delFlag`, `createTime`) VALUES
('关于我们', '<h1>关于我们</h1><p>我们是一家专业的汽车销售平台，致力于为客户提供优质的汽车购买服务。</p>', 1, 1, 0, NOW()),
('公司简介', '<h2>公司简介</h2><p>成立于2020年，我们拥有丰富的汽车行业经验和专业的服务团队。</p>', 1, 2, 0, NOW()),
('联系我们', '<h2>联系我们</h2><p>电话：400-123-4567<br>邮箱：contact@example.com</p>', 1, 3, 0, NOW()),
('汽车频道', '<h1>汽车频道</h1><p>为您提供最新的汽车资讯和专业的购车指导。</p>', 2, 1, 0, NOW()),
('购车指南', '<h2>购车指南</h2><p>详细的购车流程和注意事项，帮助您做出明智的选择。</p>', 2, 2, 0, NOW());
