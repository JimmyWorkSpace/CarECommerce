-- 推荐管理菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('推荐管理', '2000', '4', 'recommand', 'car/recommand/index', 1, 0, 'C', '0', '0', 'car:recommand:list', 'star', 'admin', sysdate(), '', null, '推荐管理菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('推荐管理查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'car:recommand:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('推荐管理新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'car:recommand:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('推荐管理修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'car:recommand:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('推荐管理删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'car:recommand:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('推荐管理导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'car:recommand:export',       '#', 'admin', sysdate(), '', null, '');

-- 精选好车菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('精选好车', '2000', '5', 'salesRecommend', 'car/salesRecommend/index', 1, 0, 'C', '0', '0', 'car:salesRecommend:list', 'car', 'admin', sysdate(), '', null, '精选好车菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('精选好车查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'car:salesRecommend:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('精选好车修改', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'car:salesRecommend:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('精选好车导出', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'car:salesRecommend:export',       '#', 'admin', sysdate(), '', null, '');

-- 精选卖家菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('精选卖家', '2000', '6', 'dealerRecommend', 'car/dealerRecommend/index', 1, 0, 'C', '0', '0', 'car:dealerRecommend:list', 'peoples', 'admin', sysdate(), '', null, '精选卖家菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('精选卖家查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'car:dealerRecommend:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('精选卖家修改', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'car:dealerRecommend:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('精选卖家导出', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'car:dealerRecommend:export',       '#', 'admin', sysdate(), '', null, '');
