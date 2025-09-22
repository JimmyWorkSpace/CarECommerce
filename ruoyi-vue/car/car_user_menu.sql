-- 用户管理菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('用户管理', '2000', '3', 'user', 'car/user/index', 1, 0, 'C', '0', '0', 'car:user:list', 'user', 'admin', sysdate(), '', null, '用户管理菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('用户管理查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'car:user:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('用户管理新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'car:user:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('用户管理修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'car:user:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('用户管理删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'car:user:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('用户管理导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'car:user:export',       '#', 'admin', sysdate(), '', null, '');
