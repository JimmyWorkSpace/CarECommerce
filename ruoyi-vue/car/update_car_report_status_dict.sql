-- 更新車輛檢舉處理狀態字典
-- 將 'rejected' 改為 'cancelled'
-- 兼容 MariaDB 5.5

-- 更新字典數據
UPDATE sys_dict_data 
SET dict_label = '已取消', dict_value = 'cancelled', remark = '已取消'
WHERE dict_type = 'car_report_status' AND dict_value = 'rejected';

-- 如果沒有找到 'rejected' 記錄，則插入新的 'cancelled' 記錄
INSERT INTO sys_dict_data (dict_code, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
SELECT 
    (SELECT MAX(dict_code) + 1 FROM sys_dict_data WHERE dict_type = 'car_report_status'),
    4,
    '已取消',
    'cancelled',
    'car_report_status',
    '',
    'danger',
    'N',
    '0',
    'admin',
    NOW(),
    '',
    NULL,
    '已取消'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict_data 
    WHERE dict_type = 'car_report_status' AND dict_value = 'cancelled'
);
