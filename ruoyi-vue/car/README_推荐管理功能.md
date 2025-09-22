# 推荐管理功能说明

## 功能概述

本模块实现了"精选好车"和"精选卖家"的推荐管理功能，用户可以设置车辆销售记录和经销商为推荐状态，推荐信息存储在`car_recommand`表中。

## 数据库表结构

### car_recommand 表
```sql
CREATE TABLE `ruoyi_car`.`car_recommand`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `recommandType` int(11) NULL DEFAULT NULL COMMENT '推荐类型 0 卖家 1 车辆',
  `recommandId` bigint(20) NULL DEFAULT NULL COMMENT '推荐ID recommandType为0时关联car_sales的ID，recommandType为1时关联car_products的id',
  `showOrder` int(11) NULL DEFAULT 0,
  `delFlag` int(11) NULL DEFAULT 0,
  `createTime` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '卖家和车辆推荐' ROW_FORMAT = Compact;
```

## 后端实现

### 1. 实体类
- `CarRecommandEntity.java` - 推荐记录实体类

### 2. 数据访问层
- `CarRecommandMapper.java` - 推荐记录Mapper接口
- `CarRecommandMapper.xml` - MyBatis映射文件

### 3. 业务逻辑层
- `ICarRecommandService.java` - 推荐管理Service接口
- `CarRecommandServiceImpl.java` - 推荐管理Service实现类

### 4. 控制器层
- `CarRecommandController.java` - 推荐管理基础控制器
- `CarSalesRecommendController.java` - 精选好车控制器
- `CarDealerRecommendController.java` - 精选卖家控制器

## 前端实现

### 1. API接口
- `recommand.js` - 推荐管理API接口
- `salesRecommend.js` - 精选好车API接口
- `dealerRecommend.js` - 精选卖家API接口

### 2. 页面组件
- `car/salesRecommend/index.vue` - 精选好车管理页面
- `car/dealerRecommend/index.vue` - 精选卖家管理页面

## 功能特性

### 精选好车功能
1. 显示车辆销售列表
2. 支持按销售标题、销售员、状态等条件搜索
3. 可以设置/取消车辆推荐状态
4. 显示车辆基本信息（价格、里程、状态等）
5. 支持查看车辆详情
6. 支持导出功能

### 精选卖家功能
1. 显示经销商列表
2. 支持按经销商名称、联系人、电话等条件搜索
3. 可以设置/取消经销商推荐状态
4. 显示经销商基本信息
5. 支持查看经销商详情
6. 支持导出功能

### 推荐管理功能
1. 推荐记录的增删改查
2. 按推荐类型查询推荐列表
3. 推荐状态管理
4. 数据导出功能

## 权限配置

### 菜单权限
- `car:recommand:*` - 推荐管理权限
- `car:salesRecommend:*` - 精选好车权限
- `car:dealerRecommend:*` - 精选卖家权限

### 操作权限
- `list` - 查询列表
- `query` - 查看详情
- `add` - 新增
- `edit` - 修改
- `remove` - 删除
- `export` - 导出

## 安装部署

### 1. 执行数据库脚本
```sql
-- 执行菜单SQL
source car/car_recommand_menu.sql;
```

### 2. 重启应用
重启Spring Boot应用以加载新的Controller和Service

### 3. 配置权限
在系统管理->菜单管理中添加相应的菜单权限

## 注意事项

1. **数据库分离**: `CarRecommandEntity`与其他表不在同一个数据库，不能进行联合查询，需要单独查询
2. **Service方法**: 已为`CarDealerService`和`CarSalesService`都添加了分页查询方法
3. **推荐类型**: 
   - `recommandType = 0` 表示推荐卖家
   - `recommandType = 1` 表示推荐车辆
4. **推荐ID**: 
   - 推荐卖家时，`recommandId`关联`car_dealers`表的ID
   - 推荐车辆时，`recommandId`关联`car_sales`表的ID
5. **实体类修复**: 已为`CarDealerEntity`添加了`@Id`注解，确保主键正确映射
6. **推荐状态逻辑**: 已修复前端和后端的推荐状态设置逻辑，确保数据传递正确

## 扩展建议

1. 实现推荐排序功能
2. 添加推荐有效期管理
3. 实现推荐统计功能
4. 添加推荐审核流程
5. 优化车辆信息显示（关联CarEntity显示更多车辆详情）
