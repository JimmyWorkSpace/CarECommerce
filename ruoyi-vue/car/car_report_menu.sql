-- 車輛檢舉管理菜单 SQL
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('車輛檢舉管理', 2000, 1, 'report', 'car/report/index', 1, 0, 'C', '0', '0', 'car:report:list', 'message', 'admin', sysdate(), '', null, '車輛檢舉管理菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('車輛檢舉查询', @parentId, 1, '', '', 1, 0, 'F', '0', '0', 'car:report:query', '#', 'admin', sysdate(), '', null, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('車輛檢舉修改', @parentId, 2, '', '', 1, 0, 'F', '0', '0', 'car:report:edit', '#', 'admin', sysdate(), '', null, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('車輛檢舉删除', @parentId, 3, '', '', 1, 0, 'F', '0', '0', 'car:report:remove', '#', 'admin', sysdate(), '', null, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('車輛檢舉导出', @parentId, 4, '', '', 1, 0, 'F', '0', '0', 'car:report:export', '#', 'admin', sysdate(), '', null, '');
