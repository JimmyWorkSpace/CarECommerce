-- 導航菜單數據
INSERT INTO `car_menu` (`title`, `showOrder`, `delFlag`, `createTime`, `isShow`, `canDel`, `linkUrl`) VALUES
('首頁', 1, 0, NOW(), 1, 0, '/'),
('買車', 2, 0, NOW(), 1, 1, '/buy-cars'),
('商城', 3, 0, NOW(), 1, 1, '/mall'),
('頻道', 4, 0, NOW(), 1, 1, '/channel'),
('關於', 5, 0, NOW(), 1, 0, '/about');
