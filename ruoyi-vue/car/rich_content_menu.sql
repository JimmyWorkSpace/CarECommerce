-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('富文本内容', '2000', '1', 'richContent', 'car/richContent/index', 1, 0, 'C', '0', '0', 'car:richContent:list', 'documentation', 'admin', sysdate(), '', null, '富文本内容菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('富文本内容查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'car:richContent:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('富文本内容新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'car:richContent:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('富文本内容修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'car:richContent:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('富文本内容删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'car:richContent:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('富文本内容导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'car:richContent:export',       '#', 'admin', sysdate(), '', null, '');
