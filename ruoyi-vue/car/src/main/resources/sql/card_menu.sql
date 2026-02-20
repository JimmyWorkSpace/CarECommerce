-- 票券方案維護選單 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('票券方案維護', '1067', '12', 'card', 'car/card/index', 1, 0, 'C', '0', '0', 'car:card:list', 'money', 'admin', sysdate(), '', null, '票券方案維護選單');

SELECT @parentId := LAST_INSERT_ID();

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('票券方案查詢', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'car:card:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('票券方案新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'car:card:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('票券方案修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'car:card:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('票券方案刪除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'car:card:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('票券方案導出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'car:card:export',       '#', 'admin', sysdate(), '', null, '');
