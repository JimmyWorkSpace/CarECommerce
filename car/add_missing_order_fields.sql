-- 为 car_order_info 表添加缺失的字段
-- 根据建表语句补充缺少的字段

-- 添加收件人县市字段（如果不存在）
ALTER TABLE `car_order_info` 
ADD COLUMN IF NOT EXISTS `receiverCity` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '縣市' AFTER `orderStatus`;

-- 添加收件人区镇字段（如果不存在）
ALTER TABLE `car_order_info` 
ADD COLUMN IF NOT EXISTS `receiverDistrict` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '區鎮' AFTER `receiverCity`;

-- 添加收件人邮编字段（如果不存在）
ALTER TABLE `car_order_info` 
ADD COLUMN IF NOT EXISTS `receiverZipCode` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收件人邮编' AFTER `receiverMobile`;

-- 修改 totalPrice 字段类型以匹配建表语句（从 INT(11) 改为 INT(10)）
ALTER TABLE `car_order_info` 
MODIFY COLUMN `totalPrice` int(10) NULL DEFAULT NULL COMMENT '总价格';

-- 修改 receiverCity 字段长度以匹配建表语句（从 VARCHAR(1000) 改为 VARCHAR(50)）
ALTER TABLE `car_order_info` 
MODIFY COLUMN `receiverCity` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '縣市';

-- 修改 receiverDistrict 字段长度以匹配建表语句（从 VARCHAR(1000) 改为 VARCHAR(50)）
ALTER TABLE `car_order_info` 
MODIFY COLUMN `receiverDistrict` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '區鎮';

-- 修改 receiverAddress 字段长度以匹配建表语句（从 VARCHAR(1000) 改为 VARCHAR(1000)）
ALTER TABLE `car_order_info` 
MODIFY COLUMN `receiverAddress` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收件人地址';

-- 修改 receiverZipCode 字段长度以匹配建表语句（从 VARCHAR(1000) 改为 VARCHAR(20)）
ALTER TABLE `car_order_info` 
MODIFY COLUMN `receiverZipCode` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收件人邮编';

-- 修改 logicNumber 字段长度以匹配建表语句（从 VARCHAR(100) 改为 VARCHAR(100)）
ALTER TABLE `car_order_info` 
MODIFY COLUMN `logicNumber` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物流单号';

-- 修改 CVSStoreID 字段长度以匹配建表语句（从 VARCHAR(50) 改为 VARCHAR(50)）
ALTER TABLE `car_order_info` 
MODIFY COLUMN `CVSStoreID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '超商店舖編號';

-- 修改 CVSStoreName 字段长度以匹配建表语句（从 VARCHAR(100) 改为 VARCHAR(100)）
ALTER TABLE `car_order_info` 
MODIFY COLUMN `CVSStoreName` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '超商店舖名稱';

-- 修改 CVSAddress 字段长度以匹配建表语句（从 VARCHAR(255) 改为 VARCHAR(255)）
ALTER TABLE `car_order_info` 
MODIFY COLUMN `CVSAddress` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '超商店舖地址';

-- 修改 CVSTelephone 字段长度以匹配建表语句（从 VARCHAR(50) 改为 VARCHAR(50)）
ALTER TABLE `car_order_info` 
MODIFY COLUMN `CVSTelephone` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '超商店舖電話';

-- 修改 CVSOutSide 字段类型以匹配建表语句（从 INT(11) 改为 INT(11)）
ALTER TABLE `car_order_info` 
MODIFY COLUMN `CVSOutSide` int(11) NULL DEFAULT NULL COMMENT '使用者選擇的超商店舖是否為離島店鋪.0：本島,1：離島';

-- 添加索引（如果不存在）
ALTER TABLE `car_order_info` 
ADD INDEX IF NOT EXISTS `userId`(`userId`) USING BTREE;
