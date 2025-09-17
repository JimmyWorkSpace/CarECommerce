-- 预约看车状态字典数据
INSERT INTO sys_dict_type (dict_name, dict_type, status, create_by, create_time, remark) 
VALUES ('预约看车状态', 'car_appointment_status', '0', 'admin', sysdate(), '预约看车状态列表');

-- 获取字典类型ID
SELECT @dictTypeId := dict_id FROM sys_dict_type WHERE dict_type = 'car_appointment_status';

-- 插入字典数据
INSERT INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) 
VALUES (1, '已预约', '1', 'car_appointment_status', '', 'primary', 'Y', '0', 'admin', sysdate(), '已预约状态');

INSERT INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) 
VALUES (2, '已看车', '2', 'car_appointment_status', '', 'success', 'N', '0', 'admin', sysdate(), '已看车状态');

INSERT INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) 
VALUES (3, '已取消', '3', 'car_appointment_status', '', 'danger', 'N', '0', 'admin', sysdate(), '已取消状态');
