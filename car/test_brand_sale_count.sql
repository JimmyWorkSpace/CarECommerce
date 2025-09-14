-- 测试品牌在售数量统计的SQL查询
-- 这个查询用于验证品牌筛选项功能

SELECT 
    cb.brand, 
    cb.id, 
    COUNT(*) as saleCount 
FROM car_brand cb
LEFT JOIN cars c ON cb.id = c.id_brand
LEFT JOIN car_sales cs ON c.id = cs.car_id
WHERE cs.is_visible = 1
GROUP BY cb.brand, cb.id
ORDER BY saleCount DESC;

-- 如果需要查看所有品牌（包括没有在售车辆的），可以使用以下查询：
-- SELECT 
--     cb.brand, 
--     cb.id, 
--     COUNT(cs.id) as saleCount 
-- FROM car_brand cb
-- LEFT JOIN cars c ON cb.id = c.id_brand
-- LEFT JOIN car_sales cs ON c.id = cs.car_id AND cs.is_visible = 1
-- GROUP BY cb.brand, cb.id
-- ORDER BY saleCount DESC;
