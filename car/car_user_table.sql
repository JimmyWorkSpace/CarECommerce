-- 创建用户表
CREATE TABLE IF NOT EXISTS `car_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `phoneNumber` varchar(20) NOT NULL COMMENT '手机号',
  `nickName` varchar(255) DEFAULT NULL COMMENT '昵称',
  `delFlag` bit(1) DEFAULT b'0' COMMENT '删除标记 1 是 0 否',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `lastLoginTime` datetime DEFAULT NULL COMMENT '上次登录时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_phone_number` (`phoneNumber`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 插入测试数据
INSERT INTO `car_user` (`phoneNumber`, `nickName`, `delFlag`, `createTime`, `lastLoginTime`) VALUES
('13800138000', '测试用户1', b'0', NOW(), NOW()),
('13800138001', '测试用户2', b'0', NOW(), NOW()),
('13800138002', '测试用户3', b'0', NOW(), NOW());
