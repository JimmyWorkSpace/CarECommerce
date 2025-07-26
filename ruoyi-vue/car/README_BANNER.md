# 轮播图维护功能

## 功能概述

轮播图维护功能是一个完整的轮播图管理系统，支持以下功能：

- 轮播图的增删改查
- 图片上传到FTP服务器
- 拖拽排序功能
- 图片缩略图预览
- 跳转链接管理
- 软删除标记

## 技术架构

### 后端组件

1. **实体类**: `CarBannerEntity`
   - 包含图片URL、跳转地址、排序、删除标记等字段

2. **Mapper层**: `CarBannerMapper`
   - 提供数据库操作接口
   - 支持按排序字段查询
   - 支持批量更新排序

3. **Service层**: `CarBannerService`
   - 业务逻辑处理
   - 事务管理
   - 默认值设置

4. **Controller层**: `CarBannerController`
   - RESTful API接口
   - 图片上传处理
   - 权限控制

5. **FTP服务**: `FtpService`
   - 图片上传到FTP服务器
   - 自动创建目录
   - UUID重命名文件

### 前端组件

1. **API层**: `banner.js`
   - 封装所有API调用
   - 统一的错误处理

2. **页面组件**: `index.vue`
   - 列表展示
   - 表单操作
   - 图片上传
   - 排序管理

## 数据库配置

### 表结构

```sql
CREATE TABLE `car_banner` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `imageUrl` varchar(500) DEFAULT NULL COMMENT '图片地址',
  `linkUrl` varchar(500) DEFAULT NULL COMMENT '跳转地址',
  `isLink` tinyint(1) DEFAULT '0' COMMENT '是否跳转',
  `showOrder` int(11) DEFAULT '0' COMMENT '排序',
  `delFlag` tinyint(1) DEFAULT '0' COMMENT '删除标记',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='轮播图表';
```

### 菜单配置

执行以下SQL创建菜单：

```sql
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
```

## 配置文件

### application.yml 配置

确保以下配置正确：

```yaml
carce:
  prefix: https://your-domain.com  # 图片访问前缀
  ftp:
    server: your-ftp-server
    port: 21
    user: your-username
    password: your-password
```

## 功能特性

### 1. 图片上传

- 支持直接输入图片URL
- 支持上传图片到FTP服务器
- 自动生成UUID文件名
- 图片存储在 `/img/car_sale/banner/` 目录
- 完整的图片URL格式：`${carce.prefix}/img/car_sale/banner/${uuid}.jpg`

### 2. 排序功能

- 支持数字输入排序
- 支持批量保存排序
- 排序变更高亮显示
- 实时排序更新

### 3. 图片预览

- 缩略图显示
- 点击放大预览
- 错误图片占位符

### 4. 权限控制

- 基于角色的权限控制
- 细粒度操作权限
- 菜单级权限管理

## 使用说明

### 1. 新增轮播图

1. 点击"新增"按钮
2. 填写图片地址或上传图片
3. 设置跳转地址（可选）
4. 选择是否启用跳转
5. 设置排序值
6. 点击"确定"保存

### 2. 修改轮播图

1. 点击"修改"按钮
2. 修改相关信息
3. 点击"确定"保存

### 3. 排序管理

1. 直接修改排序数字
2. 点击"保存排序"按钮
3. 系统会高亮显示变更的行

### 4. 删除轮播图

1. 选择要删除的轮播图
2. 点击"删除"按钮
3. 确认删除操作

## 注意事项

1. **图片格式**: 支持 jpg, jpeg, png, gif, bmp, webp 格式
2. **文件大小**: 单个图片不超过 2MB
3. **FTP配置**: 确保FTP服务器配置正确
4. **权限设置**: 确保用户有相应的操作权限
5. **排序保存**: 修改排序后需要点击"保存排序"按钮

## 扩展功能

### 1. 批量操作

可以扩展支持批量上传、批量删除等功能。

### 2. 图片处理

可以集成图片压缩、水印等功能。

### 3. 缓存优化

可以添加Redis缓存来提高访问速度。

### 4. 统计分析

可以添加轮播图点击统计、展示统计等功能。 