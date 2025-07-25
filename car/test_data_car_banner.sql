-- CarBanner测试数据
-- 表名：car_banner

INSERT INTO `car_banner` (`imageUrl`, `linkUrl`, `isLink`, `showOrder`, `delFlag`, `createTime`) VALUES
-- Banner 1: 豪华轿车banner，跳转到车辆详情页
('https://images.unsplash.com/photo-1552519507-da3b142c6e3d?w=1200&h=400&fit=crop', '/car/detail/123', 1, 1, 0, NOW()),

-- Banner 2: 跑车banner，跳转到跑车页面
('https://images.unsplash.com/photo-1503376780353-7e6692767b70?w=1200&h=400&fit=crop', '/cars/sports', 1, 2, 0, NOW()),

-- Banner 3: SUV车型banner，不跳转
('https://images.unsplash.com/photo-1549924231-f129b911e442?w=1200&h=400&fit=crop', NULL, 0, 3, 0, NOW()),

-- Banner 4: 新能源车banner，跳转到新能源车页面
('https://images.unsplash.com/photo-1593941707882-a5bacd42a048?w=1200&h=400&fit=crop', '/cars/electric', 1, 4, 0, NOW()),

-- Banner 5: 二手车banner，跳转到二手车页面
('https://images.unsplash.com/photo-1492144534655-ae79c964c9d7?w=1200&h=400&fit=crop', '/cars/used', 1, 5, 0, NOW()),

-- Banner 6: 已删除的banner（测试用）
('https://images.unsplash.com/photo-1563720223185-11003d516935?w=1200&h=400&fit=crop', '/deleted', 1, 6, 1, NOW()),

-- Banner 7: 商务车banner，跳转到商务车页面
('https://images.unsplash.com/photo-1606664515524-ed2f786a0bd6?w=1200&h=400&fit=crop', '/cars/business', 1, 7, 0, NOW()),

-- Banner 8: 经典老爷车banner，跳转到经典车页面
('https://images.unsplash.com/photo-1580273916550-e323be2ae537?w=1200&h=400&fit=crop', '/cars/classic', 1, 8, 0, NOW()),

-- Banner 9: 越野车banner，跳转到越野车页面
('https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=1200&h=400&fit=crop', '/cars/offroad', 1, 9, 0, NOW()),

-- Banner 10: 豪华品牌banner，跳转到豪华车页面
('https://images.unsplash.com/photo-1603584173870-7f23fdae1b7a?w=1200&h=400&fit=crop', '/cars/luxury', 1, 10, 0, NOW()); 