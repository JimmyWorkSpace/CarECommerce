-- 通用文件上传权限 SQL
-- 注意：此权限可以添加到任何父菜单下，或者作为独立权限使用
-- 如果需要添加到特定菜单下，请修改 parent_id 的值

-- 方式1：作为独立权限（推荐）
-- 如果已经有"文件管理"或"系统工具"等父菜单，可以使用该菜单的ID作为parent_id
-- 这里假设添加到ID为3的菜单下（通常是"系统管理"或类似的菜单）
-- 请根据实际情况修改 parent_id

-- 通用文件上传权限按钮
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('文件上传', '3', '99',  '#', '', 1, 0, 'F', '0', '0', 'car:upload:upload', 'upload', 'admin', sysdate(), '', null, '通用文件上传权限');

-- 方式2：如果需要创建独立的文件上传菜单（可选）
-- 取消下面的注释并执行，可以创建一个独立的文件上传菜单

/*
-- 文件上传菜单
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('文件上传', '3', '99', 'fileUpload', 'car/fileUpload/index', 1, 0, 'C', '0', '0', 'car:upload:list', 'upload', 'admin', sysdate(), '', null, '文件上传菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('文件上传', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'car:upload:upload',       '#', 'admin', sysdate(), '', null, '');
*/

