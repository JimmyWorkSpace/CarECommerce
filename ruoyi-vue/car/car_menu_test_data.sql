-- 菜單維護測試數據
INSERT INTO `car_menu` (`title`, `showOrder`, `delFlag`, `createTime`, `isShow`, `canDel`, `linkUrl`) VALUES
('首頁', 1, 0, NOW(), 1, 0, '/'),
('車輛展示', 2, 0, NOW(), 1, 1, '/car/list'),
('車輛詳情', 3, 0, NOW(), 1, 1, '/car/detail'),
('預約試駕', 4, 0, NOW(), 1, 1, '/appointment'),
('關於我們', 5, 0, NOW(), 1, 0, '/about'),
('聯繫我們', 6, 0, NOW(), 1, 0, '/contact');
