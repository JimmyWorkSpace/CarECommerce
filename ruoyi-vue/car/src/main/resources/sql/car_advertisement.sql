-- 创建广告位表
CREATE TABLE `car_advertisement` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `imageUrl` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图片地址',
  `linkUrl` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '跳转地址',
  `isLink` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否跳转 1 是 0 否',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '显示内容，isLink为0时设置，富文本',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题，鼠标放入时的提示信息',
  `showOrder` int(11) NULL DEFAULT NULL COMMENT '排序',
  `delFlag` bit(1) NULL DEFAULT b'0' COMMENT '删除标记 1 是 0 否',
  `createTime` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '广告位' ROW_FORMAT = Compact;

-- 插入示例数据
INSERT INTO `car_advertisement` (`imageUrl`, `linkUrl`, `isLink`, `content`, `title`, `showOrder`, `delFlag`, `createTime`) VALUES
('https://example.com/image1.jpg', 'https://example.com/link1', b'1', NULL, '示例广告1', 1, b'0', NOW()),
('https://example.com/image2.jpg', NULL, b'0', '<p>这是一个富文本内容示例</p>', '示例广告2', 2, b'0', NOW()); 