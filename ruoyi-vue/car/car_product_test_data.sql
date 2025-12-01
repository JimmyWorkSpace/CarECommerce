-- 商品目錄分類測試數據
-- 先插入一些汽車用品分類
INSERT INTO car_product_category (categoryName, parentId, showOrder, delFlag) VALUES
('汽車用品', NULL, 1, 0),
('汽車保養', 1, 1, 0),
('汽車清潔', 1, 2, 0),
('汽車裝飾', 1, 3, 0),
('汽車電子', 1, 4, 0),
('機油', 2, 1, 0),
('機油濾清器', 2, 2, 0),
('空氣濾清器', 2, 3, 0),
('空調濾清器', 2, 4, 0),
('洗車用品', 3, 1, 0),
('打蠟用品', 3, 2, 0),
('內飾清潔', 3, 3, 0),
('座墊', 4, 1, 0),
('腳墊', 4, 2, 0),
('方向盤套', 4, 3, 0),
('行車記錄儀', 5, 1, 0),
('導航儀', 5, 2, 0),
('倒車影像', 5, 3, 0);

-- 商品測試數據
-- 插入商品數據（使用子查詢獲取分類ID）
INSERT INTO car_product (productTitle, productDespShort, productDesp, productTags, supplyPrice, salePrice, amount, categoryId, onSale, delFlag) VALUES
('全合成機油 5W-30 4L', '高性能全合成機油，適用於各種車型', '<p>採用先進的全合成技術，提供卓越的發動機保護性能。適用於汽油和柴油發動機，有效降低磨損，延長發動機壽命。</p><p>規格：5W-30</p><p>容量：4升</p>', '機油,全合成,5W-30', 280.00, 350.00, 50, (SELECT id FROM car_product_category WHERE categoryName = '機油' LIMIT 1), 1, 0),
('機油濾清器 通用型', '高品質機油濾清器，有效過濾雜質', '<p>採用優質濾紙，過濾精度高，能有效過濾機油中的雜質和顆粒，保護發動機。</p><p>適用於大部分車型</p>', '機油濾清器,濾芯', 25.00, 35.00, 100, (SELECT id FROM car_product_category WHERE categoryName = '機油濾清器' LIMIT 1), 1, 0),
('空氣濾清器 活性炭', '高效過濾空氣中的灰塵和異味', '<p>採用活性炭技術，不僅能過濾空氣中的灰塵和顆粒，還能有效去除異味，提供清新的車內空氣。</p>', '空氣濾清器,活性炭', 45.00, 65.00, 80, (SELECT id FROM car_product_category WHERE categoryName = '空氣濾清器' LIMIT 1), 1, 0),
('洗車水蠟 2L裝', '一體式洗車水蠟，洗車打蠟一次完成', '<p>濃縮配方，泡沫豐富，去污力強。含有蠟質成分，洗車的同時完成打蠟，讓車身更加光亮。</p><p>容量：2升</p>', '洗車,水蠟,清潔', 35.00, 50.00, 60, (SELECT id FROM car_product_category WHERE categoryName = '洗車用品' LIMIT 1), 1, 0),
('固體車蠟 高光澤', '專業級固體車蠟，持久保護車漆', '<p>採用巴西棕櫚蠟配方，光澤度高，持久性好。能有效保護車漆，防止紫外線和酸雨侵蝕。</p><p>淨重：500g</p>', '車蠟,打蠟,保護', 85.00, 120.00, 40, (SELECT id FROM car_product_category WHERE categoryName = '打蠟用品' LIMIT 1), 1, 0),
('真皮座墊 四季通用', '高品質真皮座墊，舒適透氣', '<p>採用優質真皮製作，質感柔軟，透氣性好。四季通用，冬暖夏涼，提升駕駛舒適度。</p><p>適用於大部分車型</p>', '座墊,真皮,舒適', 280.00, 380.00, 30, (SELECT id FROM car_product_category WHERE categoryName = '座墊' LIMIT 1), 1, 0),
('全包圍腳墊 3D立體', '專車專用全包圍腳墊，完美貼合', '<p>3D立體設計，完美貼合車型。採用環保材料，無異味，易清洗。有效保護原車地毯。</p><p>專車專用，需選擇車型</p>', '腳墊,全包圍,3D', 150.00, 220.00, 25, (SELECT id FROM car_product_category WHERE categoryName = '腳墊' LIMIT 1), 1, 0),
('行車記錄儀 4K高清', '4K超高清行車記錄儀，夜視功能強大', '<p>4K超高清錄製，畫面清晰細膩。配備星光夜視功能，夜間錄製效果出色。支持循環錄製，自動覆蓋舊文件。</p><p>屏幕尺寸：3英寸</p><p>存儲：支持最大128GB</p>', '行車記錄儀,4K,夜視', 450.00, 680.00, 20, (SELECT id FROM car_product_category WHERE categoryName = '行車記錄儀' LIMIT 1), 1, 0);

