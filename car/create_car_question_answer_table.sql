-- 创建问答表
CREATE TABLE `car_question_answer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `channelId` bigint(20) NOT NULL COMMENT '频道ID',
  `question` varchar(500) NOT NULL COMMENT '问题',
  `answer` longtext NOT NULL COMMENT '答案（富文本）',
  `showOrder` int(11) DEFAULT '0' COMMENT '排序',
  `delFlag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记 1 是 0 否',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_channel_id` (`channelId`),
  KEY `idx_show_order` (`showOrder`),
  KEY `idx_del_flag` (`delFlag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='问答表';