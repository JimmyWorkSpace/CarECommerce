-- 票券明細表：關聯訂單，存儲每張票券的唯一碼及核銷狀態
-- 每筆購買產生多張票券時會產生多筆明細（每張票一個唯一碼）
CREATE TABLE `car_card_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主鍵ID',
  `orderId` bigint(20) NOT NULL COMMENT '訂單ID（關聯 car_order_info.id）',
  `cardId` bigint(20) NOT NULL COMMENT '票券方案ID（關聯 car_card.id）',
  `cardName` varchar(100) NOT NULL COMMENT '票券名稱（冗余）',
  `ticketCode` varchar(32) NOT NULL COMMENT '票券唯一碼，英數字混合、唯一、不可預測',
  `redeemed` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否核銷：0=未核銷 1=已核銷',
  `redeemedTime` datetime DEFAULT NULL COMMENT '核銷時間',
  `createTime` datetime DEFAULT NULL COMMENT '建立時間',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_ticket_code` (`ticketCode`),
  KEY `idx_order_id` (`orderId`),
  KEY `idx_card_id` (`cardId`),
  KEY `idx_redeemed` (`redeemed`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='票券明細表（訂單關聯、唯一碼、核銷）';
