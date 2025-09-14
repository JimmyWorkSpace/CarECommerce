-- 创建预约看车表
CREATE TABLE `car_appointment` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `carSaleId` BIGINT(20) NOT NULL COMMENT '车辆销售ID',
  `userId` BIGINT(20) NOT NULL COMMENT '用户ID',
  `appointmentName` VARCHAR(50) NOT NULL COMMENT '预约人姓名',
  `appointmentPhone` VARCHAR(20) NOT NULL COMMENT '预约人电话',
  `appointmentTime` DATETIME NOT NULL COMMENT '预约时间',
  `appointmentNote` TEXT COMMENT '预约备注',
  `appointmentStatus` INT(11) NOT NULL DEFAULT 1 COMMENT '预约状态：1-已预约，2-已看车，3-已取消',
  `delFlag` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '删除标记：0-未删除，1-已删除',
  `createTime` DATETIME NOT NULL COMMENT '创建时间',
  `updateTime` DATETIME COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_car_sale_id` (`carSaleId`),
  KEY `idx_user_id` (`userId`),
  KEY `idx_appointment_time` (`appointmentTime`),
  KEY `idx_appointment_status` (`appointmentStatus`),
  KEY `idx_create_time` (`createTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预约看车表';
