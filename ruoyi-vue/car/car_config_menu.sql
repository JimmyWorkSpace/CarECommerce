-- 網站配置維護菜單 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('網站配置維護', '3', '1', 'config', 'car/config/index', 1, 0, 'C', '0', '0', 'car:config:list', 'setting', 'admin', sysdate(), '', null, '網站配置維護菜單');

-- 按鈕父菜單ID
SELECT @parentId := LAST_INSERT_ID();

-- 按鈕 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('網站配置查詢', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'car:config:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('網站配置修改', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'car:config:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('網站配置導出', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'car:config:export',       '#', 'admin', sysdate(), '', null, '');
