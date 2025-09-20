-- 更新車輛檢舉處理狀態字典
-- 將 'rejected' 改為 'cancelled'
-- 兼容 MariaDB 5.5

-- 方法1：直接更新现有记录
UPDATE sys_dict_data 
SET dict_label = '已取消', dict_value = 'cancelled', remark = '已取消'
WHERE dict_type = 'car_report_status' AND dict_value = 'rejected';

-- 方法2：如果上面没有更新到记录，手动插入新记录
-- 请根据实际情况调整 dict_code 的值（确保不重复）
INSERT INTO sys_dict_data (dict_code, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES (1014, 4, '已取消', 'cancelled', 'car_report_status', '', 'danger', 'N', '0', 'admin', NOW(), '', NULL, '已取消');

-- 验证更新结果
SELECT * FROM sys_dict_data WHERE dict_type = 'car_report_status' ORDER BY dict_sort;
