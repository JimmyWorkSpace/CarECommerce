-- 關於 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('關於', '2000', '2', 'about', 'car/about/index', 1, 0, 'C', '0', '0', 'car:about:list', 'documentation', 'admin', sysdate(), '', null, '關於菜單');

-- 按鈕父菜單ID
SELECT @parentId := LAST_INSERT_ID();

-- 按鈕 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('關於查詢', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'car:about:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('關於修改', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'car:about:edit',         '#', 'admin', sysdate(), '', null, '');
