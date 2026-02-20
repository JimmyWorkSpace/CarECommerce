-- 票券方案表 car_card
-- 停用之票券方案不可再被購買；已售出票券不因方案停用而失效
CREATE TABLE `car_card` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主鍵ID',
  `cardName` varchar(100) NOT NULL COMMENT '票券名稱',
  `salePrice` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '售價',
  `usageInstruction` text COMMENT '使用說明',
  `validityType` tinyint(1) NOT NULL DEFAULT '1' COMMENT '有效期限類型：1=指定日期 2=指定天數',
  `validityEndDate` date DEFAULT NULL COMMENT '指定日期時的有效截止日（validityType=1時使用）',
  `validityDays` int(11) DEFAULT NULL COMMENT '指定天數（購買後N天內有效，validityType=2時使用）',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '啟用狀態：1=啟用 0=停用（停用後不可再被購買）',
  `createBy` varchar(64) DEFAULT '' COMMENT '建立者',
  `createTime` datetime DEFAULT NULL COMMENT '建立時間',
  `updateBy` varchar(64) DEFAULT '' COMMENT '更新者',
  `updateTime` datetime DEFAULT NULL COMMENT '更新時間',
  `remark` varchar(500) DEFAULT NULL COMMENT '備註',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='票券方案表';
