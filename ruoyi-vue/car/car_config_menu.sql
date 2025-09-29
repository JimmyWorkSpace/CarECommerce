-- 网站配置维护菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('网站配置维护', '3', '1', 'config', 'car/config/index', 1, 0, 'C', '0', '0', 'car:config:list', 'setting', 'admin', sysdate(), '', null, '网站配置维护菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('网站配置查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'car:config:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('网站配置修改', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'car:config:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('网站配置导出', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'car:config:export',       '#', 'admin', sysdate(), '', null, '');