-- 插入商品圖片數據（圖片路徑為示例，實際需要上傳圖片後使用真實路徑）
INSERT INTO car_product_image (productId, imageUrl, showOrder, delFlag) VALUES
((SELECT id FROM car_product WHERE productTitle = '全合成機油 5W-30 4L' LIMIT 1), '/img/car_product/oil_1.jpg', 1, 0),
((SELECT id FROM car_product WHERE productTitle = '全合成機油 5W-30 4L' LIMIT 1), '/img/car_product/oil_2.jpg', 2, 0),
((SELECT id FROM car_product WHERE productTitle = '機油濾清器 通用型' LIMIT 1), '/img/car_product/oil_filter_1.jpg', 1, 0),
((SELECT id FROM car_product WHERE productTitle = '空氣濾清器 活性炭' LIMIT 1), '/img/car_product/air_filter_1.jpg', 1, 0),
((SELECT id FROM car_product WHERE productTitle = '空氣濾清器 活性炭' LIMIT 1), '/img/car_product/air_filter_2.jpg', 2, 0),
((SELECT id FROM car_product WHERE productTitle = '洗車水蠟 2L裝' LIMIT 1), '/img/car_product/wash_1.jpg', 1, 0),
((SELECT id FROM car_product WHERE productTitle = '固體車蠟 高光澤' LIMIT 1), '/img/car_product/wax_1.jpg', 1, 0),
((SELECT id FROM car_product WHERE productTitle = '固體車蠟 高光澤' LIMIT 1), '/img/car_product/wax_2.jpg', 2, 0),
((SELECT id FROM car_product WHERE productTitle = '真皮座墊 四季通用' LIMIT 1), '/img/car_product/seat_1.jpg', 1, 0),
((SELECT id FROM car_product WHERE productTitle = '真皮座墊 四季通用' LIMIT 1), '/img/car_product/seat_2.jpg', 2, 0),
((SELECT id FROM car_product WHERE productTitle = '全包圍腳墊 3D立體' LIMIT 1), '/img/car_product/floor_1.jpg', 1, 0),
((SELECT id FROM car_product WHERE productTitle = '全包圍腳墊 3D立體' LIMIT 1), '/img/car_product/floor_2.jpg', 2, 0),
((SELECT id FROM car_product WHERE productTitle = '行車記錄儀 4K高清' LIMIT 1), '/img/car_product/recorder_1.jpg', 1, 0),
((SELECT id FROM car_product WHERE productTitle = '行車記錄儀 4K高清' LIMIT 1), '/img/car_product/recorder_2.jpg', 2, 0),
((SELECT id FROM car_product WHERE productTitle = '行車記錄儀 4K高清' LIMIT 1), '/img/car_product/recorder_3.jpg', 3, 0);

