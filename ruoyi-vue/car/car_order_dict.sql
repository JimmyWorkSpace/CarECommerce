-- 订单状态字典数据
INSERT INTO sys_dict_type (dict_name, dict_type, status, create_by, create_time, remark) VALUES ('订单状态', 'order_status', '0', 'admin', sysdate(), '订单状态列表');
INSERT INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) VALUES (1, '未支付', '0', 'order_status', '', 'default', 'N', '0', 'admin', sysdate(), '未支付');
INSERT INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) VALUES (2, '支付中', '1', 'order_status', '', 'default', 'N', '0', 'admin', sysdate(), '支付中');
INSERT INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) VALUES (3, '已支付', '2', 'order_status', '', 'default', 'N', '0', 'admin', sysdate(), '已支付');
INSERT INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) VALUES (4, '已取消', '3', 'order_status', '', 'default', 'N', '0', 'admin', sysdate(), '已取消');
INSERT INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) VALUES (5, '支付失败', '4', 'order_status', '', 'default', 'N', '0', 'admin', sysdate(), '支付失败');
INSERT INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) VALUES (6, '已发货', '5', 'order_status', '', 'default', 'N', '0', 'admin', sysdate(), '已发货');
INSERT INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) VALUES (7, '已完成', '6', 'order_status', '', 'default', 'N', '0', 'admin', sysdate(), '已完成');
INSERT INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) VALUES (8, '退货中', '7', 'order_status', '', 'default', 'N', '0', 'admin', sysdate(), '退货中');
INSERT INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) VALUES (9, '已退货', '8', 'order_status', '', 'default', 'N', '0', 'admin', sysdate(), '已退货');

-- 订单类型字典数据
INSERT INTO sys_dict_type (dict_name, dict_type, status, create_by, create_time, remark) VALUES ('订单类型', 'order_type', '0', 'admin', sysdate(), '订单类型列表');
INSERT INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) VALUES (1, '宅配到府', '1', 'order_type', '', 'default', 'N', '0', 'admin', sysdate(), '宅配到府');
INSERT INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) VALUES (2, '超商取货', '2', 'order_type', '', 'default', 'N', '0', 'admin', sysdate(), '超商取货');
