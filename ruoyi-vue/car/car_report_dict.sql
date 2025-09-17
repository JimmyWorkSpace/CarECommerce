-- 車輛檢舉原因字典
INSERT INTO sys_dict_type VALUES (100, '車輛檢舉原因', 'car_report_reason', '0', 'admin', sysdate(), '', null, '車輛檢舉原因列表');
INSERT INTO sys_dict_data VALUES (1000, 1, '價格與現場不符', 'price_mismatch', 'car_report_reason', '', 'warning', 'N', '0', 'admin', sysdate(), '', null, '價格與現場不符');
INSERT INTO sys_dict_data VALUES (1001, 2, '資料虛假', 'false_info', 'car_report_reason', '', 'danger', 'N', '0', 'admin', sysdate(), '', null, '資料虛假');
INSERT INTO sys_dict_data VALUES (1002, 3, '詐騙嫌疑', 'fraud_suspicion', 'car_report_reason', '', 'danger', 'N', '0', 'admin', sysdate(), '', null, '詐騙嫌疑');
INSERT INTO sys_dict_data VALUES (1003, 4, '其它', 'other', 'car_report_reason', '', 'info', 'N', '0', 'admin', sysdate(), '', null, '其它');

-- 車輛檢舉處理狀態字典
INSERT INTO sys_dict_type VALUES (101, '車輛檢舉處理狀態', 'car_report_status', '0', 'admin', sysdate(), '', null, '車輛檢舉處理狀態列表');
INSERT INTO sys_dict_data VALUES (1010, 1, '已提交', 'submitted', 'car_report_status', '', 'info', 'N', '0', 'admin', sysdate(), '', null, '已提交');
INSERT INTO sys_dict_data VALUES (1011, 2, '處理中', 'processing', 'car_report_status', '', 'warning', 'N', '0', 'admin', sysdate(), '', null, '處理中');
INSERT INTO sys_dict_data VALUES (1012, 3, '已處理', 'processed', 'car_report_status', '', 'success', 'N', '0', 'admin', sysdate(), '', null, '已處理');
INSERT INTO sys_dict_data VALUES (1013, 4, '已駁回', 'rejected', 'car_report_status', '', 'danger', 'N', '0', 'admin', sysdate(), '', null, '已駁回');