-- 插入商品屬性數據
INSERT INTO car_product_attr (productId, attrName, attrValue, showOrder, delFlag) VALUES
-- 機油屬性
((SELECT id FROM car_product WHERE productTitle = '全合成機油 5W-30 4L' LIMIT 1), '規格', '5W-30', 1, 0),
((SELECT id FROM car_product WHERE productTitle = '全合成機油 5W-30 4L' LIMIT 1), '容量', '4升', 2, 0),
((SELECT id FROM car_product WHERE productTitle = '全合成機油 5W-30 4L' LIMIT 1), '類型', '全合成', 3, 0),
((SELECT id FROM car_product WHERE productTitle = '全合成機油 5W-30 4L' LIMIT 1), '適用發動機', '汽油/柴油', 4, 0),
((SELECT id FROM car_product WHERE productTitle = '全合成機油 5W-30 4L' LIMIT 1), '認證標準', 'API SN/CF', 5, 0),
-- 機油濾清器屬性
((SELECT id FROM car_product WHERE productTitle = '機油濾清器 通用型' LIMIT 1), '適用車型', '通用型', 1, 0),
((SELECT id FROM car_product WHERE productTitle = '機油濾清器 通用型' LIMIT 1), '過濾精度', '10微米', 2, 0),
((SELECT id FROM car_product WHERE productTitle = '機油濾清器 通用型' LIMIT 1), '材質', '優質濾紙', 3, 0),
-- 空氣濾清器屬性
((SELECT id FROM car_product WHERE productTitle = '空氣濾清器 活性炭' LIMIT 1), '類型', '活性炭', 1, 0),
((SELECT id FROM car_product WHERE productTitle = '空氣濾清器 活性炭' LIMIT 1), '過濾效率', '99.9%', 2, 0),
((SELECT id FROM car_product WHERE productTitle = '空氣濾清器 活性炭' LIMIT 1), '適用車型', '大部分車型', 3, 0),
-- 洗車水蠟屬性
((SELECT id FROM car_product WHERE productTitle = '洗車水蠟 2L裝' LIMIT 1), '容量', '2升', 1, 0),
((SELECT id FROM car_product WHERE productTitle = '洗車水蠟 2L裝' LIMIT 1), '類型', '濃縮型', 2, 0),
((SELECT id FROM car_product WHERE productTitle = '洗車水蠟 2L裝' LIMIT 1), '功能', '洗車+打蠟', 3, 0),
-- 固體車蠟屬性
((SELECT id FROM car_product WHERE productTitle = '固體車蠟 高光澤' LIMIT 1), '淨重', '500g', 1, 0),
((SELECT id FROM car_product WHERE productTitle = '固體車蠟 高光澤' LIMIT 1), '類型', '固體蠟', 2, 0),
((SELECT id FROM car_product WHERE productTitle = '固體車蠟 高光澤' LIMIT 1), '主要成分', '巴西棕櫚蠟', 3, 0),
((SELECT id FROM car_product WHERE productTitle = '固體車蠟 高光澤' LIMIT 1), '光澤度', '高光澤', 4, 0),
-- 真皮座墊屬性
((SELECT id FROM car_product WHERE productTitle = '真皮座墊 四季通用' LIMIT 1), '材質', '真皮', 1, 0),
((SELECT id FROM car_product WHERE productTitle = '真皮座墊 四季通用' LIMIT 1), '適用季節', '四季通用', 2, 0),
((SELECT id FROM car_product WHERE productTitle = '真皮座墊 四季通用' LIMIT 1), '適用車型', '大部分車型', 3, 0),
((SELECT id FROM car_product WHERE productTitle = '真皮座墊 四季通用' LIMIT 1), '顏色', '黑色/棕色', 4, 0),
-- 全包圍腳墊屬性
((SELECT id FROM car_product WHERE productTitle = '全包圍腳墊 3D立體' LIMIT 1), '類型', '全包圍', 1, 0),
((SELECT id FROM car_product WHERE productTitle = '全包圍腳墊 3D立體' LIMIT 1), '設計', '3D立體', 2, 0),
((SELECT id FROM car_product WHERE productTitle = '全包圍腳墊 3D立體' LIMIT 1), '材質', '環保TPE', 3, 0),
((SELECT id FROM car_product WHERE productTitle = '全包圍腳墊 3D立體' LIMIT 1), '適用方式', '專車專用', 4, 0),
-- 行車記錄儀屬性
((SELECT id FROM car_product WHERE productTitle = '行車記錄儀 4K高清' LIMIT 1), '分辨率', '4K超高清', 1, 0),
((SELECT id FROM car_product WHERE productTitle = '行車記錄儀 4K高清' LIMIT 1), '屏幕尺寸', '3英寸', 2, 0),
((SELECT id FROM car_product WHERE productTitle = '行車記錄儀 4K高清' LIMIT 1), '夜視功能', '星光夜視', 3, 0),
((SELECT id FROM car_product WHERE productTitle = '行車記錄儀 4K高清' LIMIT 1), '存儲支持', '最大128GB', 4, 0),
((SELECT id FROM car_product WHERE productTitle = '行車記錄儀 4K高清' LIMIT 1), '供電方式', '點煙器/USB', 5, 0);

