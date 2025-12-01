-- 商品維護菜單 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('商品維護', '2000', '11', 'product', 'car/product/index', 1, 0, 'C', '0', '0', 'car:product:list', 'shopping', 'admin', sysdate(), '', null, '商品維護菜單');

-- 按鈕父菜單ID
SELECT @parentId := LAST_INSERT_ID();

-- 按鈕 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('商品查詢', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'car:product:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('商品新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'car:product:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('商品修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'car:product:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('商品刪除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'car:product:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('商品導出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'car:product:export',       '#', 'admin', sysdate(), '', null, '');

