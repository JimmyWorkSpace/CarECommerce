-- 創建車輛檢舉表
CREATE TABLE IF NOT EXISTS `car_reports` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主鍵ID',
  `sale_id` bigint(20) NOT NULL COMMENT '被檢舉的車輛銷售ID',
  `reporter_id` bigint(20) NOT NULL COMMENT '檢舉人ID',
  `reason` varchar(50) NOT NULL COMMENT '檢舉原因：price_mismatch(價格與現場不符), false_info(資料虛假), fraud_suspicion(詐騙嫌疑), other(其它)',
  `description` text NOT NULL COMMENT '詳細說明',
  `anonymous` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否匿名檢舉：0-否，1-是',
  `status` varchar(20) NOT NULL DEFAULT 'submitted' COMMENT '處理狀態：submitted(已提交), processing(處理中), processed(已處理), rejected(已駁回)',
  `process_note` text COMMENT '處理備註',
  `processor_id` bigint(20) COMMENT '處理人ID',
  `processed_at` datetime COMMENT '處理時間',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新時間',
  PRIMARY KEY (`id`),
  KEY `idx_sale_id` (`sale_id`),
  KEY `idx_reporter_id` (`reporter_id`),
  KEY `idx_status` (`status`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='車輛檢舉表';

-- 插入測試數據
INSERT INTO `car_reports` (`sale_id`, `reporter_id`, `reason`, `description`, `anonymous`, `status`, `created_at`) VALUES
(1, 1, 'price_mismatch', '現場看車時發現價格與網站標示不符，實際價格比網站高5萬元', 0, 'submitted', NOW()),
(2, 2, 'false_info', '車輛里程數被調表，實際里程比標示多10萬公里', 1, 'processing', NOW()),
(3, 1, 'fraud_suspicion', '賣家要求先付款再看車，疑似詐騙行為', 0, 'processed', NOW());
