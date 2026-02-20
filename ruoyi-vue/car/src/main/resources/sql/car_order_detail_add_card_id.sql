-- 訂單明細表增加票券方案ID，供卡券訂單使用
ALTER TABLE `car_order_detail`
  ADD COLUMN `cardId` bigint(20) DEFAULT NULL COMMENT '票券方案ID（關聯 car_card.id，卡券訂單時使用）' AFTER `priceId`;
