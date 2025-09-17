# 订单模块管理功能

## 功能概述

本模块提供了完整的订单管理功能，包括订单信息管理和订单详情管理两个子模块。

## 功能特性

### 订单信息管理 (CarOrderInfo)
- ✅ 订单列表查询（支持分页）
- ✅ 多条件搜索（订单号、用户ID、订单状态、订单类型、收件人信息等）
- ✅ 订单信息新增
- ✅ 订单信息修改
- ✅ 订单信息删除（逻辑删除）
- ✅ 订单信息导出
- ✅ 订单状态管理（未支付、支付中、已支付、已取消、支付失败、已发货、已完成、退货中、已退货）
- ✅ 订单类型管理（宅配到府、超商取货）
- ✅ 超商取货相关信息管理（店铺信息、地址、电话等）

### 订单详情管理 (CarOrderDetail)
- ✅ 订单详情列表查询（支持分页）
- ✅ 多条件搜索（订单ID、产品ID、产品名称）
- ✅ 订单详情新增
- ✅ 订单详情修改
- ✅ 订单详情删除（逻辑删除）
- ✅ 订单详情导出
- ✅ 产品信息管理（产品名称、数量、单价、总价）

## 技术实现

### 后端技术栈
- **框架**: Spring Boot + MyBatis
- **ORM**: tkMyBatis (通用Mapper)
- **数据库**: MySQL
- **权限控制**: Spring Security

### 前端技术栈
- **框架**: Vue.js 2.x
- **UI组件**: Element UI
- **状态管理**: Vuex
- **HTTP客户端**: Axios

### 数据库设计
- **订单主表**: `car_order_info` - 存储订单基本信息
- **订单详情表**: `car_order_detail` - 存储订单商品详情
- **字典表**: 订单状态、订单类型等字典数据

## 部署说明

### 1. 数据库初始化
```sql
-- 执行字典数据SQL
source car/car_order_dict.sql;

-- 执行菜单SQL
source car/car_order_menu.sql;
```

### 2. 后端部署
1. 确保所有Java文件已正确编译
2. 重启Spring Boot应用
3. 验证Controller接口可正常访问

### 3. 前端部署
1. 确保Vue页面文件已正确放置
2. 重新构建前端项目
3. 验证页面可正常访问

## 使用说明

### 权限配置
系统管理员需要为相关角色分配以下权限：
- `car:orderInfo:list` - 订单信息查询
- `car:orderInfo:add` - 订单信息新增
- `car:orderInfo:edit` - 订单信息修改
- `car:orderInfo:remove` - 订单信息删除
- `car:orderInfo:export` - 订单信息导出
- `car:orderDetail:list` - 订单详情查询
- `car:orderDetail:add` - 订单详情新增
- `car:orderDetail:edit` - 订单详情修改
- `car:orderDetail:remove` - 订单详情删除
- `car:orderDetail:export` - 订单详情导出

### 菜单访问
- 订单信息管理：系统管理 → 订单管理 → 订单信息
- 订单详情管理：系统管理 → 订单管理 → 订单详情

## API接口

### 订单信息接口
- `GET /car/orderInfo/list` - 查询订单信息列表
- `GET /car/orderInfo/{id}` - 获取订单信息详情
- `POST /car/orderInfo` - 新增订单信息
- `PUT /car/orderInfo` - 修改订单信息
- `DELETE /car/orderInfo/{ids}` - 删除订单信息
- `POST /car/orderInfo/export` - 导出订单信息

### 订单详情接口
- `GET /car/orderDetail/list` - 查询订单详情列表
- `GET /car/orderDetail/{id}` - 获取订单详情详情
- `POST /car/orderDetail` - 新增订单详情
- `PUT /car/orderDetail` - 修改订单详情
- `DELETE /car/orderDetail/{ids}` - 删除订单详情
- `POST /car/orderDetail/export` - 导出订单详情

## 注意事项

1. **数据安全**: 所有删除操作均为逻辑删除，不会物理删除数据
2. **权限控制**: 所有接口都有相应的权限验证
3. **数据验证**: 前端和后端都有相应的数据验证
4. **字典数据**: 订单状态和订单类型通过字典表管理，便于维护
5. **分页查询**: 所有列表查询都支持分页，提高性能
6. **搜索功能**: 支持多条件组合搜索，提高查询效率

## 扩展建议

1. **订单流程**: 可以增加订单状态流转的审批流程
2. **消息通知**: 可以增加订单状态变更的消息通知功能
3. **统计报表**: 可以增加订单相关的统计报表功能
4. **批量操作**: 可以增加批量修改订单状态等功能
5. **订单关联**: 可以增加订单与用户、商品的关联查询功能
