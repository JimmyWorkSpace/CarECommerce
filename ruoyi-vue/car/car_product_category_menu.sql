-- 商品目錄分類維護菜單 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('商品目錄分類', '2000', '10', 'productCategory', 'car/productCategory/index', 1, 0, 'C', '0', '0', 'car:productCategory:list', 'tree', 'admin', sysdate(), '', null, '商品目錄分類維護菜單');

-- 按鈕父菜單ID
SELECT @parentId := LAST_INSERT_ID();

-- 按鈕 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('商品目錄分類查詢', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'car:productCategory:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('商品目錄分類新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'car:productCategory:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('商品目錄分類修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'car:productCategory:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('商品目錄分類刪除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'car:productCategory:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('商品目錄分類導出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'car:productCategory:export',       '#', 'admin', sysdate(), '', null, '');

