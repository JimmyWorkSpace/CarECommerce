-- 测试车辆筛选选项的SQL查询
-- 这些查询用于验证新的筛选条件功能

-- 查询变速系统选项
SELECT DISTINCT c.transmission 
FROM cars c
INNER JOIN car_sales cs ON c.id = cs.car_id
WHERE cs.is_visible = 1 
AND c.transmission IS NOT NULL 
AND c.transmission != ''
ORDER BY c.transmission ASC;

-- 查询驱动方式选项
SELECT DISTINCT c.drivetrain 
FROM cars c
INNER JOIN car_sales cs ON c.id = cs.car_id
WHERE cs.is_visible = 1 
AND c.drivetrain IS NOT NULL 
AND c.drivetrain != ''
ORDER BY c.drivetrain ASC;

-- 查询燃料系统选项
SELECT DISTINCT c.fuel_system 
FROM cars c
INNER JOIN car_sales cs ON c.id = cs.car_id
WHERE cs.is_visible = 1 
AND c.fuel_system IS NOT NULL 
AND c.fuel_system != ''
ORDER BY c.fuel_system ASC;
