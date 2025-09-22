-- 菜單維護 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('菜單維護', '2000', '1', 'menu', 'car/menu/index', 1, 0, 'C', '0', '0', 'car:menu:list', 'list', 'admin', sysdate(), '', null, '菜單維護菜單');

-- 按鈕父菜單ID
SELECT @parentId := LAST_INSERT_ID();

-- 按鈕 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('菜單維護查詢', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'car:menu:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('菜單維護新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'car:menu:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('菜單維護修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'car:menu:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('菜單維護刪除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'car:menu:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('菜單維護導出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'car:menu:export',       '#', 'admin', sysdate(), '', null, '');
