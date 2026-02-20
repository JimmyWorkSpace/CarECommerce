-- 券碼核銷選單 SQL（掛在與票券方案同一父級，parent_id 需依實際「票券方案維護」的父節點調整）
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('券碼核銷', '1067', '13', 'cardDetail', 'car/cardDetail/index', 1, 0, 'C', '0', '0', 'car:cardDetail:list', 'validCode', 'admin', sysdate(), '', null, '券碼列表與核銷');

SELECT @parentId := LAST_INSERT_ID();

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('券碼查詢', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'car:cardDetail:list',   '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('券碼核銷', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'car:cardDetail:redeem', '#', 'admin', sysdate(), '', null, '');
