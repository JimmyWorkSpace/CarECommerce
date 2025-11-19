-- 创建 sys_yes_no_num 字典类型和字典数据
-- 用于轮播图等模块的"是否跳转"、"是否删除"等字段
-- 值为数字 0 和 1

-- 1. 创建字典类型
INSERT INTO sys_dict_type (dict_id, dict_name, dict_type, status, create_by, create_time, remark) 
VALUES (106, '系统是否(数字)', 'sys_yes_no_num', '0', 'admin', NOW(), '系统是否列表，值为数字0和1')
ON DUPLICATE KEY UPDATE 
  dict_name = '系统是否(数字)',
  status = '0',
  remark = '系统是否列表，值为数字0和1';

-- 2. 创建字典数据
-- 删除已存在的数据（如果存在）
DELETE FROM sys_dict_data WHERE dict_type = 'sys_yes_no_num';

-- 插入新的字典数据
INSERT INTO sys_dict_data (dict_code, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) 
VALUES 
(1025, 1, '是', '1', 'sys_yes_no_num', '', 'primary', 'N', '0', 'admin', NOW(), '是'),
(1026, 2, '否', '0', 'sys_yes_no_num', '', 'info', 'Y', '0', 'admin', NOW(), '否');

