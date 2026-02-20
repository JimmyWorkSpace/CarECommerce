-- 票券明細表增加核銷人ID
ALTER TABLE `car_card_detail`
ADD COLUMN `redeemedUserId` bigint(20) NULL COMMENT '核銷人ID' AFTER `createTime`;
