-- 页脚链接配置数据插入脚本
-- 将 footer.ftl 中的链接配置到 car_config 表中

-- 車主專區链接配置
INSERT INTO car_config (code, name, value, showOrder, delFlag, createTime, config_group_code) VALUES
('footer_owner_verification', '車輛驗證認證', '#', 1, 0, NOW(), 'footer_owner'),
('footer_owner_terms', '車主使用條款與規範', '#', 2, 0, NOW(), 'footer_owner'),
('footer_owner_contract', '車勢買賣車公版合約', '#', 3, 0, NOW(), 'footer_owner'),
('footer_owner_privacy', '隱私權政策', '#', 4, 0, NOW(), 'footer_owner');

-- 車勢週邊服務链接配置
INSERT INTO car_config (code, name, value, showOrder, delFlag, createTime, config_group_code) VALUES
('footer_service_warranty', '汽車保固', '#', 1, 0, NOW(), 'footer_service'),
('footer_service_insurance', '汽車保險', '#', 2, 0, NOW(), 'footer_service'),
('footer_service_loan', '汽車貸款', '#', 3, 0, NOW(), 'footer_service'),
('footer_service_supplies', '汽車用品', '#', 4, 0, NOW(), 'footer_service'),
('footer_service_coating', '汽車美容鍍膜', '#', 5, 0, NOW(), 'footer_service'),
('footer_service_maintenance', '汽車保養維修', '#', 6, 0, NOW(), 'footer_service');

-- 車廠店家專區链接配置
INSERT INTO car_config (code, name, value, showOrder, delFlag, createTime, config_group_code) VALUES
('footer_dealer_join', '優質車廠加盟', '#', 1, 0, NOW(), 'footer_dealer'),
('footer_dealer_partner', '周邊企業加入', '#', 2, 0, NOW(), 'footer_dealer'),
('footer_dealer_contract', '車勢買賣車公版合約', '#', 3, 0, NOW(), 'footer_dealer');

-- 車勢資訊链接配置
INSERT INTO car_config (code, name, value, showOrder, delFlag, createTime, config_group_code) VALUES
('footer_info_facebook', 'Facebook', '#', 1, 0, NOW(), 'footer_info'),
('footer_info_youtube', 'YouTube', '#', 2, 0, NOW(), 'footer_info'),
('footer_info_about', '關於我們', '#', 3, 0, NOW(), 'footer_info');

-- 聯繫車勢链接配置
INSERT INTO car_config (code, name, value, showOrder, delFlag, createTime, config_group_code) VALUES
('footer_contact_line_public', '民眾專用LINE', '#', 1, 0, NOW(), 'footer_contact'),
('footer_contact_line_dealer', '車行專用LINE', '#', 2, 0, NOW(), 'footer_contact');

