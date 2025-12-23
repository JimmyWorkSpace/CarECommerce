-- 按照创建日期重新设置广告位的showOrder，每个间隔100
-- 如果createTime为NULL，则按id排序
-- 适用于MySQL和MariaDB所有版本

-- 方法1：使用临时表（推荐，最兼容）
CREATE TEMPORARY TABLE IF NOT EXISTS temp_advertisement_order AS
SELECT 
    id,
    (@row_number := @row_number + 100) AS new_show_order
FROM 
    car_advertisement,
    (SELECT @row_number := 0) AS r
WHERE 
    (delFlag = 0 OR delFlag IS NULL)
ORDER BY 
    COALESCE(createTime, '1970-01-01 00:00:00') ASC, 
    id ASC;

UPDATE car_advertisement a
INNER JOIN temp_advertisement_order b ON a.id = b.id
SET a.showOrder = b.new_show_order
WHERE (a.delFlag = 0 OR a.delFlag IS NULL);

DROP TEMPORARY TABLE IF EXISTS temp_advertisement_order;

-- 方法2：直接更新（如果方法1不行，可以尝试这个）
-- SET @row_number := 0;
-- 
-- UPDATE car_advertisement a
-- INNER JOIN (
--     SELECT 
--         id,
--         (@row_number := @row_number + 100) AS new_show_order
--     FROM 
--         car_advertisement,
--         (SELECT @row_number := 0) AS r
--     WHERE 
--         (delFlag = 0 OR delFlag IS NULL)
--     ORDER BY 
--         COALESCE(createTime, '1970-01-01 00:00:00') ASC, 
--         id ASC
-- ) b ON a.id = b.id
-- SET a.showOrder = b.new_show_order
-- WHERE (a.delFlag = 0 OR a.delFlag IS NULL);

