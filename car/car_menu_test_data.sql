-- 插入测试菜单数据
INSERT INTO car_menu (title, showOrder, delFlag, createTime, isShow, canDel, linkUrl) VALUES
('首頁', 1, 0, NOW(), 1, 0, '/'),
('買車', 2, 0, NOW(), 1, 0, '/buy-cars'),
('商城', 3, 0, NOW(), 1, 0, '/mall'),
('頻道', 4, 0, NOW(), 1, 0, '/channel'),
('關於', 5, 0, NOW(), 1, 0, '/about');

-- 查询菜单数据验证
SELECT * FROM car_menu WHERE delFlag = 0 AND isShow = 1 ORDER BY showOrder;
