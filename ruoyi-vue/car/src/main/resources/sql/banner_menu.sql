-- 轮播图维护菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('轮播图维护', '3', '1', 'banner', 'car/banner/index', 1, 0, 'C', '0', '0', 'car:banner:list', 'picture', 'admin', sysdate(), '', null, '轮播图维护菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('轮播图查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'car:banner:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('轮播图新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'car:banner:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('轮播图修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'car:banner:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('轮播图删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'car:banner:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('轮播图导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'car:banner:export',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('轮播图上传', @parentId, '6',  '#', '', 1, 0, 'F', '0', '0', 'car:banner:upload',       '#', 'admin', sysdate(), '', null, ''); 